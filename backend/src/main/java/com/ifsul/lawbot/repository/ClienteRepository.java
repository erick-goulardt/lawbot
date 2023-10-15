package com.ifsul.lawbot.repository;

import com.ifsul.lawbot.domain.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
