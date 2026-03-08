package sistema;

import estructuras.conjuntistas.ArbolAVL;
import estructuras.grafo.Grafo;
import estructuras.lineales.Lista;
import java.util.HashMap; // Para almacenar partidos por clave (pais1, pais2) y lista de resultados

public class CopaAmerica {
    // Las estructuras son privadas para que nadie las toque desde fuera
    private ArbolAVL<Equipo> equipos;
    private Grafo<Ciudad, Integer> mapaCiudades; // Integer es el tiempo de vuelo en minutos
    private HashMap<ClavePartido, Lista> partidos; // la lista es de DatosPartido

    public CopaAmerica() {
        // Inicializamos las estructuras
        this.equipos = new ArbolAVL<>();
        this.mapaCiudades = new Grafo<>();
        this.partidos = new HashMap<>();
    }

    // * ========================= CIUDADES =========================

    public boolean agregarCiudad(String nombre, boolean alojamiento, boolean esSede) {
        Ciudad ciudad = new Ciudad(nombre, alojamiento, esSede);
        return mapaCiudades.insertarVertice(ciudad);
    }

    public boolean eliminarCiudad(String nombre) {
        Ciudad buscada = new Ciudad(nombre, false, false);
        return mapaCiudades.eliminarVertice(buscada); // elimina vértice y todas sus rutas
    }

    public boolean modificarCiudad(String nombre, Boolean nuevoAlojamiento, Boolean nuevaSede) {
        boolean exito = false;
        Ciudad ciudad = this.obtenerCiudad(nombre);
        if (ciudad != null) {
            if (nuevoAlojamiento != null) {
                ciudad.setTieneAlojamiento(nuevoAlojamiento);
                exito = true;
            }
            if (nuevaSede != null) {
                ciudad.setEsSede(nuevaSede);
                exito = true;
            }
        }
        return exito;
    }

    public Ciudad obtenerCiudad(String nombre) {
        // Se asume que el grafo tiene un método obtenerVertice que busca por igualdad
        Ciudad aux = new Ciudad(nombre, false, false);
        return mapaCiudades.getVertice(aux);
    }

    public boolean agregarRuta(String origen, String destino, int tiempo) {
        Ciudad c1 = new Ciudad(origen, false, false);
        Ciudad c2 = new Ciudad(destino, false, false);
        return mapaCiudades.insertarArco(c1, c2, tiempo);
    }

    // * ========================= EQUIPOS =========================
    
    public boolean agregarEquipo(String nombre, String dt, char grupo) {
        Equipo nuevo = new Equipo(nombre, dt, grupo);
        return equipos.insertar(nuevo);
    }

    public boolean eliminarEquipo(String nombre) {
        Equipo buscador = new Equipo(nombre, "", 'X');
        return equipos.eliminar(buscador);
    }

    public boolean modificarEquipo(String nombre, String nuevoDT, Character nuevoGrupo) {
        boolean exito = false;
        Equipo equipo = obtenerEquipo(nombre);
        if (equipo != null && (nuevoDT != null || nuevoGrupo != null)) {
            if (nuevoDT != null) equipo.setDirectorTecnico(nuevoDT);
            if (nuevoGrupo != null) equipo.setGrupo(nuevoGrupo); // pruebo esta sintáxis de if inline
            exito = true;
        }
        return exito;
    }

    public Equipo obtenerEquipo(String nombre) {
        Equipo buscador = new Equipo(nombre, "", 'X');
        return equipos.obtenerElemento(buscador);
    }

    public String mostrarInfoEquipo(String nombrePais) {
        // Creamos un objeto "molde" solo con la clave para buscar en el AVL
        Equipo e = this.obtenerEquipo(nombrePais);
        String resultado = "Equipo no encontrado";

        if (e != null) {
            resultado = e.toString() + "\nDiferencia de goles: " + e.getDiferenciaGoles();
        }

        return resultado;
    }

    /**
     * Listar equipos en orden alfabético dentro de un rango (ABM - consigna 4 del TPO)
     * @param min
     * @param max
     * @return
     */
    public Lista equiposEnRango(String min, String max) {
        Equipo minEq = new Equipo(min, "", 'A');
        Equipo maxEq = new Equipo(max, "", 'Z');
        return equipos.listarRango(minEq, maxEq);
    }

    // * ==================== PARTIDOS ====================

