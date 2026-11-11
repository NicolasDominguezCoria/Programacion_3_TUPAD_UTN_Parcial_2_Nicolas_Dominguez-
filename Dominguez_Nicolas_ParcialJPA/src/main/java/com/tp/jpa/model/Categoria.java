package com.tp.jpa.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString

@Entity
@Table(name = "CATEGORIAS")
public class Categoria extends Base{

    @Column(name = "NOMBRE", unique = true) //no permite que nombres de categorias duplicados
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<Producto> productos = new HashSet<>();

    //metodo helper
    public void addProducto(Producto producto) {
        if (producto != null) {
            productos.add(producto);
        }
    }
}
