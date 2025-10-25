package dev.danilobarreto.atendimento_api.service;

import dev.danilobarreto.atendimento_api.model.Atendimento;
import dev.danilobarreto.atendimento_api.model.Paciente;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class MailServiceTest {

    private JavaMailSender mailSender;
    private MailService mailService;

    @BeforeEach
    void setup() throws Exception {
        mailSender = mock(JavaMailSender.class);
        MimeMessage fakeMsg = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(fakeMsg);
        mailService = new MailService(mailSender);

        Field notifyField = MailService.class.getDeclaredField("notifyEnabled");
        notifyField.setAccessible(true);
        notifyField.set(mailService, true);

        Field toField = MailService.class.getDeclaredField("notifyTo");
        toField.setAccessible(true);
        toField.set(mailService, "test@example.com");
    }

    @Test
    void quandoAgendadoDeveEnviarEmail() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Fulano");
        paciente.setEmail("fulano@example.com");

        Atendimento at = new Atendimento();
        at.setId(123L);
        at.setPaciente(paciente);
        at.setDataAtendimento(OffsetDateTime.now());
        at.setCondicoes(new ArrayList<>());

        mailService.sendAtendimentoAsFhir(at);

        verify(mailSender, times(1)).send(ArgumentMatchers.any(MimeMessage.class));
    }
}
