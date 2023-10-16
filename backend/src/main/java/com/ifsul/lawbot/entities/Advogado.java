package com.ifsul.lawbot.entities;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Advogado implements UserDetails {

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

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return List.of(new SimpleGrantedAuthority("ROLE_USER"));
     }

     @Override
     public String getPassword() {
          return senha;
     }

     @Override
     public String getUsername() {
          return oab;
     }

     @Override
     public boolean isAccountNonExpired() {
          return true;
     }

     @Override
     public boolean isAccountNonLocked() {
          return true;
     }

     @Override
     public boolean isCredentialsNonExpired() {
          return true;
     }

     @Override
     public boolean isEnabled() {
          return true;
     }
}
