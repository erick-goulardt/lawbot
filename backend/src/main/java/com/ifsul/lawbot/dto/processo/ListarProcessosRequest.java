package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Processo;

import java.time.LocalDate;

public record ListarProcessosRequest(
        Long id,
        String nomeAdvogado,
        String cliente,
        String status,
        LocalDate dataAtualizacao,
        String descricao
) {
    public ListarProcessosRequest(Processo processo) {
        this(processo.getId(), processo.getAdvogado().getNome(), processo.getCliente().getNome(), processo.getStatus(),
                processo.getDataAtualizacao(), processo.getDescricao());
    }

}
