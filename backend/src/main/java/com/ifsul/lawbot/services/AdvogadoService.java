package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.ListarAdvogadoRequest;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ChaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private GerarChaveService gerarChaveService;

    @Autowired
    private CriptografiaService cript;

    public ResponseEntity cadastrarAdvogado(CadastrarAdvogadoRequest dados, UriComponentsBuilder uriBuilder){
        Advogado advogado = Advogado.builder().dataNascimento(dados.dataNascimento()).senha(HashSenhasService.hash(dados.senha())).build();

        Chave key = gerarChaveService.findKey();
        advogado.setCpf(
                cript.encriptar(dados.cpf(), key.getChavePublica())
        );
        advogado.setNome(
                cript.encriptar(dados.nome(), key.getChavePublica())
        );
        advogado.setEmail(
                cript.encriptar(dados.email(), key.getChavePublica())
        );
        advogado.setOab(
                cript.encriptar(dados.oab(), key.getChavePublica())
        );
        advogado.setChave(key);

        repository.save(advogado);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(advogado.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharAdvogadoRequest(advogado));

    }

    public List<Advogado> listarAdvogados() {
        int i = 0;
        List<Advogado> advogados = repository.findAll();
        List<Advogado> advogadosDecriptado = new ArrayList<>();
        for (Advogado advogado : advogados) {
            Chave chave = advogados.get(i).getChave();
            advogado.setNome(cript.decriptar(advogados.get(i).getNome(), chave.getChavePrivada()));
            advogado.setCpf(cript.decriptar(advogados.get(i).getCpf(), chave.getChavePrivada()));
            advogado.setOab(cript.decriptar(advogados.get(i).getOab(), chave.getChavePrivada()));
            advogado.setEmail(cript.decriptar(advogados.get(i).getEmail(), chave.getChavePrivada()));
            advogado.setDataNascimento(advogados.get(i).getDataNascimento());
            advogadosDecriptado.add(advogado);
            i++;
        }
        return advogadosDecriptado;
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
