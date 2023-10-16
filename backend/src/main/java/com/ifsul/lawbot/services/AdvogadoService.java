package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.ListarAdvogadoRequest;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.security.HashSenhas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;

    public ResponseEntity cadastrarAdvogado(CadastrarAdvogadoRequest dados, UriComponentsBuilder uriBuilder){
        var advogado = new Advogado(dados);
        advogado.setSenha(HashSenhas.hash(advogado.getSenha()));
        repository.save(advogado);

        var uri = uriBuilder.path("/advogado/{id}").buildAndExpand(advogado.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharAdvogadoRequest(advogado));
    }

    public ResponseEntity<Page<ListarAdvogadoRequest>> listarAdvogado(Pageable paginacao){
        var page = repository.findAll(paginacao).map(ListarAdvogadoRequest::new);
        return ResponseEntity.ok(page);
    }

    public ResponseEntity editarAdvogado(EditarAdvogadoRequest dados){
        var advogado = repository.getReferenceById(dados.id());
        advogado.atualizar(dados);

        return ResponseEntity.ok(new DetalharAdvogadoRequest(advogado));
    }

    public ResponseEntity deletarAdvogado(Long id){
        var advogado = repository.getReferenceById(id);
        repository.delete(advogado);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity detalharAdvogado(Long id){
        var advogado = repository.getReferenceById(id);

        return ResponseEntity.ok(new DetalharAdvogadoRequest(advogado));
    }
}
