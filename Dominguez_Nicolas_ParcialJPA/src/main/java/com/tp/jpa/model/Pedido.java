package com.tp.jpa.model;

import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true) //indica que para las comparaciones tenga en cuenta los atributos de la clase heredada
@SuperBuilder
@ToString

@Entity
@Table(name = "PEDIDOS")
public class Pedido extends Base implements Calculable{
    //atributos
    @Column(name = "FECHA")
    @Builder.Default
    private LocalDate fecha = LocalDate.now();

    @Column(name = "ESTADO")
    private Estado estado;

    @Column(name = "TOTAL")
    @Builder.Default
    private Double total = 0.0;

    @Column(name = "FORMA_PAGO")
    private FormaPago formaPago;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_id")
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private Set<DetallePedido> detalles = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @EqualsAndHashCode.Exclude
    private Usuario usuario;

    //constructor
    public Pedido(Estado estado, FormaPago formaPago, Usuario usuario) {
        super();
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.detalles = new HashSet<>();
        asociarPedidoUsuario();
    }

    public void asociarPedidoUsuario(){
        if (usuario != null) {
            usuario.addPedido(this);
        }
    }

    //metodo para añadir detalles pedidos
    public void addDetallePedido(int cantidad, Producto producto){
        DetallePedido nuevoDetalle = new DetallePedido(cantidad, producto);
        this.detalles.add(nuevoDetalle);
        calcularTotal();
    }

    //metodo sobre escrito para calcular total del pedido
    @Override
    public void calcularTotal(){
        double totalPedido = 0.0;
        for (DetallePedido detalle: detalles) {
            totalPedido += detalle.getSubTotal();
        }
        this.total = totalPedido;
    }

}
