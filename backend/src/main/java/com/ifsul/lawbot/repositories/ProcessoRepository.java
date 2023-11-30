package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    List<Processo> findAllByAdvogado_Id(Long id);

    List<Processo> findAllByCliente_Id(Long id);

    Optional<Processo> findByNumeroProcesso(String numeroProcesso);

    Optional<Processo> findById(Long id);

    List<Processo> findByAdvogadoIdAndClienteIdIsNull(Long id);

    List<Processo> findByAdvogadoIdAndClienteIdIsNotNull(Long id);

//    @Query("SELECT c FROM Cliente c " +
//            "JOIN Processo p ON c.id = p.cliente_id " +
//            "WHERE p.advogado_id = {id}")
//    List<Processo> findProcessosByAdvogado_Id(Long id);

}
