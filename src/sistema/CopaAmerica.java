package sistema;

import estructuras.conjuntistas.ArbolAVL;
import estructuras.grafo.Grafo;
import estructuras.lineales.Lista;
import sistema.Equipo; // Clase que representa a un país
import sistema.Ciudad; // Clase que representa una sede/escala
// import sistema.Partido; // Clase que representa un partido entre dos equipos, con rtdo y ciudad 
import java.util.HashMap; // Para almacenar partidos por clave (pais1, pais2) y lista de resultados

public class CopaAmerica {
    // Las estructuras son privadas para que nadie las toque desde fuera
    private ArbolAVL<Equipo> equipos;
    private Grafo<Ciudad, Integer> mapaCiudades;
    // private HashMap<Partido, Lista> partidos; // TODO: implementar clase Partido

    public CopaAmerica() {
        // Inicializamos las estructuras
        this.equipos = new ArbolAVL<>();
        this.mapaCiudades = new Grafo<>();
        // this.partidos = new HashMap<>();
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

    /*
     * ========================= CONSULTAS DE EQUIPOS =========================
     */

    // TODO: implementar método mostrarInfoEquipo() -> revisar si es necesario otro método en ArbolAVL
    // public Equipo obtenerEquipo(String nombre) {
    //     Equipo buscador = new Equipo(nombre, "", 'X');
    //     // return equipos.obtenerElemento(buscador); // Método que se me ocurrió para obtener el
    //     // equipo directamente desde el AVL, pero no sé si es correcto o si tengo que buscarlo
    //     // manualmente con un recorrido
    // }

    // public String mostrarInfoEquipo(String nombre) {
    //     Equipo e = obtenerEquipo(nombre);
    //     String resultado;

    //     if (e != null) {
    //         resultado = "Equipo: " + e.getNombre() + "\nPuntos: " + e.getPuntos()
    //                 + "\nGoles a favor: " + e.getGolesAFavor() + "\nGoles en contra: "
    //                 + e.getGolesEnContra() + "\nDiferencia: " + e.getDiferenciaGoles();
    //     } else {
    //         resultado = "Equipo no encontrado";
    //     }

    //     return resultado;
    // }

    // public String mostrarInfoEquipo(String nombrePais) {
    //     // Creamos un objeto "molde" solo con la clave para buscar en el AVL
    //     Equipo buscador = new Equipo(nombrePais, "DT FICTICIO", 'X'); // El DT y grupo no importan para la búsqueda
    //     Equipo encontrado = equipos.obtenerElemento(buscador); // implementar en AVL o buscar cómo solucionarlo

    //     return (encontrado != null) ? encontrado.toString() : "Equipo no encontrado";
    // }



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
