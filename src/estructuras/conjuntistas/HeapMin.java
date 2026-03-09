package estructuras.conjuntistas;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * Clase Árbol Heap mínimo
 */

public class HeapMin<T extends Comparable<T>> {

    private T[] heap;
    private int ultimo;
    private static final int TAMANIO = 20;

    // Constructor vacío
    @SuppressWarnings("unchecked")
    public HeapMin() {
        // El arreglo se usa únicamente para almacenar elementos de tipo T, y la restricción T
        // extends Comparable<T> garantiza la corrección de las comparaciones.
        this.heap = (T[]) new Comparable[TAMANIO];
        this.ultimo = 0; // la posicion 0 nunca es utilizada
    }

    /**
     * Recibe un elemento y lo inserta en el árbol con el siguiente algoritmo:
     * 1. Agregar el nuevo elemento como hoja. Se agrega en el último nivel. El nivel se completa 
     * de izquierda a derecha. Si el nivel esta completo se agrega el nuevo elemento en un nuevo 
     * nivel como primer elemento a la izquierda.
     * 2. Si el nuevo elemento tiene menor valor que su padre lo hacemos subir (intercambiando sus valores)
     * 3. Repetir el paso 2 hasta que el nuevo elemento cumpla con alguna de las siguientes condiciones:
     *   a) llegue a una posición en que sea mayor que su padre
     *   b) llegue a la raíz
     * Si la operación termina con éxito devuelve verdadero y falso en caso contrario.
     * Caso de error: que el árbol heap esté lleno
     * Se aceptan elementos repetidos.
     */
    public boolean insertar(T elem) {
        boolean exito = true;
        // Caso de fallo: heap lleno
        if (this.heap.length - 1 == this.ultimo) {
            exito = false; 
        } else {
            // Primero inserto el elemento al final del arreglo
            this.ultimo++;
            this.heap[ultimo] = elem;
            int posActual = this.ultimo;
            if (posActual > 1) {
                // Si no es la cima, busco al padre y los comparo para ver si debo intercambiarlos
                int posPadre = posActual / 2;
                T elemPadre = this.heap[posPadre];
                // El valor de 'elem' siempre es igual por más que esté en otra posición
                boolean ordenado = elem.compareTo(elemPadre) >= 0;
                while (!ordenado && posActual > 1) {
                    // Mientras el elemento sea menor que su elemPadre, intercambio sus valores (va subiendo en el árbol)
                    this.heap[posActual] = elemPadre;
                    this.heap[posPadre] = elem;

                    // Actualizo posiciones de referencia
                    posActual = posPadre;
                    posPadre = posPadre / 2;
                    
                    // Verifico si ya quedó ordenado
                    if (posActual >= 1 && posPadre >= 1) {
                        ordenado = elem.compareTo(this.heap[posPadre]) >= 0;
                        elemPadre = this.heap[posPadre];
                    }
                }
            }
        }

        return exito;
    }

    /**
     * Elimina el elemento de la raíz (o cima del montículo). 
     * 1. Poner en la raíz el valor de la hoja más a la derecha del último nivel y eliminar dicha hoja. 
     * 2. “Empujar” el elemento hacia abajo, intercambiando su valor con el del hijo de menor valor.
     * 3. Repetir el paso 2 hasta que el elemento quede en una hoja o tenga menor valor que sus hijos.
     * Si la operación termina con éxito devuelve verdadero y falso si el árbol estaba vacío.
     */
    public boolean eliminarCima() {
        boolean exito = false;
        if (this.ultimo != 0) { // Si la estructura no está vacía
            // Sacar la raíz y poner la última hoja en su lugar
            this.heap[1] = this.heap[ultimo];
            this.ultimo--;
            // Reestablece la propiedad del heap mínimo
            hacerBajar(1);
            exito = true;
        }
        return exito;
    }

    private void hacerBajar(int posPadre) {
        int posH;
        T temp = this.heap[posPadre];
        boolean salir = false;

        while (!salir) {
            posH = posPadre * 2;
            if (posH <= this.ultimo) {
                // temp tiene al menos un hijo (izq) y lo considera menor

                if (posH < this.ultimo) {
                    // hijoMenor tiene hermano derecho

                    if (this.heap[posH + 1].compareTo(this.heap[posH]) < 0) {
                        // el hijo derecho es el menor de los dos
                        posH++;
                    }
                }

                // compara al hijo menor con el padre
                if (this.heap[posH].compareTo(temp) < 0) {
                    // el hijo es menor que el padre, los intercambia
                    this.heap[posPadre] = this.heap[posH];
                    this.heap[posH] = temp;
                    posPadre = posH;
                } else {
                    // el padre es menor que su hijo, está bien ubicado
                    salir = true;
                }
            } else {
                // el temp es hoja, está bien ubicado
                salir = true;
            }
        }
    }

    /**
     * devuelve el elemento que está en la raíz (cima del montículo). Precondición: el árbol no está
     * vacío (si está vacío no se puede asegurar el funcionamiento de la operación).
     */
    public T recuperarCima() {
        return this.heap[1];
    }

    // Devuelve falso si hay al menos un elemento cargado y verdadero en caso contrario
    public boolean esVacio() {
        return this.ultimo == 0;
    }

    // TODO: implementar método listar()

    // TODO: implementar método vaciar()
}
