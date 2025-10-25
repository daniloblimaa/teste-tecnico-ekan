package dev.danilobarreto.atendimento_api.model.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
}
