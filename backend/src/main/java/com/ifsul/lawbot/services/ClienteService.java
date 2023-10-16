package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.dto.cliente.DetalharClienteRequest;
import com.ifsul.lawbot.dto.cliente.EditarClienteRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.security.HashSenhas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public ResponseEntity cadastrarCliente(CadastrarClienteRequest dados, UriComponentsBuilder uriBuilder){

        var cliente = new Cliente(dados);
        cliente.setSenha(HashSenhas.hash(cliente.getSenha()));
        repository.save(cliente);

        var uri = uriBuilder.path("/cliente/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharClienteRequest(cliente));
    }

    public ResponseEntity<Page<ListarClienteRequest>> listarClientes(Pageable paginacao){
        var page = repository.findAll(paginacao).map(ListarClienteRequest::new);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity editarCliente(EditarClienteRequest dados){
        var cliente = repository.getReferenceById(dados.id());
        cliente.atualizar(dados);

        return ResponseEntity.ok(new DetalharClienteRequest(cliente));
    }

    public ResponseEntity deletarCliente(Long id){
        var cliente = repository.getReferenceById(id);
        repository.delete(cliente);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity detalharCliente(Long id){
        var cliente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DetalharClienteRequest(cliente));
    }
}
