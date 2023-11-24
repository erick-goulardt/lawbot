package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Autor;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.entities.Reu;

import java.time.LocalDate;
import java.util.List;

public record ListarProcessosRequest(
        Long id,
        String status,
        LocalDate dataAtualizacao,
        String descricao,
        List<ListarReusRequest> nomeReu,
        List<ListarAutoresRequest> nomeAutor
) {
    public ListarProcessosRequest(Processo processo) {
        this(processo.getId(), processo.getUltimoEvento(), processo.getDataAtualizacao(), processo.getDescricao(), processo.getNomeReu().stream().map(ListarReusRequest::new).toList(), processo.getNomeAutor().stream().map(ListarAutoresRequest::new).toList());
    }

}
