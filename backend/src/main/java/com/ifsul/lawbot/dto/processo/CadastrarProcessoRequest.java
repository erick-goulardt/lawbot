package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;

import java.time.LocalDate;

public record CadastrarProcessoRequest(
        Advogado advogado,
        Cliente cliente,
        String ultimoEvento,
        LocalDate dataAtualizacao,
        String descricao,
        String numeroProcesso,
        String classe,
        String localidade,
        String assunto,
        String nomeReu,
        String nomeAutor
) {
}