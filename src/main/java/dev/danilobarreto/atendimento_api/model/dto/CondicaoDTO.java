package dev.danilobarreto.atendimento_api.model.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CondicaoDTO {
    private Long id;
    private String anotacao;
    private String cidId;
    private String cidDescricao;
}
