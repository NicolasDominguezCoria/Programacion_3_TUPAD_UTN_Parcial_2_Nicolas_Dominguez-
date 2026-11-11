package com.tp.jpa.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true) //indica que para las comparaciones tenga en cuenta los atributos de la clase heredada
@SuperBuilder
@ToString
@Entity
@Table(name = "DETALLES_PEDIDO")
public class DetallePedido extends Base{
    //atributos

    @Column(name = "CANTIDAD")
    private int cantidad;

    @Column(name = "SUBTOTAL")
    private Double subTotal;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    @EqualsAndHashCode.Exclude
    private Producto producto;

    //constructor
    public DetallePedido(int cantidad, Producto producto){
        super();
        this.cantidad = cantidad;
        this.producto = producto;
        calcularSubtotal();
    }

    //metodo para calcular subtotal
    public double calcularSubtotal() {
        double subTotalProducto = 0.0;
        if(producto != null) { //condicional defensiva para evitar posibles errores null
            subTotalProducto = producto.getPrecio() * cantidad;
        }
        return subTotal = subTotalProducto;
    }
}
