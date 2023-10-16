package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.ListarAdvogadoRequest;
import com.ifsul.lawbot.services.AdvogadoService;
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
@RequestMapping("/advogado")
public class AdvogadoController {

    @Autowired
    AdvogadoService service;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarAdvogado(@RequestBody @Valid CadastrarAdvogadoRequest dados, UriComponentsBuilder uriBuilder){
        return service.cadastrarAdvogado(dados, uriBuilder);
    }

    @GetMapping
    public ResponseEntity<Page<ListarAdvogadoRequest>> listarAdvogados(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        return service.listarAdvogado(paginacao);
    }

    @PutMapping
    @Transactional
    public ResponseEntity editarAdvogado(@RequestBody @Valid EditarAdvogadoRequest dados){
        return service.editarAdvogado(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarAdvogado(@PathVariable Long id){
        return service.deletarAdvogado(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharAdvogado(@PathVariable Long id){
        return service.detalharAdvogado(id);
    }
}
