package estructuras.lineales;

/**
 *
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 *         This year (2024) I want to implement this in English, at least the
 *         comments section, to practice my writing skills in that language.
 */

public class Lista {

    private Nodo cabecera;

    public Lista() {
        cabecera = null;
    }

    /**
     * Adds the element passed as a parameter at the position 'pos',
     * such that the quantity of elements in the list increases by 1.
     * For a succesful insertion, the received position must be
     * 1 <= pos <= length(list)+1.
     * Returns true if the element can be inserted, false, otherwise.
     */
    public boolean insertar(Object elem, int pos) {
        // Detects and reports invalid position error
        boolean exito = false;
        int largo = this.longitud() + 1;
        boolean condInsercion = (pos >= 1) && (pos <= largo); // Verifies that 'pos' position is valid
        if (condInsercion) {
            if (pos == 1) { // Inserting in the first position
                Nodo nuevoNodo = new Nodo(elem, this.cabecera);
                this.cabecera = nuevoNodo;
            } else {
                Nodo aux = this.cabecera;
                int i = 1;
                while (i < pos - 1) { // if pos = 2, this will not run
                    aux = aux.getEnlace();
                    i++;
                }
                // Creates and links the new node with the one at pos+1
                Nodo nuevoNodo = new Nodo(elem, aux.getEnlace());
                // Now this links the auxiliary node (at pos-1) with the new one
                aux.setEnlace(nuevoNodo);
            }
            exito = true;
        }

        return exito;
    }

    /**
     * Deletes the element at the position 'pos'. Preconditions: list not empty and
     * valid position (pos >= 1 and pos <= length(list) + 1).
     * Returns true if the deletion was succesful, false otherwise.
     */
    public boolean eliminar(int pos) {
        boolean exito = false;
        int largo = this.longitud();

        if (this.cabecera != null && pos >= 1 && pos <= largo) {
            int i = 1;
            if (pos == 1) { // Special case: 'pos' = 1, deleting first element
                this.cabecera = this.cabecera.getEnlace();
            } else {
                Nodo aux = this.cabecera;
                while (i < pos - 1) { // Moves forward to the node at position 'pos - 1'
                    aux = aux.getEnlace();
                    i++;
                }
                // Links the node at pos-1 with the one at pos+1, jumping over and deleting the
                // one at parameter 'pos'
                aux.setEnlace(aux.getEnlace().getEnlace());

            }
            exito = true;
        }

        return exito;
    }

    /**
     * Returns the element in the 'pos' position. Precondition: valid position.
     */

    public Object recuperar(int pos) {
        Object elem = null;

        // Verifies that list is not empty and position 'pos' is valid
        if (this.cabecera != null && pos >= 1 && pos <= this.longitud()) {
            int i = 1;
            Nodo actual = this.cabecera;
            while (i < pos) { // Moves forward to the node at position 'pos'
                actual = actual.getEnlace();
                i++;
            }
            // Save the element of the node that is at position 'pos'
            elem = actual.getElem();
        }

        return elem;
    }

    /**
     * Returns the position where the first ocurrence of 'elem' is in the list.
     * If it is not found, it returns -1.
     */
    public int localizar(Object elem) {
        int position = -1;

        // Verifies that list is not empty
        if (this.cabecera != null) {
            boolean found = false; // Represents if 'elem' was found or not
            Nodo aux = this.cabecera; // Starts from the first element
            int i = 1;
            while (aux != null && !found) {
                if (aux.getElem().equals(elem)) {
                    // When the element is found, the position is saved and the
                    // 'found' variable is updated to break the while loop
                    position = i;
                    found = true;
                } else {
                    // If element is not found, moves forward to the next node
                    aux = aux.getEnlace();
                    i++;
                }
            }

        }

        return position;
    }

    // Empties the list, removing all of the elements inside.
    public void vaciar() {
        this.cabecera = null;
    }

    // Returns true if the list is empty; false otherwise.
    public boolean esVacia() {
        return this.cabecera == null;
    }

    /**
     * Returns an exact copy of the data in the original structure, respecting
     * its order, in another structure of the same type.
     */
    @Override
    public Lista clone() {
        Lista clon = new Lista(); // creates an empty list

        if (!this.esVacia()) { // if the list isn't empty
            Nodo aux = this.cabecera;
            Nodo ultimo;
            Nodo nuevo = new Nodo(aux.getElem(), null);
            clon.cabecera = nuevo;
            while (aux.getEnlace() != null) {
                // reference to the last cloned node, the 'nuevo' variable
                ultimo = nuevo;
                // moves to the next node in the original structure
                aux = aux.getEnlace();
                // copies the element of the original node on the new copy
                nuevo = new Nodo(aux.getElem(), null);
                // links the last copied node to the recently created one
                ultimo.setEnlace(nuevo);
            }
        }

        return clon;
    }

