package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.domain.Advogado;
import com.ifsul.lawbot.domain.dto.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.DetalharAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.EditarAdvogadoRequest;
import com.ifsul.lawbot.domain.dto.ListarAdvogadoRequest;
import com.ifsul.lawbot.repository.AdvogadoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/advogado")
public class AdvogadoController {

    @Autowired
    private AdvogadoRepository repository;

    @PostMapping
    @Transactional
    public void cadastrarAdvogado(@RequestBody @Valid CadastrarAdvogadoRequest dados){
        var advogado = new Advogado(dados);
        repository.save(advogado);
    }

    @GetMapping
    public Page<ListarAdvogadoRequest> listarAdvogados(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        var page = repository.findAll(paginacao).map(ListarAdvogadoRequest::new);
        return page;
    }

    @PutMapping
    @Transactional
    public void editarAdvogado(@RequestBody @Valid EditarAdvogadoRequest dados){
        var advogado = repository.getReferenceById(dados.id());
        advogado.atualizar(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletarAdvogado(@PathVariable Long id){
        var advogado = repository.getReferenceById(id);
        repository.delete(advogado);
    }

    @GetMapping("/{id}")
    public DetalharAdvogadoRequest detalharAdvogado(@PathVariable Long id){
        var advogado = repository.getReferenceById(id);
        return new DetalharAdvogadoRequest(advogado);
    }
}
