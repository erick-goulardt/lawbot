package com.ifsul.lawbot.dto.utils;

public record MessageDTO(
        String mensagem) {
    public MessageDTO(MensagemResponse mensagem){
        this(mensagem.mensagem());
    }
}
