package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
