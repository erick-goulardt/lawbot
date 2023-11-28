package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Historico;
import com.ifsul.lawbot.entities.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findByProcesso_Id(Long id);

    List<Historico> findByProcesso_IdOrderByDataAtualizacaoDesc(Long id);
}
