package br.com.lucas.dto;

import jakarta.validation.constraints.*;

public record ProdutoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        String descricao,

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser maior que zero")
        Double preco,

        @Min(value = 0, message = "Estoque não pode ser negativo")
        Integer estoque
) {}