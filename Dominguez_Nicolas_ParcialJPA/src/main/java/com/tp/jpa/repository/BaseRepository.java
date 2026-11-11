package com.tp.jpa.repository;

import com.tp.jpa.model.Base;
import com.tp.jpa.util.JPAUtil;

import jakarta.persistence.*;

import java.util.Optional;
import java.util.List;

//declaramos la clase como abstacta ya que se usa como clase padre
//e indicamos que si bien es un genérica solo puede utilizar tipos de clases que hereden de la clase Base
public abstract class BaseRepository<T extends Base> {
    //atributos
    private final Class<T> entityClass; //almacena la clase concreta con la que trabajará el repositorio
    protected final EntityManagerFactory emf; //crea los distintos EntityManager que utilizaremos en cada operación.
                                              //la declaramos como protected para que las clases hijas también puedan acceder a este atributo

    //constructor
    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    protected Class<T> getEntityClass() { //devuelve la clase concreta que recibió el repositorio.
        return entityClass;
    }

    // 1. Guardar (T entity)
    public T guardar(T entity) {
        EntityManager em = emf.createEntityManager(); //crea el entity manager
        try {
            em.getTransaction().begin(); //inicia la transaccion

            T resultado; //entidad que vamos a devolver

            if (entity.getId() == null) { //si el ID es null usamos persist()
                em.persist(entity);
                resultado = entity;
            } else {
                resultado = em.merge(entity); //si no lo es usamos merge()
            }

            em.getTransaction().commit(); //confirma transacción

            return resultado;
        } catch (Exception e) { //manejo de excepciones
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {  //cerramos el entity manager
            em.close();
        }
    }

    // 2. buscarPorId(Long id)
    public Optional<T> buscarPorId(Long id){

        EntityManager em = emf.createEntityManager(); //creamos el entity manager
        try{

            T entidad = em.find(entityClass, id); //realiza la busqueda y almacena el resultado
            return Optional.ofNullable(entidad); //convierte el resultado en un optional

        } finally {
            em.close(); //cierra el entity manager
        }
    }

    // 3. Listar activos
    public List<T> listarActivos() {
        EntityManager em = emf.createEntityManager(); // crea el entity manager
        try {
            String nombreEntidad = getEntityClass().getSimpleName(); //obtiene unicamente el nombre de la entidad

            String jpql = "SELECT e FROM " + nombreEntidad // consulta JPQL con la condicion eliminado = false
                    + " e WHERE e.eliminado = false";

            return em.createQuery(jpql, entityClass).getResultList(); //ejecuta la consulta tipada y devuelve una lista con los resultados
                                                                      //si no encuentra registros devuelve una lista vacia
        } finally {
            em.close(); //cierra el entity manager
        }
    }

    // 4. Eliminar lógico
    // 4. Eliminar lógicamente
    public boolean eliminarLogico(Long id) {

        EntityManager em = emf.createEntityManager();

        try {

            T entidad = em.find(entityClass, id); //busca la entidad y almacena el resultado

            if (entidad == null || entidad.isEliminado()) { //comprueba si existe y el estado del atributo eliminado de la clase Base
                return false; //cancela la operacion
            }

            em.getTransaction().begin(); //inicia la transaccion

            entidad.setEliminado(true); //modifica el estado del atributo eliminar de la entidad

            em.merge(entidad); //sincroniza el cambio

            em.getTransaction().commit(); //confirma la operacion

            return true; //indica que la operacion se realizó correctamente

        } catch (Exception e) { //captura errores y ejecuta rollback

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            throw e;

        } finally {
            em.close(); //cierra el entity manager
        }
    }

}
