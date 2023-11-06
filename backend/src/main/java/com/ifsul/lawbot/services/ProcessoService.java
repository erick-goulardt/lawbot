package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.processo.CadastrarProcessoRequest;
import com.ifsul.lawbot.dto.processo.ListarProcessosRequest;
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

import java.util.List;
import java.util.stream.Collectors;

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
    private ClienteRepository clienteRepository;

    public Processo cadastra(CadastrarProcessoRequest dados, Cliente cliente, Advogado advogado){
        Processo processo = Processo.builder().build();
        processo.setAdvogado(advogado);
        processo.setCliente(cliente);
        processo.setStatus(encriptar(dados.status()));
        processo.setDataAtualizacao(dados.dataAtualizacao());
        processo.setDescricao(encriptar(dados.descricao()));

        return processo;
    }

    public MessageDTO cadastrarProcesso(CadastrarProcessoRequest dados){

        Cliente cliente = clienteRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dados.cliente().getId()));

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " + dados.advogado().getId()));

        Processo processo = cadastra(dados, cliente, advogado);
        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }

    public MessageDTO cadastrarProcessoComClienteNovo(CadastrarProcessoRequest dados){

        Cliente cliente = new Cliente(dados.cliente());

        Cliente encriptado = new Cliente(encriptarCliente(cliente));
        System.out.println("nome: " + decriptar(encriptado.getNome()));
        clienteRepository.save(encriptado);

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " +
                        dados.advogado().getId()));

        Processo processo = cadastra(dados, encriptado, advogado);

        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }

    public List<ListarProcessosRequest> listarProcessos(){
        List<Processo> processos = processoRepository.findAll();
        return processos.stream()
                .map(this::descriptografarProcesso)
                .map(ListarProcessosRequest::new)
                .collect(Collectors.toList());
    }

    private Processo descriptografarProcesso(Processo processo){
        processo.setDescricao(decriptar(processo.getDescricao()));
        processo.setStatus(decriptar(processo.getStatus()));
        processo.setAdvogado(descriptografarAdvogado(processo.getAdvogado()));
        processo.setCliente(descriptografarCliente(processo.getCliente()));
        return processo;
    }

    private Advogado descriptografarAdvogado(Advogado advogado) {
        advogado.setNome(decriptar(advogado.getNome()));
        advogado.setCpf(decriptar(advogado.getCpf()));
        advogado.setOab(decriptar(advogado.getOab()));
        advogado.setEmail(decriptar(advogado.getEmail()));
        System.out.println(advogado);
        return advogado;
    }

    private Cliente descriptografarCliente(Cliente cliente) {
        cliente.setNome(decriptar(cliente.getNome()));
        cliente.setCpf(decriptar(cliente.getCpf()));
        cliente.setEmail(decriptar(cliente.getEmail()));
        return cliente;
    }
}
