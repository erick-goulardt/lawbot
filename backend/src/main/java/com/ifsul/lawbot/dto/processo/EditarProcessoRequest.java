package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;

public record EditarProcessoRequest(
        String descricao,
        Cliente cliente,
        String ultimoEvento

) {
}
