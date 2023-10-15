package com.ifsul.lawbot.domain.advogado.dto;

import com.ifsul.lawbot.domain.advogado.Advogado;

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
