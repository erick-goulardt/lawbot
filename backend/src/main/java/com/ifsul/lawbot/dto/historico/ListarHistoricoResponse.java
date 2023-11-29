package com.ifsul.lawbot.dto.historico;

import com.ifsul.lawbot.entities.Historico;

import java.time.LocalDate;

public record ListarHistoricoResponse(
        String atualizacao,
        LocalDate dataAtualizacao,
        String descricao
) {

    public ListarHistoricoResponse(Historico h){
        this(h.getUltimaAtualizacao(), h.getDataAtualizacao(), h.getDescricao());
    }

}
