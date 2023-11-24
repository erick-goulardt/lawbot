package com.ifsul.lawbot.security;

import com.ifsul.lawbot.entities.Advogado;
import com.ifsul.lawbot.entities.Cliente;
import com.ifsul.lawbot.entities.Usuario;
import com.ifsul.lawbot.repositories.AdvogadoRepository;
import com.ifsul.lawbot.repositories.ClienteRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.ifsul.lawbot.services.CriptografiaService.decriptar;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AdvogadoRepository advogadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJKT = recuperarToken(request);

        if(tokenJKT != null){
            var subject = tokenService.getSubject(tokenJKT);

            Usuario usuario = confereLoginAdvogado(subject);
            if(usuario == null){
                usuario = confereLoginCliente(subject);
            }

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }


        filterChain.doFilter(request, response);

    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null){
            return authorizationHeader.replace("Bearer ", "");

        }
        return null;
    }

    private Advogado confereLoginAdvogado(String login){
        var advogados = advogadoRepository.findAll();
        for(Advogado adv : advogados){
            var email = decriptar(adv.getEmail(), adv.getChave().getChavePrivada());
            if(login.equals(email)){
                return adv;
            }
        }
        return null;

    }

    private Cliente confereLoginCliente(String login){
        var clientes = clienteRepository.findAll();
        for(Cliente cliente : clientes){
            var email = decriptar(cliente.getEmail(), cliente.getChave().getChavePrivada());
            if(login.equals(email)){
                return cliente;
            }
        }
        return null;
    }
}
