package com.ifsul.lawbot.dto.cliente;

import com.ifsul.lawbot.entities.Cliente;

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
