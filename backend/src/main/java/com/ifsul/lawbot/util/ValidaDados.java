package com.ifsul.lawbot.util;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public boolean CPFCliente(String cpf, Long idAdvogado){
        var advogado = advogadoRepository.findById(idAdvogado);

        var clienteCriptografados = advogado.get().getClientes();

        for(Cliente cliente : clienteCriptografados){
            var cpfDecriptado = decriptar(cliente.getCpf(), cliente.getChave().getChavePrivada());
            if(cpf.equals(cpfDecriptado)){
                return true;
            }
        }
        return false;
    }

    public boolean emailCliente(String email, Long idAdvogado){
        var advogado = advogadoRepository.findById(idAdvogado);

        var clienteCriptografados = advogado.get().getClientes();

        for(Cliente cliente : clienteCriptografados){
            var emailDecriptado = decriptar(cliente.getEmail(), cliente.getChave().getChavePrivada());
            if(email.equals(emailDecriptado)){
                return true;
            }
        }
        return false;
    }
    public boolean emailClienteLista(Long id, String email, List<Advogado> advogados){

        for(Advogado adv : advogados){
            var clienteCriptografados = adv.getClientes();

            for(Cliente cliente : clienteCriptografados){
                var emailDecriptado = decriptar(cliente.getEmail(), cliente.getChave().getChavePrivada());
                if(email.equals(emailDecriptado)){
                    if(cliente.getId() != id){
                        return true;
                    }
                }
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

    public boolean cpfClienteLista(Long id, String cpf, List<Advogado> advogados){

        for(Advogado adv : advogados){
            var clienteCriptografados = adv.getClientes();

            for(Cliente cliente : clienteCriptografados){
                var cpfDecriptado = decriptar(cliente.getCpf(), cliente.getChave().getChavePrivada());
                if(cpf.equals(cpfDecriptado)){
                    if(cliente.getId() != id){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
