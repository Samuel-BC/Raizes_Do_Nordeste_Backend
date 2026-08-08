package com.raizesdonordeste.api.domain.service;

import com.raizesdonordeste.api.domain.dto.CriarPedidoDTO;
import com.raizesdonordeste.api.domain.dto.ItemPedidoDTO;
import com.raizesdonordeste.api.domain.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.exception.RegraNegocioException;
import com.raizesdonordeste.api.domain.model.*;
import com.raizesdonordeste.api.domain.repository.PedidoRepository;
import com.raizesdonordeste.api.domain.repository.ProdutoRepository;
import com.raizesdonordeste.api.domain.repository.UnidadeRepository;
import com.raizesdonordeste.api.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;
    private final PagamentoService pagamentoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         ProdutoRepository produtoRepository,
                         UnidadeRepository unidadeRepository,
                         PagamentoService pagamentoService) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.produtoRepository = produtoRepository;
        this.unidadeRepository = unidadeRepository;
        this.pagamentoService = pagamentoService;
    }

    @Transactional
    public PedidoResponseDTO criarPedido(CriarPedidoDTO dto) {
        // 1. Validar Cliente e Unidade (Agora lançando RecursoNaoEncontradoException -> 404)
        Usuario cliente = usuarioRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado com ID: " + dto.clienteId()));

        Unidade unidade = unidadeRepository.findById(dto.unidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada com ID: " + dto.unidadeId()));

        // 2. Preparar a Entidade Pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setUnidade(unidade);

        try {
            pedido.setCanalPedido(CanalPedido.valueOf(dto.canalPedido().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RegraNegocioException("Canal de pedido inválido: " + dto.canalPedido());
        }

        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        BigDecimal valorTotal = BigDecimal.ZERO;
        List<ItemPedido> itensPedido = new ArrayList<>();

        // 3. Validar Itens, Estoque e Calcular Total
        for (ItemPedidoDTO itemDto : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado com ID: " + itemDto.produtoId()));

            if (produto.getEstoque() == null || produto.getEstoque() < itemDto.quantidade()) {
                throw new RegraNegocioException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            // Criar ItemPedido
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDto.quantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());

            itensPedido.add(itemPedido);

            // Somar ao total
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade()));
            valorTotal = valorTotal.add(subtotal);
        }

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotal);

        // 4. Salvar pedido inicial
        pedido = pedidoRepository.save(pedido);

        // 5. Processar Pagamento Mock
        boolean pagamentoAprovado = pagamentoService.processarPagamento(pedido.getId(), valorTotal);

        if (pagamentoAprovado) {
            pedido.setStatus(StatusPedido.PAGO);
            // 6. Baixa de estoque
            for (ItemPedido item : pedido.getItens()) {
                Produto p = item.getProduto();
                p.setEstoque(p.getEstoque() - item.getQuantidade());
                produtoRepository.save(p);
            }
        } else {
            pedido.setStatus(StatusPedido.CANCELADO);
        }

        pedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente().getId(),
                dto.itens(),
                pedido.getValorTotal(),
                pedido.getStatus().name(),
                pedido.getCriadoEm()
        );
    }
}
