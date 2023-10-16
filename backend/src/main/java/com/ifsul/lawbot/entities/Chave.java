package com.ifsul.lawbot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Entity
@Table(name = "chave")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PrivateKey chavePrivada;

    private PublicKey chavePublica;

    @OneToMany(mappedBy = "chave")
    private List<Advogado> advogados;
}
