package com.ifsul.lawbot.domain.cliente.dto;

import com.ifsul.lawbot.domain.advogado.Advogado;
import com.ifsul.lawbot.domain.cliente.Cliente;

import java.time.LocalDate;

public record DetalharClienteRequest(
        Long id,
        String nome,
        String email,
        String cpf,
        LocalDate dataNascimento
) {
    public DetalharClienteRequest(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getEmail(),
                cliente.getCpf(), cliente.getDataNascimento());
    }
}
