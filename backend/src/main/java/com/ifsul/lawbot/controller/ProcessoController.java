package com.ifsul.lawbot.controller;

import com.ifsul.lawbot.dto.historico.ListarHistoricoResponse;
import com.ifsul.lawbot.dto.processo.*;
import com.ifsul.lawbot.dto.utils.MessageDTO;

import com.ifsul.lawbot.services.ProcessoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return ResponseEntity.status(response.status()).body(new MessageDTO(response));
    }

    @GetMapping("/buscarTodos")
    public ResponseEntity<List<ListarProcessosRequest>> listarProcessos(){
        var response = service.listarProcessos();
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/advogado/{id}")
    public List<ListarProcessosRequest> listarProcessosPeloAdvogado(@PathVariable Long id){
        var response = service.listarPeloAdvogado(id);
        return response;
    }

    @GetMapping("/cliente/{id}")
    public List<ListarProcessosRequest> listarProcessosPeloCliente(@PathVariable Long id){
        var response = service.listarPeloCliente(id);
        return response;
    }

    @PostMapping("/cadastro-arquivo/{id}")
    public ResponseEntity<MessageDTO> cadastrarProcessosEmBloco(@RequestParam("file") MultipartFile file, @PathVariable Long id){
        var response = service.cadastrarProcessoEmBloco(file, id);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response));
    }

    @GetMapping("/reu/{id}")
    public List<ListarReusRequest> listaReu(@PathVariable Long id){
        var response = service.listaReu((Long) id);
        return response;
    }

    @GetMapping("/autor/{id}")
    public List<ListarAutoresRequest> listaAutor(@PathVariable Long id){
        var response = service.listaAutor( id);
        return response;
    }

    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity<MessageDTO> atualizaProcesso(@PathVariable Long id, @RequestBody @Valid EditarProcessoRequest dados){
        var response = service.atualizarProcesso(id, dados);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response));
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity<MessageDTO> deletaProcesso(@PathVariable Long id){
        var response = service.deletarProcesso(id);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response));
    }

    @GetMapping("/historico/{id}")
    public List<ListarHistoricoResponse> listarHistorico(@PathVariable Long id){
        System.out.println();
        var response = service.listaHistoricoPorProcesso(id);
        return response;
    }

    @PutMapping("/definir-cliente")
    @Transactional
    public ResponseEntity<MessageDTO> definirCliente(@RequestBody DefinirClienteRequest dados){
        var response = service.defineCliente(dados);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response));
    }

    @PutMapping("/definir-descricao")
    @Transactional
    public ResponseEntity<MessageDTO> definirDescricao(@RequestBody DefinirDescricaoRequest dados){
        var response = service.defineDescricao(dados);
        return ResponseEntity.status(response.status()).body(new MessageDTO(response));
    }

    @GetMapping("/semCliente/{idAdvogado}")
    public List<ListarProcessosRequest> listarProcessoSemCliente(@PathVariable Long idAdvogado){
        var response = service.listarProcessoSemCliente(idAdvogado);
        return response;
    }

    @GetMapping("/comCliente/{idAdvogado}")
    public List<ListarProcessosRequest> listarProcessoComCliente(@PathVariable Long idAdvogado){
        var response = service.listarProcessoComCliente(idAdvogado);
        return response;
    }
}
