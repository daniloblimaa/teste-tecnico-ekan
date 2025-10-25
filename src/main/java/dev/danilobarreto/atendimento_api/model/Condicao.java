package dev.danilobarreto.atendimento_api.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "condicao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Condicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String anotacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cid_id")
    private Cid cid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "atendimento_id")
    private Atendimento atendimento;


}
