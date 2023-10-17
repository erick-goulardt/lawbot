package com.ifsul.lawbot.services;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private CriptografiaService cript;

    @Override
    public UserDetails loadUserByUsername(String oab) throws UsernameNotFoundException {
        System.out.println(oab);
        var adv = new Advogado();
        List<Advogado> lista = (List<Advogado>) repository.findAll().stream().map(
                advogado -> {
                    if(cript.decriptar(advogado.getOab(), advogado.getChave().getChavePrivada()).equals(oab)){
                        adv.setNome(advogado.getNome());
                        adv.setEmail(advogado.getEmail());
                        adv.setSenha(advogado.getSenha());
                        adv.setOab(advogado.getOab());
                        adv.setCpf(advogado.getCpf());
                        adv.setDataNascimento(advogado.getDataNascimento());
                    }
                    return adv;
                }
        );
        return adv;
    }
}
