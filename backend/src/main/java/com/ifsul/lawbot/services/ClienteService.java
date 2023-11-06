package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.dto.cliente.DetalharClienteRequest;
import com.ifsul.lawbot.dto.cliente.EditarClienteRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public MessageDTO encriptarCliente(CadastrarClienteRequest dados) {
        Cliente cliente = Cliente.builder().build();

        cliente.setDataNascimento(dados.dataNascimento());
        cliente.setSenha(
                HashSenhasService.hash(dados.senha())
        );
        cliente.setNome(
                encriptar(dados.nome())
        );
        cliente.setEmail(
                encriptar(dados.email())
        );
        cliente.setCpf(
                encriptar(dados.cpf())
        );

        repository.save(cliente);
        return new MessageDTO("Usuário cadastrado!");
    }

    public List<ListarClienteRequest> listarClientes() {
        List<Cliente> clientes = repository.findAll();
        return clientes.stream()
                .map(this::descriptografarCliente)
                .map(ListarClienteRequest::new)
                .collect(Collectors.toList());
    }

    public Cliente editarCliente(Long clienteId, EditarClienteRequest dados){
        var cliente = repository.getReferenceById(clienteId);

        if ( dados.senha() != null) {
            cliente.setSenha(HashSenhasService.hash(dados.senha()));
        }
        if( dados.email() != null){
            cliente.setEmail(encriptar(dados.email()));
        }
        if( dados.nome() != null){
            cliente.setNome(encriptar(dados.nome()));
        }

        repository.save(cliente);
        return cliente;
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
        cliente.setNome(CriptografiaService.decriptar(cliente.getNome()));
        cliente.setCpf(CriptografiaService.decriptar(cliente.getCpf()));
        cliente.setEmail(CriptografiaService.decriptar(cliente.getEmail()));
        return cliente;
    }

    static public Cliente encriptarCliente(Cliente c) {


        Cliente cliente = new Cliente();

        cliente.setDataNascimento(c.getDataNascimento());
        cliente.setSenha(
                HashSenhasService.hash(c.getSenha())
        );
        cliente.setNome(
                encriptar(c.getNome())
        );
        cliente.setEmail(
                encriptar(c.getEmail())
        );
        cliente.setCpf(
                encriptar(c.getCpf())
        );
        return cliente;
    }
}
