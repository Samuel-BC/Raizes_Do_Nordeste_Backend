package com.raizesdonordeste.api.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CriarPedidoDTO(
        @NotNull(message = "O ID do cliente é obrigatório")
        Long clienteId,

        @NotNull(message = "O ID da unidade é obrigatório")
        Long unidadeId,

        @NotBlank(message = "O canal do pedido é obrigatório")
        String canalPedido,

        @NotNull(message = "A lista de itens não pode ser nula")
        @NotEmpty(message = "O pedido deve conter pelo menos um item")
        @Valid // <--- ADICIONADO: Força o Spring a validar as regras de CADA item da lista!
        List<ItemPedidoDTO> itens
) {}