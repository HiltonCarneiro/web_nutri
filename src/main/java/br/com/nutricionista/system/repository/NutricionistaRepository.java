package br.com.nutricionista.system.repository;

import br.com.nutricionista.system.entity.Nutricionista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NutricionistaRepository extends JpaRepository<Nutricionista, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByCrnIgnoreCase(String crn);

    Optional<Nutricionista> findByEmailIgnoreCase(String email);
}