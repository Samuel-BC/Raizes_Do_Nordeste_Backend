package com.raizesdonordeste.api.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalPedido canalPedido; // Exigência estrita de multicanalidade[cite: 2]

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    private LocalDateTime criadoEm;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
        }
    }
}
