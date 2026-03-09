package estructuras.grafo;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Nodo Adyacente para el TDA Grafo etiquetado.
 * Cada NodoAdy representa una ruta aérea entre dos ciudades.
 *
 * Almacena:
 *   - vertice: referencia directa al NodoVert destino (no solo el nombre, sino el nodo)
 *   - etiqueta: tiempo de vuelo en minutos (int)
 *   - sigAdyacente: enlace al siguiente NodoAdy en la lista de adyacentes
 */
public class NodoAdy<T, E> {

    private NodoVert<T, E> vertice;    // Ciudad destino del arco
    private E etiqueta;        // Tiempo de vuelo en minutos
    private NodoAdy<T, E> sigAdyacente;

    public NodoAdy(NodoVert<T, E> vertice, E etiqueta) {
        this.vertice = vertice;
        this.etiqueta = etiqueta;
        this.sigAdyacente = null;
    }

    public NodoVert<T, E> getVertice() {
        return vertice;
    }

    public void setVertice(NodoVert<T, E> vertice) {
        this.vertice = vertice;
    }

    public E getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(E etiqueta) {
        this.etiqueta = etiqueta;
    }

    public NodoAdy<T, E> getSigAdyacente() {
        return sigAdyacente;
    }

    public void setSigAdyacente(NodoAdy<T, E> sigAdyacente) {
        this.sigAdyacente = sigAdyacente;
    }
}