    public boolean agregarPartido(String nombreEq1, String nombreEq2, String ronda,
            String nombreCiudad, String estadio, int goles1, int goles2) {

        boolean exito = false;
        Equipo eq1 = obtenerEquipo(nombreEq1);
        Equipo eq2 = obtenerEquipo(nombreEq2);
        Ciudad ciudad = obtenerCiudad(nombreCiudad);
        if (eq1 != null && eq2 != null && ciudad != null) {
            ClavePartido clave = new ClavePartido(eq1, eq2);
            DatosPartido datos = new DatosPartido(ronda, ciudad, estadio, goles1, goles2);

            Lista lista = this.partidos.get(clave);
            if (lista == null) {
                lista = new Lista();
                // asociamos la nueva lista vacía a la clave del partido en el hashmap
                partidos.put(clave, lista);
            }
            lista.insertar(datos, lista.longitud() + 1);

            this.actualizarEstadisticas(eq1, eq2, goles1, goles2);
            exito = true;
        }

        return exito;
    }

    /**
     * Método auxiliar para actualizar datos de los equipos después de cargar un partido.
     * 
     * @param eq1 equipo 1
     * @param eq2 equipo 2
     * @param g1 goles a favor del equipo 1
     * @param g2 goles a favor del equipo 2
     */
    private void actualizarEstadisticas(Equipo eq1, Equipo eq2, int g1, int g2) {
        eq1.agregarGolesAFavor(g1);
        eq1.agregarGolesEnContra(g2); // los goles en contra de eq1 son los goles a favor de eq2
        eq2.agregarGolesAFavor(g2);
        eq2.agregarGolesEnContra(g1); // los goles en contra de eq2 son los goles a favor de eq1

        if (g1 > g2) {
            eq1.agregarPuntos(3); // si gana eq1, suma 3 puntos
            eq2.agregarPuntos(0);
        } else if (g1 < g2) {
            eq1.agregarPuntos(0);
            eq2.agregarPuntos(3); // si gana eq2, suma 3 puntos
        } else {
            eq1.agregarPuntos(1); // si empatan sólo suman 1 punto
            eq2.agregarPuntos(1);
        }
    }

    /**
     * Devuelve una lista de DatosPartido entre dos equipos, o null si no existen los equipos o no
     * jugaron un partido entre sí.
     * 
     * @param nombreEq1
     * @param nombreEq2
     * @return Lista de DatosPartido o null
     */
    public Lista obtenerPartidosEntre(String nombreEq1, String nombreEq2) {
        Lista lis = null;
        Equipo eq1 = obtenerEquipo(nombreEq1);
        Equipo eq2 = obtenerEquipo(nombreEq2);
        if (eq1 != null && eq2 != null) {
            ClavePartido clave = new ClavePartido(eq1, eq2);
            lis = partidos.get(clave); // devuelva la lista de partidos entre eq1 y eq2
        }
        return lis;
    }

    // * ==================== VIAJES ====================

    // Devuelve lista de Ciudad que forma el camino mínimo entre origen y destino
    public Lista caminoMinimoCiudades(String origen, String destino) {
        Lista lis = null;
        Ciudad c1 = obtenerCiudad(origen);
        Ciudad c2 = obtenerCiudad(destino);
        if (c1 != null && c2 != null) {
            lis = mapaCiudades.caminoMenosEscalas(c1, c2);
        }
        return lis;
    }

    // Devuelve lista de Ciudad que forma el camino de menor tiempo entre origen y destino
    public Lista caminoMenorTiempo(String origen, String destino) {
        Lista lis = null;
        Ciudad c1 = obtenerCiudad(origen);
        Ciudad c2 = obtenerCiudad(destino);
        if (c1 != null && c2 != null) {
            lis = mapaCiudades.caminoMenosMinutos(c1, c2);
        }
        return lis;
    }
    
    /**
     * Devuelve lista de Ciudad que forma el camino de menor tiempo entre origen y destino, evitando
     * la ciudad indicada
     * 
     * @param origen Nombre de ciudad de origen
     * @param destino Nombre de ciudad destino
     * @param evitar Nombre de ciudad a evitar
     * @return Lista de ciudades que forman el camino solicitado, null si no existe tal camino
     */
    public Lista caminoMenorTiempoEvitando(String origen, String destino, String evitar) {
        Lista lis = null;
        Ciudad c1 = obtenerCiudad(origen);
        Ciudad c2 = obtenerCiudad(destino);
        Ciudad cEvitar = obtenerCiudad(evitar);
        if (c1 != null && c2 != null && cEvitar != null) {
            lis = mapaCiudades.caminoMenosMinutosSinPasar(c1, c2, cEvitar);
        }
        return lis;
    }

