package dev.danilobarreto.atendimento_api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.danilobarreto.atendimento_api.model.Atendimento;
import dev.danilobarreto.atendimento_api.model.Condicao;

public class FhirConverter {
    public static String toEncounterJson(Atendimento at) throws JsonProcessingException {
        ObjectNode root = JsonNodeFactory.instance.objectNode();
        root.put("resourceType", "Encounter");
        root.put("id", at.getId().toString());
        root.put("status", "finished");
        ObjectNode subject = root.putObject("subject");
        subject.put("reference", "Patient/" + at.getPaciente().getId());
        subject.put("display", at.getPaciente().getNome());
        ObjectNode period = root.putObject("period");
        period.put("start", at.getDataAtendimento().toString());
        ArrayNode diagnosisArr = root.putArray("diagnosis");
        for (Condicao c : at.getCondicoes()) {
            ObjectNode diag = diagnosisArr.addObject();
            ObjectNode conditionRef = diag.putObject("condition");
            ObjectNode coding = conditionRef.putObject("coding").putObject("0");
            coding.put("system", "http://hl7.org/fhir/sid/icd-10");
            coding.put("code", c.getCid().getId());
            coding.put("display", c.getCid().getNome());
            conditionRef.put("text", c.getAnotacao());
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
    }
}