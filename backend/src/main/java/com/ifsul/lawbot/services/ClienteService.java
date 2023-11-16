package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.dto.cliente.DetalharClienteRequest;
import com.ifsul.lawbot.dto.cliente.EditarClienteRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.dto.utils.MensagemResponse;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class ClienteService {

    @Autowired
    private ValidaDados valida;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private AdvogadoService advogadoService;

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private GerarChaveService gerarChaveService;

    public MensagemResponse cadastrarCliente(CadastrarClienteRequest dados) {
        Cliente cliente = new Cliente();

        if(valida.CPFCliente(dados.cpf())){
            return new MensagemResponse("CPF já cadastrado!", 409);
        }

        if(valida.emailCliente(dados.email())){
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

//        advogadoService.definirCliente(cliente.getId(), dados.idAdvogado());

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

        if ( dados.senha() != null) {
            cliente.setSenha(HashSenhasService.hash(dados.senha()));
        }
        if( dados.email() != null){
            System.out.println("oi");
            if(valida.emailCliente(dados.email())){
                System.out.println("ENTROU!!");
                return new MensagemResponse("Email já cadastrado!", 409);
            }
            System.out.println("SAIU");
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
        cliente.setNome(CriptografiaService.decriptar(cliente.getNome(), chavePrivada));
        cliente.setCpf(CriptografiaService.decriptar(cliente.getCpf(), chavePrivada));
        cliente.setEmail(CriptografiaService.decriptar(cliente.getEmail(), chavePrivada));
        return cliente;
    }

    static public Cliente cadastrarCliente(Cliente c) {
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
}
