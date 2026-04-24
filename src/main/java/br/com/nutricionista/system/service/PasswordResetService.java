package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.RedefinirSenhaRequest;
import br.com.nutricionista.system.dto.SolicitarRedefinicaoSenhaRequest;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.TokenRedefinicaoSenha;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.repository.NutricionistaRepository;
import br.com.nutricionista.system.repository.TokenRedefinicaoSenhaRepository;
import br.com.nutricionista.system.util.PasswordResetTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final NutricionistaRepository nutricionistaRepository;
    private final TokenRedefinicaoSenhaRepository tokenRedefinicaoSenhaRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetEmailService passwordResetEmailService;
    private final PasswordResetTokenUtil passwordResetTokenUtil;

    @Value("${app.security.password-reset.expiration-minutes:30}")
    private long expirationMinutes;

    @Transactional
    public void solicitarRedefinicaoSenha(SolicitarRedefinicaoSenhaRequest request) {
        String emailNormalizado = request.email().trim().toLowerCase();

        Optional<Nutricionista> nutricionistaOptional = nutricionistaRepository.findByEmailIgnoreCase(emailNormalizado);

        if (nutricionistaOptional.isEmpty()) {
            return;
        }

        Nutricionista nutricionista = nutricionistaOptional.get();

        if (!Boolean.TRUE.equals(nutricionista.getAtivo())) {
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        tokenRedefinicaoSenhaRepository.invalidarTokensPendentesDoNutricionista(nutricionista.getId(), agora);

        String tokenBruto = passwordResetTokenUtil.gerarToken();
        String tokenHash = passwordResetTokenUtil.gerarHash(tokenBruto);

        TokenRedefinicaoSenha token = new TokenRedefinicaoSenha();
        token.setNutricionista(nutricionista);
        token.setTokenHash(tokenHash);
        token.setDataExpiracao(agora.plusMinutes(expirationMinutes));
        token.setUsado(Boolean.FALSE);
        token.setUsadoEm(null);

        tokenRedefinicaoSenhaRepository.save(token);

        passwordResetEmailService.enviarEmailRedefinicao(
                nutricionista,
                tokenBruto,
                token.getDataExpiracao()
        );
    }

    @Transactional
    public void redefinirSenha(RedefinirSenhaRequest request) {
        validarConfirmacaoSenha(request);

        String tokenHash = passwordResetTokenUtil.gerarHash(request.token());

        TokenRedefinicaoSenha token = tokenRedefinicaoSenhaRepository.findByTokenHash(tokenHash)
                .orElseThrow(this::buildInvalidTokenException);

        validarToken(token);

        Nutricionista nutricionista = token.getNutricionista();

        if (!Boolean.TRUE.equals(nutricionista.getAtivo())) {
            throw buildInvalidTokenException();
        }

        nutricionista.setSenha(passwordEncoder.encode(request.novaSenha()));

        LocalDateTime agora = LocalDateTime.now();
        token.setUsado(Boolean.TRUE);
        token.setUsadoEm(agora);

        nutricionistaRepository.save(nutricionista);
        tokenRedefinicaoSenhaRepository.save(token);
        tokenRedefinicaoSenhaRepository.invalidarTokensPendentesDoNutricionista(nutricionista.getId(), agora);
    }

    private void validarConfirmacaoSenha(RedefinirSenhaRequest request) {
        if (!request.novaSenha().equals(request.confirmacaoNovaSenha())) {
            throw new BusinessException("A confirmação da nova senha não confere.");
        }
    }

    private void validarToken(TokenRedefinicaoSenha token) {
        if (Boolean.TRUE.equals(token.getUsado())) {
            throw buildInvalidTokenException();
        }

        if (token.getDataExpiracao() == null || !token.getDataExpiracao().isAfter(LocalDateTime.now())) {
            throw buildInvalidTokenException();
        }
    }

    private BusinessException buildInvalidTokenException() {
        return new BusinessException("Token inválido, expirado ou já utilizado.");
    }
}