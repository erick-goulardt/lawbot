package com.ifsul.lawbot.domain;

import com.ifsul.lawbot.domain.dto.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.EditarAdvogadoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "advogado")
public class Advogado {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     private String nome;
     @Column(unique = true)
     private String email;
     private String senha;
     @OneToMany(mappedBy = "advogado")
     private List<Processo> processos;
     @Column(unique = true)
     private String oab;
     @Column(unique = true)
     private String cpf;
     private LocalDate dataNascimento;

     public Advogado (CadastrarAdvogadoRequest dados){
          this.nome = dados.nome();
          this.email = dados.email();
          this.senha = dados.senha();
          this.oab = dados.oab();
          this.cpf = dados.cpf();
          this.dataNascimento = dados.dataNascimento();
     }

     public void atualizar(EditarAdvogadoRequest dados){
          if( dados.dataNascimento() != null){
               this.dataNascimento = dados.dataNascimento();
          }
          if( dados.email() != null){
               this.email = dados.email();
          }
          if( dados.nome() != null){
               this.nome = dados.nome();
          }
     }
}
