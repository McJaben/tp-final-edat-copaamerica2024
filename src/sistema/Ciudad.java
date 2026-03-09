package sistema;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Clase Ciudad para el sistema Copa América 2024.
 * Representa los vértices del Grafo<Ciudad, Integer>.
 *
 * Requisitos del dominio:
 *   - nombre único como identificador (equals por nombre, case-insensitive)
 *   - compareTo por nombre
 *   - tieneAlojamiento y esSede modificables (setters) -> para las ABM del menú
 *   - toString informativo para el debugging (punto 8 del TPO)
 *   ? El tiempo de vuelo entre ciudades se almacena como etiqueta en los arcos del grafo
 */
public class Ciudad implements Comparable<Ciudad> {

    private final String nombre; // es final porque es su identificador único
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
        return this.nombre;
    }

    // * No hay setter en nombre de la ciudad porque es su identificador único (y es final).

    public boolean getTieneAlojamiento() {
        return this.tieneAlojamiento;
    }

    /**
     * Permite modificar disponibilidad de alojamiento (ABM — punto 1 del TPO).
     * Importante para el filtro de caminos con alojamiento disponible (6*).
     */
    public void setTieneAlojamiento(boolean tieneAlojamiento) {
        this.tieneAlojamiento = tieneAlojamiento;
    }

    public boolean getEsSede() {
        return this.esSede;
    }

    // Permite modificar si la ciudad es sede (ABM — punto 1 del TPO).
    public void setEsSede(boolean esSede) {
        this.esSede = esSede;
    }

    // Equals, compareTo y toString

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

    /**
     * toString informativo para debugging y para mostrar información de la ciudad en las consultas
     * (punto 8 del TPO). Ejemplo de salida: "Ciudad: MIAMI | Alojamiento: true | Sede: true"
     */
    @Override
    public String toString() {
        // TODO: Verificar si se admite el uso de StringBuilder o si debo usar concatenación simple
        StringBuilder sb = new StringBuilder();

        sb.append("Ciudad: ");
        sb.append(this.nombre);
        sb.append(" | Alojamiento: ");
        sb.append(this.tieneAlojamiento); // quizás podría cambiar a "Si/No", pero dejo boolean por ahora
        sb.append(" | Sede: ");
        sb.append(this.esSede);

        return sb.toString();
    }
}
