package sistema;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 *
 * Clase utilitaria para la carga inicial del sistema Copa América 2024
 * desde un archivo de texto con el formato definido en el enunciado del TPO.
 *
 * FORMATO SOPORTADO (una línea por elemento, separador ';'):
 *   E: NOMBRE_PAIS; APELLIDO_TECNICO; GRUPO
 *   C: NOMBRE_CIUDAD; TIENE_ALOJAMIENTO; ES_SEDE
 *   R: CIUDAD_ORIGEN; CIUDAD_DESTINO; MINUTOS_VUELO
 *   P: EQUIPO1; EQUIPO2; RONDA; CIUDAD; ESTADIO; GOLES_EQ1; GOLES_EQ2
 *
 * Reglas de parseo:
 *   - Las líneas vacías y las que comienzan con '#' se ignoran (comentarios).
 *   - Los campos se recortan con trim() y se normalizan a mayúsculas donde corresponde.
 *   - Si una línea tiene formato inválido (campos faltantes, número no parseable, etc.)
 *     se registra el error en el Logger y se continúa con la siguiente línea.
 *   - Los partidos se cargan DESPUÉS de equipos y ciudades; el orden dentro del archivo
 *     es responsabilidad del usuario (se recomienda E → C → R → P).
 *
 * Uso:
 *   CargadorArchivo cargador = new CargadorArchivo(copa, logger);
 *   boolean exito = cargador.cargar("cargaInicial.txt");
 */
public class CargadorArchivo {

    private final CopaAmerica copa;
    private final Logger logger;

    // Contadores para el resumen final de carga
    private int equiposCargados;
    private int ciudadesCargadas;
    private int rutasCargadas;
    private int partidosCargados;
    private int errores;

    public CargadorArchivo(CopaAmerica copa, Logger logger) {
        this.copa = copa;
        this.logger = logger;
        this.equiposCargados = 0;
        this.ciudadesCargadas = 0;
        this.rutasCargadas = 0;
        this.partidosCargados = 0;
        this.errores = 0;
    }

    /**
     * Lee el archivo línea por línea y delega el parseo según el prefijo de cada línea.
     *
     * @param rutaArchivo ruta relativa o absoluta al archivo de texto
     * @return true si el archivo se abrió y procesó sin errores fatales,
     *         false si el archivo no pudo abrirse
     */
    public boolean cargar(String rutaArchivo) {
        boolean exito = false;
        logger.registrar("=== INICIO DE CARGA DESDE ARCHIVO: " + rutaArchivo + " ===");

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int nroLinea = 0;

            while ((linea = br.readLine()) != null) {
                nroLinea++;
                linea = linea.trim();

                // Ignorar líneas vacías y comentarios
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                // Separar el prefijo del resto del contenido
                // Formato esperado: "X: campo1; campo2; ..."
                int dosPuntos = linea.indexOf(':');
                if (dosPuntos < 0) {
                    registrarError(nroLinea, linea, "No se encontró el separador ':'");
                    continue;
                }

                String prefijo = linea.substring(0, dosPuntos).trim().toUpperCase();
                String contenido = linea.substring(dosPuntos + 1).trim();

                switch (prefijo) {
                    case "E" -> procesarEquipo(contenido, nroLinea);
                    case "C" -> procesarCiudad(contenido, nroLinea);
                    case "R" -> procesarRuta(contenido, nroLinea);
                    case "P" -> procesarPartido(contenido, nroLinea);
                    default  -> registrarError(nroLinea, linea, "Prefijo desconocido: '" + prefijo + "'");
                }
            }

            exito = true;

        } catch (IOException e) {
            logger.registrar("ERROR FATAL: No se pudo abrir el archivo '" + rutaArchivo + "'. " + e.getMessage());
        }

        // Resumen de carga
        logger.registrar("--- Resumen de carga ---");
        logger.registrar("  Equipos cargados  : " + equiposCargados);
        logger.registrar("  Ciudades cargadas : " + ciudadesCargadas);
        logger.registrar("  Rutas cargadas    : " + rutasCargadas);
        logger.registrar("  Partidos cargados : " + partidosCargados);
        logger.registrar("  Errores de parseo : " + errores);
        logger.registrar("=== FIN DE CARGA ===");

