package br.com.lucas.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.lucas.entity.Produto;
import br.com.lucas.repository.ProdutoRepository;
import br.com.lucas.service.ProdutoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService{
	
	
	private ProdutoRepository repositorio;
	
	@Override
	public List<Produto> buscarTodos(){
		return repositorio.findAll();
	}
	
	@Override
    public Optional<Produto> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    @Override
    public Produto salvar(Produto produto) {
        return repositorio.save(produto);
    }

    @Override
    public void excluirPorId(Long id) {
        repositorio.deleteById(id);
    }

    @Override
    public boolean existePorId(Long id) {
        return repositorio.existsById(id);
    }
	
}
