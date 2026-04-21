package br.com.nutricionista.system.service;

import br.com.nutricionista.system.dto.AtualizacaoDietaRequest;
import br.com.nutricionista.system.dto.AtualizacaoItemRefeicaoRequest;
import br.com.nutricionista.system.dto.AtualizacaoRefeicaoRequest;
import br.com.nutricionista.system.dto.CadastroDietaRequest;
import br.com.nutricionista.system.dto.CadastroItemRefeicaoRequest;
import br.com.nutricionista.system.dto.CadastroRefeicaoRequest;
import br.com.nutricionista.system.dto.DietaResponse;
import br.com.nutricionista.system.dto.DietaResumoResponse;
import br.com.nutricionista.system.dto.HistoricoDietaResponse;
import br.com.nutricionista.system.dto.ItemRefeicaoResponse;
import br.com.nutricionista.system.dto.RefeicaoResponse;
import br.com.nutricionista.system.entity.Dieta;
import br.com.nutricionista.system.entity.ItemRefeicao;
import br.com.nutricionista.system.entity.Nutricionista;
import br.com.nutricionista.system.entity.Paciente;
import br.com.nutricionista.system.entity.Refeicao;
import br.com.nutricionista.system.exception.BusinessException;
import br.com.nutricionista.system.exception.ResourceNotFoundException;
import br.com.nutricionista.system.repository.DietaRepository;
import br.com.nutricionista.system.repository.ItemRefeicaoRepository;
import br.com.nutricionista.system.repository.PacienteRepository;
import br.com.nutricionista.system.repository.RefeicaoRepository;
import br.com.nutricionista.system.security.SistemaUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DietaService {

    private final DietaRepository dietaRepository;
    private final RefeicaoRepository refeicaoRepository;
    private final ItemRefeicaoRepository itemRefeicaoRepository;
    private final PacienteRepository pacienteRepository;

    @Transactional
    public DietaResponse cadastrarDieta(Long pacienteId, CadastroDietaRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        desativarDietasAtivasDoPaciente(pacienteId, nutricionista.getId());

        Dieta dieta = new Dieta();
        preencherDadosDieta(
                dieta,
                paciente,
                nutricionista,
                request.titulo(),
                request.descricaoGeral(),
                request.objetivoDieta(),
                request.observacoes()
        );
        dieta.setAtiva(Boolean.TRUE);
        dieta.setAtivo(Boolean.TRUE);

        Dieta dietaSalva = dietaRepository.save(dieta);
        criarRefeicoesEItensIniciais(dietaSalva, request.refeicoes());

        return toDietaResponse(dietaSalva, true);
    }

    @Transactional(readOnly = true)
    public List<DietaResumoResponse> listarPorPaciente(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        return dietaRepository.findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByCreatedAtDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toResumoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HistoricoDietaResponse> obterHistorico(Long pacienteId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        return dietaRepository.findAllByPacienteIdAndNutricionistaIdAndAtivoTrueOrderByCreatedAtDescIdDesc(
                        pacienteId,
                        nutricionista.getId()
                )
                .stream()
                .map(this::toHistoricoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DietaResponse buscarDieta(Long pacienteId, Long dietaId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());

        Dieta dieta = buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());
        return toDietaResponse(dieta, true);
    }

    @Transactional
    public DietaResponse editarDieta(Long pacienteId, Long dietaId, AtualizacaoDietaRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Paciente paciente = buscarPacienteAtivoDoNutricionista(pacienteId, nutricionista.getId());
        Dieta dieta = buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());

        preencherDadosDieta(
                dieta,
                paciente,
                nutricionista,
                request.titulo(),
                request.descricaoGeral(),
                request.objetivoDieta(),
                request.observacoes()
        );

        if (Boolean.TRUE.equals(request.ativa())) {
            desativarDietasAtivasDoPaciente(pacienteId, nutricionista.getId());
            dieta.setAtiva(Boolean.TRUE);
        } else {
            dieta.setAtiva(Boolean.FALSE);
        }

        return toDietaResponse(dietaRepository.save(dieta), true);
    }

    @Transactional
    public void excluirDieta(Long pacienteId, Long dietaId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Dieta dieta = buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());

        dieta.setAtiva(Boolean.FALSE);
        dieta.setAtivo(Boolean.FALSE);
        dietaRepository.save(dieta);

        List<Refeicao> refeicoes = refeicaoRepository.findAllByDietaIdAndAtivoTrueOrderByOrdemAscIdAsc(dieta.getId());
        for (Refeicao refeicao : refeicoes) {
            refeicao.setAtivo(Boolean.FALSE);
            refeicaoRepository.save(refeicao);

            List<ItemRefeicao> itens = itemRefeicaoRepository.findAllByRefeicaoIdAndAtivoTrueOrderByOrdemAscIdAsc(refeicao.getId());
            for (ItemRefeicao item : itens) {
                item.setAtivo(Boolean.FALSE);
                itemRefeicaoRepository.save(item);
            }
        }
    }

    @Transactional
    public RefeicaoResponse cadastrarRefeicao(Long pacienteId, Long dietaId, CadastroRefeicaoRequest request) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        Dieta dieta = buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());

        Refeicao refeicao = new Refeicao();
        preencherDadosRefeicao(
                refeicao,
                dieta,
                request.nomeRefeicao(),
                request.horario(),
                request.descricao(),
                request.observacoes(),
                request.ordem()
        );
        refeicao.setAtivo(Boolean.TRUE);

        Refeicao refeicaoSalva = refeicaoRepository.save(refeicao);
        criarItensIniciais(refeicaoSalva, request.itens());

        return toRefeicaoResponse(refeicaoSalva, true);
    }

    @Transactional
    public RefeicaoResponse editarRefeicao(
            Long pacienteId,
            Long dietaId,
            Long refeicaoId,
            AtualizacaoRefeicaoRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());

        Refeicao refeicao = buscarRefeicaoAtiva(pacienteId, dietaId, refeicaoId, nutricionista.getId());
        preencherDadosRefeicao(
                refeicao,
                refeicao.getDieta(),
                request.nomeRefeicao(),
                request.horario(),
                request.descricao(),
                request.observacoes(),
                request.ordem()
        );

        return toRefeicaoResponse(refeicaoRepository.save(refeicao), true);
    }

    @Transactional
    public void excluirRefeicao(Long pacienteId, Long dietaId, Long refeicaoId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());

        Refeicao refeicao = buscarRefeicaoAtiva(pacienteId, dietaId, refeicaoId, nutricionista.getId());
        refeicao.setAtivo(Boolean.FALSE);
        refeicaoRepository.save(refeicao);

        List<ItemRefeicao> itens = itemRefeicaoRepository.findAllByRefeicaoIdAndAtivoTrueOrderByOrdemAscIdAsc(refeicaoId);
        for (ItemRefeicao item : itens) {
            item.setAtivo(Boolean.FALSE);
            itemRefeicaoRepository.save(item);
        }
    }

    @Transactional
    public ItemRefeicaoResponse cadastrarItem(
            Long pacienteId,
            Long dietaId,
            Long refeicaoId,
            CadastroItemRefeicaoRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());

        Refeicao refeicao = buscarRefeicaoAtiva(pacienteId, dietaId, refeicaoId, nutricionista.getId());

        ItemRefeicao item = new ItemRefeicao();
        preencherDadosItem(
                item,
                refeicao,
                request.alimento(),
                request.quantidade(),
                request.unidadeMedida(),
                request.substituicoes(),
                request.observacoes(),
                request.ordem()
        );
        item.setAtivo(Boolean.TRUE);

        return toItemResponse(itemRefeicaoRepository.save(item));
    }

    @Transactional
    public ItemRefeicaoResponse editarItem(
            Long pacienteId,
            Long dietaId,
            Long refeicaoId,
            Long itemId,
            AtualizacaoItemRefeicaoRequest request
    ) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());
        buscarRefeicaoAtiva(pacienteId, dietaId, refeicaoId, nutricionista.getId());

        ItemRefeicao item = buscarItemAtivo(
                pacienteId,
                dietaId,
                refeicaoId,
                itemId,
                nutricionista.getId()
        );

        preencherDadosItem(
                item,
                item.getRefeicao(),
                request.alimento(),
                request.quantidade(),
                request.unidadeMedida(),
                request.substituicoes(),
                request.observacoes(),
                request.ordem()
        );

        return toItemResponse(itemRefeicaoRepository.save(item));
    }

    @Transactional
    public void excluirItem(Long pacienteId, Long dietaId, Long refeicaoId, Long itemId) {
        Nutricionista nutricionista = getNutricionistaAutenticado();
        buscarDietaAtiva(pacienteId, dietaId, nutricionista.getId());
        buscarRefeicaoAtiva(pacienteId, dietaId, refeicaoId, nutricionista.getId());

        ItemRefeicao item = buscarItemAtivo(
                pacienteId,
                dietaId,
                refeicaoId,
                itemId,
                nutricionista.getId()
        );

        item.setAtivo(Boolean.FALSE);
        itemRefeicaoRepository.save(item);
    }

    private void criarRefeicoesEItensIniciais(Dieta dieta, List<CadastroRefeicaoRequest> refeicoes) {
        if (refeicoes == null || refeicoes.isEmpty()) {
            return;
        }

        for (CadastroRefeicaoRequest requestRefeicao : refeicoes) {
            Refeicao refeicao = new Refeicao();
            preencherDadosRefeicao(
                    refeicao,
                    dieta,
                    requestRefeicao.nomeRefeicao(),
                    requestRefeicao.horario(),
                    requestRefeicao.descricao(),
                    requestRefeicao.observacoes(),
                    requestRefeicao.ordem()
            );
            refeicao.setAtivo(Boolean.TRUE);

            Refeicao refeicaoSalva = refeicaoRepository.save(refeicao);
            criarItensIniciais(refeicaoSalva, requestRefeicao.itens());
        }
    }

    private void criarItensIniciais(Refeicao refeicao, List<CadastroItemRefeicaoRequest> itens) {
        if (itens == null || itens.isEmpty()) {
            return;
        }

        for (CadastroItemRefeicaoRequest requestItem : itens) {
            ItemRefeicao item = new ItemRefeicao();
            preencherDadosItem(
                    item,
                    refeicao,
                    requestItem.alimento(),
                    requestItem.quantidade(),
                    requestItem.unidadeMedida(),
                    requestItem.substituicoes(),
                    requestItem.observacoes(),
                    requestItem.ordem()
            );
            item.setAtivo(Boolean.TRUE);
            itemRefeicaoRepository.save(item);
        }
    }

    private void preencherDadosDieta(
            Dieta dieta,
            Paciente paciente,
            Nutricionista nutricionista,
            String titulo,
            String descricaoGeral,
            String objetivoDieta,
            String observacoes
    ) {
        dieta.setTitulo(titulo.trim());
        dieta.setDescricaoGeral(normalizarTexto(descricaoGeral));
        dieta.setObjetivoDieta(normalizarTexto(objetivoDieta));
        dieta.setObservacoes(normalizarTexto(observacoes));
        dieta.setPaciente(paciente);
        dieta.setNutricionista(nutricionista);
    }

    private void preencherDadosRefeicao(
            Refeicao refeicao,
            Dieta dieta,
            String nomeRefeicao,
            LocalTime horario,
            String descricao,
            String observacoes,
            Integer ordem
    ) {
        validarOrdem(ordem, "A ordem da refeição deve ser maior que zero.");
        refeicao.setNomeRefeicao(nomeRefeicao.trim());
        refeicao.setHorario(horario);
        refeicao.setDescricao(normalizarTexto(descricao));
        refeicao.setObservacoes(normalizarTexto(observacoes));
        refeicao.setOrdem(ordem);
        refeicao.setDieta(dieta);
    }

    private void preencherDadosItem(
            ItemRefeicao item,
            Refeicao refeicao,
            String alimento,
            String quantidade,
            String unidadeMedida,
            String substituicoes,
            String observacoes,
            Integer ordem
    ) {
        validarOrdem(ordem, "A ordem do item deve ser maior que zero.");
        item.setAlimento(alimento.trim());
        item.setQuantidade(quantidade.trim());
        item.setUnidadeMedida(normalizarTexto(unidadeMedida));
        item.setSubstituicoes(normalizarTexto(substituicoes));
        item.setObservacoes(normalizarTexto(observacoes));
        item.setOrdem(ordem);
        item.setRefeicao(refeicao);
    }

    private void desativarDietasAtivasDoPaciente(Long pacienteId, Long nutricionistaId) {
        List<Dieta> dietasAtivas = dietaRepository.findAllByPacienteIdAndNutricionistaIdAndAtivaTrueAndAtivoTrue(
                pacienteId,
                nutricionistaId
        );

        for (Dieta dietaAtiva : dietasAtivas) {
            dietaAtiva.setAtiva(Boolean.FALSE);
            dietaRepository.save(dietaAtiva);
        }
    }

    private Paciente buscarPacienteAtivoDoNutricionista(Long pacienteId, Long nutricionistaId) {
        return pacienteRepository.findByIdAndNutricionistaIdAndAtivoTrue(pacienteId, nutricionistaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente não encontrado para o nutricionista autenticado."
                ));
    }

    private Dieta buscarDietaAtiva(Long pacienteId, Long dietaId, Long nutricionistaId) {
        return dietaRepository.findByIdAndPacienteIdAndNutricionistaIdAndAtivoTrue(
                        dietaId,
                        pacienteId,
                        nutricionistaId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dieta não encontrada para o paciente informado."
                ));
    }

    private Refeicao buscarRefeicaoAtiva(Long pacienteId, Long dietaId, Long refeicaoId, Long nutricionistaId) {
        return refeicaoRepository.findByIdAndDietaIdAndDietaPacienteIdAndDietaNutricionistaIdAndAtivoTrue(
                        refeicaoId,
                        dietaId,
                        pacienteId,
                        nutricionistaId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Refeição não encontrada para a dieta informada."
                ));
    }

    private ItemRefeicao buscarItemAtivo(
            Long pacienteId,
            Long dietaId,
            Long refeicaoId,
            Long itemId,
            Long nutricionistaId
    ) {
        return itemRefeicaoRepository
                .findByIdAndRefeicaoIdAndRefeicaoDietaIdAndRefeicaoDietaPacienteIdAndRefeicaoDietaNutricionistaIdAndAtivoTrue(
                        itemId,
                        refeicaoId,
                        dietaId,
                        pacienteId,
                        nutricionistaId
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item da refeição não encontrado para os dados informados."
                ));
    }

    private void validarOrdem(Integer ordem, String mensagem) {
        if (ordem == null || ordem <= 0) {
            throw new BusinessException(mensagem);
        }
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

    private DietaResponse toDietaResponse(Dieta dieta, boolean carregarRefeicoes) {
        List<RefeicaoResponse> refeicoes = carregarRefeicoes
                ? refeicaoRepository.findAllByDietaIdAndAtivoTrueOrderByOrdemAscIdAsc(dieta.getId())
                  .stream()
                  .map(refeicao -> toRefeicaoResponse(refeicao, true))
                  .toList()
                : List.of();

        return new DietaResponse(
                dieta.getId(),
                dieta.getTitulo(),
                dieta.getDescricaoGeral(),
                dieta.getObjetivoDieta(),
                dieta.getObservacoes(),
                dieta.getAtiva(),
                dieta.getAtivo(),
                refeicoes,
                dieta.getCreatedAt(),
                dieta.getUpdatedAt()
        );
    }

    private RefeicaoResponse toRefeicaoResponse(Refeicao refeicao, boolean carregarItens) {
        List<ItemRefeicaoResponse> itens = carregarItens
                ? itemRefeicaoRepository.findAllByRefeicaoIdAndAtivoTrueOrderByOrdemAscIdAsc(refeicao.getId())
                  .stream()
                  .map(this::toItemResponse)
                  .toList()
                : List.of();

        return new RefeicaoResponse(
                refeicao.getId(),
                refeicao.getNomeRefeicao(),
                refeicao.getHorario(),
                refeicao.getDescricao(),
                refeicao.getObservacoes(),
                refeicao.getOrdem(),
                refeicao.getAtivo(),
                itens,
                refeicao.getCreatedAt(),
                refeicao.getUpdatedAt()
        );
    }

    private ItemRefeicaoResponse toItemResponse(ItemRefeicao item) {
        return new ItemRefeicaoResponse(
                item.getId(),
                item.getAlimento(),
                item.getQuantidade(),
                item.getUnidadeMedida(),
                item.getSubstituicoes(),
                item.getObservacoes(),
                item.getOrdem(),
                item.getAtivo(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    private DietaResumoResponse toResumoResponse(Dieta dieta) {
        int quantidadeRefeicoes = refeicaoRepository.findAllByDietaIdAndAtivoTrueOrderByOrdemAscIdAsc(dieta.getId()).size();

        return new DietaResumoResponse(
                dieta.getId(),
                dieta.getTitulo(),
                dieta.getObjetivoDieta(),
                dieta.getAtiva(),
                quantidadeRefeicoes,
                dieta.getCreatedAt(),
                dieta.getUpdatedAt()
        );
    }

    private HistoricoDietaResponse toHistoricoResponse(Dieta dieta) {
        int quantidadeRefeicoes = refeicaoRepository.findAllByDietaIdAndAtivoTrueOrderByOrdemAscIdAsc(dieta.getId()).size();

        return new HistoricoDietaResponse(
                dieta.getId(),
                dieta.getTitulo(),
                dieta.getObjetivoDieta(),
                dieta.getAtiva(),
                quantidadeRefeicoes,
                dieta.getCreatedAt(),
                dieta.getUpdatedAt()
        );
    }
}