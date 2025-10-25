package dev.danilobarreto.atendimento_api.service;

import dev.danilobarreto.atendimento_api.model.Paciente;
import dev.danilobarreto.atendimento_api.model.dto.PacienteDTO;
import dev.danilobarreto.atendimento_api.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarStatusCreatedAoCriar() {
        PacienteDTO dto = new PacienteDTO(null, "Danilo", "danilobarreto15@gmail.com", LocalDate.of(1998, 11, 18));
        Paciente saved = new Paciente(1L, dto.getNome(), dto.getEmail(), dto.getDataNascimento());

        when(pacienteRepository.save(any(Paciente.class))).thenReturn(saved);

        PacienteDTO result = pacienteService.criar(dto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("Danilo");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void listarTodos() {
        Paciente p1 = new Paciente(1L, "Danilo", "danilobarreto15@gmail.com", LocalDate.of(1990,1,1));
        Paciente p2 = new Paciente(2L, "Danilo Lima", "eu@danilobarreto.dev", LocalDate.of(1991,2,2));

        when(pacienteRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PacienteDTO> lista = pacienteService.listarTodos();

        assertThat(lista).hasSize(2);
        assertThat(lista.get(0).getNome()).isEqualTo("Danilo");
    }

    @Test
    void buscarPorId() {
        Paciente p = new Paciente(5L, "Danilo", "danilobarreto15@gmail.com", LocalDate.of(1980,1,1));
        when(pacienteRepository.findById(5L)).thenReturn(Optional.of(p));

        PacienteDTO dto = pacienteService.buscarPorId(5L);

        assertThat(dto.getId()).isEqualTo(5L);
        assertThat(dto.getNome()).isEqualTo("Danilo");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAoBuscarPorId() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> pacienteService.buscarPorId(99L));
    }
}

