package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Processo;

public record ListarProcessoSemOuComClienteRequest(
        Advogado advogado
) {
}
