package sistema;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
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

    // método toString() para debugging y visualización de datos
    @Override
    public String toString() {
        return "Ronda: " + ronda + " | Ciudad: " + ciudad.getNombre() + " | Estadio: " + estadio
                + " | Resultado: " + goles1 + "-" + goles2;
    }

    /**
     * Método toString() específico para mostrar el resultado entre dos equipos, usando también sus
     * nombres
     * 
     * @param eq1 primer equipo del partido
     * @param eq2 segundo equipo del partido
     * @return String con la información completa del partido, con nombre de equipos y resultado
     */
    public String toString(Equipo eq1, Equipo eq2) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ronda: ");
        sb.append(ronda);
        sb.append(" | Ciudad: ");
        sb.append(ciudad.getNombre());
        sb.append(" | Estadio: ");
        sb.append(estadio);
        sb.append(" | Resultado: ");
        sb.append(eq1.getNombre());
        sb.append(" ");
        sb.append(goles1);
        sb.append(" - ");
        sb.append(goles2);
        sb.append(" ");
        sb.append(eq2.getNombre());
        return sb.toString();
    }
}
