package br.com.nutricionista.system.security;

import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.repository.NutricionistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final NutricionistaRepository nutricionistaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Nutricionista nutricionista = nutricionistaRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if (Boolean.FALSE.equals(nutricionista.getAtivo())) {
            throw new DisabledException("Usuário inativo.");
        }

        return new SistemaUserDetails(nutricionista);
    }
}