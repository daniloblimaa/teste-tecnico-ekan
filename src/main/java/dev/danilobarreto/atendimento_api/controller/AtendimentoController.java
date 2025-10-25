package dev.danilobarreto.atendimento_api.controller;

import dev.danilobarreto.atendimento_api.model.dto.AtendimentoDTO;
import dev.danilobarreto.atendimento_api.model.dto.CondicaoDTO;
import dev.danilobarreto.atendimento_api.service.AtendimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
@RequiredArgsConstructor
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    @PostMapping("/create")
    public ResponseEntity<AtendimentoDTO> criar(@RequestBody @Valid AtendimentoDTO dto) {
        AtendimentoDTO criado = atendimentoService.criarAtendimento(dto);
        return ResponseEntity.created(URI.create("/atendimentos/" + criado.getId())).body(criado);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<AtendimentoDTO>> listarTodos() {
        List<AtendimentoDTO> lista = atendimentoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AtendimentoDTO> buscarPorId(@PathVariable Long id) {
        AtendimentoDTO dto = atendimentoService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AtendimentoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtendimentoDTO dto) {
        AtendimentoDTO atualizado = atendimentoService.atualizarAtendimento(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("/paciente/{pacienteId}/condicoes")
    public ResponseEntity<List<CondicaoDTO>> listarCondicoesPorPaciente(@PathVariable Long pacienteId) {
        List<CondicaoDTO> condicoes = atendimentoService.listarCondicoesPorPaciente(pacienteId);
        return ResponseEntity.ok(condicoes);
    }
}
