package com.ifsul.lawbot.domain.dto;

import com.ifsul.lawbot.domain.Advogado;

import java.time.LocalDate;

public record DetalharAdvogadoRequest(
        Long id,
        String nome,
        String email,
        String oab,
        String cpf,
        LocalDate dataNascimento
) {
    public DetalharAdvogadoRequest(Advogado advogado){
        this(advogado.getId(), advogado.getNome(), advogado.getEmail(), advogado.getOab(),
                advogado.getCpf(), advogado.getDataNascimento());
    }
}
