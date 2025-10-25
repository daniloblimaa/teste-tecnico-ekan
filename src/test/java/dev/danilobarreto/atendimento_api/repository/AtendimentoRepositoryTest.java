package dev.danilobarreto.atendimento_api.repository;

import dev.danilobarreto.atendimento_api.model.Atendimento;
import dev.danilobarreto.atendimento_api.model.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {"spring.sql.init.mode=never"})
class AtendimentoRepositoryTest {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    void deveRetornarAtendimentosAoBuscarPorPacienteId() {
        Paciente p = new Paciente(null, "Danilo", "danilobarreto15@gmail.com", null);
        p = pacienteRepository.save(p);

        Atendimento a1 = new Atendimento(null, OffsetDateTime.now(), p, List.of());
        Atendimento a2 = new Atendimento(null, OffsetDateTime.now(), p, List.of());
        atendimentoRepository.saveAll(List.of(a1, a2));

        List<Atendimento> encontrados = atendimentoRepository.findByPacienteId(p.getId());

        assertThat(encontrados).hasSize(2);
        assertThat(encontrados.get(0).getPaciente().getId()).isEqualTo(p.getId());
    }
}
