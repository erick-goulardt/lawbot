package com.ifsul.lawbot.security.repositories;

import com.ifsul.lawbot.entities.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
    Advogado findByOab(String oab);


    Advogado findByEmail(String email);
}
