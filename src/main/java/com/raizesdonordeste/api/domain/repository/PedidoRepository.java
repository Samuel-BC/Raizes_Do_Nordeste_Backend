package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
