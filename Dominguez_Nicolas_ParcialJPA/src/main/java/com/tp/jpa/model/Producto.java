package com.tp.jpa.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@ToString

@Entity
@Table(name = "PRODUCTOS")
public class Producto extends Base{

    @Column(name = "NOMBRE", unique = true) //no permite productos duplicados
    private String nombre;

    @Column(name = "PRECIO")
    private Double precio;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "STOCK")
    private Integer stock;

    @Column(name = "IMAGEN")
    private String imagen;

    @Column(name = "DISPONIBLE")
    @Builder.Default
    private Boolean disponible = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
