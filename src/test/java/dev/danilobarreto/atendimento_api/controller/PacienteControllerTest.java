package dev.danilobarreto.atendimento_api.controller;

import dev.danilobarreto.atendimento_api.model.dto.PacienteDTO;
import dev.danilobarreto.atendimento_api.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class PacienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private PacienteController pacienteController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(pacienteController).build();
    }

    @Test
    void listarTodos() throws Exception {
        PacienteDTO p = new PacienteDTO(1L, "Danilo", "danilobarreto15@gmail.com", LocalDate.of(1995,5,5));
        when(pacienteService.listarTodos()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/pacientes/findall"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nome").value("Danilo"));
    }

    @Test
    void deveRetornarStatusCreatedAoCriar() throws Exception {
        PacienteDTO input = new PacienteDTO(null, "Danilo", "danilobarreto15@gmail.com", LocalDate.of(1998,11,18));
        PacienteDTO created = new PacienteDTO(2L, "Danilo Barreto", "eu@danilobarreto.dev", LocalDate.of(1998,11,18));

        when(pacienteService.criar(org.mockito.ArgumentMatchers.any())).thenReturn(created);

        String json = "{\"nome\":\"Danilo\",\"email\":\"danilobarreto15@gmail.com\",\"dataNascimento\":\"1998-11-18\"}";

        mockMvc.perform(post("/api/pacientes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }
}
