package com.ifsul.lawbot.entities;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente extends Usuario{

    @OneToMany(mappedBy = "cliente")
    private List<Processo> processos;

    public Cliente(CadastrarClienteRequest dados) {
        super(dados);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Cliente (Cliente cliente){
        this.setDataNascimento(cliente.getDataNascimento());
        this.setNome(cliente.getNome());
        this.setCpf(cliente.getCpf());
        this.setSenha(cliente.getSenha());
        this.setProcessos(cliente.getProcessos());
        this.setEmail(cliente.getEmail());
        this.setChave(cliente.getChave());
    }

    public List<Processo> getProcessos() {
        if(processos == null){
            processos = new ArrayList<>();
        }
        return processos;
    }
}
