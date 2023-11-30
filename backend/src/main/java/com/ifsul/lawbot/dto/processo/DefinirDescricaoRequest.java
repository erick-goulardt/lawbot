package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Processo;

public record DefinirDescricaoRequest(
        Processo processo,
        String descricao
) {
}
