package com.raizesdonordeste.api.controller;

import com.raizesdonordeste.api.domain.dto.CriarPedidoDTO;
import com.raizesdonordeste.api.domain.dto.PedidoResponseDTO;
import com.raizesdonordeste.api.domain.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para gerenciamento e criação de pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Valida cliente, estoque e processa o pagamento mock antes de finalizar o pedido.")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou estoque insuficiente")
    @ApiResponse(responseCode = "404", description = "Cliente ou produto não encontrado")
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody @Valid CriarPedidoDTO dto) {
        PedidoResponseDTO novoPedido = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }
}
