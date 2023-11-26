package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private String ultimoEvento;

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

    @ManyToOne
    @JoinColumn(name = "chave_id")
    private Chave chave;

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reu> nomeReu = new ArrayList<>();

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Autor> nomeAutor = new ArrayList<>();

    private boolean clienteDefinido;
}

