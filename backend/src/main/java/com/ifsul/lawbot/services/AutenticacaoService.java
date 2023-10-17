package com.ifsul.lawbot.services;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ChaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private CriptografiaService cript;

    @Autowired
    ChaveRepository chaveRepository;

    @Override
    public UserDetails loadUserByUsername(String oab) throws UsernameNotFoundException {
        PublicKey chave = chaveRepository.findById(1L).get().getChavePublica();
        System.out.println(chave.toString());
        return repository.findByOab(cript.encriptar(oab, chave));
    }
}
