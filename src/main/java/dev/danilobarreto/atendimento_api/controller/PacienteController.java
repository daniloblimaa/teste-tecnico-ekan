package dev.danilobarreto.atendimento_api.controller;

import dev.danilobarreto.atendimento_api.model.dto.PacienteDTO;
import dev.danilobarreto.atendimento_api.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping("/create")
    public ResponseEntity<PacienteDTO> criar(@RequestBody @Valid PacienteDTO dto) {
        PacienteDTO criado = pacienteService.criar(dto);
        return ResponseEntity.created(URI.create("/pacientes/" + criado.getId())).body(criado);
    }

    @GetMapping("/findall")
    public ResponseEntity<List<PacienteDTO>> listarTodos() {
        List<PacienteDTO> lista = pacienteService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<PacienteDTO> buscarPorId(@PathVariable Long id) {
        PacienteDTO dto = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PacienteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PacienteDTO dto) {
        PacienteDTO atualizado = pacienteService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
