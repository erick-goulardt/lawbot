package com.ifsul.lawbot.repository;

import com.ifsul.lawbot.domain.advogado.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {

    UserDetails findByOab(String oab);

}
