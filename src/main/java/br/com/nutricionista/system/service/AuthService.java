package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.CadastroNutricionistaRequest;
import br.com.nutricionista.system.dto.LoginRequest;
import br.com.nutricionista.system.dto.LoginResponse;
import br.com.nutricionista.system.dto.NutricionistaResponse;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.exception.CredenciaisInvalidasException;
import br.com.nutricionista.system.exception.CrnJaCadastradoException;
import br.com.nutricionista.system.exception.EmailJaCadastradoException;
import br.com.nutricionista.system.repository.NutricionistaRepository;
import br.com.nutricionista.system.security.SistemaUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final NutricionistaRepository nutricionistaRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public NutricionistaResponse cadastrar(CadastroNutricionistaRequest request) {
        if (nutricionistaRepository.existsByEmailIgnoreCase(request.email())) {
            throw new EmailJaCadastradoException();
        }

        if (nutricionistaRepository.existsByCrnIgnoreCase(request.crn())) {
            throw new CrnJaCadastradoException();
        }

        Nutricionista nutricionista = new Nutricionista();
        nutricionista.setNome(request.nome().trim());
        nutricionista.setEmail(request.email().trim().toLowerCase());
        nutricionista.setSenha(passwordEncoder.encode(request.senha()));
        nutricionista.setCrn(request.crn().trim().toUpperCase());
        nutricionista.setAtivo(Boolean.TRUE);

        Nutricionista salvo = nutricionistaRepository.save(nutricionista);
        return mapToResponse(salvo);
    }

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.email().trim().toLowerCase(),
                            request.senha()
                    )
            );
        } catch (Exception ex) {
            throw new CredenciaisInvalidasException();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        SistemaUserDetails userDetails = (SistemaUserDetails) authentication.getPrincipal();

        return new LoginResponse(
                mapToResponse(userDetails.getNutricionista()),
                session.getId()
        );
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private NutricionistaResponse mapToResponse(Nutricionista nutricionista) {
        return new NutricionistaResponse(
                nutricionista.getId(),
                nutricionista.getNome(),
                nutricionista.getEmail(),
                nutricionista.getCrn(),
                nutricionista.getAtivo()
        );
    }
}