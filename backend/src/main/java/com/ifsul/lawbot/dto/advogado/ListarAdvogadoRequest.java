package com.ifsul.lawbot.dto.advogado;

import com.ifsul.lawbot.entities.Advogado;

public record ListarAdvogadoRequest (
        Long id,
        String nome,
        String email,
        String oab
) {
    public ListarAdvogadoRequest(Advogado advogado){
        this(advogado.getId(), advogado.getNome(), advogado.getEmail(), advogado.getOab());
    }
}
