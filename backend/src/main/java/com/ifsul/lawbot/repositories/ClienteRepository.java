package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmail(String email);

}
