package com.ifsul.lawbot.security.repositories;

import com.ifsul.lawbot.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
