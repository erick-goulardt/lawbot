package com.ifsul.lawbot.repository;

import com.ifsul.lawbot.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
