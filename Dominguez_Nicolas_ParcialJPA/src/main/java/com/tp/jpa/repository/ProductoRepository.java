package com.tp.jpa.repository;

import com.tp.jpa.model.Producto;

public class ProductoRepository extends BaseRepository<Producto> {

    //constructor
    public ProductoRepository() {
        super(Producto.class); //llama al constructor de la clase padre BaseRepository()
    }
}
