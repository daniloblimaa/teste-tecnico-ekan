package dev.danilobarreto.atendimento_api.controller;

import dev.danilobarreto.atendimento_api.model.dto.AtendimentoDTO;
import dev.danilobarreto.atendimento_api.model.dto.CondicaoDTO;
import dev.danilobarreto.atendimento_api.service.AtendimentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AtendimentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AtendimentoService atendimentoService;

    @InjectMocks
    private AtendimentoController atendimentoController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(atendimentoController).build();
    }

    @Test
    void listarTodos() throws Exception {
        AtendimentoDTO dto = new AtendimentoDTO(1L, OffsetDateTime.parse("2025-10-24T10:00:00Z"), 3L, List.of());
        when(atendimentoService.listarTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/atendimentos/findall"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveRetornarStatusCreatedAoCriar() throws Exception {
        AtendimentoDTO created = new AtendimentoDTO(5L, OffsetDateTime.parse("2025-10-24T10:00:00Z"), 2L, List.of());
        when(atendimentoService.criarAtendimento(any())).thenReturn(created);

        String json = "{\"pacienteId\":2, \"condicoes\": []}";

        mockMvc.perform(post("/api/atendimentos/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void listarCondicoesPorPaciente() throws Exception {
        CondicaoDTO c = new CondicaoDTO(1L, "paciente com gripe", "J09", "Influenza due to identified zoonotic or pandemic influenza virus");
        when(atendimentoService.listarCondicoesPorPaciente(2L)).thenReturn(List.of(c));

        mockMvc.perform(get("/api/atendimentos/paciente/2/condicoes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].anotacao").value("paciente com gripe"));
    }
}
