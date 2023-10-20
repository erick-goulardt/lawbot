package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.ListarAdvogadoRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.services.AdvogadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advogado")
public class AdvogadoController {

    @Autowired
    private AdvogadoService service;

    @PostMapping("/cadastro")
    @Transactional
    public ResponseEntity<MessageDTO> cadastrarAdvogado(@RequestBody @Valid CadastrarAdvogadoRequest dados){
        var response = service.cadastrarAdvogado(dados);
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/buscarTodos")
    public ResponseEntity<List<ListarAdvogadoRequest>> listarAdvogados(){
        var response = service.listarAdvogados();
        return ResponseEntity.status(200).body(response);
    }

    @PutMapping("/editar/{advogadoId}")
    @Transactional
    public ResponseEntity<?> editarAdvogado(@PathVariable Long advogadoId, @RequestBody @Valid EditarAdvogadoRequest dados){
        Advogado advogadoUpdated = service.editarAdvogado(advogadoId, dados);
        return ResponseEntity.status(200).body("Advogado atualizado!");
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity<MessageDTO> deletarAdvogado(@PathVariable Long id){
        service.deletarAdvogado(id);
        return ResponseEntity.status(200).body(new MessageDTO(("Deletado com sucesso!")));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<DetalharAdvogadoRequest> detalharAdvogado(@PathVariable Long id){
        var response = service.detalharAdvogado(id);
        return ResponseEntity.status(200).body(response);
    }
}
