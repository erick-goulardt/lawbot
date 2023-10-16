package com.ifsul.lawbot.dto.cliente;

import com.ifsul.lawbot.entities.Cliente;

public record ListarClienteRequest(
        Long id,
        String nome,
        String email
) {
    public ListarClienteRequest(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