    /**
     * Reemplaza el contenido de esta lista con una copia del contenido de 'origen', recorriendo
     * ambas listas nodo a nodo en una única pasada: O(n).
     *  
     * Uso principal: lograr actualizar mejorCamino en los algoritmos DFS de grafos sin tener que
     * incurrir en costo O(n²) al utilizar vaciar() + un for con insertar(longitud()+1) y recuperar(i)
     *
     * @param origen la lista cuyo contenido se copiará en esta lista
     */
    public void copiarDesde(Lista origen) {
        this.cabecera = null; // vaciamos sin llamar a vaciar() para no recorrer dos veces

        if (origen != null && !origen.esVacia()) {
            Nodo auxOrigen = origen.cabecera;

            // Crear el primer nodo y fijar la cabecera
            Nodo nuevo = new Nodo(auxOrigen.getElem(), null);
            this.cabecera = nuevo;
            Nodo ultimo = nuevo;
            auxOrigen = auxOrigen.getEnlace();

            // Recorrer el resto de origen, enlazando nodos uno a uno
            while (auxOrigen != null) {
                nuevo = new Nodo(auxOrigen.getElem(), null);
                ultimo.setEnlace(nuevo);
                ultimo = nuevo;
                auxOrigen = auxOrigen.getEnlace();
            }
        }
    }

    /**
     * Returns the quantity of elements in the list.
     */
    public int longitud() {
        int count = 0;

        Nodo aux = this.cabecera;
        // Starts counting the nodes from the first position, one by one until
        // it gets to the last one (last position on the List)
        while (aux != null) {
            count++;
            aux = aux.getEnlace();
        }

        return count;
    }

    /**
     * Creates and returns a String formed by all the elements of the list, so
     * that it can print it. This method is for testing purposes only.
     */
    @Override
    public String toString() {
        String cadena = "";

        if (this.esVacia()) {
            cadena = "[]";
        } else {
            cadena = "[";
            Nodo aux = this.cabecera;
            while (aux.getEnlace() != null) {
                cadena += aux.getElem() + ",";
                aux = aux.getEnlace();
            }
            cadena += aux.getElem() + "]";
        }

        return cadena;
    }

    // Resolving exercise 1 from "Simulation of the first partial exam".
    /*
     * a) Add to the ADD Lista the operation obenerMultiplos(int num) that receives
     * a number and returns a new list that contains all the elements of the
     * multiples of NUM, in the same order found, making a single route of the
     * original structures and copy ; and without using other TDA operations.
     */
    public Lista obtenerMultiplos(int num) {
        Lista copy = new Lista();

        // Verifies that list is not empty and the position 'num' is valid
        if (this.cabecera != null && num >= 1 && num <= this.longitud()) {
            int pos = num;
            int i = 1;
            Nodo actual = this.cabecera; // Auxiliary node, used to travel the original list
            while (pos <= this.longitud()) {
                while (i < pos) {
                    actual = actual.getEnlace();
                    i++;
                }
                // Insert the element of the position that is a multiple of 'num' in the copy
                copy.insertar(actual.getElem(), copy.longitud() + 1); // Intentar hacerlo sin el método insertar

                // Increase the 'pos' position in 'num' times to travel only what is necessary
                // and thus guarantee that it is always in the correct position
                pos += num;
            }
        }

        return copy;
    }

    /*
     * Mueve el elemento que se encuentra en la posición pasada por parámetro a
     * la anteúltima posición de la lista. Retorna true si pudo, y false en caso
     * contrario.
     */
    public boolean moverAAnteultimaPosicion(int pos) {
        boolean exito = false;
        int longitud = this.longitud();

        if (this.cabecera != null && pos != (longitud - 1) && pos >= 1 && pos <= longitud && longitud >= 2) {
            Nodo aux = this.cabecera;
            if (longitud == 2) { // CASO long = 2, sé que pos != longitud -1 por el primer if.
                // Intercambio el primero con el segundo y ya queda bien ubicado
                this.cabecera = this.cabecera.getEnlace();
                this.cabecera.setEnlace(aux);
                aux.setEnlace(null);
            } else {
                Nodo porMover = null;
                Nodo temp = aux;
                
                // Busco al nodo porMover (el que está en la posición 'pos')
                if (pos == 1) { // CASO pos == 1
                    porMover = this.cabecera; // Guardo nodo porMover de pos == 1
                    this.cabecera = this.cabecera.getEnlace();
                    aux = this.cabecera;
                } else { // Caso pos != 1 (funciona igual para pos == long o pos != long)
                    for (int i = 1; i < pos - 1; i++) {
                        temp = aux;
                        aux = aux.getEnlace();
                    }
                    porMover = aux.getEnlace();
                    if (pos != longitud) {
                        aux.setEnlace(porMover.getEnlace()); // Quito enlace del aux al nodo porMover
                    }
                }

                // Ya encontré al nodo que quiero mover, procedo a moverlo
                if (pos == longitud) { // Caso pos == long: el nodo porMover es el último
                    temp.setEnlace(porMover); // nodo porMover pasa de ser último a anteúltimo
                    porMover.setEnlace(aux); // aux pasa a ser el último
                    aux.setEnlace(null);
                } else {
                    // Recorro la estructura hasta longitud - 1
                    while (aux.getEnlace().getEnlace() != null) {
                        aux = aux.getEnlace(); // aux es el anteúltimo actual
                    }
                    porMover.setEnlace(aux.getEnlace()); // enlazo porMover con el último nodo
                    aux.setEnlace(porMover); // Muevo nodo porMover a anteúltima posición
                }
            }
            exito = true;
        }
        return exito;
    }
}
