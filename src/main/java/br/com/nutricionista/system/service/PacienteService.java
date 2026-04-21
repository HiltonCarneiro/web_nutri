package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.AtualizacaoPacienteRequest;
import br.com.nutricionista.system.dto.CadastroPacienteRequest;
import br.com.nutricionista.system.dto.PacienteResponse;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.Paciente;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.exception.CpfPacienteJaCadastradoException;
import br.com.nutricionista.system.exception.EmailPacienteJaCadastradoException;
import br.com.nutricionista.system.exception.ResourceNotFoundException;
import br.com.nutricionista.system.repository.PacienteRepository;
import br.com.nutricionista.system.security.SistemaUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional
    public PacienteResponse cadastrar(CadastroPacienteRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        validarDuplicidadeCadastro(nutricionista.getId(), normalizarCpf(request.cpf()), request.email(), null);

        Paciente paciente = new Paciente();
        preencherDadosPaciente(
                paciente,
                request.nome(),
                request.cpf(),
                request.dataNascimento(),
                request.sexo(),
                request.telefone(),
                request.email(),
                request.endereco(),
                request.observacoes()
        );
        paciente.setAtivo(Boolean.TRUE);
        paciente.setNutricionista(nutricionista);

        return toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public PacienteResponse editar(Long pacienteId, AtualizacaoPacienteRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        validarDuplicidadeCadastro(nutricionista.getId(), normalizarCpf(request.cpf()), request.email(), pacienteId);

        preencherDadosPaciente(
                paciente,
                request.nome(),
                request.cpf(),
                request.dataNascimento(),
                request.sexo(),
                request.telefone(),
                request.email(),
                request.endereco(),
                request.observacoes()
        );

        return toResponse(pacienteRepository.save(paciente));
    }

    @Transactional
    public void excluir(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        paciente.setAtivo(Boolean.FALSE);
        pacienteRepository.save(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> listarAtivos() {
        Nutricionista nutricionista = getNutricionistaAutenticado();

        return pacienteRepository.findAllByNutricionistaIdAndAtivoTrueOrderByNomeAsc(nutricionista.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PacienteResponse> buscarPorNome(String nome) {
        Nutricionista nutricionista = getNutricionistaAutenticado();

        if (!StringUtils.hasText(nome)) {
            return listarAtivos();
        }

        return pacienteRepository
                .findByNutricionistaIdAndAtivoTrueAndNomeContainingIgnoreCaseOrderByNomeAsc(
                        nutricionista.getId(),
                        nome.trim()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());
        return toResponse(paciente);
    }

    private Paciente buscarPacienteAtivoDoNutricionista(Long pacienteId, Long nutricionistaId) {
        return pacienteRepository.findByIdAndNutricionistaIdAndAtivoTrue(pacienteId, nutricionistaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado para o nutricionista autenticado."
                ));
    }

    private void validarDuplicidadeCadastro(Long nutricionistaId, String cpfNormalizado, String email, Long pacienteId) {
        if (pacienteId == null) {
            if (pacienteRepository.existsByNutricionistaIdAndCpfAndAtivoTrue(nutricionistaId, cpfNormalizado)) {
                throw new CpfPacienteJaCadastradoException();
            }

            if (StringUtils.hasText(email)
                    && pacienteRepository.existsByNutricionistaIdAndEmailIgnoreCaseAndAtivoTrue(
                    nutricionistaId,
                    email.trim()
            )) {
                throw new EmailPacienteJaCadastradoException();
            }

            return;
        }

        if (pacienteRepository.existsByNutricionistaIdAndCpfAndAtivoTrueAndIdNot(
                nutricionistaId,
                cpfNormalizado,
                pacienteId
        )) {
            throw new CpfPacienteJaCadastradoException();
        }

        if (StringUtils.hasText(email)
                && pacienteRepository.existsByNutricionistaIdAndEmailIgnoreCaseAndAtivoTrueAndIdNot(
                nutricionistaId,
                email.trim(),
                pacienteId
        )) {
            throw new EmailPacienteJaCadastradoException();
        }
    }

    private void preencherDadosPaciente(
            Paciente paciente,
            String nome,
            String cpf,
            LocalDate dataNascimento,
            String sexo,
            String telefone,
            String email,
            String endereco,
            String observacoes
    ) {
        paciente.setNome(nome.trim());
        paciente.setCpf(normalizarCpf(cpf));
        paciente.setDataNascimento(dataNascimento);
        paciente.setSexo(sexo.trim());
        paciente.setTelefone(normalizarTexto(telefone));
        paciente.setEmail(normalizarEmail(email));
        paciente.setEndereco(normalizarTexto(endereco));
        paciente.setObservacoes(normalizarTexto(observacoes));
    }

    private String normalizarCpf(String cpf) {
        return cpf.replaceAll("\\D", "");
    }

    private String normalizarEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    private String normalizarTexto(String valor) {
        if (!StringUtils.hasText(valor)) {
            return null;
        }
        return valor.trim();
    }

    private Nutricionista getNutricionistaAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof SistemaUserDetails userDetails)) {
            throw new BusinessException("Usuário autenticado inválido.", HttpStatus.UNAUTHORIZED);
        }

        return userDetails.getNutricionista();
    }

    private PacienteResponse toResponse(Paciente paciente) {
        return new PacienteResponse(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getSexo(),
                paciente.getTelefone(),
                paciente.getEmail(),
                paciente.getEndereco(),
                paciente.getObservacoes(),
                paciente.getAtivo(),
                paciente.getCreatedAt(),
                paciente.getUpdatedAt()
        );
    }
}