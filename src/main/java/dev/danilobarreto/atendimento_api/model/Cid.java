package dev.danilobarreto.atendimento_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cid")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cid {

    @Id
    @Column(length = 10)
    private String id;

    @Column(nullable = false)
    private String nome;
}
