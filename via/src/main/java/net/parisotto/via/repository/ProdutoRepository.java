package net.parisotto.via.repository;

import org.springframework.data.repository.CrudRepository;
import net.parisotto.via.models.Produto;

public interface ProdutoRepository extends CrudRepository<Produto, String> {
	
}
