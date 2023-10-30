package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.processo.CadastrarProcessoRequest;
import com.ifsul.lawbot.dto.processo.ListarProcessosRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Processo;
import com.ifsul.lawbot.services.ProcessoService;
import jakarta.validation.Valid;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/processo")
public class ProcessoController {

    @Autowired
    private ProcessoService service;

    @PostMapping("/cadastro")
    @Transactional
    public ResponseEntity<MessageDTO> cadastrarProcesso(@RequestBody @Valid CadastrarProcessoRequest dados){
        var response = service.cadastrarProcesso(dados);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/cadastro-novo")
    @Transactional
    public ResponseEntity<MessageDTO> cadastrarProcessoECliente(@RequestBody @Valid CadastrarProcessoRequest dados){
        var response = service.cadastrarProcessoComClienteNovo(dados);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/buscarTodos")
    public ResponseEntity<List<ListarProcessosRequest>> listarProcessos(){
        var response = service.listarProcessos();
        return ResponseEntity.status(200).body(response);
    }
}
