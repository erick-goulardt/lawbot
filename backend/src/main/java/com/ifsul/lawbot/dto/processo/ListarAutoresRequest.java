package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Autor;
import com.ifsul.lawbot.entities.Reu;

public record ListarAutoresRequest(
        String nome
) {
    public ListarAutoresRequest(Autor autor){
        this(autor.getNome());
    }
}
