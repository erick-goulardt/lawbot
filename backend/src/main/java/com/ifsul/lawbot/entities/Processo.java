package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

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
    @Lob
    @Column(columnDefinition = "BLOB")
    private String status;
    private LocalDate dataAtualizacao;
    @Lob
    @Column(columnDefinition = "BLOB")
    private String descricao;
    @ManyToOne
    @JoinColumn(name = "chave_id")
    private Chave chave;
}

