package com.ifsul.lawbot.entities;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Reu extends Pessoa{

    public Reu(String nome){
        this.setNome(nome);
    }
}
