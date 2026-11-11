//TECNICATURA UNIVERSITARIA EN PROGRAMACION A DISTANCIA - UTN
//PROGRAMACION 3 - ENTREGA 2DO PARCIAL
//Alumno: Daniel Nicolas Dominguez Coria. DNI: 35085946

package com.tp.jpa;

import com.tp.jpa.model.Producto;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.util.JPAUtil;

import com.tp.jpa.model.Categoria;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {

    // adicional: al momento de ejecución se ejecutan una gran cantidad de logs de hibernate en consola
    // lo cual dificulta la lectura e interaccion de los menus. Este metodo estático los quita dejando ver solo mensajes de errores.
    static {
        java.util.logging.Logger
                .getLogger("org.hibernate")
                .setLevel(Level.SEVERE);
    }

    //Repositorios
    //instanciamos los repositoriios CategoriaRepositoy y ProductoRepository como private static para que solo pertenezcan a la clase Main y solo ella pueda accederlos, y como final para
    //que la referencia al repositorio no pueda apuntar a otro objeto despues de inicilizzarse
    private static final CategoriaRepository categoriaRepo = new CategoriaRepository();
    private static final ProductoRepository productoRepo = new ProductoRepository();

    //Tratamiento de datos inputs
    //Scanner para leer los datos ingresados por consola
    private static final Scanner sc = new Scanner(System.in);

    //tratamiento de inputs de tipo entero
    private static int intSeguro(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine(); //recibe la informacion como int

            try {
                return Integer.parseInt(input); //transforma el string en entero
            } catch (NumberFormatException e) { //captura la excepcion si no se ingresó un numero entero
                System.out.println("Valor ingresado no válido, se espera un valor tipo entero");
            }
        }
    }

    //Tratamiento de inputs de tipo Long
    public static Long longSeguro(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine();

            try {
                return Long.parseLong(input); //transforma el string en long
            } catch (NumberFormatException e) {
                System.out.println("Valor ingresado no válido, se espera un valor tipo Long");
            }
        }
    }

    //tratamiento de datos de tipo Double
    public static Double doubleSeguro(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine();

            try {
                return Double.parseDouble(input); //transforma el string en double
            } catch (NumberFormatException e) {
                System.out.println("Valor ingresado no válido, se espera un valor tipo double");
            }
        }
    }

    //SUB MENUS Y METODOS
    //CATEGORIA:
    // Submenú de gestión de categorías
    private static void menuCategorias() {

        int opcion; //recibe el valor ingresado por el usuario

        do {
            System.out.println("\n===== GESTIÓN DE CATEGORÍAS =====");
            System.out.println("1. Agregar categoría");
            System.out.println("2. Listar categorías");
            System.out.println("3. Modificar categoría");
            System.out.println("4. Eliminar categoría");
            System.out.println("0. Volver al menú principal");
            System.out.println("===============================");

            opcion = intSeguro(sc, "    Ingresá una opción: "); //evita una excepcion por ingresar un valor no numperico

            switch (opcion) {

                case 1:
                    agregarCategoria();
                    break;

                case 2:
                    listarCategorias();
                    break;

                case 3:
                    modificarCategoria();
                    break;

                case 4:
                    eliminarCategoria();
                    break;

                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;

                default:
                    System.out.println("Opción inválida.");
            }

        } while (opcion != 0);
    }

    //metodo para agregar una categoria
    private static void agregarCategoria() {
        System.out.println("\n --- NUEVA CATEGORIA ---");

        String nombre; //aloja el valor ingresado por el usuario

        do { //lo usamos para asegurarnos de que no se ingresen valores en blanco
            System.out.println("Ingrese el nombre (obligatorio): ");
            nombre = sc.nextLine().trim();

            if (nombre.isEmpty()) { //evita que se registre una categoría sin nombre o compuesta únicamente por espacios
                System.out.println("El nombre es obligatorio.");
            }
        } while (nombre.isEmpty()); //nos aseguramos de que el ciclo se ejecute al menos una vez

        //valida si ya existe una categoria con ese nombre para evitar errores de unicidad de clave primaria
        //aún si esta categoría a sido elimiada logicamente.
        List<Categoria> categorias = categoriaRepo.listarActivos();
        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Ya existe una categoria con el nombre " + nombre);
                return;
            }
        }

        System.out.println("Ingrese la descripción (opcional): ");
        String descripcion = sc.nextLine().trim(); //si no se ingresa un valor queda como null.

        Categoria categoria = new Categoria(); //instancia la categoria
        categoria.setNombre(nombre); //setea el atributo nombre con el valor ingresado
        categoria.setDescripcion(descripcion); //setea el atributo descripcion con el valor ingresado

        Categoria resultado = categoriaRepo.guardar(categoria); //añade la categoria a la bd
        System.out.println("\nCategoría " + resultado.getNombre() +" registrada correctamente con ID: " + resultado.getId());
    }

    //metodo para listar categorias activas
    private static void listarCategorias() {
        List<Categoria> categorias = categoriaRepo.listarActivos(); //busca y obtiene las categorias activas de la bd

        if(categorias.isEmpty()) { //comprueba que exista al menos un objeto en la lista
            System.out.println("\nNo hay categorias registradas aún.");
            return;
        }
        //imprime por consola las categorias
        System.out.println("\n --- CATEGORIAS REGISTRADAS ---");
        for (Categoria categoria : categorias) {
            System.out.println("------------------------------");
            System.out.println("ID: " + categoria.getId());
            System.out.println("Nombre: " + categoria.getNombre());
            System.out.println("Descripción: " + categoria.getDescripcion());
        }
        System.out.println("------------------------------");
    }

    // metodo para modificar una categoria
    private static void modificarCategoria() {
        List<Categoria> categorias = categoriaRepo.listarActivos(); //busca y obtiene las categorias activas de la bd

        if(categorias.isEmpty()) { //comprueba que exista al menos un objeto en la lista
            System.out.println("\nNo hay categorias disponibles para modificar.");
            return;
        }
        //imprime por consola las categorias con su id
        System.out.println("\n--- CATEGORIAS DISPONIBLES ---");
        for (Categoria categoria : categorias) {
            System.out.println("ID: " + categoria.getId()
            + " | Nombre: " + categoria.getNombre());
        }

        Long id = longSeguro(sc, "\nIngrese el ID de la categoria a modificar: "); //recibe el id de la categoria a modificar

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id); //obtiene la categorias de la bd que coincida con el id ingresado, es Optional por si devuelve un valor null

        if (resultado.isEmpty() || resultado.get().isEliminado()) { //comprueba si la busqueda encontró una categoria
            System.out.println("La categoria no existe o está eliminada.");
            return;
        }

        Categoria categoria = resultado.get(); //recupera el objeto
        //imprime los datos el objeto recuperado por consola
        System.out.println("\n--- DATOS ACTUALES ---");
        System.out.println("Nombre: " + categoria.getNombre());
        System.out.println("Descripción: " + categoria.getDescripcion());

        //solicitud de nuevos datos
        System.out.println("\nIngrese el nuevo nombre (Enter para conservar): ");
        String nuevoNombre = sc.nextLine().trim();

        //valida si ya existe una categoria con ese nombre para evitar errores de unicidad de clave primaria
        //aún si esta categoría a sido elimiada logicamente.
        for (Categoria existente : categorias) {
            if (existente.getNombre().equalsIgnoreCase(nuevoNombre)) {
                System.out.println("No se puede actualizar porque ya existe una categoria con el nombre " + nuevoNombre);
                return;
            }
        }
        //actualiza el nombre de la categoria
        if (!nuevoNombre.isEmpty()) {
            categoria.setNombre(nuevoNombre);
        }

        System.out.println("Ingrese la nueva descripción: (Enter para conseervar): ");
        String nuevaDescripcion = sc.nextLine().trim();
        //actualiza la descripcion de la categoria
        if (!nuevaDescripcion.isEmpty()) {
            categoria.setDescripcion(nuevaDescripcion);
        }

        //persiste la categoria con los datos modificados
        Categoria actualizada = categoriaRepo.guardar(categoria);
        System.out.println("\nCategoria actualizada correctamente.");
        System.out.println("Nombre: " + actualizada.getNombre());
        System.out.println("Descripcion: " + actualizada.getDescripcion());

    }

    //metodo para liminar una categoria de forma lógica
    private static void eliminarCategoria() {
        List<Categoria> categorias = categoriaRepo.listarActivos(); //obtiene y aloja las categorias activas

        if(categorias.isEmpty()) { //comprueba que exista al menos un objeto dentro de la lista
            System.out.println("\nNo hay categorias disponibles para eliminar.");
            return;
        }

        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");
        for (Categoria categoria : categorias) { //imprime las categorias por consola
            System.out.println("ID: " + categoria.getId()
                    + " | Nombre: " + categoria.getNombre());
        }

        Long id = longSeguro(sc, "\nIngresá el id de la categoría a eliminar");

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id); //obtiene la categoria que coincida con el id ingresado

        if(resultado.isEmpty() || resultado.get().isEliminado()) { //comprueba si existe un resutlado en el contenedor y su estado
            System.out.println("La categoria no existe o ya se encuentra eliminada.");
            return;
        }


        Categoria categoria = resultado.get(); //recupera el objeto
        List<Producto> productos = categoriaRepo.buscarProductosPorCategoria(id); //obtiene productos asociados a esa categoria

        if (!productos.isEmpty()) { //comprueba si existen productos asociados a esa categoria
            System.out.println("\nNo se puede eliminar la categoría porque tiene "
                    + productos.size() + " productos activos asociados.");
            return;
        }

        boolean eliminado = categoriaRepo.eliminarLogico(id); //elimina la categoria logimanente

        if (eliminado) { //si eliminado = true
            System.out.println("\nCategoría '" + categoria.getNombre()
                    + "' eliminada correctamente.");
        } else { //si categoria = false
            System.out.println("\nNo se pudo eliminar la categoría.");
        }
    }

    //PRODUCTOS
    //Sub menu de productos
    private static void menuProductos() {
        int opcion;

        do {
            System.out.println("\n==== GESTIÓN DE PRODUCTOS ====");
            System.out.println("1. Agregar producto");
            System.out.println("2. Listar productos");
            System.out.println("3. Modificar producto");
            System.out.println("4. Eliminar producto");
            System.out.println("0. Volver al menú principal");
            System.out.println("===============================");

            opcion = intSeguro(sc, "    Ingresá una opción: ");

            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    modificarProducto();
                    break;
                case 4:
                    eliminarProducto();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
    }

    //metodo para agregar un producto
    private static void agregarProducto() {

        List<Categoria> categorias = categoriaRepo.listarActivos(); //obtiene categorias activas

        if (categorias.isEmpty()) { //comprueba que existan categorias en la lista
            System.out.println("\nNo hay categorias disponibles, debe exixtir al menos una categoria para poder agregar productos");
            return;
        }

        //imprime por consola las categorias activas
        System.out.println("\n--- CATEGORIAS ACTIVAS ---");
        for (Categoria categoria : categorias) {
            System.out.println("ID: " + categoria.getId()
                    + " | Nombre: " + categoria.getNombre());
        }

        Long idCategoria = longSeguro(sc, "\nIngrese el ID de la categoría: " ); //obtiene el valor ingresado por el usuario

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(idCategoria); //obtiene el objeto categoria que coincida con el id proporcionado

        if (resultado.isEmpty() || resultado.get().isEliminado()) { //comprueba que el contenedor no este vacio y el estado eliminado de la categoria
            System.out.println("La categoría no existe o está eliminada.");
            return;
        }

        Categoria categoria = resultado.get(); //recupera el objeto

        //Solicitar datos del producto
        String nombre;

        do { //solicita nombre de producto
            System.out.print("\nIngrese el nombre del producto (obligatorio): ");
            nombre = sc.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("El nombre es obligatorio.");
            }

        } while (nombre.isEmpty());

        List<Producto> productos = productoRepo.listarActivos(); //obtiene los productos activos
        //valida si el nombre ingresado coincido con un producto existente, aun si el mismo está eliminado
        for (Producto producto : productos) {
            if (producto.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Ya existe un producto con el nombre " + nombre);
                return;
            }
        }

        System.out.print("Ingrese la descripción (Opcional): ");
        String descripcion = sc.nextLine().trim();

        double precio; //recibe el valor ingresado para el precio

        do {
            precio = doubleSeguro(sc, "Ingrese el precio (obligatorio): ");

            if (precio <= 0) {
                System.out.println("El precio debe ser mayor a cero.");
            }

        } while (precio <= 0);

        int stock; //recibe el valor ingresado para el stock

        do {
            stock = intSeguro(sc, "Ingrese el stock (obligatorio): ");

            if (stock < 0) {
                System.out.println("El stock no puede ser negativo.");
            }

        } while (stock < 0);

        System.out.print("Ingrese la ruta de la imagen (opcional): ");
        String imagen = sc.nextLine().trim();

        // Disponibilidad

        String respuesta;
        do{
        System.out.print("¿El producto está disponible? (S/N): ");
        respuesta = sc.nextLine().trim().toLowerCase();

        if(respuesta.isEmpty() || respuesta != "s" || respuesta != "s") {
            System.out.println(" Respuesta no válida, se espera S o N");
        }
        } while (respuesta.isEmpty() || respuesta == "s" || respuesta == "n");

        boolean disponible = !respuesta.equalsIgnoreCase("N");

        // Crear producto
        Producto producto = new Producto(); //instancia el nuevo producto
        //setea loa valores del objeto instanciado con los valores ingresados
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagen(imagen);
        producto.setDisponible(disponible);

        // Guarda y asocia a la categoría
        Producto guardado = categoriaRepo.agregarProducto(idCategoria, producto);

        //imprime por consola
        System.out.println("\nProducto agregado correctamente.");
        System.out.println("ID: " + guardado.getId());
        System.out.println("Nombre: " + guardado.getNombre());
        System.out.println("Categoría: " + categoria.getNombre());
    }

    //Metodo para listar productos
    private static void listarProductos() {

        List<Categoria> categorias = categoriaRepo.listarActivos(); //obtiene las categorias en estado disponible desde la bd

        if (categorias.isEmpty()) { //comprueba si existen categorias
            System.out.println("\nNo hay categorías disponibles.");
            return;
        }

        boolean hayProductos = false; //se usa como guia para verificar si existen productos asociados a una categoria, por defecto se inicia como false

        System.out.println("\n--- LISTADO DE PRODUCTOS ---");
        for (Categoria categoria : categorias) { //busca los productos relacionados a cada categoria
            List<Producto> productos =
                    categoriaRepo.buscarProductosPorCategoria(categoria.getId());

            if (!productos.isEmpty()) { //comprueba si existen productos asociados a una categoria
                hayProductos = true; //cambia a true el booleano para poder imprimirlos

                System.out.println("\nCategoría: " + categoria.getNombre());
                //imprime por consola cada producto asociado a la categoria
                for (Producto producto : productos) {

                    System.out.println("--------------------------------");
                    System.out.println("ID: " + producto.getId());
                    System.out.println("Nombre: " + producto.getNombre());
                    System.out.println("Precio: $" + producto.getPrecio());
                    System.out.println("Stock: " + producto.getStock());
                    System.out.println("Disponible: "
                            + (producto.getDisponible() ? "Sí" : "No"));
                }
            }
        }

        if (!hayProductos) { //en caso de no encontrar productos asociados a categorias imprime por consola
            System.out.println("\nNo hay productos activos registrados.");
        }

        System.out.println("--------------------------------");
    }

    //Metodo para modificar un producto
    private static void modificarProducto() {

        // Obtiene productos activos
        List<Producto> productos = productoRepo.listarActivos();

        if (productos.isEmpty()) {
            System.out.println("\nNo hay productos disponibles para modificar.");
            return;
        }

        // Muestra productos disponibles
        System.out.println("\n--- PRODUCTOS DISPONIBLES ---");

        for (Producto producto : productos) {
            System.out.println("ID: " + producto.getId()
                    + " | Nombre: " + producto.getNombre());
        }

        // recibe el Id del producto a modificar
        Long id = longSeguro(sc, "\nIngrese el ID del producto a modificar: ");
        //obtiene el producto que coincida con el ID ingresado
        Optional<Producto> resultado = productoRepo.buscarPorId(id);

        if (resultado.isEmpty() || resultado.get().isEliminado()) { //comprueba que el el producto exista
            System.out.println("El producto no existe o está eliminado.");
            return;
        }

        Producto producto = resultado.get(); //recupera el producto

        // Muestra datos actuales
        System.out.println("\n--- DATOS ACTUALES ---");
        System.out.println("Nombre: " + producto.getNombre());
        System.out.println("Descrupción: " + producto.getDescripcion());
        System.out.println("Precio: $" + producto.getPrecio());
        System.out.println("Stock: " + producto.getStock());

        // Modificar nombre
        System.out.print("\nIngrese el nuevo nombre (Enter para conservar): ");
        String nuevoNombre = sc.nextLine().trim();

        //valida si ya existe una categoria con ese nombre para evitar errores de unicidad de clave primaria
        //aún si esta categoría a sido elimiada logicamente.
        for (Producto existente : productos) {
            if (existente.getNombre().equalsIgnoreCase(nuevoNombre)) {
                System.out.println("No se puede actualizar porque ya existe un producto con el nombre " + nuevoNombre);
                return;
            }
        }

        if (!nuevoNombre.isEmpty()) {
            producto.setNombre(nuevoNombre);
        }

        //Modificar descripción
        System.out.print("\nIngrese la nueva descripción (Enter para conservar): ");
        String nuevaDescripción = sc.nextLine().trim();

        if (nuevaDescripción.isEmpty()) {
            producto.setDescripcion(nuevaDescripción);
        }

        // Modificar precio
        System.out.print("Ingrese el nuevo precio (Enter para conservar): ");
        String entradaPrecio = sc.nextLine().trim(); //se ingresa como string para comprobar si el valor ingresado es distinto de null

        if (!entradaPrecio.isEmpty()) { //comprueba si no se ingresó un valor

            try {
                double nuevoPrecio = Double.parseDouble(entradaPrecio); //comprueba si el valor ingresado es de tipo double

                if (nuevoPrecio > 0) { //comprueba si el valor es mayor a cero
                    producto.setPrecio(nuevoPrecio); //actualiza el precio
                } else {
                    System.out.println("El precio no puede ser negativo. Se conservará el anterior.");
                }
            }  catch (NumberFormatException e) {
                  System.out.println("Valor ingresado no válido, se conservará el anterior");
            }

        }


        // Modificar stock
        System.out.print("Ingrese el nuevo stock (Enter para conservar): ");
        String entradaStock = sc.nextLine().trim();

        if (!entradaStock.isEmpty()) {
            try {
                int nuevoStock = Integer.parseInt(entradaStock);

                if (nuevoStock >= 0) {
                    producto.setStock(nuevoStock);
                } else {
                    System.out.println("El stock no puede ser negativo. Se conservará el anterior.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor ingresado no válido, se conservará el anterior.");
            }
        }

        // Guardar cambios
        Producto actualizado = productoRepo.guardar(producto);

        System.out.println("\nProducto actualizado correctamente.");
        System.out.println("Nombre: " + actualizado.getNombre());
        System.out.println("Descripción: " + actualizado.getDescripcion());
        System.out.println("Precio: $" + actualizado.getPrecio());
        System.out.println("Stock: " + actualizado.getStock());
    }

    //metodo para eliminar un producto de forma lógica
    private static void eliminarProducto() {

        // Obtiene productos activos
        List<Producto> productos = productoRepo.listarActivos();

        if (productos.isEmpty()) { //comprueba si existen objetos en la lista
            System.out.println("\nNo hay productos disponibles para eliminar.");
            return;
        }

        // Muestra productos disponibles
        System.out.println("\n--- PRODUCTOS DISPONIBLES ---");

        for (Producto producto : productos) {
            System.out.println("ID: " + producto.getId()
                    + " | Nombre: " + producto.getNombre());
        }

        // Seleccionar producto
        Long id = longSeguro(sc, "\nIngrese el ID del producto a eliminar: ");
        //obtiene el producto que coincida con el id ingresado
        Optional<Producto> resultado = productoRepo.buscarPorId(id);

        if (resultado.isEmpty() || resultado.get().isEliminado()) { //comprueba el estado del producto
            System.out.println("El producto no existe o está eliminado.");
            return;
        }

        Producto producto = resultado.get(); //recupera el objeto

        // Ejecuta la baja lógica
        boolean eliminado = productoRepo.eliminarLogico(id);

        if (eliminado) {
            System.out.println("\nProducto '" + producto.getNombre()
                    + "' eliminado correctamente.");
        } else {
            System.out.println("\nNo se pudo eliminar el producto.");
        }
    }

    //SUB MENU DE REPORTES
    private static void menuReportes() {

        List<Categoria> categorias = categoriaRepo.listarActivos(); //Obtiene las categorías activas

        if (categorias.isEmpty()) { //comprueba que haya al menos una categoria en la lista
            System.out.println("\nNo hay categorías disponibles para consultar.");
            return;
        }

        // Mostrar categorías
        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");

        for (Categoria categoria : categorias) { //lista las categorias con su Id y nombre
            System.out.println("ID: " + categoria.getId()
                    + " | Nombre: " + categoria.getNombre());
        }

        Long id = longSeguro(sc, "\nIngrese el ID de la categoría: ");

        Optional<Categoria> resultado = categoriaRepo.buscarPorId(id);

        if (resultado.isEmpty() || resultado.get().isEliminado()) {
            System.out.println("La categoría no existe o está eliminada.");
            return;
        }

        Categoria categoria = resultado.get();

        // 4. Obtener productos activos de la categoría
        List<Producto> productos =
                categoriaRepo.buscarProductosPorCategoria(id);

        System.out.println("\n--- REPORTE DE PRODUCTOS ---");
        System.out.println("Categoría: " + categoria.getNombre());

        if (productos.isEmpty()) {
            System.out.println("Esta categoría no tiene productos activos.");
            return;
        }

        // 5. Mostrar productos
        for (Producto producto : productos) {
            System.out.println("--------------------------------");
            System.out.println("ID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Descripción: " + producto.getDescripcion());
            System.out.println("Precio: $" + producto.getPrecio());
            System.out.println("Stock: " + producto.getStock());
        }

        System.out.println("--------------------------------");
        System.out.println("Total de productos: " + productos.size());
    }


    //metodo Main
    public static void main(String[] args) {


        boolean salir = false;

        System.out.println("BIENVENIDO AL SISTEMA DE GESTION DE PRODUCTOS DEL FOOD STORE!");
        while (!salir) {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1. Gestionar categorías");
            System.out.println("2. Gestionar productos");
            System.out.println("3. Reportes");
            System.out.println("0. Salir");
            System.out.println("=========================");


            int opcion = intSeguro(sc,  "Ingresá una opción: ");

            switch (opcion) {
                case 1:
                    menuCategorias();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuReportes();
                    break;
                case 0:
                    salir = true;
                    JPAUtil.close(); //cierra el EntityManagerFactory.
                    System.out.println("Aplicación finalizada. Hasta la Próxima!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }
}
