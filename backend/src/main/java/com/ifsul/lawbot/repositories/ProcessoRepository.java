package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    List<Processo> findAllByAdvogado_Id(Long id);

    List<Processo> findAllByCliente_Id(Long id);
}
