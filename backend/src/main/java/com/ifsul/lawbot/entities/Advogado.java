package com.ifsul.lawbot.entities;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "advogado")
@Builder
public class Advogado extends Usuario {


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
