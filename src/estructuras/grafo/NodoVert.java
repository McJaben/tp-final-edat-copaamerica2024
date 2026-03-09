package estructuras.grafo;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Nodo Vértice para el TDA Grafo etiquetado.
 * Cada NodoVert representa una Ciudad en el mapa de Copa América.
 * 
 * Almacena:
 *   - elem: el objeto Ciudad (o elemento genérico T)
 *   - sigVertice: enlace al siguiente vértice en la lista de vértices
 *   - primerAdy: enlace al primer NodoAdy (primer ciudad adyacente)
 */
public class NodoVert<T, E> {

    private T elem;
    private NodoVert<T, E> sigVertice;
    private NodoAdy<T, E> primerAdy;

    public NodoVert(T elem, NodoVert<T, E> sigVertice) {
        this.elem = elem;
        this.sigVertice = sigVertice;
        this.primerAdy = null;
    }

    public T getElem() {
        return elem;
    }

    public void setElem(T elem) {
        this.elem = elem;
    }

    public NodoVert<T, E> getSigVertice() {
        return sigVertice;
    }

    public void setSigVertice(NodoVert<T, E> sigVertice) {
        this.sigVertice = sigVertice;
    }

    public NodoAdy<T, E> getPrimerAdy() {
        return primerAdy;
    }

    public void setPrimerAdy(NodoAdy<T, E> primerAdy) {
        this.primerAdy = primerAdy;
    }
}
