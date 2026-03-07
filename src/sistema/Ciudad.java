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

    private final String nombre;
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

    // * No hay setter en nombre de la ciudad porque es su identificador único.

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

    /**
     * Orden alfabético por nombre (case-insensitive). Permite ordenar ciudades si fuese necesario
     * en alguna consulta.
     */
    @Override
    public int compareTo(Ciudad otra) {
        return this.nombre.compareToIgnoreCase(otra.nombre);
    }

    /**
     * Dos ciudades son iguales si tienen el mismo nombre (case-insensitive). Es importante para que
     * el Grafo pueda ubicar los vértices por nombre y evitar duplicados.
     */
    @Override
    public boolean equals(Object obj) {
        boolean iguales = false;
        // Podría agregar un try/catch, pero asumo que siempre se usa Ciudad por el dominio del TPO
        if (obj != null) {
            Ciudad otra = (Ciudad) obj;
            iguales = this.nombre.equalsIgnoreCase(otra.getNombre());
        }
        return iguales;
    }

    @Override
    public int hashCode() {
        return this.nombre.toUpperCase().hashCode();
    }

    /**
     * toString informativo para debugging y para mostrar información de la ciudad en las consultas
     * (punto 8 del TPO). Ejemplo de salida: "Ciudad: MIAMI | Alojamiento: true | Sede: true"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Ciudad: ");
        sb.append(nombre);
        sb.append(" | Alojamiento: ");
        sb.append(tieneAlojamiento); // quizás podría cambiar a "Si/No", pero dejo boolean por ahora
        sb.append(" | Sede: ");
        sb.append(esSede);

        return sb.toString();
    }
}
