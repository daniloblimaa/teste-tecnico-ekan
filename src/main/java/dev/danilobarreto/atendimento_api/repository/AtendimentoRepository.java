package dev.danilobarreto.atendimento_api.repository;

import dev.danilobarreto.atendimento_api.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
    List<Atendimento> findByPacienteId(Long pacienteId);
}
