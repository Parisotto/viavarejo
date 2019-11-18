package net.parisotto.via.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.parisotto.via.models.Parcela;
import net.parisotto.via.models.Produto;
import net.parisotto.via.repository.ProdutoRepository;

@Controller
public class ProdutoController {
	@Autowired
	private ProdutoRepository pr;
	double valorProduto = 0;
	
	@RequestMapping(value="/cadastrarProduto", method=RequestMethod.GET)
	public String form() {
		return "produto/formProduto";
	}
	
	@RequestMapping(value="/cadastrarProduto", method=RequestMethod.POST)
	public String form(Produto produto) {
		pr.save(produto);
		return "redirect:/cadastrarProduto";
	}
	
	@RequestMapping("/produtos")
	public ModelAndView listaProdutos() {
		ModelAndView mv = new ModelAndView("produto/listarprodutos");
		Iterable<Produto> produtos = pr.findAll();
		mv.addObject("produtos", produtos);
		return mv;
	}
	
	@RequestMapping("/{idprod}")
	public ModelAndView comprarProduto(Model model, @PathVariable("idprod") long idprod) {
		Produto produto = pr.findByIdprod(idprod);
		valorProduto = Double.parseDouble(produto.getValor());
		ModelAndView mv = new ModelAndView("produto/comprarProduto");
		mv.addObject("produto", produto);
		model.addAttribute("parcela", new Parcela());
		return mv;
	}
	
	@PostMapping("/parcelamento")
	public String calcular(Model model,@ModelAttribute Parcela parcelas) {
		double principal = valorProduto - parcelas.getEntrada();
		int meses = parcelas.getNumeroParcelas();
		double taxa = 0.0115; // 1,15% a.m.
		double parcelado;
		
		if(meses == 0) meses = 1;
		
		if(meses > 6) {
			parcelado = (principal * Math.pow(1 + taxa, meses)) / meses;
		} else {
			parcelado = principal / meses;
			taxa = 0;
		}
		
		String strParcela = String.format("%,.2f", parcelado);
		model.addAttribute("parcelado", strParcela);
		model.addAttribute("meses", meses);
		model.addAttribute("taxa", taxa * 100);
		
		return "produto/listaparcelas";
	}
}
