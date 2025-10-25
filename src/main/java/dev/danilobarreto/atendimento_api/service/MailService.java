package dev.danilobarreto.atendimento_api.service;

import dev.danilobarreto.atendimento_api.model.Atendimento;
import dev.danilobarreto.atendimento_api.util.FhirConverter;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.notify.to:rdoni.ekan@iamspe.sp.gov.br}")
    private String notifyTo;

    @Value("${app.notify.enabled:false}")
    private boolean notifyEnabled;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendAtendimentoAsFhir(Atendimento atendimento) {
        if (!notifyEnabled) {
            logger.debug("Mail notifications are disabled (app.notify.enabled=false). Skipping send for atendimento id={}", atendimento != null ? atendimento.getId() : null);
            return;
        }

        if (atendimento == null) {
            logger.warn("Attempted to send null Atendimento as FHIR; aborting");
            return;
        }

        try {
            String fhirJson = FhirConverter.toEncounterJson(atendimento);
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, false, "UTF-8");
            helper.setTo(notifyTo);
            helper.setSubject("Atendimento (FHIR/R5) - paciente " + (atendimento.getPaciente() != null ? atendimento.getPaciente().getNome() : "[sem paciente]"));
            helper.setText(fhirJson, false);
            mailSender.send(msg);
            logger.info("Sent atendimento id={} as FHIR to {}", atendimento.getId(), notifyTo);
        } catch (Exception ex) {
            logger.error("Failed to send atendimento id={} as FHIR to {}", atendimento.getId(), notifyTo, ex);
        }
    }
}
