package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
}
