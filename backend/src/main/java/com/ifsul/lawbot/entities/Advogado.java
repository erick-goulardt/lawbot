package com.ifsul.lawbot.entities;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
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
