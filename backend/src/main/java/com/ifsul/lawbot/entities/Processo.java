package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @Column(columnDefinition = "LONGTEXT")
    private String status;
    private LocalDate dataAtualizacao;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "chave_id")
    private Chave chave;
}

