package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.processo.CadastrarProcessoRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public MessageDTO cadastrarProcesso(CadastrarProcessoRequest dados){

        Cliente cliente = clienteRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dados.cliente().getId()));

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " + dados.advogado().getId()));

        Processo processo = Processo.builder().build();
        processo.setAdvogado(advogado);
        processo.setCliente(cliente);
        processo.setStatus(dados.status());
        processo.setDataAtualizacao(dados.dataAtualizacao());
        processo.setDescricao(dados.descricao());
        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }

    public MessageDTO cadastrarProcessoComClienteNovo(CadastrarProcessoRequest dados){
        Cliente cliente = new Cliente(dados.cliente());
        clienteRepository.save(cliente);

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " + dados.advogado().getId()));

        Processo processo = Processo.builder().build();
        processo.setAdvogado(advogado);
        processo.setCliente(cliente);
        processo.setStatus(dados.status());
        processo.setDataAtualizacao(dados.dataAtualizacao());
        processo.setDescricao(dados.descricao());
        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }
}
