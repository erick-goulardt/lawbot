package com.ifsul.lawbot.repository;

import com.ifsul.lawbot.domain.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvogadoRepository extends JpaRepository<Advogado, Long> {
}
