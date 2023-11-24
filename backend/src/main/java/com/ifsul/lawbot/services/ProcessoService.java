package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.processo.*;
import com.ifsul.lawbot.dto.utils.MensagemResponse;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.*;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import com.ifsul.lawbot.util.ProcessoExcel;
import com.ifsul.lawbot.util.ValidaDados;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.PrivateKey;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;
import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class ProcessoService {

    @Autowired
    private ValidaDados valida;

    @Autowired
    private ProcessoExcel excel;

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
        processo.setUltimoEvento(encriptar(dados.ultimoEvento(), key.getChavePublica()));
        processo.setDataAtualizacao(dados.dataAtualizacao());
        processo.setDescricao(encriptar(dados.descricao(), key.getChavePublica()));
        processo.setNumeroProcesso(encriptar(dados.numeroProcesso(), key.getChavePublica()));
        processo.setClasse(encriptar(dados.classe(), key.getChavePublica()));
        processo.setLocalidade(encriptar(dados.localidade(), key.getChavePublica()));
        processo.setAssunto(encriptar(dados.assunto(), key.getChavePublica()));
        processo.getNomeAutor().add(dados.nomeAutor());
        processo.getNomeReu().add(dados.nomeReu());
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
        processoRepository.save(processo);
        return new MessageDTO("Processo cadastrado!");
    }

    public MensagemResponse cadastrarProcessoComClienteNovo(CadastrarProcessoRequest dados){
        Chave key = gerarChaveService.findKey();

        if(valida.emailCliente(dados.cliente().getEmail(), dados.advogado().getId())){
            return new MensagemResponse("Email já cadastrado!", 409);
        }
        if(valida.CPFCliente(dados.cliente().getCpf(), dados.advogado().getId())){
            return new MensagemResponse("CPF já cadastrado!", 409);
        }
        Cliente cliente = new Cliente(dados.cliente());
        cliente.setChave(key);

        Cliente encriptado = new Cliente(cadastrarCliente(cliente));
        clienteRepository.save(encriptado);

        Advogado advogado = advogadoRepository.findById(dados.advogado().getId())
                .orElseThrow(() -> new EntityNotFoundException("Advogado não encontrado com ID: " +
                        dados.advogado().getId()));

        Processo processo = cadastra(dados, encriptado, advogado);
        processoRepository.save(processo);
        advogado.getProcessos().add(processo);
        encriptado.getProcessos().add(processo);
        advogado.getClientes().add(encriptado);
        encriptado.getAdvogados().add(advogado);
        return new MensagemResponse("Processo cadastrado!", 200);
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
        Processo p = new Processo();

        p.setId(processo.getId());
        p.setDataAtualizacao(processo.getDataAtualizacao());
        p.setDescricao(decriptar(processo.getDescricao(), chaveProcesso));
        p.setUltimoEvento(decriptar(processo.getUltimoEvento(), chaveProcesso));
        p.setAdvogado(descriptografarAdvogado(processo.getAdvogado()));
        p.setCliente(descriptografarCliente(processo.getCliente()));
        p.setNomeAutor(decriptaAutor(processo));
        p.setNomeReu(decriptaReu(processo));
        return p;
    }

    private Advogado descriptografarAdvogado(Advogado advogado) {
        try{
            Advogado novoAdvogado = new Advogado();

            novoAdvogado.setChave(advogado.getChave());
            PrivateKey chavePrivada = novoAdvogado.getChave().getChavePrivada();

            novoAdvogado.setNome(decriptar(advogado.getNome(), chavePrivada));
            novoAdvogado.setCpf(decriptar(advogado.getCpf(), chavePrivada));
            novoAdvogado.setOab(decriptar(advogado.getOab(), chavePrivada));
            novoAdvogado.setEmail(decriptar(advogado.getEmail(), chavePrivada));
            return novoAdvogado;
        } catch (Exception ex){
            return null;
        }
    }

    private Cliente descriptografarCliente(Cliente cliente) {
        try{
            Cliente novoCliente = new Cliente();

            novoCliente.setChave(cliente.getChave());
            PrivateKey chavePrivada = novoCliente.getChave().getChavePrivada();

            novoCliente.setNome(decriptar(cliente.getNome(), chavePrivada));
            novoCliente.setCpf(decriptar(cliente.getCpf(), chavePrivada));
            novoCliente.setEmail(decriptar(cliente.getEmail(), chavePrivada));
            return novoCliente;
        } catch(Exception ex){
            return null;
        }
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

    public Cliente cadastrarCliente(Cliente c) {
        Chave key = c.getChave();

        Cliente cliente = new Cliente();

        cliente.setDataNascimento(c.getDataNascimento());
        cliente.setSenha(
                HashSenhasService.hash(c.getSenha())
        );
        cliente.setNome(
                encriptar(c.getNome(), key.getChavePublica())
        );
        cliente.setEmail(
                encriptar(c.getEmail(), key.getChavePublica())
        );
        cliente.setCpf(
                encriptar(c.getCpf(), key.getChavePublica())
        );
        cliente.setChave(c.getChave());

        return cliente;
    }

    public MensagemResponse cadastrarProcessoEmBloco(MultipartFile file, Long id){
        excel.leArquivo(file, id);
        return new MensagemResponse("Processos salvos com sucesso!", 200);
    }

    public List<ListarReusRequest> listaReu(Long id) {
        var processo = processoRepository.findById(id);
        var lista = processo.get().getNomeReu().stream().toList();
        List<ListarReusRequest> reus = new ArrayList<>();
        for(Reu reu : lista){
            reus.add(new ListarReusRequest(decriptar(reu.getNome(), processo.get().getChave().getChavePrivada())));
        }
        return reus;
    }

    public List<ListarAutoresRequest> listaAutor(Long id) {
        var processo = processoRepository.findById(id);
        var lista = processo.get().getNomeAutor().stream().toList();
        List<ListarAutoresRequest> autores = new ArrayList<>();
        for(Autor autor : lista){
            autores.add(new ListarAutoresRequest(decriptar(autor.getNome(), processo.get().getChave().getChavePrivada())));
        }
        return autores;
    }

    public List<Autor> decriptaAutor(Processo processo){
        List<Autor> autores = processo.getNomeAutor();
        List<Autor> a = new ArrayList<>();
        for(Autor au : autores){
            Autor aa = new Autor();
            aa.setNome(decriptar(au.getNome(), processo.getChave().getChavePrivada()));
            a.add(aa);
        }
        return a;
    }

    public List<Reu> decriptaReu(Processo processo){
        List<Reu> reus = processo.getNomeReu();
        List<Reu> a = new ArrayList<>();
        for(Reu au : reus){
            Reu aa = new Reu();
            aa.setId(au.getId());
            aa.setProcesso(au.getProcesso());
            aa.setNome(decriptar(au.getNome(), processo.getChave().getChavePrivada()));
            a.add(aa);
        }
        return a;
    }

    public MensagemResponse atualizarProcesso(Long id, EditarProcessoRequest dados){
        var processo = processoRepository.getReferenceById(id);

        var cliente = clienteRepository.getReferenceById(dados.cliente().getId());

        if ( dados.cliente().getId() != null) {
            processo.setCliente(cliente);
        }
        if( dados.descricao() != null){
            processo.setDescricao(encriptar(dados.descricao(), processo.getChave().getChavePublica()));
        }
        if( dados.ultimoEvento() != null){
            processo.setUltimoEvento(encriptar(dados.ultimoEvento(), processo.getChave().getChavePublica()));
            processo.setDataAtualizacao(LocalDate.now());
        }

        processoRepository.save(processo);
        return new MensagemResponse("Processo atualizado!", 200);
    }
}
