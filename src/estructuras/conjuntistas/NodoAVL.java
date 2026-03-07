package estructuras.conjuntistas;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar> NodoAVL es genérica de la
 *         misma manera en que NodoABB lo es
 */

public class NodoAVL<T> extends NodoABB<T> {
    private int altura; // altura del subárbol en este nodo

    public NodoAVL(T nuevoElem, NodoABB<T> izq, NodoABB<T> der) {
        super(nuevoElem, izq, der);
        // inicializamos altura teniendo en cuenta hijos si ya vienen dados
        this.recalcularAltura();
    }

    public NodoAVL(T nuevoElem) {
        super(nuevoElem);
        this.altura = 0; // Si no tiene hijos, es una hoja y las hojas tienen altura 0.
    }

    /**
     * Devuelve la altura almacenada en el nodo. En nuestro esquema un nodo vacío se considera -1,
     * una hoja 0, etc.
     */
    public int getAltura() {
        return altura;
    }

    /**
     * Getters específicos para evitar casteos externos (evita tener que hacer casting en ArbolAVL).
     */
    @Override
    public NodoAVL<T> getIzquierdo() {
        return (NodoAVL<T>) super.getIzquierdo();
    }

    @Override
    public NodoAVL<T> getDerecho() {
        return (NodoAVL<T>) super.getDerecho();
    }

    @Override
    public void setIzquierdo(NodoABB<T> izquierdo) {
        super.setIzquierdo(izquierdo);
    }

    @Override
    public void setDerecho(NodoABB<T> derecho) {
        super.setDerecho(derecho);
    }

    /**
     * Recalcula y almacena la altura basándose en las alturas de los hijos. Convención: altura de
     * nodo nulo = -1; hoja = 0. La fórmula unificada es 1 + max(altura(izq), altura(der)), donde
     * los hijos nulos se tratan como -1.
     */
    public void recalcularAltura() {
        int altIzq = -1; // altura de subárbol nulo
        int altDer = -1;

        if (getIzquierdo() != null) {
            altIzq = getIzquierdo().getAltura();
        }
        if (getDerecho() != null) {
            altDer = getDerecho().getAltura();
        }

        altura = 1 + Math.max(altIzq, altDer); // si ambos hijos son nulos, esto da 0
    }

}
