package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {

    UserDetails findByOab(String oab);

}
