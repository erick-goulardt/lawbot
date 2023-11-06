package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.advogado.*;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;


    public MessageDTO cadastrarAdvogado(CadastrarAdvogadoRequest dados) {
        Advogado advogado = Advogado.builder().build();

        advogado.setDataNascimento(dados.dataNascimento());
        advogado.setSenha(HashSenhasService.hash(dados.senha())
        );

        advogado.setNome(
                encriptar(dados.nome())
        );
        advogado.setEmail(
                encriptar(dados.email())
        );
        advogado.setOab(
                encriptar(dados.oab())
        );
        advogado.setCpf(
                encriptar(dados.cpf())
        );
        repository.save(advogado);
        return new MessageDTO("Usuário cadastrado!");
    }

    public List<ListarAdvogadoRequest> listarAdvogados() {
        List<Advogado> advogados = repository.findAll();
        return advogados.stream()
                .map(this::descriptografarAdvogado)
                .map(ListarAdvogadoRequest::new)
                .collect(Collectors.toList());
    }

    public Advogado editarAdvogado(Long advogadoId, EditarAdvogadoRequest dados){
        var advogado = repository.getReferenceById(advogadoId);

        if ( dados.senha() != null) {
            advogado.setSenha(HashSenhasService.hash(dados.senha()));
        }
        if( dados.email() != null){
            advogado.setEmail(CriptografiaService.encriptar(dados.email()));
        }
        if( dados.nome() != null){
            advogado.setNome(CriptografiaService.encriptar(dados.nome()));
        }

        repository.save(advogado);
        return advogado;
    }

    public MessageDTO deletarAdvogado(Long id){
        var advogado = repository.getReferenceById(id);
        repository.delete(advogado);
        return new MessageDTO("Deletado com sucesso!");
    }

    public DetalharAdvogadoRequest detalharAdvogado(Long id) {
        Advogado advogado = repository.getReferenceById(id);
        Advogado advogadoDecriptografado = descriptografarAdvogado(advogado);

        return new DetalharAdvogadoRequest(advogadoDecriptografado.getId(), advogadoDecriptografado.getNome(), advogadoDecriptografado.getEmail(),
                advogadoDecriptografado.getOab(), advogadoDecriptografado.getCpf(), advogadoDecriptografado.getDataNascimento());
    }

    private Advogado descriptografarAdvogado(Advogado advogado) {
        advogado.setNome(CriptografiaService.decriptar(advogado.getNome()));
        advogado.setCpf(CriptografiaService.decriptar(advogado.getCpf()));
        advogado.setOab(CriptografiaService.decriptar(advogado.getOab()));
        advogado.setEmail(CriptografiaService.decriptar(advogado.getEmail()));
        return advogado;
    }
}
