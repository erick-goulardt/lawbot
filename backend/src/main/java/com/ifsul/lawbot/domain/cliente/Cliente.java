package com.ifsul.lawbot.domain.cliente;

import com.ifsul.lawbot.domain.advogado.dto.EditarAdvogadoRequest;
import com.ifsul.lawbot.domain.cliente.dto.CadastrarClienteRequest;
import com.ifsul.lawbot.domain.cliente.dto.EditarClienteRequest;
import com.ifsul.lawbot.domain.processo.Processo;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;
    @OneToMany(mappedBy = "cliente")
    private List<Processo> processos;
    @Column(unique = true)
    private String cpf;
    private LocalDate dataNascimento;

    public Cliente(CadastrarClienteRequest dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
        this.email = dados.email();
        this.senha = dados.senha();
        this.dataNascimento = dados.dataNascimento();
    }

    public void atualizar(EditarClienteRequest dados){
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
