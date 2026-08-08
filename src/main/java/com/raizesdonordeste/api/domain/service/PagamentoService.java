package com.raizesdonordeste.api.domain.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PagamentoService {

    /**
     * Simula o processamento de pagamento.
     * Retorna true se aprovado (ex: para valores até R$ 10.000).
     */
    public boolean processarPagamento(Long pedidoId, BigDecimal valorTotal) {
        // Regra mock simples: aprova se o valor for positivo e razoável
        if (valorTotal == null || valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Simulação de aprovação (90% de taxa de sucesso fictícia ou regra fixa)
        return true;
    }
}
