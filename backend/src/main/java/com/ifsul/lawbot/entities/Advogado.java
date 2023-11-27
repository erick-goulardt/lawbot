package com.ifsul.lawbot.entities;

import ch.qos.logback.core.net.server.Client;
import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "advogado")
public class Advogado extends Usuario {

     @ManyToMany(mappedBy = "advogados")
     private List<Cliente> clientes = new ArrayList<>();

     @OneToMany(mappedBy = "advogado")
     private List<Processo> processos;
     @Column(unique = true, columnDefinition = "VARCHAR(2048)")
     private String oab;

     public Advogado (CadastrarAdvogadoRequest dados){
          super(dados);
          this.oab = dados.oab();
     }

     public Advogado (DetalharAdvogadoRequest dados) {
          super(dados);
          this.oab = dados.oab();
     }



}
