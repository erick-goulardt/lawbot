package com.ifsul.lawbot.security.repositories;

import com.ifsul.lawbot.entities.Chave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChaveRepository extends JpaRepository<Chave, Long> {
}
