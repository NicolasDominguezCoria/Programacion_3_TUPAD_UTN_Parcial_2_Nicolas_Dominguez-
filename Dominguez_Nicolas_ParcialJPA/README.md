# Java Persistence API (JPA) – ABM de Categorías y Productos

## Programación 3 - Segundo Examen Parcial

## Alumno: Daniel Nicolas Dominguez Coria

## TUPAD - UTN

## Descripción del proyecto

Aplicación de consola desarrollada en Java que permite gestionar categorías y productos mediante operaciones ABM (Alta, Baja y Modificación), utilizando **Java Persistence API (JPA)** para la persistencia de datos.

El proyecto implementa una arquitectura basada en el patrón Repository, con un repositorio genérico (`BaseRepository`) que centraliza las operaciones comunes y repositorios específicos para categorías y productos.

Se utiliza Hibernate como implementación de JPA y H2 como sistema de gestión de base de datos. Además, se implementa la baja lógica de registros, permitiendo conservar los datos en la base de datos sin eliminarlos físicamente.

### Funcionalidades principales

- Gestión de categorías: alta, listado, modificación y baja lógica.
- Gestión de productos: alta, listado, modificación y baja lógica.
- Asociación de productos con sus respectivas categorías.
- Validación de categorías existentes y control de productos asociados antes de eliminar una categoría.
- Reportes de productos agrupados por categoría.

## Tecnologías utilizadas

- Java 21.
- Gradle.
- Jakarta Persistence API (JPA).
- Hibernate.
- H2 Database.
- Lombok.

## Instrucciones de ejecución

1. Descargar o clonar el repositorio del proyecto.
2. Abrir el proyecto desde un entorno de desarrollo compatible con Gradle como IntelliJ IDEA.
3. Verificar que se encuentre instalado Java 21.
4. Esperar a que Gradle sincronice las dependencias definidas en `build.gradle`.
5. Ejecutar la clase `Main`, ubicada en el paquete `com.tp.jpa`.

La aplicación iniciará mostrando un menú principal desde el cual se podrá acceder a la gestión de categorías, productos y reportes.

## Base de datos

El proyecto utiliza H2 en modo archivo, por lo que los datos se almacenan localmente y se conservan entre ejecuciones.

La configuración de conexión y persistencia se encuentra en el archivo `persistence.xml`, dentro de `src/main/resources/META-INF/`.

Hibernate se encarga de gestionar la estructura de las tablas mediante la configuración de JPA.