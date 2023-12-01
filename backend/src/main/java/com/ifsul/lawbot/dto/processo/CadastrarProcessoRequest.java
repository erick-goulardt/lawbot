package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.*;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.time.LocalDate;
import java.util.List;

public record CadastrarProcessoRequest(
        Long idAdvogado,
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