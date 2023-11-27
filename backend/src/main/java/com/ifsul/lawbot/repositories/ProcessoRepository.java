package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    List<Processo> findAllByAdvogado_Id(Long id);

    List<Processo> findAllByCliente_Id(Long id);

    Processo findByNumeroProcesso(String numeroProcesso);

//    @Query("SELECT c FROM Cliente c " +
//            "JOIN Processo p ON c.id = p.cliente_id " +
//            "WHERE p.advogado_id = {id}")
//    List<Processo> findProcessosByAdvogado_Id(Long id);

}
