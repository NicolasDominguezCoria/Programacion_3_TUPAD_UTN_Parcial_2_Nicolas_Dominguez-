package com.tp.jpa.model;

import com.tp.jpa.model.enums.Rol;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@ToString

@Entity
@Table(name = "USUARIOS")
public class Usuario extends Base{

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "MAIL", unique = true)
    private String mail;

    @Column(name = "CELULAR")
    private String celular;

    @Column(name = "CONTRASENIA")
    private String contrasenia;

    @Column(name = "ROL")
    @Enumerated(EnumType.STRING)
    private Rol rol;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<Pedido> pedidos = new HashSet<>();

    public void addPedido(Pedido pedido) {
        if (pedido != null && !this.pedidos.contains(pedido)) {
            this.pedidos.add(pedido);
            pedido.setUsuario(this); //sincroniza la relacion
        }
    }
}
