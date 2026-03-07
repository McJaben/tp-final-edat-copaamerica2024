package sistema;

import estructuras.conjuntistas.ArbolAVL;
import estructuras.grafo.Grafo;
import estructuras.lineales.Lista;
import sistema.Equipo; // Clase que representa a un país
import sistema.Ciudad; // Clase que representa una sede/escala

public class CopaAmerica {
    // Las estructuras son privadas para que nadie las toque desde fuera
    private ArbolAVL<Equipo> equipos; 
    private Grafo ciudades;

    public CopaAmerica() {
        // Inicializamos las estructuras
        this.equipos = new ArbolAVL<>();
        this.ciudades = new Grafo();
    }

    // --- MÉTODOS DE CARGA (Basado en el archivo de texto) ---
    // TODO: implementar métodos cargarSede() y cargarEquipo()

    // --- MÉTODOS DE CONSULTA (Lo que pide el enunciado) ---
    // TODO: implementar métodos mostrarInfoEquipo() y obtenerCaminoMasRapido()

}