package sistema;

import java.util.Objects;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Clase ClavePartido para el sistema Copa América 2024.
 * Representa la clave de un partido entre dos equipos, usada para almacenar los resultados en el HashMap.
 *
 * Requisitos a cumplir:
 *   - eq1 y eq2 son los dos equipos del partido, ordenados alfabéticamente por nombre (compareTo)
 *   - equals y hashCode basados en eq1 y eq2 (sin importar el orden original de los equipos al crear la clave)
 *   No tiene setters porque una vez creada la clave de un partido, los equipos no deberían cambiar.
 */
public class ClavePartido {

    private Equipo eq1;
    private Equipo eq2;

    public ClavePartido(Equipo e1, Equipo e2) {

        if (e1.compareTo(e2) < 0) {
            this.eq1 = e1;
            this.eq2 = e2;
        } else {
            this.eq1 = e2;
            this.eq2 = e1;
        }
    }

    // Getters
    public Equipo getEq1() {
        return eq1;
    }

    public Equipo getEq2() {
        return eq2;
    }

    // *No hay setters porque los equipos de un partido no deberían cambiar una vez creado el objeto

    // Métodos de comparación
    @Override
    public boolean equals(Object o) {
        boolean iguales = false;
        if (o != null) {
            // No verifico tipos porque asumo que siempre se usa ClavePartido
            ClavePartido otro = (ClavePartido) o;
            iguales = this.eq1.equals(otro.eq1) && this.eq2.equals(otro.eq2);
        }
        return iguales;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eq1, eq2);
    }
}