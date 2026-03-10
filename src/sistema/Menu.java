package sistema;

import java.util.Scanner;
import estructuras.lineales.Lista;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Menú del sistema Copa América 2024.
 */

public class Menu {
    private CopaAmerica copa;
    private Scanner sc;

    public Menu() {
        copa = new CopaAmerica();
        sc = new Scanner(System.in);
    }

    public void iniciar() {
        this.copa.getLogger().iniciarSesion(); // encabezado de sesión
        int opcion;
        do {
            this.mostrarMenuPrincipal();
            opcion = leerEntero("Ingrese opción: ");
            switch (opcion) {
                case 1 -> this.menuCiudades();
                case 2 -> this.menuEquipos();
                case 3 -> this.altaPartido();
                case 4 -> this.consultasEquipos();    // Ahora tienen un bucle interno para voler al menú de consulta
                case 5 -> this.consultasPartidos();   // Ahora tienen un bucle interno para voler al menú de consulta
                case 6 -> this.consultasViajes();     // Ahora tienen un bucle interno para voler al menú de consulta
                case 7 -> this.listarEquiposPorGoles();
                case 8 -> this.mostrarSistema();
                case 9 -> this.cargarDatosIniciales();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida");
            }
        } while (opcion != 0);
        // Al salir del menú, registramos el estado final del sistema y cerramos el logger
        this.copa.getLogger().registrarEstado("ESTADO FINAL DEL SISTEMA", copa.mostrarEstructuras());
        this.copa.getLogger().cerrarSesion();
        this.copa.getLogger().cerrar();
        sc.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- COPA AMÉRICA 2024 ---");
        System.out.println("1. ABM Ciudades");
        System.out.println("2. ABM Equipos");
        System.out.println("3. Alta de partidos");
        System.out.println("4. Consultas sobre equipos");
        System.out.println("5. Consultas sobre partidos");
        System.out.println("6. Consultas sobre viajes");
        System.out.println("7. Listar equipos ordenados por goles a favor");
        System.out.println("8. Mostrar sistema (debug)");
        System.out.println("9. Cargar datos desde archivo");
        System.out.println("0. Salir");
    }

