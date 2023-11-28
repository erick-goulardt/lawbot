package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;

import java.time.LocalDate;

public record EditarProcessoRequest(
        String descricao,
        Cliente cliente,
        String ultimoEvento,
        LocalDate dataAtualizacao

) {

    public EditarProcessoRequest(Processo processo){
        this(processo.getDescricao(), processo.getCliente(), processo.getUltimoEvento(), LocalDate.now());
    }
}
