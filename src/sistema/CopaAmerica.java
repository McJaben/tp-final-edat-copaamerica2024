package sistema;

import estructuras.conjuntistas.ArbolAVL;
import estructuras.grafo.Grafo;
import estructuras.lineales.Lista;
import sistema.Equipo; // Clase que representa a un país
import sistema.Ciudad; // Clase que representa una sede/escala
import sistema.ClavePartido; // Clase que representa un partido entre dos equipos 
import sistema.DatosPartido; // representa los datos de un partido (ronda, ciudad, estadio y goles)
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

    /*
     * ========================= CIUDADES =========================
     */

    public boolean agregarCiudad(String nombre, boolean alojamiento, boolean esSede) {
        Ciudad ciudad = new Ciudad(nombre, alojamiento, esSede);
        return mapaCiudades.insertarVertice(ciudad);
    }

    public boolean eliminarCiudad(String nombre) {
        Ciudad buscada = new Ciudad(nombre, false, false);
        return mapaCiudades.eliminarVertice(buscada);
    }

    public boolean agregarRuta(String origen, String destino, int tiempo) {
        Ciudad c1 = new Ciudad(origen, false, false);
        Ciudad c2 = new Ciudad(destino, false, false);
        return mapaCiudades.insertarArco(c1, c2, tiempo);
    }

    // --- MÉTODOS DE CONSULTA (Lo que pide el enunciado) ---
    // TODO: implementar métodos mostrarInfoEquipo() y obtenerCaminoMasRapido()

    /*
     * ========================= EQUIPOS =========================
     */

    public boolean agregarEquipo(String nombre, String dt, char grupo) {
        Equipo nuevo = new Equipo(nombre, dt, grupo);
        return equipos.insertar(nuevo);
    }

    public boolean eliminarEquipo(String nombre) {
        Equipo buscador = new Equipo(nombre, "", 'X');
        return equipos.eliminar(buscador);
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



    /*
     * ========================= CONSULTAS DE VIAJES =========================
     */

    // public Lista caminoMinimoCiudades(String origen, String destino) {
    //     Ciudad c1 = new Ciudad(origen, false, false);
    //     Ciudad c2 = new Ciudad(destino, false, false);
    //     return mapaCiudades.caminoMasCorto(c1, c2);
    // }

    // public Lista caminoMenorTiempo(String origen, String destino) {
    //     Ciudad c1 = new Ciudad(origen, false, false);
    //     Ciudad c2 = new Ciudad(destino, false, false);
    //     return mapaCiudades.caminoMasRapido(c1, c2);
    // }

}
