package sistema;

/**
 * Clase DatosPartido para el sistema Copa América 2024.
 * Representa los datos de un partido entre dos equipos, incluyendo:
 *   - Ronda del torneo (ej. "Grupo A", "Cuartos de Final", etc.)
 *   - Ciudad donde se jugó el partido
 *   Estadio específico dentro de la ciudad
 *   Goles anotados por cada equipo
 *
 * Esta clase se usará para almacenar los resultados de los partidos en la estructura HashMap<ClavePartido, Lista<DatosPartido>>
 * dentro de la clase CopaAmerica, permitiendo registrar múltiples partidos entre los mismos equipos (ej. fase de grupos y cuartos de final).
 * Nota: No tiene setters porque una vez creado el objeto con los datos de un partido, esos datos no deberían cambiar.
 */
public class DatosPartido {
    private String ronda;
    private Ciudad ciudad;
    private String estadio;
    private int goles1;
    private int goles2;

    // Constructor completo
    public DatosPartido(String ronda, Ciudad ciudad, String estadio, int goles1, int goles2) {
        
        if (goles1 < 0 || goles2 < 0) {
            throw new IllegalArgumentException("Los goles no pueden ser negativos");
        }

        this.ronda = ronda.trim().toUpperCase();
        this.ciudad = ciudad;
        this.estadio = estadio.trim().toUpperCase();
        this.goles1 = goles1;
        this.goles2 = goles2;
    }

    public String getRonda() {
        return ronda;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public String getEstadio() {
        return estadio;
    }

    public int getGoles1() {
        return goles1;
    }

    public int getGoles2() {
        return goles2;
    }
}
