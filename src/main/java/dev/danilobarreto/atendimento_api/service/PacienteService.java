package dev.danilobarreto.atendimento_api.service;

import dev.danilobarreto.atendimento_api.model.Paciente;
import dev.danilobarreto.atendimento_api.model.dto.PacienteDTO;
import dev.danilobarreto.atendimento_api.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional
    public PacienteDTO criar(PacienteDTO dto) {
        Paciente paciente = new Paciente();
        paciente.setNome(dto.getNome());
        paciente.setEmail(dto.getEmail());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente = pacienteRepository.save(paciente);

        return toDTO(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PacienteDTO buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));
        return toDTO(paciente);
    }

    @Transactional
    public PacienteDTO atualizar(Long id, PacienteDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        if (dto.getNome() != null) paciente.setNome(dto.getNome());
        if (dto.getEmail() != null) paciente.setEmail(dto.getEmail());
        if (dto.getDataNascimento() != null) paciente.setDataNascimento(dto.getDataNascimento());

        paciente = pacienteRepository.save(paciente);
        return toDTO(paciente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Paciente não encontrado");
        }
        pacienteRepository.deleteById(id);
    }

    private PacienteDTO toDTO(Paciente paciente) {
        return new PacienteDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getEmail(),
                paciente.getDataNascimento()
        );
    }
}