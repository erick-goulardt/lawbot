package com.ifsul.lawbot.services;

import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try{
            return advogadoRepository.findAll().stream().filter(usuario ->
                    Objects.equals(decriptar(usuario.getUsername(), usuario.getChave().getChavePrivada()), login)
            ).toList().stream().findFirst().get();
        }
        catch (Exception ex){
            try{
                return clienteRepository.findAll().stream().filter(usuario ->
                        Objects.equals(decriptar(usuario.getUsername(), usuario.getChave().getChavePrivada()), login)
                ).toList().stream().findFirst().get();
            }
            catch (Exception exception){
                exception.printStackTrace();
            }
        }
        return null;


    }

}
