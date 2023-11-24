package com.ifsul.lawbot.repositories;

import com.ifsul.lawbot.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
}
