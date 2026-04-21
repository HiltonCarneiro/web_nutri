package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.AvaliacaoAntropometricaResponse;
import br.com.nutricionista.system.dto.DietaResponse;
import br.com.nutricionista.system.dto.DietaResumoResponse;
import br.com.nutricionista.system.dto.EvolucaoConsultaResponse;
import br.com.nutricionista.system.dto.HistoricoAntropometricoResponse;
import br.com.nutricionista.system.dto.HistoricoDietaResponse;
import br.com.nutricionista.system.dto.PacienteResponse;
import br.com.nutricionista.system.dto.ProntuarioCompletoResponse;
import br.com.nutricionista.system.dto.ResumoProntuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProntuarioCompletoService {

    private final PacienteService pacienteService;
    private final EvolucaoConsultaService evolucaoConsultaService;
    private final AvaliacaoAntropometricaService avaliacaoAntropometricaService;
    private final DietaService dietaService;

    @Transactional(readOnly = true)
    public ProntuarioCompletoResponse obterProntuarioCompleto(Long pacienteId) {
        PacienteResponse paciente = pacienteService.buscarPorId(pacienteId);

        List<EvolucaoConsultaResponse> evolucoes = evolucaoConsultaService.listarPorPaciente(pacienteId);
        List<AvaliacaoAntropometricaResponse> avaliacoesAntropometricas =
                avaliacaoAntropometricaService.listarPorPaciente(pacienteId);
        List<HistoricoAntropometricoResponse> historicoAntropometrico =
                avaliacaoAntropometricaService.obterHistoricoAntropometrico(pacienteId);
        List<DietaResumoResponse> dietas = dietaService.listarPorPaciente(pacienteId);
        List<HistoricoDietaResponse> historicoDietas = dietaService.obterHistorico(pacienteId);

        EvolucaoConsultaResponse ultimaEvolucao = evolucoes.isEmpty() ? null : evolucoes.get(0);
        AvaliacaoAntropometricaResponse ultimaAvaliacaoAntropometrica =
                avaliacoesAntropometricas.isEmpty() ? null : avaliacoesAntropometricas.get(0);

        DietaResumoResponse dietaAtivaAtual = dietas.stream()
                .filter(dieta -> Boolean.TRUE.equals(dieta.ativa()))
                .findFirst()
                .orElse(null);

        DietaResponse dietaAtiva = dietaAtivaAtual != null
                ? dietaService.buscarDieta(pacienteId, dietaAtivaAtual.id())
                : null;

        LocalDateTime ultimaAtualizacaoProntuario = calcularUltimaAtualizacaoProntuario(
                paciente,
                evolucoes,
                avaliacoesAntropometricas,
                dietas
        );

        ResumoProntuarioResponse resumo = new ResumoProntuarioResponse(
                ultimaEvolucao,
                ultimaAvaliacaoAntropometrica,
                dietaAtivaAtual,
                ultimaAtualizacaoProntuario
        );

        return new ProntuarioCompletoResponse(
                paciente,
                resumo,
                evolucoes,
                avaliacoesAntropometricas,
                historicoAntropometrico,
                dietaAtiva,
                dietas,
                historicoDietas
        );
    }

    private LocalDateTime calcularUltimaAtualizacaoProntuario(
            PacienteResponse paciente,
            List<EvolucaoConsultaResponse> evolucoes,
            List<AvaliacaoAntropometricaResponse> avaliacoesAntropometricas,
            List<DietaResumoResponse> dietas
    ) {
        return Stream.concat(
                        Stream.of(paciente.updatedAt()),
                        Stream.of(
                                evolucoes.isEmpty() ? null : evolucoes.get(0).updatedAt(),
                                avaliacoesAntropometricas.isEmpty() ? null : avaliacoesAntropometricas.get(0).updatedAt(),
                                dietas.isEmpty() ? null : dietas.get(0).updatedAt()
                        )
                )
                .filter(java.util.Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(paciente.updatedAt());
    }
}