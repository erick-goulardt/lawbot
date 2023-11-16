package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.processo.CadastrarProcessoRequest;
import com.ifsul.lawbot.dto.processo.ListarProcessosRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.ClienteService.cadastrarCliente;
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
        Processo processo = new Processo();
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

        Cliente cliente = clienteRepository.findById(dados.cliente().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + dados.cliente().getId()));

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " + dados.advogado().getId()));

        Processo processo = cadastra(dados, cliente, advogado);
        advogado.getProcessos().add(processo);
        cliente.getProcessos().add(processo);
        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }

    public MessageDTO cadastrarProcessoComClienteNovo(CadastrarProcessoRequest dados){
        Chave key = gerarChaveService.findKey();

        Cliente cliente = new Cliente(dados.cliente());
        cliente.setChave(key);

        Cliente encriptado = new Cliente(cadastrarCliente(cliente));
        clienteRepository.save(encriptado);

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " +
                        dados.advogado().getId()));

        Processo processo = cadastra(dados, encriptado, advogado);
        advogado.getProcessos().add(processo);
        cliente.getProcessos().add(processo);
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
        PrivateKey chaveProcesso = processo.getChave().getChavePrivada();
        processo.setDescricao(decriptar(processo.getDescricao(), chaveProcesso));
        processo.setStatus(decriptar(processo.getStatus(), chaveProcesso));
        processo.setAdvogado(descriptografarAdvogado(processo.getAdvogado()));
        processo.setCliente(descriptografarCliente(processo.getCliente()));
        return processo;
    }

    private Advogado descriptografarAdvogado(Advogado advogado) {
        Advogado novoAdvogado = new Advogado();

        novoAdvogado.setChave(advogado.getChave());
        PrivateKey chavePrivada = novoAdvogado.getChave().getChavePrivada();

        novoAdvogado.setNome(decriptar(advogado.getNome(), chavePrivada));
        novoAdvogado.setCpf(decriptar(advogado.getCpf(), chavePrivada));
        novoAdvogado.setOab(decriptar(advogado.getOab(), chavePrivada));
        novoAdvogado.setEmail(decriptar(advogado.getEmail(), chavePrivada));
        return novoAdvogado;
    }

    private Cliente descriptografarCliente(Cliente cliente) {
        Cliente novoCliente = new Cliente();

        novoCliente.setChave(cliente.getChave());
        PrivateKey chavePrivada = novoCliente.getChave().getChavePrivada();

        novoCliente.setNome(decriptar(cliente.getNome(), chavePrivada));
        novoCliente.setCpf(decriptar(cliente.getCpf(), chavePrivada));
        novoCliente.setEmail(decriptar(cliente.getEmail(), chavePrivada));
        return novoCliente;
    }

    public List<ListarProcessosRequest> listarPeloAdvogado(Long id){
        List<Processo> processos = processoRepository.findAllByAdvogado_Id(id);
        return processos.stream()
                .map(this::descriptografarProcesso)
                .map(ListarProcessosRequest::new)
                .collect(Collectors.toList());
    }

    public List<ListarProcessosRequest> listarPeloCliente(Long id){
        List<Processo> processos = processoRepository.findAllByCliente_Id(id);
        return processos.stream()
                .map(this::descriptografarProcesso)
                .map(ListarProcessosRequest::new)
                .collect(Collectors.toList());
    }
}
