package estructuras.conjuntistas;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 */

public class NodoABB<T> {

    // Atributos
    private T elem; // para poder ordenar elementos en el árbol
    private NodoABB<T> izquierdo;
    private NodoABB<T> derecho;

    // Constructores
    public NodoABB(T nuevoElem, NodoABB<T> izq, NodoABB<T> der) {
        this.elem = nuevoElem;
        this.izquierdo = izq;
        this.derecho = der;
    }

    public NodoABB(T nuevoElem) {
        this.elem = nuevoElem;
    }

    // Observadores y modificadores

    public T getElem() {
        return elem;
    }

    public void setElem(T elem) {
        this.elem = elem;
    }

    public NodoABB<T> getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoABB<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoABB<T> getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoABB<T> derecho) {
        this.derecho = derecho;
    }

}