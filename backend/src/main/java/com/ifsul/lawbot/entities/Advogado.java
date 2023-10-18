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
public class Advogado implements UserDetails {

     @Id @ApiModelProperty(dataType = "int64")
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     @Column(columnDefinition = "VARCHAR(2048)")
     private String nome;
     @Column(unique = true, columnDefinition = "VARCHAR(2048)")
     private String email;
     private String senha;
     @OneToMany(mappedBy = "advogado")
     private List<Processo> processos;
     @Column(unique = true, columnDefinition = "VARCHAR(2048)")
     private String oab;
     @Column(unique = true, columnDefinition = "VARCHAR(2048)")
     private String cpf;
     private LocalDate dataNascimento;

     @ManyToOne
     @JoinColumn(name = "chave_id")
     private Chave chave;

     public Advogado (CadastrarAdvogadoRequest dados){
          this.nome = dados.nome();
          this.email = dados.email();
          this.senha = dados.senha();
          this.oab = dados.oab();
          this.cpf = dados.cpf();
          this.dataNascimento = dados.dataNascimento();
     }

     public Advogado (DetalharAdvogadoRequest dados) {
          this.nome = dados.nome();
          this.email = dados.email();
          this.oab = dados.oab();
          this.cpf = dados.cpf();
          this.dataNascimento = dados.dataNascimento();
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
