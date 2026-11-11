package com.tp.jpa.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@MappedSuperclass
public abstract class Base {
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private boolean eliminado = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
