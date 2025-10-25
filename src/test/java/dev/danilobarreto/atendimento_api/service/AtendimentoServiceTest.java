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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtendimentoServiceTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private CidRepository cidRepository;

    @Mock
    private CondicaoRepository condicaoRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private AtendimentoService atendimentoService;

    @BeforeEach
    void setup() {
    }

    @Test
    void deveCriarAtendimentoSemCondicoesERetornarDto() {
        Paciente paciente = new Paciente(1L, "Danilo", "danilobarreto15@gmail.com", null);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        Atendimento saved = new Atendimento(10L, OffsetDateTime.parse("2025-10-24T10:00:00Z"), paciente, List.of());
        when(atendimentoRepository.save(any(Atendimento.class))).thenReturn(saved);
        when(atendimentoRepository.findById(10L)).thenReturn(Optional.of(saved));

        AtendimentoDTO dto = new AtendimentoDTO(null, null, 1L, null);

        AtendimentoDTO result = atendimentoService.criarAtendimento(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getPacienteId()).isEqualTo(1L);
    }

    @Test
    void deveCriarAtendimentoComCondicoesESalvarERetornarDto() {
        Paciente paciente = new Paciente(2L, "Danilo", "danilobarreto15@gmail.com", null);
        when(pacienteRepository.findById(2L)).thenReturn(Optional.of(paciente));

        Atendimento saved = new Atendimento(11L, OffsetDateTime.parse("2025-10-24T11:00:00Z"), paciente, new java.util.ArrayList<>());
        when(atendimentoRepository.save(any(Atendimento.class))).thenReturn(saved);

        Cid cid = new Cid("J09", "Influenza due to identified zoonotic or pandemic influenza virus");
        when(cidRepository.findById("J09")).thenReturn(Optional.of(cid));

        Condicao cond = new Condicao(1L, "anotacao", cid, saved);
        saved.getCondicoes().add(cond);
        when(atendimentoRepository.findById(11L)).thenReturn(Optional.of(saved));

        CondicaoDTO condDto = new CondicaoDTO(null, "anotacao", "J09", null);
        AtendimentoDTO dto = new AtendimentoDTO(null, null, 2L, List.of(condDto));

        AtendimentoDTO result = atendimentoService.criarAtendimento(dto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(11L);
        assertThat(result.getCondicoes()).hasSize(1);
        assertThat(result.getCondicoes().get(0).getCidDescricao()).isEqualTo("Influenza due to identified zoonotic or pandemic influenza virus");
        assertThat(result.getCondicoes().get(0).getCidId()).isEqualTo("J09");
    }

    @Test
    void deveLancarExcecaoQuandoPacienteNaoForEncontradoAoCriarAtendimento() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        AtendimentoDTO dto = new AtendimentoDTO(null, null, 99L, null);

        assertThrows(EntityNotFoundException.class, () -> atendimentoService.criarAtendimento(dto));
    }

    @Test
    void deveListarCondicoesPorPacienteERetornarCondicoes() {
        Paciente paciente = new Paciente(3L, "Danilo", "danilobarreto15@gmail.com", null);
        Atendimento at = new Atendimento(20L, OffsetDateTime.now(), paciente, new java.util.ArrayList<>());
        Cid cid = new Cid("J09", "Influenza due to identified zoonotic or pandemic influenza virus");
        Condicao cond = new Condicao(2L, "nota", cid, at);
        at.getCondicoes().add(cond);

        when(pacienteRepository.existsById(3L)).thenReturn(true);
        when(atendimentoRepository.findByPacienteId(3L)).thenReturn(List.of(at));

        List<dev.danilobarreto.atendimento_api.model.dto.CondicaoDTO> result = atendimentoService.listarCondicoesPorPaciente(3L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAnotacao()).isEqualTo("nota");
    }

    @Test
    void deveLancarExcecaoQuandoPacienteNaoForEncontradoAoListarCondicoesPorPaciente() {
        when(pacienteRepository.existsById(50L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> atendimentoService.listarCondicoesPorPaciente(50L));
    }
}
