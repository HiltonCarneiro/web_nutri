package br.com.nutricionista.system.security;

import br.com.nutricionista.system.entity.Nutricionista;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class SistemaUserDetails implements UserDetails {

    private final Nutricionista nutricionista;

    public SistemaUserDetails(Nutricionista nutricionista) {
        this.nutricionista = nutricionista;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_NUTRICIONISTA"));
    }

    @Override
    public String getPassword() {
        return nutricionista.getSenha();
    }

    @Override
    public String getUsername() {
        return nutricionista.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(nutricionista.getAtivo());
    }
}