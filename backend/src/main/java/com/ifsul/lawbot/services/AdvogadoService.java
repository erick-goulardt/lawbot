package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.advogado.*;
import com.ifsul.lawbot.dto.utils.MessageDTO;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;
import java.util.stream.Collectors;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;
import static com.ifsul.lawbot.services.CriptografiaService.encriptar;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private GerarChaveService gerarChaveService;


    public MessageDTO cadastrarAdvogado(CadastrarAdvogadoRequest dados) {
        Advogado advogado = Advogado.builder().dataNascimento(dados.dataNascimento()).senha(HashSenhasService.hash(dados.senha())).build();

        Chave key = gerarChaveService.findKey();
        advogado.setCpf(
<<<<<<< HEAD
                CriptografiaService.encriptar(dados.cpf(), key.getChavePublica())
        );
        advogado.setNome(
                CriptografiaService.encriptar(dados.nome(), key.getChavePublica())
        );
        advogado.setEmail(
                CriptografiaService.encriptar(dados.email(), key.getChavePublica())
        );
        advogado.setOab(
                CriptografiaService.encriptar(dados.oab(), key.getChavePublica())
=======
                encriptar(dados.cpf(), key.getChavePublica())
        );
        advogado.setNome(
                encriptar(dados.nome(), key.getChavePublica())
        );
        advogado.setEmail(
                encriptar(dados.email(), key.getChavePublica())
        );
        advogado.setOab(
                encriptar(dados.oab(), key.getChavePublica())
>>>>>>> f4f29d47b9c8edc301dad3602290975262fb8167
        );
        advogado.setChave(key);
        repository.save(advogado);
        return new MessageDTO("Usuário cadastrado!");
    }

<<<<<<< HEAD
    public List<ListarAdvogadoRequest> listarAdvogados() {
        List<Advogado> advogados = repository.findAll();
        return advogados.stream()
                .map(this::descriptografarAdvogado)
                .map(ListarAdvogadoRequest::new)
                .collect(Collectors.toList());
=======
    public ResponseEntity<List<ListarAdvogadoRequest>> listarAdvogados() {
        List<ListarAdvogadoRequest> advogadosDecriptado = new ArrayList<>();
        List<Advogado> advogados = repository.findAll().stream().map(
                advogado -> {
                    advogado.setNome(decriptar(advogado.getNome(), advogado.getChave().getChavePrivada()));
                    advogado.setCpf(decriptar(advogado.getCpf(), advogado.getChave().getChavePrivada()));
                    advogado.setOab(decriptar(advogado.getOab(), advogado.getChave().getChavePrivada()));
                    advogado.setEmail(decriptar(advogado.getEmail(), advogado.getChave().getChavePrivada()));

                    advogadosDecriptado.add(new ListarAdvogadoRequest(advogado));
                    return advogado;

                }
        ).toList();

        return ResponseEntity.ok(advogadosDecriptado);
>>>>>>> f4f29d47b9c8edc301dad3602290975262fb8167
    }

    public Advogado editarAdvogado(Long advogadoId, EditarAdvogadoRequest dados){
        var advogado = repository.getReferenceById(advogadoId);

        if ( dados.senha() != null) {
            advogado.setSenha(HashSenhasService.hash(dados.senha()));
        }
        if( dados.email() != null){
<<<<<<< HEAD
            advogado.setEmail(CriptografiaService.encriptar(dados.email(), advogado.getChave().getChavePublica()));
        }
        if( dados.nome() != null){
            advogado.setNome(CriptografiaService.encriptar(dados.nome(), advogado.getChave().getChavePublica()));
=======
            advogado.setEmail(encriptar(dados.email(), advogado.getChave().getChavePublica()));
        }
        if( dados.nome() != null){
            advogado.setNome(encriptar(dados.nome(), advogado.getChave().getChavePublica()));
>>>>>>> f4f29d47b9c8edc301dad3602290975262fb8167
        }
        return advogado;
    }

    public MessageDTO deletarAdvogado(Long id){
        var advogado = repository.getReferenceById(id);
        repository.delete(advogado);
        return new MessageDTO("Deletado com sucesso!");
    }

    public DetalharAdvogadoRequest detalharAdvogado(Long id){
        Advogado advogado = repository.getReferenceById(id);
        Advogado advogadoDecriptografado = descriptografarAdvogado(advogado);
        advogadoDecriptografado.setId(id);
<<<<<<< HEAD
=======
        advogadoDecriptografado.setNome(decriptar(advogado.getNome(), advogado.getChave().getChavePrivada()));
        advogadoDecriptografado.setCpf(decriptar(advogado.getCpf(), advogado.getChave().getChavePrivada()));
        advogadoDecriptografado.setOab(decriptar(advogado.getOab(), advogado.getChave().getChavePrivada()));
        advogadoDecriptografado.setEmail(decriptar(advogado.getEmail(), advogado.getChave().getChavePrivada()));
>>>>>>> f4f29d47b9c8edc301dad3602290975262fb8167
        advogadoDecriptografado.setDataNascimento(advogado.getDataNascimento());

        return new DetalharAdvogadoRequest(advogadoDecriptografado.getNome(), advogadoDecriptografado.getEmail(), advogadoDecriptografado.getOab(),
                advogadoDecriptografado.getCpf(), advogadoDecriptografado.getDataNascimento());
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
