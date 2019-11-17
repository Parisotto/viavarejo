package net.parisotto.via.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.parisotto.via.models.Produto;
import net.parisotto.via.repository.ProdutoRepository;

@Controller
public class ProdutoController {
	@Autowired
	private ProdutoRepository pr;
	
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
		ModelAndView mv = new ModelAndView("index");
		Iterable<Produto> produtos = pr.findAll();
		mv.addObject("produtos", produtos);
		return mv;
	}
	
	@RequestMapping("/{idprod}")
	public ModelAndView comprarProduto(@PathVariable("idprod") long idprod) {
		Produto produto = pr.findByIdprod(idprod);
		ModelAndView mv = new ModelAndView("comprarProduto");
		mv.addObject("produto", produto);
		return mv;
	}
}
