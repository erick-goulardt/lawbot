package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Reu;

public record ListarReusRequest(
        String nome
) {
    public ListarReusRequest(Reu reu){
        this(reu.getNome());
    }
}
