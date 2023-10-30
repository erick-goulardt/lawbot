package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;

import java.time.LocalDate;

public record ListarProcessosRequest(
        Long id,
        Advogado advogado,
        Cliente cliente,
        String status,
        LocalDate dataAtualizacao,
        String descricao
) {
    public ListarProcessosRequest(Processo processo) {
        this(processo.getId(), processo.getAdvogado(), processo.getCliente(), processo.getStatus(), processo.getDataAtualizacao(), processo.getDescricao());
    }

}
