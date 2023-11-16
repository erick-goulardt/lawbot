package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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

    @Column(columnDefinition = "LONGTEXT")
    private String numeroProcesso;

    @Column(columnDefinition = "LONGTEXT")
    private String descricao;

    @Column(columnDefinition = "LONGTEXT")
    private String classe;

    @Column(columnDefinition = "LONGTEXT")
    private String localidade;

    @Column(columnDefinition = "LONGTEXT")
    private String assunto;

    @Column(columnDefinition = "LONGTEXT")
    private String ultimoEvento;

    @ManyToOne
    @JoinColumn(name = "chave_id")
    private Chave chave;

    @Column(columnDefinition = "LONGTEXT")
    private List<String> nomeReu;

    @Column(columnDefinition = "LONGTEXT")
    private List<String> nomeAutor;

    private boolean clienteDefinido;
}

