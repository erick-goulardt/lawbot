package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "processos")
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "advogado_id")
    private Advogado advogado;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    private String status;
    private LocalDate dataAtualizacao;
    @Column(unique = true, columnDefinition = "VARCHAR(6000)")
    private String descricao;

}

