package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.AtualizacaoEvolucaoRequest;
import br.com.nutricionista.system.dto.CadastroEvolucaoRequest;
import br.com.nutricionista.system.dto.EvolucaoConsultaResponse;
import br.com.nutricionista.system.dto.PacienteResponse;
import br.com.nutricionista.system.dto.ProntuarioPacienteResponse;
import br.com.nutricionista.system.entity.EvolucaoConsulta;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.Paciente;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.exception.ResourceNotFoundException;
import br.com.nutricionista.system.repository.EvolucaoConsultaRepository;
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
public class EvolucaoConsultaService {

    private final EvolucaoConsultaRepository evolucaoConsultaRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public EvolucaoConsultaResponse cadastrar(Long pacienteId, CadastroEvolucaoRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        EvolucaoConsulta evolucao = new EvolucaoConsulta();
        preencherDadosEvolucao(
                evolucao,
                request.dataConsulta(),
                request.observacoes(),
                request.condutas(),
                request.queixasPaciente(),
                request.metasDefinidas()
        );
        evolucao.setAtivo(Boolean.TRUE);
        evolucao.setPaciente(paciente);
        evolucao.setNutricionista(nutricionista);

        return toResponse(evolucaoConsultaRepository.save(evolucao));
    }

    @Transactional(readOnly = true)
    public List<EvolucaoConsultaResponse> listarPorPaciente(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        return evolucaoConsultaRepository
                .findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataConsultaDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EvolucaoConsultaResponse buscarPorId(Long pacienteId, Long evolucaoId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        EvolucaoConsulta evolucao = buscarEvolucaoAtivaDoPaciente(
                evolucaoId,
                pacienteId,
                nutricionista.getId()
        );

        return toResponse(evolucao);
    }

    @Transactional
    public EvolucaoConsultaResponse editar(
            Long pacienteId,
            Long evolucaoId,
            AtualizacaoEvolucaoRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        EvolucaoConsulta evolucao = buscarEvolucaoAtivaDoPaciente(
                evolucaoId,
                pacienteId,
                nutricionista.getId()
        );

        preencherDadosEvolucao(
                evolucao,
                request.dataConsulta(),
                request.observacoes(),
                request.condutas(),
                request.queixasPaciente(),
                request.metasDefinidas()
        );

        return toResponse(evolucaoConsultaRepository.save(evolucao));
    }

    @Transactional
    public void excluir(Long pacienteId, Long evolucaoId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        EvolucaoConsulta evolucao = buscarEvolucaoAtivaDoPaciente(
                evolucaoId,
                pacienteId,
                nutricionista.getId()
        );

        evolucao.setAtivo(Boolean.FALSE);
        evolucaoConsultaRepository.save(evolucao);
    }

    @Transactional(readOnly = true)
    public ProntuarioPacienteResponse obterProntuarioCompleto(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        List<EvolucaoConsultaResponse> evolucoes = evolucaoConsultaRepository
                .findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataConsultaDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();

        return new ProntuarioPacienteResponse(
                toPacienteResponse(paciente),
                evolucoes
        );
    }

    private Paciente buscarPacienteAtivoDoNutricionista(Long pacienteId, Long nutricionistaId) {
        return pacienteRepository.findByIdAndNutricionistaIdAndAtivoTrue(pacienteId, nutricionistaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado para o nutricionista autenticado."
                ));
    }

    private EvolucaoConsulta buscarEvolucaoAtivaDoPaciente(
            Long evolucaoId,
            Long pacienteId,
            Long nutricionistaId
    ) {
        return evolucaoConsultaRepository
                .findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
                        evolucaoId,
                        pacienteId,
                        nutricionistaId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evolução não encontrada para o paciente informado."
                ));
    }

    private void preencherDadosEvolucao(
            EvolucaoConsulta evolucao,
            LocalDate dataConsulta,
            String observacoes,
            String condutas,
            String queixasPaciente,
            String metasDefinidas
    ) {
        evolucao.setDataConsulta(dataConsulta);
        evolucao.setObservacoes(observacoes.trim());
        evolucao.setCondutas(normalizarTexto(condutas));
        evolucao.setQueixasPaciente(normalizarTexto(queixasPaciente));
        evolucao.setMetasDefinidas(normalizarTexto(metasDefinidas));
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

    private EvolucaoConsultaResponse toResponse(EvolucaoConsulta evolucao) {
        return new EvolucaoConsultaResponse(
                evolucao.getId(),
                evolucao.getDataConsulta(),
                evolucao.getObservacoes(),
                evolucao.getCondutas(),
                evolucao.getQueixasPaciente(),
                evolucao.getMetasDefinidas(),
                evolucao.getAtivo(),
                evolucao.getCreatedAt(),
                evolucao.getUpdatedAt()
        );
    }

    private PacienteResponse toPacienteResponse(Paciente paciente) {
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