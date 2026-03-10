package sistema;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Clase Equipo para el sistema Copa América 2024.
 * Representa los equipos de fútbol participantes del torneo.
 *
 * Estructura de almacenamiento:
 *   - Los equipos se guardan en un Árbol AVL ordenados alfabéticamente por nombre
 *   - El AVL permite búsquedas eficientes O(log n) y listar equipos en rango [min, max] alfabético
 * 
 * Requisitos del dominio:
 *   - nombre del país único como identificador (equals por nombre, case-insensitive)
 *   - nombre/apellido del director técnico (DT) modificable
 *   - grupo (A, B, C o D) modificable
 *   - puntos ganados, goles a favor y goles en contra acumulativos (se actualizan al cargar resultados de partidos)
 * 
 * Nota: restringí los grupos a sólo 4 para representar cómo fue realmente la Copa América 2024
 */
public class Equipo implements Comparable<Equipo> {
    private final String nombre; // identificador único del equipo (nombre del país)
    private String directorTecnico;
    private char grupo; // 'A', 'B', 'C' o 'D'
    private int golesAFavor, golesEnContra, puntos;

    /**
     * Constructor completo para crear un equipo con todos sus datos. Usado durante la carga inicial
     * desde archivo y en las altas del menú.
     *
     * Nota: Los puntos inician en 0 y se actualizan automáticamente al cargar los resultados de los
     * partidos (no se cargan manualmente).
     */
    public Equipo(String nombre, String directorTecnico, char grupo) {
        this.nombre = nombre.trim().toUpperCase(); // Normalizamos a mayúsculas para consistencia;
        this.directorTecnico = directorTecnico;
        this.grupo = grupo;
        this.golesAFavor = 0;
        this.golesEnContra = 0;
        this.puntos = 0;
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    // * No hay setter de nombre porque es el identificador único del equipo.

    public String getDirectorTecnico() {
        return directorTecnico;
    }

    /**
     * Permite modificar el DT del país (ABM — modificación, consigna 2 del TPO).
     * @param directorTecnico nombre del nuevo DT
     */
    public void setDirectorTecnico(String directorTecnico) {
        this.directorTecnico = directorTecnico;
    }

    public char getGrupo() {
        return grupo;
    }

    /**
     * Permite cambiar el grupo inicial (ABM — consigna 2 del TPO).
     * Útil si hay reestructuración del torneo o error en la carga.
     * Valores válidos: 'A', 'B', 'C', 'D'.
     * @param grupo el nuevo grupo del equipo (A, B, C o D)
     * @throws IllegalArgumentException si el grupo recibido no es válido
     */
    public void setGrupo(char grupo) {
        char g = Character.toUpperCase(grupo);
        if (g == 'A' || g == 'B' || g == 'C' || g == 'D') {
            this.grupo = Character.toUpperCase(grupo);
        } else {
            throw new IllegalArgumentException("Grupo inválido: " + grupo + ". Debe ser A, B, C o D.");
        }
    }

    public int getGolesAFavor() {
        return golesAFavor;
    }

    public int getGolesEnContra() {
        return golesEnContra;
    }

    public int getPuntos() {
        return puntos;
    }

    /*
     * Métodos para actualizar goles y puntos al procesar resultados de partidos. No se usan setters
     * porque la idea es que se acumulen, no que se reemplacen manualmente.
     */

    /**
     * Agrega goles a favor (usado al procesar resultados de partidos).
     *
     * @param goles goleas a favor a sumar (debe ser >= 0)
     * @throws IllegalArgumentException si goles es negativo
     */
    public void agregarGolesAFavor(int goles) {
        if (goles >= 0) {
            this.golesAFavor += goles;
        } else {
            throw new IllegalArgumentException("No se pueden agregar goles negativos: " + goles);
        }
    }

    /**
     * Agrega goles en contra (usado al procesar resultados de partidos).
     *
     * @param goles goles en contra a sumar (debe ser >= 0)
     * @throws IllegalArgumentException si goles es negativo
     */
    public void agregarGolesEnContra(int goles) {
        if (goles >= 0) {
            this.golesEnContra += goles;
        } else {
            throw new IllegalArgumentException("No se pueden agregar goles negativos: " + goles);
        }
    }

    /**
     * Agrega puntos según resultado del partido (3 victoria, 1 empate, 0 derrota). Usado
     * automáticamente al cargar partidos desde archivo.
     *
     * @param puntos puntos a sumar (debe ser >= 0)
     * @throws IllegalArgumentException si puntos es negativo
     */
    public void agregarPuntos(int puntos) {
        if (puntos >= 0) {
            this.puntos += puntos;
        } else {
            throw new IllegalArgumentException("No se pueden agregar puntos negativos: " + puntos);
        }
    }

    /**
     * Calcula la diferencia de goles (goles a favor - goles en contra).
     * Consigna 4 del TPO: "mostrar diferencia de goles".
     */
    public int getDiferenciaGoles() {
        return golesAFavor - golesEnContra;
    }

    // * Métodos de comparación

    /**
     * Orden alfabético por nombre del país (case-insensitive). Es crítico para el funcionamiento
     * del Árbol AVL, dado que permite que las búsquedas sean binarias O(log n) y que los listados
     * estén ordenados alfabéticamente. Ejemplo: "ARGENTINA".compareTo("BRASIL") < 0
     */
    @Override
    public int compareTo(Equipo otro) {
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    /**
     * Dos equipos son iguales si tienen el mismo nombre de país (case-insensitive).
     * El AVL usa este método para verificar duplicados al insertar.
     */
    @Override
    public boolean equals(Object o) {
        boolean iguales = false;
        // Podría agregar un try/catch, pero asumo que siempre se usa Equipo por el dominio del TPO
        // Tmb podría verificar con instanceOf o getClass, pero asumo que no se mezclan tipos en AVL
        if (o != null) {
            Equipo otro = (Equipo) o;
            iguales = this.nombre.equalsIgnoreCase(otro.getNombre());
        }
        return iguales;
    }

    /**
     * El hasCode es necesario para el correcto funcionamiento del HashMap con ClavePartido.
     * @return int hash code basado únicament en el nombre del equipo (case-insensitive)
     */
    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    /**
     * toString informativo para debugging y para mostrar información del equipo en las consultas
     * (punto 8 del TPO).
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Equipo: ");
        sb.append(nombre);
        sb.append(" | DT: ");
        sb.append(directorTecnico);
        sb.append(" | Grupo: ");
        sb.append(grupo);
        sb.append(" | Goles a favor: ");
        sb.append(golesAFavor);
        sb.append(" | Goles en contra: ");
        sb.append(golesEnContra);
        sb.append(" | Puntos: ");
        sb.append(puntos);

        return sb.toString();
    }
}