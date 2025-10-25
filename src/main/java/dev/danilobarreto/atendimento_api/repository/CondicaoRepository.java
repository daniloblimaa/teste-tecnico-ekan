package dev.danilobarreto.atendimento_api.repository;

import dev.danilobarreto.atendimento_api.model.Condicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CondicaoRepository extends JpaRepository<Condicao, Long> {
    @Query("select c from Condicao c join c.atendimento a where a.paciente.id = :pacienteId")
    List<Condicao> findByPacienteId(@Param("pacienteId") Long pacienteId);
}
