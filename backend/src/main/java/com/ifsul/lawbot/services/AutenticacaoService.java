package com.ifsul.lawbot.services;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
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
    private AdvogadoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String oab) throws UsernameNotFoundException {

        Advogado adv = repository.findAll().stream().filter(advogado ->
                Objects.equals(decriptar(advogado.getOab(), advogado.getChave().getChavePrivada()), oab)
        ).toList().stream().findFirst().get();

        return adv;
    }
}
