package com.ifsul.lawbot.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Autor extends Pessoa {
    public Autor(String nome) {
        this.setNome(nome);
    }
}