    /**
     * Devuelve una lista de caminos (lista de lista de Ciudad) entre origen y destino.
     * 
     * @param origen
     * @param destino
     * @return
     */
    public Lista todosLosCaminos(String origen, String destino) {
        Lista lis = null;
        Ciudad c1 = obtenerCiudad(origen);
        Ciudad c2 = obtenerCiudad(destino);
        if (c1 != null && c2 != null) {
            lis = mapaCiudades.todosLosCaminos(c1, c2); // backtracking
        }
        return lis;
    }

    /**
     * Devuelve una lista de caminos (lista de lista de Ciudad) entre origen y destino que tengan al
     * menos un alojamiento en dicho camino.
     * 
     * @param caminos
     * @return Lista o null si la lista de caminos es null
     */
    public Lista filtrarCaminosConAlojamiento(Lista caminos) {
        Lista resultado = null;
        if (caminos != null) {
            resultado = new Lista();
            for (int i = 1; i <= caminos.longitud(); i++) {
                Lista camino = (Lista) caminos.recuperar(i);
                if (tieneAlojamientoEnCamino(camino)) {
                    resultado.insertar(camino, resultado.longitud() + 1);
                }
            }
        }
        return resultado;
    }

    private boolean tieneAlojamientoEnCamino(Lista camino) {
        boolean tieneAlojamiento = false;
        for (int i = 1; i <= camino.longitud(); i++) {
            Ciudad ciudad = (Ciudad) camino.recuperar(i);
            if (ciudad.getTieneAlojamiento()) {
                tieneAlojamiento = true;
            }
        }
        return tieneAlojamiento;
    }

    // * ==================== LISTA ORDENADA POR GOLES ====================

    /**
     * @return Lista de equipos ordenada por goles a favor (descendente).
     */
    public Lista equiposOrdenadosPorGoles() {
        // Obtener todos los equipos en orden alfabético
        Lista todos = equipos.listar();
        // Crear una nueva lista para ordenar (no se guarda)
        Lista ordenada = new Lista();
        for (int i = 1; i <= todos.longitud(); i++) {
            ordenada.insertar(todos.recuperar(i), i);
        }
        // Ordenamiento por selección (descendente por goles a favor)
        for (int i = 1; i <= ordenada.longitud(); i++) {
            for (int j = i + 1; j <= ordenada.longitud(); j++) {
                Equipo e1 = (Equipo) ordenada.recuperar(i);
                Equipo e2 = (Equipo) ordenada.recuperar(j);
                if (e1.getGolesAFavor() < e2.getGolesAFavor()) {
                    // Intercambiar
                    ordenada.eliminar(i);
                    ordenada.insertar(e2, i);
                    ordenada.eliminar(j);
                    ordenada.insertar(e1, j);
                }
            }
        }
        return ordenada;
    }

    // * ==================== MOSTRAR SISTEMA ====================

    public String mostrarEstructuras() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== EQUIPOS (AVL) =====\n");
        sb.append(equipos.toString()); // asume que el AVL tiene toString en orden
        sb.append("\n===== CIUDADES (GRAFO) =====\n");
        sb.append(mapaCiudades.toString());
        sb.append("\n===== PARTIDOS (HASHMAP) =====\n");
        for (ClavePartido clave : partidos.keySet()) {
            sb.append("Partidos entre ").append(clave.getEq1().getNombre()).append(" y ")
                    .append(clave.getEq2().getNombre()).append(":\n");
            Lista lista = partidos.get(clave);
            for (int i = 1; i <= lista.longitud(); i++) {
                DatosPartido dp = (DatosPartido) lista.recuperar(i);
                sb.append("  ").append(dp.toString()).append("\n");
            }
        }
        return sb.toString();
    }

    // * ==================== CARGA DESDE ARCHIVO ====================

    // TODO: Implementar método para cargar datos desde un archivo de texto
    // Probablemente cree una clase Menu
}
