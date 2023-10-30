package com.ifsul.lawbot.entities;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.services.GerarChaveService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
@Builder
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

}
