package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.cliente.CadastrarClienteRequest;
import com.ifsul.lawbot.dto.cliente.DetalharClienteRequest;
import com.ifsul.lawbot.dto.cliente.EditarClienteRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.dto.processo.ListarProcessosRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteService service;

    @PostMapping("/cadastro")
    @Transactional
    public ResponseEntity<MessageDTO> cadastrarCliente(@RequestBody @Valid CadastrarClienteRequest dados){
        var response = service.cadastrarCliente(dados);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response.mensagem()));
    }

    @GetMapping("/buscarTodos")
    public ResponseEntity<List<ListarClienteRequest>> listarClientes(){
        var response = service.listarClientes();
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/editar/{clienteId}")
    @Transactional
    public ResponseEntity<?> editarCliente(@PathVariable Long clienteId, @RequestBody @Valid EditarClienteRequest dados){
        var response = service.editarCliente(clienteId, dados);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response.mensagem()));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public MessageDTO deletarCliente(@PathVariable Long id){
        return service.deletarCliente(id);
    }

    @GetMapping("/{id}")
    public DetalharClienteRequest detalharCliente(@PathVariable Long id){
        return service.detalharCliente(id);
    }
}
