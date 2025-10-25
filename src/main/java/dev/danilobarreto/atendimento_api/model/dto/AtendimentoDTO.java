package dev.danilobarreto.atendimento_api.model.dto;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtendimentoDTO {
    private Long id;
    private OffsetDateTime dataAtendimento;
    private Long pacienteId;
    private List<CondicaoDTO> condicoes;
}
