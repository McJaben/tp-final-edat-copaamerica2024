package sistema;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime; // para timestamp de las entradas del log
import java.time.format.DateTimeFormatter;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 *
 * Logger del sistema Copa América 2024.
 * Cumple el requisito del enunciado de mantener un archivo .LOG con:
 *   - Estado del sistema al momento de terminar la carga inicial
 *   - Listado de operaciones realizadas durante la ejecución
 *   - Estado del sistema al momento de terminar de ejecutarse
 *
 * Cada entrada en el log tiene la forma:
 *   [YYYY-MM-DD HH:MM:SS] mensaje
 *
 * El archivo se abre en modo "append" (true) para no sobreescribir sesiones
 * anteriores. Cada sesión comienza con un separador visual.
 *
 * Uso típico:
 *   Logger logger = new Logger("sistema.log");
 *   logger.iniciarSesion();
 *   logger.registrar("Se insertó la ciudad MIAMI");
 *   logger.registrarEstado(copa.mostrarEstructuras());
 *   logger.cerrarSesion();
 *   logger.cerrar();
 */
public class Logger {

    private final String rutaArchivo;
    private BufferedWriter writer;
    private boolean abierto;

    // Formato de fecha y hora para cada entrada del log
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Crea el logger apuntando al archivo indicado. El archivo se crea si no existe, o se continúa
     * al final si ya existe.
     *
     * @param rutaArchivo nombre o ruta del archivo .log (ej. "sistema.log")
     */
    public Logger(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.abierto = false;
        abrirArchivo();
    }

    /**
     * Abre el archivo en modo append. Si falla, el logger queda en modo "silencioso" (no lanza
     * excepciones al resto del sistema, solo imprime en consola).
     */
    private void abrirArchivo() {
        try {
            // true = modo append: no sobreescribe sesiones anteriores
            this.writer = new BufferedWriter(new FileWriter(rutaArchivo, true));
            this.abierto = true;
        } catch (IOException e) {
            System.err.println("[Logger] ADVERTENCIA: No se pudo abrir el archivo de log '"
                    + rutaArchivo + "'. " + e.getMessage());
            this.abierto = false;
        }
    }

    /**
     * Escribe el encabezado de la sesión actual con fecha y hora de inicio. Llamar una vez al
     * inicio del programa.
     */
    public void iniciarSesion() {
        String separador = "=".repeat(60);
        escribir(separador);
        escribir("SESIÓN INICIADA: " + ahora());
        escribir(separador);
    }

    /**
     * Escribe el pie de la sesión con fecha y hora de finalización. Llamar justo antes de cerrar el
     * programa.
     */
    public void cerrarSesion() {
        String separador = "=".repeat(60);
        escribir(separador);
        escribir("SESIÓN FINALIZADA: " + ahora());
        escribir(separador);
        escribir(""); // línea en blanco entre sesiones
    }

    /**
     * Registra una operación o evento con timestamp. Es el método principal que se llama desde
     * CopaAmerica y Menu.
     *
     * @param mensaje descripción de la operación (ej. "Se insertó la ciudad X")
     */
    public void registrar(String mensaje) {
        escribir("[" + ahora() + "] " + mensaje);
    }

    /**
     * Registra el estado completo del sistema (volcado de las tres estructuras). Se usa al terminar
     * la carga inicial y al finalizar la ejecución.
     *
     * @param titulo encabezado descriptivo (ej. "ESTADO TRAS CARGA INICIAL")
     * @param estado resultado de copa.mostrarEstructuras()
     */
    public void registrarEstado(String titulo, String estado) {
        String separador = "-".repeat(40);
        escribir(separador);
        escribir("[" + ahora() + "] " + titulo);
        escribir(separador);
        // Escribir el estado línea por línea para que el timestamp no se repita
        String[] lineas = estado.split("\n");
        for (String linea : lineas) {
            escribir("  " + linea);
        }
        escribir(separador);
    }

    /**
     * Cierra el archivo de log y libera el recurso. Llamar una vez al finalizar el programa
     * (después de cerrarSesion()).
     */
    public void cerrar() {
        if (abierto && writer != null) {
            try {
                writer.close();
                abierto = false;
            } catch (IOException e) {
                System.err.println("[Logger] Error al cerrar el archivo de log: " + e.getMessage());
            }
        }
    }

    // =========================================================
    // Métodos privados auxiliares
    // =========================================================

    /**
     * Escribe una línea en el archivo y hace flush inmediato para que el log no se pierda si el
     * programa termina abruptamente.
     */
    private void escribir(String linea) {
        if (abierto && writer != null) {
            try {
                writer.write(linea);
                writer.newLine();
                writer.flush(); // garantiza escritura inmediata
            } catch (IOException e) {
                System.err.println("[Logger] Error al escribir en el log: " + e.getMessage());
            }
        }
        // Si el logger no pudo abrir el archivo, imprime en consola como fallback
        else {
            System.out.println("[LOG] " + linea);
        }
    }

    /**
     * Devuelve la fecha y hora actual formateada.
     */
    private String ahora() {
        return LocalDateTime.now().format(FORMATO_FECHA);
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }
}
