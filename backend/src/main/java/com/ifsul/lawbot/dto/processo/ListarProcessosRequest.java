package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Autor;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.entities.Reu;

import java.time.LocalDate;
import java.util.List;

public record ListarProcessosRequest(
        Long id,
<<<<<<< HEAD
        String status,
        LocalDate dataAtualizacao,
        String descricao,
=======
        String numProcesso,
        String status,
        LocalDate dataAtualizacao,
        String descricao,
        String assunto,
        String classe,
>>>>>>> ec77320d04d4626f44382f8269338ec5de53ac2d
        List<ListarReusRequest> nomeReu,
        List<ListarAutoresRequest> nomeAutor
) {
    public ListarProcessosRequest(Processo processo) {
<<<<<<< HEAD
        this(processo.getId(), processo.getUltimoEvento(), processo.getDataAtualizacao(), processo.getDescricao(), processo.getNomeReu().stream().map(ListarReusRequest::new).toList(), processo.getNomeAutor().stream().map(ListarAutoresRequest::new).toList());
=======
        this(processo.getId(), processo.getNumeroProcesso(), processo.getUltimoEvento(), processo.getDataAtualizacao(), processo.getDescricao(), processo.getAssunto(), processo.getClasse(), processo.getNomeReu().stream().map(ListarReusRequest::new).toList(), processo.getNomeAutor().stream().map(ListarAutoresRequest::new).toList());
>>>>>>> ec77320d04d4626f44382f8269338ec5de53ac2d
    }

}
