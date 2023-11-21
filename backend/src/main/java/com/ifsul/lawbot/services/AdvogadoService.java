package com.ifsul.lawbot.services;

import com.ifsul.lawbot.dto.advogado.CadastrarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.DetalharAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.EditarAdvogadoRequest;
import com.ifsul.lawbot.dto.advogado.ListarAdvogadoRequest;
import com.ifsul.lawbot.dto.cliente.ListarClienteRequest;
import com.ifsul.lawbot.dto.utils.MensagemResponse;
import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Chave;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import com.ifsul.lawbot.repositories.ProcessoRepository;
import com.ifsul.lawbot.util.ValidaDados;
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
    private ValidaDados valida;

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private GerarChaveService gerarChaveService;

    public MensagemResponse cadastrarAdvogado(CadastrarAdvogadoRequest dados) {
        Advogado advogado = new Advogado();

        if(valida.emailAdvogado(dados.email())){
            return new MensagemResponse("Email já cadastrado!", 409);
        }
        if(valida.CPFAdvogado(dados.cpf())){
            return new MensagemResponse("CPF já cadastrado!", 409);
        }
        if(valida.OABAdvogado(dados.oab())){
            return new MensagemResponse("OAB já cadastrada!", 409);
        }
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
        return new MensagemResponse("Usuário cadastrado!", 200);
    }

    public List<ListarAdvogadoRequest> listarAdvogados() {
        List<Advogado> advogados = repository.findAll();
        return advogados.stream()
                .map(this::descriptografarAdvogado)
                .map(ListarAdvogadoRequest::new)
                .collect(Collectors.toList());
    }

    public MensagemResponse editarAdvogado(Long advogadoId, EditarAdvogadoRequest dados){
        var advogado = repository.getReferenceById(advogadoId);

        if ( dados.senha() != null) {
            advogado.setSenha(HashSenhasService.hash(dados.senha()));
        }
        if( dados.email() != null){
            if(valida.emailAdvogado(dados.email())){
                return new MensagemResponse("Email já cadastrado!", 409);
            }
            advogado.setEmail(CriptografiaService.encriptar(dados.email(), advogado.getChave().getChavePublica()));
        }
        if( dados.nome() != null){
            advogado.setNome(CriptografiaService.encriptar(dados.nome(), advogado.getChave().getChavePublica()));
        }

        repository.save(advogado);
        return new MensagemResponse("Advogado atualizado!", 200);
    }

    public MensagemResponse deletarAdvogado(Long id){
        var advogado = repository.getReferenceById(id);
        repository.delete(advogado);
        return new MensagemResponse("Deletado com sucesso!", 200);
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

    public List<ListarClienteRequest> listarClientesDoAdvogado(Long id){
        var advogado = repository.findById(id);
        var clientes = advogado.get().getClientes()
                .stream().map(this::descriptografarCliente)
                .map(ListarClienteRequest::new)
                .collect(Collectors.toList());
        return clientes;
    }

    private Cliente descriptografarCliente(Cliente cliente) {
        Cliente novoCliente = new Cliente();

        novoCliente.setId(cliente.getId());

        novoCliente.setChave(cliente.getChave());
        PrivateKey chavePrivada = novoCliente.getChave().getChavePrivada();

        novoCliente.setNome(decriptar(cliente.getNome(), chavePrivada));
        novoCliente.setCpf(decriptar(cliente.getCpf(), chavePrivada));
        novoCliente.setEmail(decriptar(cliente.getEmail(), chavePrivada));
        return novoCliente;
    }

    public void definirCliente(Long idCliente, Long idAdvogado) {
        var advogado = repository.findById(idAdvogado);
        var cliente = clienteRepository.findById(idCliente);
        advogado.get().getClientes().add(cliente.get());
        cliente.get().getAdvogados().add(advogado.get());
    }
}