        return exito;
    }

    // =========================================================
    // Métodos privados de parseo por tipo de elemento
    // =========================================================

    /**
     * Parsea y carga un equipo.
     * Formato esperado: NOMBRE_PAIS; APELLIDO_TECNICO; GRUPO
     * Ejemplo: "ARGENTINA; SCALONI; A"
     */
    private void procesarEquipo(String contenido, int nroLinea) {
        // Separar por ';' y verificar que haya exactamente 3 campos
        String[] partes = contenido.split(";");

        boolean valido = true;
        String nombre = null;
        String dt = null;
        char grupo = 0;

        if (partes.length < 3) {
            registrarError(nroLinea, contenido, "Se esperaban 3 campos (nombre; dt; grupo), se encontraron " + partes.length);
            valido = false;
        } else {
            nombre = partes[0].trim();
            dt = partes[1].trim();
            String grupoStr = partes[2].trim().toUpperCase();

            // Validar grupo
            if (grupoStr.isEmpty()) {
                registrarError(nroLinea, contenido, "El grupo no puede estar vacío");
                valido = false;
            } else {
                grupo = grupoStr.charAt(0);
                if (grupo != 'A' && grupo != 'B' && grupo != 'C' && grupo != 'D') {
                    registrarError(nroLinea, contenido, "Grupo inválido: '" + grupo + "'. Debe ser A, B, C o D");
                    valido = false;
                }
            }
        }

        // Intentar insertar en el sistema si todo fue válido
        if (valido) {
            if (copa.agregarEquipo(nombre, dt, grupo)) {
                equiposCargados++;
                logger.registrar("Se cargó el equipo: " + nombre.toUpperCase() + " (DT: " + dt + ", Grupo: " + grupo + ")");
            } else {
                registrarError(nroLinea, contenido, "El equipo '" + nombre + "' ya existe en el sistema");
            }
        }
    }

    /**
     * Parsea y carga una ciudad.
     * Formato esperado: NOMBRE; TIENE_ALOJAMIENTO; ES_SEDE
     * Ejemplo: "MIAMI; TRUE; TRUE"
     */
    private void procesarCiudad(String contenido, int nroLinea) {
        String[] partes = contenido.split(";");

        boolean valido = true;
        String nombre = null;
        String alojStr = null;
        String sedeStr = null;

        if (partes.length < 3) {
            registrarError(nroLinea, contenido, "Se esperaban 3 campos (nombre; alojamiento; sede), se encontraron " + partes.length);
            valido = false;
        } else {
            nombre = partes[0].trim();
            alojStr = partes[1].trim().toLowerCase();
            sedeStr = partes[2].trim().toLowerCase();

            // Validar booleanos
            if (!alojStr.equals("true") && !alojStr.equals("false")) {
                registrarError(nroLinea, contenido, "El campo 'alojamiento' debe ser TRUE o FALSE, se recibió: '" + alojStr + "'");
                valido = false;
            }
            if (!sedeStr.equals("true") && !sedeStr.equals("false")) {
                registrarError(nroLinea, contenido, "El campo 'esSede' debe ser TRUE o FALSE, se recibió: '" + sedeStr + "'");
                valido = false;
            }
        }

        if (valido) {
            boolean alojamiento = Boolean.parseBoolean(alojStr);
            boolean esSede      = Boolean.parseBoolean(sedeStr);

            if (copa.agregarCiudad(nombre, alojamiento, esSede)) {
                ciudadesCargadas++;
                logger.registrar("Se cargó la ciudad: " + nombre.toUpperCase()
                        + " (Alojamiento: " + alojamiento + ", Sede: " + esSede + ")");
            } else {
                registrarError(nroLinea, contenido, "La ciudad '" + nombre + "' ya existe en el sistema");
            }
        }
    }

    /**
     * Parsea y carga una ruta aérea.
     * Formato esperado: CIUDAD_ORIGEN; CIUDAD_DESTINO; MINUTOS
     * Ejemplo: "MIAMI; ATLANTA; 110"
     */
    private void procesarRuta(String contenido, int nroLinea) {
        String[] partes = contenido.split(";");

        boolean valido = true;
        String origen = null;
        String destino = null;
        String minutosStr = null;
        int minutos = 0;

        if (partes.length < 3) {
            registrarError(nroLinea, contenido, "Se esperaban 3 campos (origen; destino; minutos), se encontraron " + partes.length);
            valido = false;
        } else {
            origen = partes[0].trim();
            destino = partes[1].trim();
            minutosStr = partes[2].trim();

            // Parsear tiempo de vuelo
            try {
                minutos = Integer.parseInt(minutosStr);
            } catch (NumberFormatException e) {
                registrarError(nroLinea, contenido, "El tiempo de vuelo no es un número válido: '" + minutosStr + "'");
                valido = false;
            }

            if (valido && minutos < 0) {
                registrarError(nroLinea, contenido, "El tiempo de vuelo no puede ser negativo: " + minutos);
                valido = false;
            }
        }

        if (valido) {
            if (copa.agregarRuta(origen, destino, minutos)) {
                rutasCargadas++;
                logger.registrar("Se cargó la ruta: " + origen.toUpperCase() + " <-> " + destino.toUpperCase() + " (" + minutos + " min)");
            } else {
                registrarError(nroLinea, contenido, "No se pudo cargar la ruta '" + origen + " <-> " + destino
                        + "'. Verifique que ambas ciudades existan y que la ruta no sea duplicada");
            }
        }
    }

    /**
     * Parsea y carga un partido.
     * Formato esperado: EQUIPO1; EQUIPO2; RONDA; CIUDAD; ESTADIO; GOLES_EQ1; GOLES_EQ2
     * Ejemplo: "ARGENTINA; CANADA; GRUPO; ATLANTA; MERCEDES_BENZ; 2; 0"
     *
     * NOTA: El orden eq1/eq2 en el archivo no necesita estar ya ordenado alfabéticamente;
     * CopaAmerica.agregarPartido() delega esa responsabilidad a ClavePartido.
     */
    private void procesarPartido(String contenido, int nroLinea) {
        String[] partes = contenido.split(";");

        boolean valido = true;
        String eq1 = null;
        String eq2 = null;
        String ronda = null;
        String ciudad = null;
        String estadio = null;
        String g1Str = null;
        String g2Str = null;
        int goles1 = 0;
        int goles2 = 0;

        if (partes.length < 7) {
            registrarError(nroLinea, contenido, "Se esperaban 7 campos (eq1; eq2; ronda; ciudad; estadio; g1; g2), se encontraron " + partes.length);
            valido = false;
        } else {
            eq1 = partes[0].trim();
            eq2 = partes[1].trim();
            ronda = partes[2].trim();
            ciudad = partes[3].trim();
            estadio = partes[4].trim();
            g1Str = partes[5].trim();
            g2Str = partes[6].trim();

            // Parsear goles
            try {
                goles1 = Integer.parseInt(g1Str);
                goles2 = Integer.parseInt(g2Str);
            } catch (NumberFormatException e) {
                registrarError(nroLinea, contenido, "Los goles deben ser números enteros. Se recibió: '" + g1Str + "' y '" + g2Str + "'");
                valido = false;
            }

            if (valido && (goles1 < 0 || goles2 < 0)) {
                registrarError(nroLinea, contenido, "Los goles no pueden ser negativos: " + goles1 + " - " + goles2);
                valido = false;
            }
        }

        if (valido) {
            if (copa.agregarPartido(eq1, eq2, ronda, ciudad, estadio, goles1, goles2)) {
                partidosCargados++;
                logger.registrar("Se cargó el partido: " + eq1.toUpperCase() + " vs " + eq2.toUpperCase()
                        + " | Ronda: " + ronda + " | Resultado: " + goles1 + "-" + goles2);
            } else {
                registrarError(nroLinea, contenido, "No se pudo cargar el partido '" + eq1 + " vs " + eq2
                        + "'. Verifique que los equipos y la ciudad existan en el sistema");
            }
        }
    }

    // =========================================================
    // Métodos auxiliares
    // =========================================================

    /**
     * Registra un error de parseo en el logger e incrementa el contador de errores.
     */
    private void registrarError(int nroLinea, String contenido, String motivo) {
        errores++;
        logger.registrar("ERROR en línea " + nroLinea + " [" + contenido + "]: " + motivo);
    }

    // Getters para que CopaAmerica o el Menú puedan consultar el resultado
    public int getEquiposCargados()  { return equiposCargados; }
    public int getCiudadesCargadas() { return ciudadesCargadas; }
    public int getRutasCargadas()    { return rutasCargadas;    }
    public int getPartidosCargados() { return partidosCargados; }
    public int getErrores()          { return errores;          }
}