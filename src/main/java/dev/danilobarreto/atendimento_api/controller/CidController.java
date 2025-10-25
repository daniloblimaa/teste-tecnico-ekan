package dev.danilobarreto.atendimento_api.controller;

import dev.danilobarreto.atendimento_api.model.dto.CidDTO;
import dev.danilobarreto.atendimento_api.service.CidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cids")
@RequiredArgsConstructor
public class CidController {

    private final CidService cidService;

    @GetMapping("/find/{id}")
    public ResponseEntity<CidDTO> buscarPorId(@PathVariable String id) {
        CidDTO dto = cidService.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }
}
