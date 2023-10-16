package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.dto.cliente.EditarClienteRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.services.ClienteService;
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
    ClienteService service;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarCliente(@RequestBody @Valid CadastrarClienteRequest dados, UriComponentsBuilder uriBuilder){
        return service.cadastrarCliente(dados, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<Page<ListarClienteRequest>> listarClientes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        return service.listarClientes(paginacao);
    }

    @PutMapping
    @Transactional
    public ResponseEntity editarCliente(@RequestBody @Valid EditarClienteRequest dados){
        return service.editarCliente(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarCliente(@PathVariable Long id){
        return service.deletarCliente(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharCliente(@PathVariable Long id){
        return service.detalharCliente(id);
    }
}
