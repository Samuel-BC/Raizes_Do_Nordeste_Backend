package com.raizesdonordeste.api.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        Long clienteId,
        List<ItemPedidoDTO> itens,
        BigDecimal valorTotal,
        String status,
        LocalDateTime dataCriacao
) {}