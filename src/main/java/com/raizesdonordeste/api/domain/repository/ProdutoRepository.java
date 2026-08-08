package com.raizesdonordeste.api.domain.repository;

import com.raizesdonordeste.api.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
