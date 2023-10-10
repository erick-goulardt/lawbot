package com.ifsul.lawbot.domain.dto;

import com.ifsul.lawbot.domain.Advogado;

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
