package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Autor;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.entities.Reu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ListarProcessosRequest(
        Long id,
        String status,
        LocalDate dataAtualizacao,
        String descricao,
        String numProcesso,
        String assunto,
        String classe,
        List<ListarReusRequest> nomeReu,
        List<ListarAutoresRequest> nomeAutor
) {
    public ListarProcessosRequest(Processo processo) {
        this(processo.getId(), processo.getUltimoEvento(), processo.getDataAtualizacao(),
                processo.getDescricao(), processo.getNumeroProcesso(), processo.getAssunto(),
                processo.getClasse(), converterReu(processo.getNomeReu()), converterAutor(processo.getNomeAutor()));
    }

    private static List<ListarReusRequest> converterReu(List<Reu> reus){
        List<ListarReusRequest> lista = new ArrayList<>();
        for(Reu reu : reus){
            ListarReusRequest lr = new ListarReusRequest(reu);
            lista.add(lr);
        }
        return lista;
    }

    private static List<ListarAutoresRequest> converterAutor(List<Autor> autores){
        List<ListarAutoresRequest> lista = new ArrayList<>();
        for(Autor autor : autores){
            ListarAutoresRequest la = new ListarAutoresRequest(autor);
            lista.add(la);
        }
        return lista;
    }


}
