package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.domain.Advogado;
import com.ifsul.lawbot.domain.dto.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.DetalharAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.EditarAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.ListarAdvogadoRequest;
import com.ifsul.lawbot.infra.security.HashSenhas;
import com.ifsul.lawbot.repository.AdvogadoRepository;
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
    private AdvogadoRepository repository;

    @Autowired
    private HashSenhas hashSenhas;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid CadastrarAdvogadoRequest dados, UriComponentsBuilder uriBuilder){

        var advogado = new Advogado(dados);
        advogado.setSenha(hashSenhas.hash(advogado.getSenha()));
        repository.save(advogado);

        var uri = uriBuilder.path("/advogado/{id}").buildAndExpand(advogado.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharAdvogadoRequest(advogado));
    }

    @GetMapping
    public ResponseEntity<Page<ListarAdvogadoRequest>> listarAdvogados(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page = repository.findAll(paginacao).map(ListarAdvogadoRequest::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity editarAdvogado(@RequestBody @Valid EditarAdvogadoRequest dados){
        var advogado = repository.getReferenceById(dados.id());
        advogado.atualizar(dados);

        return ResponseEntity.ok(new DetalharAdvogadoRequest(advogado));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarAdvogado(@PathVariable Long id){
        var advogado = repository.getReferenceById(id);
        repository.delete(advogado);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharAdvogado(@PathVariable Long id){
        var advogado = repository.getReferenceById(id);

        return ResponseEntity.ok(new DetalharAdvogadoRequest(advogado));
    }
}
