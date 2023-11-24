package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.*;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
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
        Reu nomeReu,
        Autor nomeAutor
) {
}