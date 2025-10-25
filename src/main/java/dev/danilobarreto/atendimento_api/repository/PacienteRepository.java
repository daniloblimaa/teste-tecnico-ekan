package dev.danilobarreto.atendimento_api.repository;

import dev.danilobarreto.atendimento_api.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {}

