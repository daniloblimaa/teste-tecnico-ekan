package dev.danilobarreto.atendimento_api.repository;

import dev.danilobarreto.atendimento_api.model.Paciente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    void deveSalvarRegistroEBuscarPorId() {
        Paciente p = new Paciente();
        p.setNome("Danilo");
        p.setEmail("danilobarreto15@gmail.com");
        p.setDataNascimento(LocalDate.of(1998,11,18));

        Paciente saved = pacienteRepository.save(p);

        assertThat(saved.getId()).isNotNull();
        assertThat(pacienteRepository.findById(saved.getId())).isPresent();
    }
}

