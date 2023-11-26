package com.ifsul.lawbot.dto.cliente;

import com.ifsul.lawbot.entities.Cliente;

public record AutenticacaoClienteResponse(
        Long id
) {

    public AutenticacaoClienteResponse(Cliente cliente){
        this(cliente.getId());
    }
}
