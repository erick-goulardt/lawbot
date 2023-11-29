package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Historico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String ultimaAtualizacao;
    private LocalDate dataAtualizacao;

    @Column(columnDefinition = "LONGTEXT")
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    public Historico(Processo processo){
        this.dataAtualizacao = processo.getDataAtualizacao();
        this.ultimaAtualizacao = processo.getUltimoEvento();
        this.descricao = processo.getDescricao();
        this.processo = processo;
    }
}
