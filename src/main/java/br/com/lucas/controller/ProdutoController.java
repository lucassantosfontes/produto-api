package br.com.lucas.controller;

import br.com.lucas.dto.ProdutoRequestDTO;
import br.com.lucas.dto.ProdutoResponseDTO;
import br.com.lucas.entity.Produto;
import br.com.lucas.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Produtos", description = "API de gerenciamento de produtos")
@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private ProdutoService servico;

    @Operation(summary = "Lista todos os produtos")
    @GetMapping
    @PreAuthorize("hasRole('USER')")  // Exige autenticação
    public List<ProdutoResponseDTO> listarTodos() {
        return servico.buscarTodos().stream()
                .map(ProdutoResponseDTO::fromEntity)
                .toList();
    }

    @Operation(summary = "Busca produto por ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProdutoResponseDTO> obterPorId(@PathVariable Long id) {
        return servico.buscarPorId(id)
                .map(ProdutoResponseDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo produto")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")  // Apenas ADMIN cria
    public ProdutoResponseDTO criar(@RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = Produto.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .preco(dto.preco())
                .estoque(dto.estoque())
                .build();
        return ProdutoResponseDTO.fromEntity(servico.salvar(produto));
    }

    @Operation(summary = "Atualiza um produto existente")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoRequestDTO dto) {

        if (!servico.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }

        Produto produto = Produto.builder()
                .id(id)
                .nome(dto.nome())
                .descricao(dto.descricao())
                .preco(dto.preco())
                .estoque(dto.estoque())
                .build();

        return ResponseEntity.ok(ProdutoResponseDTO.fromEntity(servico.salvar(produto)));
    }

    @Operation(summary = "Exclui um produto")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!servico.existePorId(id)) {
            return ResponseEntity.notFound().build();
        }
        servico.excluirPorId(id);
        return ResponseEntity.noContent().build();
    }
}