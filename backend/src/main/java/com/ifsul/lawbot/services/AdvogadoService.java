package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.ListarAdvogadoRequest;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private GerarChaveService gerarChaveService;

    public MessageDTO cadastrarAdvogado(CadastrarAdvogadoRequest dados) {
        Advogado advogado = Advogado.builder().build();

        advogado.setDataNascimento(dados.dataNascimento());
        advogado.setSenha(HashSenhasService.hash(dados.senha())
        );

        Chave key = gerarChaveService.findKey();
        advogado.setNome(
                encriptar(dados.nome(), key.getChavePublica())
        );
        advogado.setEmail(
                encriptar(dados.email(), key.getChavePublica())
        );
        advogado.setOab(
                encriptar(dados.oab(), key.getChavePublica())
        );
        advogado.setCpf(
                encriptar(dados.cpf(), key.getChavePublica())
        );
        advogado.setChave(key);
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
            advogado.setEmail(CriptografiaService.encriptar(dados.email(), advogado.getChave().getChavePublica()));
        }
        if( dados.nome() != null){
            advogado.setNome(CriptografiaService.encriptar(dados.nome(), advogado.getChave().getChavePublica()));
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
        PrivateKey chavePrivada = advogado.getChave().getChavePrivada();
        advogado.setNome(CriptografiaService.decriptar(advogado.getNome(), chavePrivada));
        advogado.setCpf(CriptografiaService.decriptar(advogado.getCpf(), chavePrivada));
        advogado.setOab(CriptografiaService.decriptar(advogado.getOab(), chavePrivada));
        advogado.setEmail(CriptografiaService.decriptar(advogado.getEmail(), chavePrivada));
        return advogado;
    }
}
