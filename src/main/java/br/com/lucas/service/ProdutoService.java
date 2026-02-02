package br.com.lucas.service;

import br.com.lucas.entity.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {
	
	List<Produto> buscarTodos();
	
	Optional<Produto> buscarPorId(Long id);
	
	Produto salvar(Produto produto);
	
	void excluirPorId(Long id);
	
	boolean existePorId(Long id);
	
}
