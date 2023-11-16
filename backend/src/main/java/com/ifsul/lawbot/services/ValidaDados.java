package com.ifsul.lawbot.services;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;

@Service
public class ValidaDados {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;



    public boolean CPFAdvogado(String cpf){
        var advogadosCriptografados = advogadoRepository.findAll();

        for(Advogado advogado : advogadosCriptografados){
            var cpfDecriptado = decriptar(advogado.getCpf(), advogado.getChave().getChavePrivada());
            if(cpf.equals(cpfDecriptado)){
                return true;
            }
        }
        return false;
    }

    public boolean OABAdvogado(String cpf){
        var advogadosCriptografados = advogadoRepository.findAll();

        for(Advogado advogado : advogadosCriptografados){
            var oabDecriptado = decriptar(advogado.getOab(), advogado.getChave().getChavePrivada());
            if(cpf.equals(oabDecriptado)){
                return true;
            }
        }
        return false;
    }

    public boolean CPFCliente(String cpf){
        var clienteCriptografados = clienteRepository.findAll();

        for(Cliente cliente : clienteCriptografados){
            var cpfDecriptado = decriptar(cliente.getCpf(), cliente.getChave().getChavePrivada());
            if(cpf.equals(cpfDecriptado)){
                return true;
            }
        }
        return false;
    }

    public boolean emailCliente(String email){
        var clientesCriptografados = clienteRepository.findAll();

        for(Cliente cliente : clientesCriptografados){
            var emailDecriptado = decriptar(cliente.getEmail(), cliente.getChave().getChavePrivada());
            if(email.equals(emailDecriptado)){
                return true;
            }
        }
        return false;
    }
    public boolean emailAdvogado(String email){
        var advogadosCriptografados = advogadoRepository.findAll();

        for(Advogado advogado : advogadosCriptografados){
            var emailDecriptado = decriptar(advogado.getEmail(), advogado.getChave().getChavePrivada());
            if(email.equals(emailDecriptado)){
                return true;
            }
        }
        return false;
    }
}
