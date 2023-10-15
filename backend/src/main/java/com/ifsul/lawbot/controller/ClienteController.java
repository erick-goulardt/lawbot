package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.domain.advogado.Advogado;
import com.ifsul.lawbot.domain.advogado.dto.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.domain.advogado.dto.DetalharAdvogadoRequest;
import com.ifsul.lawbot.domain.advogado.dto.EditarAdvogadoRequest;
import com.ifsul.lawbot.domain.advogado.dto.ListarAdvogadoRequest;
import com.ifsul.lawbot.domain.cliente.Cliente;
import com.ifsul.lawbot.domain.cliente.dto.CadastrarClienteRequest;
import com.ifsul.lawbot.domain.cliente.dto.DetalharClienteRequest;
import com.ifsul.lawbot.domain.cliente.dto.EditarClienteRequest;
import com.ifsul.lawbot.domain.cliente.dto.ListarClienteRequest;
import com.ifsul.lawbot.infra.security.HashSenhas;
import com.ifsul.lawbot.repository.AdvogadoRepository;
import com.ifsul.lawbot.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private HashSenhas hashSenhas;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid CadastrarClienteRequest dados, UriComponentsBuilder uriBuilder){

        var cliente = new Cliente(dados);
        cliente.setSenha(hashSenhas.hash(cliente.getSenha()));
        repository.save(cliente);

        var uri = uriBuilder.path("/cliente/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharClienteRequest(cliente));
    }

    @GetMapping
    public ResponseEntity<Page<ListarClienteRequest>> listarAdvogados(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page = repository.findAll(paginacao).map(ListarClienteRequest::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity editarAdvogado(@RequestBody @Valid EditarClienteRequest dados){
        var cliente = repository.getReferenceById(dados.id());
        cliente.atualizar(dados);

        return ResponseEntity.ok(new DetalharClienteRequest(cliente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarAdvogado(@PathVariable Long id){
        var cliente = repository.getReferenceById(id);
        repository.delete(cliente);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharAdvogado(@PathVariable Long id){
        var cliente = repository.getReferenceById(id);

        return ResponseEntity.ok(new DetalharClienteRequest(cliente));
    }
}
