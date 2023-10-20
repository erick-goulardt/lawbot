package com.ifsul.lawbot.services;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findAll().stream().filter(usuario ->
                Objects.equals(decriptar(usuario.getUsername(), usuario.getChave().getChavePrivada()), login)
        ).toList().stream().findFirst().get();

    }

}