    // Submenú de Ciudades
    private void menuCiudades() {
        int op;
        do {
            System.out.println("\n--- ABM Ciudades ---");
            System.out.println("1. Agregar ciudad");
            System.out.println("2. Modificar ciudad");
            System.out.println("3. Eliminar ciudad");
            System.out.println("4. Agregar ruta");
            System.out.println("5. Eliminar ruta");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Seleccione: ");
            
            switch (op) {
                case 1 -> agregarCiudad();
                case 2 -> modificarCiudad();
                case 3 -> eliminarCiudad();
                case 4 -> agregarRuta();
                case 5 -> eliminarRuta();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    private void agregarCiudad() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim().toUpperCase();
        System.out.print("¿Tiene alojamiento? (true/false): ");
        boolean aloj = leerBoolean();
        System.out.print("¿Es sede? (true/false): ");
        boolean sede = leerBoolean();
        if (copa.agregarCiudad(nombre, aloj, sede)) {
            System.out.println("Ciudad agregada.");
            copa.getLogger().registrar("ABM Ciudad - Alta: se agregó la ciudad " + nombre
                    + " (Alojamiento: " + aloj + ", Sede: " + sede + ")");
        } else {
            System.out.println("Error: la ciudad ya existe.");
            copa.getLogger()
                    .registrar("ABM Ciudad - Alta FALLIDA: la ciudad " + nombre + " ya existe");
        }
    }

    private void modificarCiudad() {
        System.out.print("Nombre de la ciudad a modificar: ");
        String nombre = sc.nextLine().trim().toUpperCase();
        System.out.print("¿Nuevo alojamiento? (vacío para no cambiar): ");
        String alojStr = sc.nextLine().trim();
        Boolean aloj = alojStr.isEmpty() ? null : Boolean.parseBoolean(alojStr);
        System.out.print("¿Nueva sede? (vacío para no cambiar): ");
        String sedeStr = sc.nextLine().trim();
        Boolean sede = sedeStr.isEmpty() ? null : Boolean.parseBoolean(sedeStr);
        if (copa.modificarCiudad(nombre, aloj, sede)) {
            System.out.println("Ciudad modificada.");
            copa.getLogger()
                    .registrar("ABM Ciudad - Modificación: " + nombre
                            + (aloj != null ? " | Alojamiento -> " + aloj : "")
                            + (sede != null ? " | Sede -> " + sede : ""));
        } else {
            System.out.println("Ciudad no encontrada.");
            copa.getLogger().registrar(
                    "ABM Ciudad - Modificación FALLIDA: ciudad " + nombre + " no encontrada");
        }
    }

    private void eliminarCiudad() {
        System.out.print("Nombre de la ciudad a eliminar: ");
        String nombre = sc.nextLine().trim().toUpperCase();
        if (copa.eliminarCiudad(nombre)) {
            System.out.println("Ciudad eliminada.");
            copa.getLogger().registrar("ABM Ciudad - Baja: se eliminó la ciudad " + nombre);
        } else {
            System.out.println("Ciudad no encontrada o no se pudo eliminar.");
            copa.getLogger()
                    .registrar("ABM Ciudad - Baja FALLIDA: ciudad " + nombre + " no encontrada");
        }
    }

    private void agregarRuta() {
        System.out.print("Ciudad origen: ");
        String origen = sc.nextLine().trim().toUpperCase();
        System.out.print("Ciudad destino: ");
        String destino = sc.nextLine().trim().toUpperCase();
        System.out.print("Tiempo de vuelo (minutos): ");
        int tiempo = leerEntero("");
        if (copa.agregarRuta(origen, destino, tiempo)) {
            System.out.println("Ruta agregada.");
            copa.getLogger().registrar("ABM Ciudad - Alta de ruta: " + origen + " <-> " + destino
                    + " (" + tiempo + " min)");
        } else {
            System.out.println("Error: alguna ciudad no existe o la ruta ya existe.");
            copa.getLogger()
                    .registrar("ABM Ciudad - Alta de ruta FALLIDA: " + origen + " <-> " + destino);
        }
    }

    private void eliminarRuta() {
        System.out.print("Ciudad origen: ");
        String origen = sc.nextLine().trim().toUpperCase();
        System.out.print("Ciudad destino: ");
        String destino = sc.nextLine().trim().toUpperCase();
        if (copa.eliminarRuta(origen, destino)) {
            System.out.println("Ruta eliminada.");
            copa.getLogger().registrar("ABM Ciudad - Baja de ruta: " + origen + " <-> " + destino);
        } else {
            System.out.println("Error: ruta no existe o ciudades no válidas.");
            copa.getLogger()
                    .registrar("ABM Ciudad - Baja de ruta FALLIDA: " + origen + " <-> " + destino);
        }
    }

    // Submenú de Equipos
    private void menuEquipos() {
        int op;
        do {
            System.out.println("\n--- ABM Equipos ---");
            System.out.println("1. Agregar equipo");
            System.out.println("2. Modificar equipo");
            System.out.println("3. Eliminar equipo");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Seleccione: ");
            
            switch (op) {
                case 1 -> agregarEquipo();
                case 2 -> modificarEquipo();
                case 3 -> eliminarEquipo();
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    private void agregarEquipo() {
        System.out.print("Nombre del país: ");
        String nombre = sc.nextLine().trim().toUpperCase();
        System.out.print("Director técnico: ");
        String dt = sc.nextLine().trim();
        System.out.print("Grupo (A/B/C/D): ");
        char grupo = leerGrupoObligatorio();
        if (copa.agregarEquipo(nombre, dt, grupo)) {
            System.out.println("Equipo agregado.");
            copa.getLogger().registrar(
                    "ABM Equipo - Alta: " + nombre + " (DT: " + dt + ", Grupo: " + grupo + ")");
        } else {
            System.out.println("Error: el equipo ya existe.");
            copa.getLogger()
                    .registrar("ABM Equipo - Alta FALLIDA: el equipo " + nombre + " ya existe");
        }
    }

    private void modificarEquipo() {
        System.out.print("Nombre del país a modificar: ");
        String nombre = sc.nextLine().trim().toUpperCase();
        System.out.print("Nuevo DT (vacío para no cambiar): ");
        String dt = sc.nextLine().trim().toUpperCase();
        if (dt.isEmpty())
            dt = null;
        System.out.print("Nuevo grupo (vacío para no cambiar): ");
        Character grupo = leerGrupoOpcional();

        if (copa.modificarEquipo(nombre, dt, grupo)) {
            System.out.println("Equipo modificado.");
            copa.getLogger()
                    .registrar("ABM Equipo - Modificación: " + nombre
                            + (dt != null ? " | DT -> " + dt : "")
                            + (grupo != null ? " | Grupo -> " + grupo : ""));
        } else {
            System.out.println("Equipo no encontrado.");
            copa.getLogger().registrar(
                    "ABM Equipo - Modificación FALLIDA: equipo " + nombre + " no encontrado");
        }
    }

    private void eliminarEquipo() {
        System.out.print("Nombre del país a eliminar: ");
        String nombre = sc.nextLine().trim().toUpperCase();
        if (copa.eliminarEquipo(nombre)) {
            System.out.println("Equipo eliminado.");
            copa.getLogger().registrar("ABM Equipo - Baja: se eliminó el equipo " + nombre);
        } else {
            System.out.println("Equipo no encontrado.");
            copa.getLogger()
                    .registrar("ABM Equipo - Baja FALLIDA: equipo " + nombre + " no encontrado");
        }
    }

    // Alta de partido
    private void altaPartido() {
        System.out.println("--- Alta de partido ---");
        System.out.print("Equipo 1: ");
        String eq1 = sc.nextLine().trim().toUpperCase();
        System.out.print("Equipo 2: ");
        String eq2 = sc.nextLine().trim().toUpperCase();
        System.out.print("Ronda (ej. GRUPO, CUARTOS, SEMIFINAL, FINAL): ");
        String ronda = sc.nextLine().trim().toUpperCase();
        System.out.print("Ciudad del evento: ");
        String ciudad = sc.nextLine().trim().toUpperCase();
        System.out.print("Estadio: ");
        String estadio = sc.nextLine().trim().toUpperCase();
        System.out.print("Goles de " + eq1 + ": ");
        int g1 = leerEntero("");
        System.out.print("Goles de " + eq2 + ": ");
        int g2 = leerEntero("");
        if (copa.agregarPartido(eq1, eq2, ronda, ciudad, estadio, g1, g2))
            System.out.println("Partido registrado.");
        else
            System.out.println("Error: verifique que los equipos y la ciudad existan.");
    }

    // Consultas de equipos
    private void consultasEquipos() {
        int op;
        do {
            System.out.println("\n--- Consultas de equipos ---");
            System.out.println("1. Mostrar información de un equipo");
            System.out.println("2. Listar equipos en rango alfabético");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Seleccione: ");
            
            switch (op) {
                case 1 -> {
                    System.out.print("Nombre del país: ");
                    String nombre = sc.nextLine().trim().toUpperCase();
                    System.out.println(copa.mostrarInfoEquipo(nombre));
                }
                case 2 -> {
                    System.out.print("Límite inferior (min): ");
                    String min = sc.nextLine().trim().toUpperCase();
                    System.out.print("Límite superior (max): ");
                    String max = sc.nextLine().trim().toUpperCase();
                    Lista lista = copa.equiposEnRango(min, max);
                    if (lista == null || lista.longitud() == 0)
                        System.out.println("No hay equipos en ese rango.");
                    else {
                        System.out.println("Equipos en el rango [" + min + ", " + max + "]:");
                        for (int i = 1; i <= lista.longitud(); i++)
                            System.out.println("  " + lista.recuperar(i));
                    }
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    // Consultas de partidos - AHORA CON BUCLE INTERNO (Y CORREGIDO)
    private void consultasPartidos() {
        int op;
        do {
            System.out.println("\n--- Consultas de partidos ---");
            System.out.println("1. Buscar partidos entre dos equipos");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Seleccione: ");

            switch (op) {
                case 1 -> {
                    System.out.print("Ingrese equipo 1: ");
                    String eq1 = sc.nextLine().trim().toUpperCase();
                    System.out.print("Ingrese equipo 2: ");
                    String eq2 = sc.nextLine().trim().toUpperCase();

                    Equipo e1 = copa.obtenerEquipo(eq1);
                    Equipo e2 = copa.obtenerEquipo(eq2);

                    if (e1 != null && e2 != null) {
                        if (e1.compareTo(e2) > 0) {
                            Equipo aux = e1;
                            e1 = e2;
                            e2 = aux;
                        }
                        Lista partidos = copa.obtenerPartidosEntre(e1.getNombre(), e2.getNombre());

                        if (partidos != null && partidos.longitud() > 0) {
                            System.out.println("\nPartidos entre " + e1.getNombre() + " y "
                                    + e2.getNombre() + ":");
                            for (int i = 1; i <= partidos.longitud(); i++) {
                                DatosPartido dp = (DatosPartido) partidos.recuperar(i);
                                System.out.println(dp.toString(e1, e2));
                            }
                        } else {
                            System.out.println("No se encontraron partidos entre esos equipos.");
                        }
                    } else {
                        System.out.println("Uno o ambos equipos no existen.");
                    }
                }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    // Consultas de viajes
    private void consultasViajes() {
        int op;
        do {
            System.out.println("\n--- Consultas de viajes ---");
            System.out.println("1. Camino con mínima cantidad de ciudades");
            System.out.println("2. Camino de menor tiempo");
            System.out.println("3. Camino más corto evitando una ciudad");
            System.out.println("4. Todos los caminos y filtrar por alojamiento");
            System.out.println("0. Volver al menú principal");
            op = leerEntero("Seleccione: ");
            
            switch (op) {
                case 1 -> {
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine().trim().toUpperCase();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine().trim().toUpperCase();
                    mostrarCamino(copa.caminoMinimoCiudades(origen, destino),
                            "Camino con menos ciudades");
                }
                case 2 -> {
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine().trim().toUpperCase();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine().trim().toUpperCase();
                    mostrarCamino(copa.caminoMenorTiempo(origen, destino),
                            "Camino de menor tiempo");
                }
                case 3 -> {
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine().trim().toUpperCase();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine().trim().toUpperCase();
                    System.out.print("Ciudad a evitar: ");
                    String evitar = sc.nextLine().trim().toUpperCase();
                    if (evitar.equalsIgnoreCase(origen) || evitar.equalsIgnoreCase(destino)) {
                        System.out.println("La ciudad a evitar no puede ser el origen ni el destino.");
                    } else {
                        mostrarCamino(copa.caminoMenorTiempoEvitando(origen, destino, evitar),
                                "Camino más corto evitando " + evitar);
                    }
                }
                case 4 -> {
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine().trim().toUpperCase();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine().trim().toUpperCase();

                    // Lista de listas de ciudades (cada lista es un camino posible)
                    Lista todos = copa.todosLosCaminos(origen, destino);

                    if (todos == null || todos.longitud() == 0) {
                        System.out.println("No hay caminos.");
                    } else {
                        // Generar nombre de archivo con timestamp
                        String timestamp = java.time.LocalDateTime.now().format(
                                java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                        String nombreArchivo =
                                String.format("caminos_%s_%s_%s.log", origen, destino, timestamp);

                        System.out.println(
                                "Generando reporte de " + todos.longitud() + " caminos...");
                        System.out.println("Esto puede tomar unos segundos...");

                        // Formatear TODOS los caminos (sin filtro)
                        String contenidoTodos = copa.formatearCaminosParaArchivo(todos,
                                "TODOS LOS CAMINOS DE " + origen + " A " + destino);

                        // Guardar archivo de todos los caminos
                        if (guardarEnArchivo(contenidoTodos, "TODOS_" + nombreArchivo)) {
                            System.out.println("✓ Archivo guardado: TODOS_" + nombreArchivo);
                        }

                        // Filtrar por alojamiento
                        Lista filtrados = copa.filtrarCaminosConAlojamiento(todos);

                        // Formatear caminos con alojamiento
                        String contenidoFiltrados = copa.formatearCaminosParaArchivo(filtrados,
                                "CAMINOS CON ALOJAMIENTO DE " + origen + " A " + destino
                                        + " (Total: " + filtrados.longitud() + " de "
                                        + todos.longitud() + ")");

                        // Guardar archivo de caminos filtrados
                        if (guardarEnArchivo(contenidoFiltrados, "ALOJAMIENTO_" + nombreArchivo)) {
                            System.out.println("✓ Archivo guardado: ALOJAMIENTO_" + nombreArchivo);
                        }

                        // Mostrar solo un resumen en pantalla
                        System.out.println("\n=== RESUMEN ===");
                        System.out.println("Total de caminos encontrados: " + todos.longitud());
                        System.out.println("Caminos con alojamiento: " + filtrados.longitud());
                        System.out.println("Caminos sin alojamiento: "
                                + (todos.longitud() - filtrados.longitud()));
                        System.out.println("\nLos detalles completos se guardaron en:");
                        System.out.println("  - TODOS_" + nombreArchivo);
                        System.out.println("  - ALOJAMIENTO_" + nombreArchivo);
                    }
                }
                // case 98 -> { // otro test
                //     System.out.println("=== ESTADO DE CIUDADES ===");
                //     String[] ciudadesTest = {"ATLANTA", "ORLANDO", "DENVER", "MIAMI", "DALLAS"};
                //     for (String nom : ciudadesTest) {
                //         Ciudad c = copa.obtenerCiudad(nom);
                //         System.out.println(nom + ": alojamiento=" + c.getTieneAlojamiento()
                //                 + ", sede=" + c.getEsSede());
                //     }
                // }
                // // ! Temporal, para test
                // case 99 -> { // opción de debug
                //     Ciudad miami = copa.obtenerCiudad("MIAMI");
                //     System.out.println("MIAMI tiene alojamiento? " + miami.getTieneAlojamiento());
                //     // Cambio a false y verifico
                //     miami.setTieneAlojamiento(false);
                //     System.out.println(
                //             "Ahora MIAMI tiene alojamiento? " + miami.getTieneAlojamiento());
                // }
                case 0 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida");
            }
        } while (op != 0);
    }

    // Método privado para mostrar un camino (Lista de ciudades) con un título descriptivo
    private void mostrarCamino(Lista camino, String titulo) {
        if (camino != null && camino.longitud() > 0) {
            System.out.print(titulo + ": ");
            for (int i = 1; i <= camino.longitud(); i++) {
                Ciudad cam = (Ciudad) camino.recuperar(i);
                System.out.print(cam.getNombre());
                if (i < camino.longitud()) System.out.print(" -> ");
            }
            System.out.println();
        } else {
            System.out.println(titulo + ": No hay camino.");
        }
    }

    // Lista ordenada por goles
    private void listarEquiposPorGoles() {
        Lista lista = copa.listarEquiposPorGoles();
        if (lista.longitud() == 0) {
            System.out.println("No hay equipos cargados.");
        } else {
            System.out.println("Equipos ordenados por goles a favor (mayor a menor):");
            for (int i = 1; i <= lista.longitud(); i++) {
                Equipo e = (Equipo) lista.recuperar(i);
                System.out.println("  " + e.getNombre() + " - Goles a favor: " + e.getGolesAFavor());
            }
        }
    }

    // Mostrar sistema
    private void mostrarSistema() {
        System.out.println(copa.mostrarEstructuras());
    }

    // Carga desde archivo
    private void cargarDatosIniciales() {
        System.out.print(
                "Ingrese la ruta del archivo de carga o no ingrese nada y se usará 'cargaInicial.txt' por defecto: ");
        String ruta = sc.nextLine();
        if (ruta.isEmpty()) {
            ruta = "cargaInicial.txt";
        }
        if (copa.cargarDesdeArchivo(ruta)) {
            System.out.println("Datos cargados correctamente.");
        } else {
            System.out.println("Error al cargar los datos. Revise el formato del archivo.");
        }
    }

    // Métodos auxiliares de lectura
    private int leerEntero(String mensaje) {
        int num;
        while (true) {
            System.out.print(mensaje);
            try {
                num = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
        return num;
    }

    private boolean leerBoolean() {
        boolean resultado = false;
        boolean valido = false;
        while (!valido) {
            String entrada = sc.nextLine().trim().toLowerCase();
            if (entrada.equals("true")) {
                resultado = true;
                valido = true;
            } else if (entrada.equals("false")) {
                resultado = false;
                valido = true;
            } else {
                System.out.print("Debe ingresar true o false: ");
            }
        }
        return resultado;
    }

    private char leerGrupoObligatorio() {
        String grupoStr;
        char grupoValido = ' ';
        boolean valido;
        do {
            grupoStr = sc.nextLine().trim().toUpperCase();
            valido = grupoStr.length() == 1 && "ABCD".contains(grupoStr);
            if (valido) {
                grupoValido = grupoStr.charAt(0);
            } else {
                System.out.print("Grupo inválido. Ingrese A/B/C/D: ");
            }
        } while (!valido);
        return grupoValido;
    }

    private Character leerGrupoOpcional() {
        String grupoStr;
        Character grupoValido = null;
        boolean valido;
        do {
            grupoStr = sc.nextLine().trim();
            if (grupoStr.isEmpty()) {
                valido = true;
                grupoValido = null;
            } else {
                grupoStr = grupoStr.toUpperCase();
                valido = grupoStr.length() == 1 && "ABCD".contains(grupoStr);
                if (valido) {
                    grupoValido = grupoStr.charAt(0);
                } else {
                    System.out.print("Grupo inválido. Ingrese A/B/C/D (vacío para no cambiar): ");
                }
            }
        } while (!valido);
        return grupoValido;
    }

    /**
     * Guarda un String en un archivo.
     * 
     * @param contenido El texto a guardar
     * @param nombreArchivo Nombre del archivo (ej. "caminos_MIAMI_DALLAS.log")
     * @return true si se guardó correctamente, false en caso contrario
     */
    private boolean guardarEnArchivo(String contenido, String nombreArchivo) {
        try (java.io.BufferedWriter writer =
                new java.io.BufferedWriter(new java.io.FileWriter("logs/" + nombreArchivo))) {
            writer.write(contenido);
            return true;
        } catch (java.io.IOException e) {
            System.err.println(
                    "Error al guardar el archivo " + nombreArchivo + ": " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.iniciar();
    }
}