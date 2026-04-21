package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.AtualizacaoAvaliacaoAntropometricaRequest;
import br.com.nutricionista.system.dto.AvaliacaoAntropometricaResponse;
import br.com.nutricionista.system.dto.CadastroAvaliacaoAntropometricaRequest;
import br.com.nutricionista.system.dto.HistoricoAntropometricoResponse;
import br.com.nutricionista.system.entity.AvaliacaoAntropometrica;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.Paciente;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.exception.ResourceNotFoundException;
import br.com.nutricionista.system.repository.AvaliacaoAntropometricaRepository;
import br.com.nutricionista.system.repository.PacienteRepository;
import br.com.nutricionista.system.security.SistemaUserDetails;
import br.com.nutricionista.system.util.AntropometriaCalculatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoAntropometricaService {

    private final AvaliacaoAntropometricaRepository avaliacaoAntropometricaRepository;
    private final PacienteRepository pacienteRepository;
    private final AntropometriaCalculatorUtil antropometriaCalculatorUtil;

    @Transactional
    public AvaliacaoAntropometricaResponse cadastrar(Long pacienteId, CadastroAvaliacaoAntropometricaRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        AvaliacaoAntropometrica avaliacao = new AvaliacaoAntropometrica();
        preencherDadosAvaliacao(avaliacao, paciente, nutricionista, request);
        avaliacao.setAtivo(Boolean.TRUE);

        return toResponse(avaliacaoAntropometricaRepository.save(avaliacao));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoAntropometricaResponse> listarPorPaciente(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        return avaliacaoAntropometricaRepository
                .findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataAvaliacaoDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AvaliacaoAntropometricaResponse buscarPorId(Long pacienteId, Long avaliacaoId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        AvaliacaoAntropometrica avaliacao = buscarAvaliacaoAtivaDoPaciente(
                avaliacaoId,
                pacienteId,
                nutricionista.getId()
        );

        return toResponse(avaliacao);
    }

    @Transactional(readOnly = true)
    public List<HistoricoAntropometricoResponse> obterHistoricoAntropometrico(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        return avaliacaoAntropometricaRepository
                .findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByDataAvaliacaoDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toHistoricoResponse)
                .toList();
    }

    @Transactional
    public AvaliacaoAntropometricaResponse editar(
            Long pacienteId,
            Long avaliacaoId,
            AtualizacaoAvaliacaoAntropometricaRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        AvaliacaoAntropometrica avaliacao = buscarAvaliacaoAtivaDoPaciente(
                avaliacaoId,
                pacienteId,
                nutricionista.getId()
        );

        preencherDadosAvaliacao(avaliacao, paciente, nutricionista, request);

        return toResponse(avaliacaoAntropometricaRepository.save(avaliacao));
    }

    private void preencherDadosAvaliacao(
            AvaliacaoAntropometrica avaliacao,
            Paciente paciente,
            Nutricionista nutricionista,
            CadastroAvaliacaoAntropometricaRequest request
    ) {
        preencherDadosBase(
                avaliacao,
                paciente,
                nutricionista,
                request.dataAvaliacao(),
                request.peso(),
                request.altura(),
                request.circunferenciaCintura(),
                request.circunferenciaQuadril(),
                request.circunferenciaBraco(),
                request.circunferenciaCoxa(),
                request.circunferenciaPanturrilha(),
                request.circunferenciaPescoco(),
                request.tricipital(),
                request.subescapular(),
                request.peitoral(),
                request.axilarMedia(),
                request.suprailiaca(),
                request.abdominal(),
                request.coxa(),
                request.observacoes()
        );
    }

    private void preencherDadosAvaliacao(
            AvaliacaoAntropometrica avaliacao,
            Paciente paciente,
            Nutricionista nutricionista,
            AtualizacaoAvaliacaoAntropometricaRequest request
    ) {
        preencherDadosBase(
                avaliacao,
                paciente,
                nutricionista,
                request.dataAvaliacao(),
                request.peso(),
                request.altura(),
                request.circunferenciaCintura(),
                request.circunferenciaQuadril(),
                request.circunferenciaBraco(),
                request.circunferenciaCoxa(),
                request.circunferenciaPanturrilha(),
                request.circunferenciaPescoco(),
                request.tricipital(),
                request.subescapular(),
                request.peitoral(),
                request.axilarMedia(),
                request.suprailiaca(),
                request.abdominal(),
                request.coxa(),
                request.observacoes()
        );
    }

    private void preencherDadosBase(
            AvaliacaoAntropometrica avaliacao,
            Paciente paciente,
            Nutricionista nutricionista,
            LocalDate dataAvaliacao,
            BigDecimal peso,
            BigDecimal altura,
            BigDecimal circunferenciaCintura,
            BigDecimal circunferenciaQuadril,
            BigDecimal circunferenciaBraco,
            BigDecimal circunferenciaCoxa,
            BigDecimal circunferenciaPanturrilha,
            BigDecimal circunferenciaPescoco,
            BigDecimal tricipital,
            BigDecimal subescapular,
            BigDecimal peitoral,
            BigDecimal axilarMedia,
            BigDecimal suprailiaca,
            BigDecimal abdominal,
            BigDecimal coxa,
            String observacoes
    ) {
        BigDecimal somaDobras = antropometriaCalculatorUtil.calcularSomaDobras(
                tricipital,
                subescapular,
                peitoral,
                axilarMedia,
                suprailiaca,
                abdominal,
                coxa
        );

        BigDecimal imc = antropometriaCalculatorUtil.calcularImc(peso, altura);
        BigDecimal percentualGordura = antropometriaCalculatorUtil.calcularPercentualGordura(
                paciente.getSexo(),
                paciente.getDataNascimento(),
                dataAvaliacao,
                somaDobras
        );
        BigDecimal relacaoCinturaQuadril = antropometriaCalculatorUtil.calcularRelacaoCinturaQuadril(
                circunferenciaCintura,
                circunferenciaQuadril
        );
        BigDecimal relacaoCinturaEstatura = antropometriaCalculatorUtil.calcularRelacaoCinturaEstatura(
                circunferenciaCintura,
                altura
        );

        avaliacao.setDataAvaliacao(dataAvaliacao);
        avaliacao.setPeso(normalizarDecimal(peso));
        avaliacao.setAltura(normalizarDecimal(altura));
        avaliacao.setImc(imc);
        avaliacao.setCircunferenciaCintura(normalizarDecimal(circunferenciaCintura));
        avaliacao.setCircunferenciaQuadril(normalizarDecimal(circunferenciaQuadril));
        avaliacao.setCircunferenciaBraco(normalizarDecimalOpcional(circunferenciaBraco));
        avaliacao.setCircunferenciaCoxa(normalizarDecimalOpcional(circunferenciaCoxa));
        avaliacao.setCircunferenciaPanturrilha(normalizarDecimalOpcional(circunferenciaPanturrilha));
        avaliacao.setCircunferenciaPescoco(normalizarDecimalOpcional(circunferenciaPescoco));
        avaliacao.setTricipital(normalizarDecimal(tricipital));
        avaliacao.setSubescapular(normalizarDecimal(subescapular));
        avaliacao.setPeitoral(normalizarDecimal(peitoral));
        avaliacao.setAxilarMedia(normalizarDecimal(axilarMedia));
        avaliacao.setSuprailiaca(normalizarDecimal(suprailiaca));
        avaliacao.setAbdominal(normalizarDecimal(abdominal));
        avaliacao.setCoxa(normalizarDecimal(coxa));
        avaliacao.setSomaDobras(somaDobras);
        avaliacao.setPercentualGordura(percentualGordura);
        avaliacao.setRelacaoCinturaQuadril(relacaoCinturaQuadril);
        avaliacao.setRelacaoCinturaEstatura(relacaoCinturaEstatura);
        avaliacao.setObservacoes(normalizarTexto(observacoes));
        avaliacao.setPaciente(paciente);
        avaliacao.setNutricionista(nutricionista);
    }

    private Paciente buscarPacienteAtivoDoNutricionista(Long pacienteId, Long nutricionistaId) {
        return pacienteRepository.findByIdAndNutricionistaIdAndAtivoTrue(pacienteId, nutricionistaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado para o nutricionista autenticado."
                ));
    }

    private AvaliacaoAntropometrica buscarAvaliacaoAtivaDoPaciente(
            Long avaliacaoId,
            Long pacienteId,
            Long nutricionistaId
    ) {
        return avaliacaoAntropometricaRepository
                .findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
                        avaliacaoId,
                        pacienteId,
                        nutricionistaId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Avaliação antropométrica não encontrada para o paciente informado."
                ));
    }

    private BigDecimal normalizarDecimal(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Todos os campos numéricos obrigatórios devem ser maiores que zero.");
        }
        return valor.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private BigDecimal normalizarDecimalOpcional(BigDecimal valor) {
        if (valor == null) {
            return null;
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Os campos numéricos opcionais, quando informados, devem ser maiores que zero.");
        }
        return valor.setScale(2, java.math.RoundingMode.HALF_UP);
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

    private AvaliacaoAntropometricaResponse toResponse(AvaliacaoAntropometrica avaliacao) {
        return new AvaliacaoAntropometricaResponse(
                avaliacao.getId(),
                avaliacao.getDataAvaliacao(),
                avaliacao.getPeso(),
                avaliacao.getAltura(),
                avaliacao.getImc(),
                avaliacao.getCircunferenciaCintura(),
                avaliacao.getCircunferenciaQuadril(),
                avaliacao.getCircunferenciaBraco(),
                avaliacao.getCircunferenciaCoxa(),
                avaliacao.getCircunferenciaPanturrilha(),
                avaliacao.getCircunferenciaPescoco(),
                avaliacao.getTricipital(),
                avaliacao.getSubescapular(),
                avaliacao.getPeitoral(),
                avaliacao.getAxilarMedia(),
                avaliacao.getSuprailiaca(),
                avaliacao.getAbdominal(),
                avaliacao.getCoxa(),
                avaliacao.getSomaDobras(),
                avaliacao.getPercentualGordura(),
                avaliacao.getRelacaoCinturaQuadril(),
                avaliacao.getRelacaoCinturaEstatura(),
                avaliacao.getObservacoes(),
                avaliacao.getAtivo(),
                avaliacao.getCreatedAt(),
                avaliacao.getUpdatedAt()
        );
    }

    private HistoricoAntropometricoResponse toHistoricoResponse(AvaliacaoAntropometrica avaliacao) {
        return new HistoricoAntropometricoResponse(
                avaliacao.getId(),
                avaliacao.getDataAvaliacao(),
                avaliacao.getPeso(),
                avaliacao.getImc(),
                avaliacao.getPercentualGordura(),
                avaliacao.getSomaDobras(),
                avaliacao.getCircunferenciaCintura(),
                avaliacao.getCircunferenciaQuadril(),
                avaliacao.getRelacaoCinturaQuadril(),
                avaliacao.getRelacaoCinturaEstatura(),
                avaliacao.getCreatedAt(),
                avaliacao.getUpdatedAt()
        );
    }
}