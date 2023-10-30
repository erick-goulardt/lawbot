package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.processo.CadastrarProcessoRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.security.repositories.AdvogadoRepository;
import com.ifsul.lawbot.security.repositories.ClienteRepository;
import com.ifsul.lawbot.security.repositories.ProcessoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ifsul.lawbot.services.ClienteService.encriptarCliente;
import static com.ifsul.lawbot.services.CriptografiaService.decriptar;
import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private GerarChaveService gerarChaveService;

    @Autowired
    private ClienteRepository clienteRepository;

    public Processo cadastra(CadastrarProcessoRequest dados, Cliente cliente, Advogado advogado){
        Processo processo = Processo.builder().build();
        Chave key = gerarChaveService.findKey();
        processo.setAdvogado(advogado);
        processo.setCliente(cliente);
        processo.setStatus(encriptar(dados.status(), key.getChavePublica()));
        processo.setDataAtualizacao(dados.dataAtualizacao());
        processo.setDescricao(encriptar(dados.descricao(), key.getChavePublica()));
        processo.setChave(key);

        return processo;
    }
    public MessageDTO cadastrarProcesso(CadastrarProcessoRequest dados){

        Cliente cliente = clienteRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dados.cliente().getId()));

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " + dados.advogado().getId()));

        System.out.println(decriptar(cliente.getNome(), cliente.getChave().getChavePrivada()));
        Processo processo = cadastra(dados, cliente, advogado);
        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }

    public MessageDTO cadastrarProcessoComClienteNovo(CadastrarProcessoRequest dados){
        Chave key = gerarChaveService.findKey();

        Cliente cliente = new Cliente(dados.cliente());
        cliente.setChave(key);

        Cliente encriptado = new Cliente(encriptarCliente(cliente));
        clienteRepository.save(encriptado);

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " + dados.advogado().getId()));

        Processo processo = cadastra(dados, cliente, advogado);

        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }
}
