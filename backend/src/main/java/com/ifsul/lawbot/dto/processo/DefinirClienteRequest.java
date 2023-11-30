package com.ifsul.lawbot.dto.processo;

import com.ifsul.lawbot.entities.Processo;

public record DefinirClienteRequest(
        Processo processo,
        Long id
) {
}
