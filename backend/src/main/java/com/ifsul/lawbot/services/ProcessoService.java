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

    public Processo descriptografarLista(Processo processo){
        processo = descriptografarProcesso(processo);
        processo.getAdvogado().setNome(decriptar(processo.getAdvogado().getNome(), processo.getAdvogado().getChave().getChavePrivada()));
        processo.getCliente().setNome(decriptar(processo.getCliente().getNome(), processo.getCliente().getChave().getChavePrivada()));
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
        Chave key = gerarChaveService.findKey();

        Cliente cliente = new Cliente(dados.cliente());
        cliente.setChave(key);

        Cliente encriptado = new Cliente(encriptarCliente(cliente));
        System.out.println("nome: " + decriptar(encriptado.getNome(), encriptado.getChave().getChavePrivada()));
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
        PrivateKey chaveProcesso = processo.getChave().getChavePrivada();
        processo.setDescricao(decriptar(processo.getDescricao(), chaveProcesso));
        processo.setStatus(decriptar(processo.getStatus(), chaveProcesso));
        processo.setAdvogado(descriptografarAdvogado(processo.getAdvogado()));
        processo.setCliente(descriptografarCliente(processo.getCliente()));
        return processo;
    }

    private Advogado descriptografarAdvogado(Advogado advogado) {
        PrivateKey chavePrivada = advogado.getChave().getChavePrivada();
        advogado.setNome(decriptar(advogado.getNome(), chavePrivada));
        advogado.setCpf(decriptar(advogado.getCpf(), chavePrivada));
        advogado.setOab(decriptar(advogado.getOab(), chavePrivada));
        advogado.setEmail(decriptar(advogado.getEmail(), chavePrivada));
        System.out.println(advogado);
        return advogado;
    }

    private Cliente descriptografarCliente(Cliente cliente) {
        PrivateKey chavePrivada = cliente.getChave().getChavePrivada();
        cliente.setNome(decriptar(cliente.getNome(), chavePrivada));
        cliente.setCpf(decriptar(cliente.getCpf(), chavePrivada));
        cliente.setEmail(decriptar(cliente.getEmail(), chavePrivada));
        return cliente;
    }
}
