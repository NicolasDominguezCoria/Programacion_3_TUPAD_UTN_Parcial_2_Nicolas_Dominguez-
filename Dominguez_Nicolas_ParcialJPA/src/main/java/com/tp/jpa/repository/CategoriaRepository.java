package com.tp.jpa.repository;

import com.tp.jpa.model.Categoria;
import  com.tp.jpa.model.Producto;

import jakarta.persistence.EntityManager;
import java.util.List;


public class CategoriaRepository extends BaseRepository<Categoria> {
    //constructor
    public CategoriaRepository() {
        super(Categoria.class); //llama al constructor de la clase padre BaseRepository()
    }

    //1. Buscar productos activos pertenecientes a una categoria
    public List<Producto> buscarProductosPorCategoria(Long categoriaId) {
        EntityManager em = emf.createEntityManager();

        try {
            String jpql = "SELECT p FROM Categoria c " //consulta JPQL
                    + "JOIN c.productos p "            //recorre la coleccion de productos asociada a cada categoria
                    + "WHERE c.id = :catId "           //permite pasar el ID de la categoria sin concatenarlo en la consulta
                    + "AND p.eliminado = false";       //evita productos que hayan sido dado de baja (logica)

            return em.createQuery(jpql, Producto.class) //ejecuta la consulta tipada
                    .setParameter("catId", categoriaId) //asigna el valor recibido al parámetro
                    .getResultList(); //devuelve los productos encotnrados o una lista vacia

        } finally {
            em.close(); //cierra el entity manager
        }
    }

    //2. Agregar un producto a una categoria
    public Producto agregarProducto(Long categoriaId, Producto producto) {
        EntityManager em = emf.createEntityManager(); //crea el entity manager

        try {
            em.getTransaction().begin(); //inicia la transaccion

            Categoria categoria = em.find(Categoria.class, categoriaId); //obtiene la categoria por su id

            if (categoria == null || categoria.isEliminado()) {
                throw  new IllegalArgumentException("La categoria no existe o está eliminada."); //Lanza una excepcion si la categoria no existe o está eliminada

            }

            em.persist(producto); //persiste el nuevo producto

            categoria.addProducto(producto); //asocia el producto a la categoria
            em.getTransaction().commit(); //confirma la transaccion

            return producto; //devuelve el producto creado

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
