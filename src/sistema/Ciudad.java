package sistema;

/**
 * Clase Ciudad para el sistema Copa América 2024.
 * Representa los vértices del Grafo<Ciudad, Integer>.
 *
 * Requisitos a cumplir:
 *   - nombre único como identificador (equals y hashCode por nombre, case-insensitive)
 *   - compareTo por nombre
 *   - tieneAlojamiento y esSede modificables (setters) -> se necesitan para las ABM del menú
 *   - toString informativo para el debugging (punto 8 del TPO)
 */
public class Ciudad implements Comparable<Ciudad> {

    private String nombre;
    private boolean tieneAlojamiento;
    private boolean esSede;

    // Constructor
    public Ciudad(String nombre, boolean tieneAlojamiento, boolean esSede) {
        // Normalizar a mayúsculas para consistencia con el formato del archivo de carga
        // (ej. "C: MIAMI; TRUE; TRUE") y con equals/compareTo case-insensitive
        this.nombre = nombre.trim().toUpperCase();
        this.tieneAlojamiento = tieneAlojamiento;
        this.esSede = esSede;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    /**
     * Permite renombrar la ciudad (ABM — modificación, opción 1 del TPO).
     * Se normaliza a mayúsculas para mantener consistencia.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre.trim().toUpperCase();
    }

    public boolean tieneAlojamiento() {
        return tieneAlojamiento;
    }

    /**
     * Permite modificar disponibilidad de alojamiento (ABM — opción 1 del TPO).
     * Importante para el filtro de caminos con alojamiento disponible (6*).
     */
    public void setTieneAlojamiento(boolean tieneAlojamiento) {
        this.tieneAlojamiento = tieneAlojamiento;
    }

    public boolean esSede() {
        return esSede;
    }

    /**
     * Permite modificar si la ciudad es sede (ABM — opción 1 del TPO).
     */
    public void setEsSede(boolean esSede) {
        this.esSede = esSede;
    }

    // Equals, hashCode, compareTo y toString

    // TODO: implementar método equals() basado en el nombre para que el Grafo ubique vértices por nombre
    // TODO: implementar hashCode() y que sea consistente con equals()

    /**
     * Orden alfabético por nombre (case-insensitive). Permite ordenar ciudades si fuese necesario
     * en alguna consulta.
     */
    @Override
    public int compareTo(Ciudad otra) {
        return this.nombre.compareToIgnoreCase(otra.nombre);
    }

    // TODO: implementar método toString() y buscar cómo usar StringBuilder
}
