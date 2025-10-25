package dev.danilobarreto.atendimento_api.service;

import dev.danilobarreto.atendimento_api.model.Atendimento;
import dev.danilobarreto.atendimento_api.model.Cid;
import dev.danilobarreto.atendimento_api.model.Condicao;
import dev.danilobarreto.atendimento_api.model.Paciente;
import dev.danilobarreto.atendimento_api.model.dto.AtendimentoDTO;
import dev.danilobarreto.atendimento_api.model.dto.CondicaoDTO;

import dev.danilobarreto.atendimento_api.repository.AtendimentoRepository;
import dev.danilobarreto.atendimento_api.repository.CidRepository;
import dev.danilobarreto.atendimento_api.repository.CondicaoRepository;
import dev.danilobarreto.atendimento_api.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtendimentoService {

    private static final Logger logger = LoggerFactory.getLogger(AtendimentoService.class);

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final CidRepository cidRepository;
    private final CondicaoRepository condicaoRepository;
    private final MailService mailService;

    @Transactional
    public AtendimentoDTO criarAtendimento(AtendimentoDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        Atendimento atendimento = new Atendimento();
        atendimento.setDataAtendimento(dto.getDataAtendimento() != null ? dto.getDataAtendimento() : OffsetDateTime.now());
        atendimento.setPaciente(paciente);

        atendimento = atendimentoRepository.save(atendimento);

        addCondicoesToAtendimento(atendimento, dto.getCondicoes());

        AtendimentoDTO atendimentoDTO = buscarPorId(atendimento.getId());

        try {
            mailService.sendAtendimentoAsFhir(atendimento);
        } catch (Exception ex) {
            logger.error("Falha ao enviar atendimento id={} como FHIR", atendimento.getId(), ex);
        }

        return atendimentoDTO;
    }

    @Transactional(readOnly = true)
    public AtendimentoDTO buscarPorId(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado"));

        List<CondicaoDTO> condicoes = atendimento.getCondicoes().stream()
                .map(c -> new CondicaoDTO(
                        c.getId(),
                        c.getAnotacao(),
                        String.valueOf(c.getCid().getId()),
                        c.getCid().getNome()
                ))
                .toList();

        return new AtendimentoDTO(
                atendimento.getId(),
                atendimento.getDataAtendimento(),
                atendimento.getPaciente().getId(),
                condicoes
        );
    }

    @Transactional(readOnly = true)
    public List<AtendimentoDTO> listarTodos() {
        return atendimentoRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public AtendimentoDTO atualizarAtendimento(Long id, AtendimentoDTO dto) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atendimento não encontrado"));

        if (dto.getDataAtendimento() != null) {
            atendimento.setDataAtendimento(dto.getDataAtendimento());
        }


        if (dto.getCondicoes() != null) {
            condicaoRepository.deleteAll(atendimento.getCondicoes());
            addCondicoesToAtendimento(atendimento, dto.getCondicoes());
        }

        atendimento = atendimentoRepository.save(atendimento);
        return buscarPorId(atendimento.getId());
    }

    private void addCondicoesToAtendimento(Atendimento atendimento, List<CondicaoDTO> condicoes) {
        if (condicoes == null || condicoes.isEmpty()) return;

        for (CondicaoDTO condicaoDTO : condicoes) {
            String cidId = condicaoDTO.getCidId();
            if (cidId == null || cidId.trim().isEmpty()) {
                throw new EntityNotFoundException("CID não informado");
            }

            Cid cid = cidRepository.findById(cidId.trim())
                    .orElseThrow(() -> new EntityNotFoundException("CID não encontrado: " + cidId));

            Condicao condicao = new Condicao();
            condicao.setAnotacao(condicaoDTO.getAnotacao());
            condicao.setCid(cid);
            condicao.setAtendimento(atendimento);
            condicaoRepository.save(condicao);
        }
    }

    @Transactional(readOnly = true)
    public List<CondicaoDTO> listarCondicoesPorPaciente(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException("Paciente não encontrado");
        }

        List<Atendimento> atendimentos = atendimentoRepository.findByPacienteId(pacienteId);

        return atendimentos.stream()
                .flatMap(at -> at.getCondicoes().stream())
                .map(c -> new CondicaoDTO(
                        c.getId(),
                        c.getAnotacao(),
                        String.valueOf(c.getCid().getId()),
                        c.getCid().getNome()
                ))
                .toList();
    }

    private AtendimentoDTO toDTO(Atendimento atendimento) {
        List<CondicaoDTO> condicoes = atendimento.getCondicoes().stream()
                .map(c -> new CondicaoDTO(
                        c.getId(),
                        c.getAnotacao(),
                        String.valueOf(c.getCid().getId()),
                        c.getCid().getNome()
                ))
                .toList();

        return new AtendimentoDTO(
                atendimento.getId(),
                atendimento.getDataAtendimento(),
                atendimento.getPaciente().getId(),
                condicoes
        );
    }
}
