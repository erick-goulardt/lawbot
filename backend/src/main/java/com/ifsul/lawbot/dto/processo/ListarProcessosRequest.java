package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;

import java.time.LocalDate;

public record ListarProcessosRequest(
        Long id,
        String advogado,
        String cliente,
        String status,
        LocalDate dataAtualizacao,
        String descricao
) {
    public ListarProcessosRequest(Processo processo) {
        this(processo.getId(), processo.getAdvogado().getNome(), processo.getCliente().getNome(), processo.getStatus(), processo.getDataAtualizacao(), processo.getDescricao());
    }

}
