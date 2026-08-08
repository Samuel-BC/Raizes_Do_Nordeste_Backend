package com.raizesdonordeste.api.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cidade;

    private Boolean possuiCozinhaCompleta;
}
