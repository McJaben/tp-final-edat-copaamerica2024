package estructuras.lineales;

/**
 *
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 */
public class Cola {

    private Nodo frente;
    private Nodo fin;

    // Crea y devuelve una cola vacía.
    public Cola() {
        frente = null;
        fin = null;
    }

    /*
     * Pone el elemento al final de la cola. Devuelve verdadero si el elemento
     * se pudo agregar en la estructura y falso en caso contrario
     */
    public boolean poner(Object nuevoElem) {
        Nodo nuevoNodo = new Nodo(nuevoElem, null);

        if (this.frente == null) {
            this.frente = nuevoNodo;
            this.fin = nuevoNodo;
        } else {
            this.fin.setEnlace(nuevoNodo);
            this.fin = nuevoNodo;
        }
        // nunca hay error de cola llena
        return true;
    }

    /*
     * Saca el elemento que está en el frente de la cola. Devuelve verdadero
     * si el elemento se pudo sacar y falso en caso contrario.
     */
    public boolean sacar() {
        boolean exito = false;

        if (this.frente != null) {
            // Si la cola no está vacía, hay al menos un elemento
            // Quita el elemento del frente y actualiza frente
            this.frente = this.frente.getEnlace();
            if (this.frente == null) {
                // Si la cola quedó vacía, actualiza fin
                this.fin = null;
            }
            exito = true;
        }
        return exito;
    }

    // Retorna el elemento que está en el frente de la cola.
    public Object obtenerFrente() {
        Object elementoFrente = null;
        if (this.frente != null) {
            elementoFrente = this.frente.getElem();
        }
        return elementoFrente;
    }

    public boolean esVacia() {
        // Sólo puedo acceder al primer elemento (frente), si éste es nulo significa
        // que la cola está vacía.
        return this.frente == null;
    }

    public void vaciar() {
        // Actualiza al frente y fin para que apunten a null. De esta manera, el
        // garbage collector se lleva los nodos que no están apuntados (inaccesibles)
        this.frente = null;
        this.fin = null;
    }

    /*
     * Devuelve una copia exacta de los datos en la estructura original, y
     * respetando el orden de los mismos, en otras estructuras del mismo tipo.
     */
    @Override
    public Cola clone() {
        Cola clon = new Cola();
        Nodo aux = this.frente;
        Nodo nuevo;
        Nodo ultimo = null;

        // Manipulo la estructura de manera directa y eficiente.
        while (aux != null) {
            nuevo = new Nodo(aux.getElem(), null);
            if (ultimo == null) {
                clon.frente = nuevo;
            } else {
                ultimo.setEnlace(nuevo);
            }
            aux = aux.getEnlace();
            ultimo = nuevo;
        }
        clon.fin = ultimo;

        return clon;
    }

    /*
     * Crea y devuelve una cadena de caracteres formada por todos los elementos
     * de la cola para poder mostrarla por pantalla.
     */
    @Override
    public String toString() {
        String s = "";
        Nodo aux = this.frente;
        if (aux == null) { // Verifica si la cola está vacía.
            s = "[]";
        } else {
            s = "[";
            while (aux != null) { // bucle que recorre toda la estructura
                s += aux.getElem();
                if (aux.getEnlace() != null) {
                    s += ",";
                }
                aux = aux.getEnlace();
            }
            s += "]";
        }

        return s;
    }

}
