package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.auth.AutenticarRequest;
import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.dto.cliente.DetalharClienteRequest;
import com.ifsul.lawbot.dto.cliente.EditarClienteRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.dto.utils.MensagemResponse;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import com.ifsul.lawbot.util.ValidaDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;
import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class ClienteService {

    @Autowired
    private ValidaDados valida;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoService advogadoService;

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private GerarChaveService gerarChaveService;

    public MensagemResponse cadastrarCliente(CadastrarClienteRequest dados) {
        Cliente cliente = new Cliente();

        if(valida.CPFCliente(dados.cpf(), dados.idAdvogado())){
            return new MensagemResponse("CPF já cadastrado!", 409);
        }

        if(valida.emailCliente(dados.email(), dados.idAdvogado())){
            return new MensagemResponse("Email já cadastrado!", 409);
        }

        cliente.setDataNascimento(dados.dataNascimento());
        cliente.setSenha(
                HashSenhasService.hash(dados.senha())
        );
        Chave key = gerarChaveService.findKey();
        cliente.setNome(
                encriptar(dados.nome(), key.getChavePublica())
        );
        cliente.setEmail(
                encriptar(dados.email(), key.getChavePublica())
        );
        cliente.setCpf(
                encriptar(dados.cpf(), key.getChavePublica())
        );

        cliente.setChave(key);


        repository.save(cliente);

        advogadoService.definirCliente(cliente.getId(), dados.idAdvogado());

        return new MensagemResponse("Usuário cadastrado!", 200);
    }

    public List<ListarClienteRequest> listarClientes() {
        List<Cliente> clientes = repository.findAll();
        return clientes.stream()
                .map(this::descriptografarCliente)
                .map(ListarClienteRequest::new)
                .collect(Collectors.toList());
    }

    public MensagemResponse editarCliente(Long clienteId, EditarClienteRequest dados){
        var cliente = repository.getReferenceById(clienteId);
        var advogado = cliente.getAdvogados().stream().toList();

        if ( dados.cpf() != null) {
            if(valida.cpfClienteLista(clienteId, dados.cpf(), advogado)){
                return new MensagemResponse("CPF já cadastrado!", 409);
            }
            cliente.setCpf(encriptar(dados.cpf(), cliente.getChave().getChavePublica()));
        }
        if( dados.email() != null){
            if(valida.emailClienteLista(clienteId, dados.email(), advogado)){
                return new MensagemResponse("Email já cadastrado!", 409);
            }
            cliente.setEmail(encriptar(dados.email(), cliente.getChave().getChavePublica()));
        }
        if( dados.nome() != null){
            cliente.setNome(encriptar(dados.nome(), cliente.getChave().getChavePublica()));
        }

        repository.save(cliente);
        return new MensagemResponse("Cliente atualizado!", 200);
    }

    public MessageDTO deletarCliente(Long id){
        var cliente = repository.getReferenceById(id);
        repository.delete(cliente);

        return new MessageDTO("Deletado com sucesso!");
    }

    public DetalharClienteRequest detalharCliente(Long id) {
        Cliente cliente = repository.getReferenceById(id);
        Cliente ClienteDecriptografado = descriptografarCliente(cliente);

        return new DetalharClienteRequest(ClienteDecriptografado.getId(), ClienteDecriptografado.getNome(), ClienteDecriptografado.getEmail(),
                ClienteDecriptografado.getCpf(), ClienteDecriptografado.getDataNascimento());
    }

    private Cliente descriptografarCliente(Cliente cliente) {
        PrivateKey chavePrivada = cliente.getChave().getChavePrivada();
        Cliente c = new Cliente();

        c.setId(cliente.getId());
        c.setNome(CriptografiaService.decriptar(cliente.getNome(), chavePrivada));
        c.setCpf(CriptografiaService.decriptar(cliente.getCpf(), chavePrivada));
        c.setEmail(CriptografiaService.decriptar(cliente.getEmail(), chavePrivada));

        return c;
    }

    public Processo descriptografarProcesso(Processo processo){
        PrivateKey chaveProcesso = processo.getChave().getChavePrivada();

        Processo p = new Processo();

        p.setDescricao(decriptar(processo.getDescricao(), chaveProcesso));
        p.setUltimoEvento(decriptar(processo.getUltimoEvento(), chaveProcesso));
        p.setAdvogado(descriptografarAdvogado(processo.getAdvogado()));
        p.setCliente(descriptografarCliente(processo.getCliente()));

        return p;
    }

    public Advogado descriptografarAdvogado(Advogado advogado) {
        Advogado novoAdvogado = new Advogado();

        novoAdvogado.setChave(advogado.getChave());
        PrivateKey chavePrivada = novoAdvogado.getChave().getChavePrivada();

        novoAdvogado.setNome(decriptar(advogado.getNome(), chavePrivada));
        novoAdvogado.setCpf(decriptar(advogado.getCpf(), chavePrivada));
        novoAdvogado.setOab(decriptar(advogado.getOab(), chavePrivada));
        novoAdvogado.setEmail(decriptar(advogado.getEmail(), chavePrivada));
        return novoAdvogado;
    }

    public Cliente logarCliente(AutenticarRequest dados){
        Cliente cliente = new Cliente();
        var lista = repository.findAll();
        for(Cliente c : lista){
            var email = decriptar(c.getEmail(), c.getChave().getChavePrivada());
            if (dados.login().equals(email)){
                cliente = c;
            }
        }
        var autenticou = HashSenhasService.verificaCliente(dados.senha(), cliente.getSenha());
        if(autenticou){
            return cliente;
        }
        return null;
    }
}
