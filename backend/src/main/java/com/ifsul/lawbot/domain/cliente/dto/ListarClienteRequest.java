package com.ifsul.lawbot.domain.cliente.dto;

import com.ifsul.lawbot.domain.advogado.Advogado;
import com.ifsul.lawbot.domain.cliente.Cliente;

public record ListarClienteRequest(
        Long id,
        String nome,
        String email
) {
    public ListarClienteRequest(Cliente cliente){
        this(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }
}
