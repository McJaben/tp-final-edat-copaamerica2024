/*
 * Clase Pila de la materia EDAT. Apunte 2.
 */
package estructuras.lineales;

/**
 *
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 */
public class Pila {

    private Nodo tope;

    public Pila() {
        // crea y devuelve la pila vacía
        this.tope = null;
    }

    // de aplicacion
    public boolean apilar(Object nuevoElem) {
        // Pone el elemento nuevoElem en el tope de la pila.
        // Crea un nuevo nodo delante de la antigua cabecera o frente
        Nodo nuevo = new Nodo(nuevoElem, this.tope);

        // actualiza el tope para que apunte al nodo nuevo
        this.tope = nuevo;

        // Como no hay error de pila llena, al ser ésta dinámica, devuelve true.
        return true;
    }

    public boolean desapilar() {
        /*
         * Saca el elemento del tope de la pila. Devuelve true si la pila tenía
         * elementos al momento de desapilar y falso en caso de que esté vacía.
         */
        boolean exito = false;
        if (this.tope != null) {
            /*
             * Enlazo el tope al siguiente nodo, como si fuera un puente. De esta
             * manera, el garbage collector se lleva al antiguo frente al no estar apuntado.
             */
            this.tope = this.tope.getEnlace();
            exito = true;
        }
        return exito;
    }

    public Object obtenerTope() {
        Object elemento = null;
        if (this.tope != null) {
            elemento = this.tope.getElem();
        }
        return elemento;
    }

    public boolean esVacia() {
        // Devuelve verdadero si la pila está vacía. Falso en caso contrario.
        // El valor null de 'tope' representa una pila vacía (sin nodos).
        return (this.tope == null);
    }

    public void vaciar() {
        // Saca todos los elementos de la pila hasta que queda vacía.
        this.tope = null;
    }

    @Override
    public Pila clone() {
        /*
         * Devuelve una copia exacta de los datos en la estructura original, y
         * respetando el orden de los mismos en otra estructura del mismo tipo.
         * En la pila clon siempre voy recorriendo desde un elemento atrás que 
         * en la pila original, copiando desde el tope y respetando el orden.
         */

        Pila clon = new Pila();
        Nodo aux = this.tope;
        Nodo nuevo;
        Nodo ultimo = null;

        // Manipulo la estructura de manera directa y eficiente.
        if (this.tope != null) { // Se ejecuta si la pila no esté vacía
            nuevo = new Nodo(aux.getElem(), null);
            clon.tope = nuevo;

            while (aux.getEnlace() != null) {
                ultimo = nuevo; // recorro la pila clon
                aux = aux.getEnlace(); // recorre la pila original
                nuevo = new Nodo(aux.getElem(), null);
                ultimo.setEnlace(nuevo);
            }
        }
        return clon;
    }

    @Override
    public String toString() {
        String s = "";

        if (this.tope == null) { // Verifica que la pila no esté vacía
            s = "Pila vacía";
        } else {
            s = "[";
            Nodo aux = this.tope;
            // llamada recursiva que recorre toda la pila
            s += cadenaNodoRecursivo(aux);
            s += "]";
        }

        return s;
    }

    public String cadenaNodoRecursivo(Nodo nodo) {
        String cadena = "";
        // Verifica si hay sólo 1 elemento en la pila, para no imprimir una coma de más.
        if (this.tope.getEnlace() == null) {
            cadena = nodo.getElem().toString();
        } else { // Si hay más de 1 elemento, se ejecuta esta alternativa
            if (nodo == this.tope) {
                // Si estoy en el tope, hago el llamado recursivo dejando el tope a la derecha.
                // Se ejecuta sólo una vez (cuando estoy en el tope).
                cadena += cadenaNodoRecursivo(nodo.getEnlace()) + nodo.getElem().toString();
            } else {
                if (nodo.getEnlace() != null) {
                    /*
                     * Si hay nodos después del actual, sigo con el llamado recursivo
                     * y voy concatenando los elementos seguidos de una coma
                     */
                    cadena += cadenaNodoRecursivo(nodo.getEnlace()) + nodo.getElem().toString() + ",";
                } else {
                    // En el último nodo guardo su elemento como String, seguido de una coma
                    cadena = nodo.getElem().toString() + ",";
                }
            }
        }
        return cadena;
    }

}
