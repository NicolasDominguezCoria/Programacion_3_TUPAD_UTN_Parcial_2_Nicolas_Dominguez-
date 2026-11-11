package com.tp.jpa.util;

import jakarta.persistence.*;

public class JPAUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("miUnidad");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    public static void close() {
        if (emf.isOpen()) emf.close();
    }
}
