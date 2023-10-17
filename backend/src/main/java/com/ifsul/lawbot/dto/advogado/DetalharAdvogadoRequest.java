package com.ifsul.lawbot.dto.advogado;

import com.ifsul.lawbot.entities.Advogado;

import java.time.LocalDate;

public record DetalharAdvogadoRequest(
        String nome,
        String email,
        String oab,
        String cpf,
        LocalDate dataNascimento
) {
    public DetalharAdvogadoRequest(Advogado advogado){
        this(advogado.getNome(), advogado.getEmail(), advogado.getOab(),
                advogado.getCpf(), advogado.getDataNascimento());
    }
}
