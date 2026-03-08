package estructuras.conjuntistas;

import estructuras.lineales.Lista;
import estructuras.lineales.Cola;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar> Clase Árbol Binario AVL. Este
 *         tipo de árboles, como se mantienen balanceados, se puede asegurar que en el peor de los
 *         casos son de orden O(log n). Si bien las operaciones de inserción y eliminación tienen un
 *         costo extra (el balanceo), si el cálculo de la altura se hace de manera constante, el
 *         orden de insertar o eliminar se mantiene en O(log n)
 */

public class ArbolAVL<T extends Comparable<T>> {
    // Atributos
    private NodoAVL<T> raiz;

    // Constructor vacío
    public ArbolAVL() {
        this.raiz = null;
    }

    /**
     * Se tuvo en cuenta del Apunte 4 - Estructuras conjuntistas: La detección del desbalance se hace
     * siempre después de la inserción o eliminación de un elemento, y es un chequeo que se realiza
     * en todos los niveles a la vuelta de la recursión. En cuanto se detecta que el balance no
     * respeta el balance permitido se aplican una o dos rotaciones para solucionarlo
     */

    /**
     * Clase interna privada para manejar los resultados de inserción y eliminación en el árbol AVL.
     * Se implementa para lograr retornar más de un resultado en un método. Basado en el Apéndice C
     * de la cátedra.
     */
    private static class Resultado<T> {
        NodoAVL<T> nodo; // nueva raíz del subárbol (posiblemente modificada por rotaciones)
        boolean exito; // indica si la operación modificó el árbol (true) o no (false)

        Resultado(NodoAVL<T> nodo, boolean exito) {
            this.nodo = nodo;
            this.exito = exito;
        }
    }


    /**
     * Inserta un elemento en el árbol AVL manteniendo el balance. La inserción es O(log n) porque
     * el árbol permanece balanceado.
     * 
     * @param elemento el elemento a insertar
     * @return true si se insertó, false si ya existía
     */
    public boolean insertar(T elem) {
        Resultado<T> resultado = insertarAux(this.raiz, elem);
        this.raiz = resultado.nodo;
        return resultado.exito;
    }

    /**
     * Método privado para insertar elementos en el árbol AVL manteniendo el balance. La inserción
     * es O(log n) porque el árbol permanece balanceado. La altura se recalcula sólo en caso de
     * inserciones y lo hace el método balancear().
     * 
     * * Estrategia: 1. Descender recursivamente hasta encontrar posición de inserción o duplicado.
     * 2. A la vuelta de la recursión, reconectar el subárbol modificado 3. Balancear el nodo actual
     * si hubo inserción (detecta desbalance en la vuelta)
     * 
     * @param n nodo actual usado para comparar con el nuevo elemento
     * @param elem el elemento a insertar
     * @return Resultado: contiene al nodo raíz y el boolean que indica si se insertó o no
     */
    private Resultado<T> insertarAux(NodoAVL<T> n, T elem) {
        Resultado<T> res;

        if (n == null) {
            // Caso base: llegamos a una hoja o árbol vacío
            res = new Resultado<>(new NodoAVL<>(elem), true);
        } else {
            int comparacion = elem.compareTo(n.getElem());

            if (comparacion == 0) {
                // Elemento duplicado -> no se inserta y no se modifica el árbol
                res = new Resultado<>(n, false);
            } else {
                if (comparacion < 0) {
                    // Bajamos por la izquierda
                    res = insertarAux(n.getIzquierdo(), elem);
                    n.setIzquierdo(res.nodo);
                } else {
                    // Bajamos por la derecha
                    res = insertarAux(n.getDerecho(), elem);
                    n.setDerecho(res.nodo);
                }

                // A la vuelta de la recursión se realiza detección de desbalance
                if (res.exito) {
                    // Solo si hubo inserción real, balanceamos este nodo
                    res.nodo = balancear(n);
                } else {
                    // Si no hubo inserción, el nodo actual sigue siendo n
                    res.nodo = n;
                }
            }
        }
        return res;
    }

    /**
     * Balancea el nodo aplicando rotaciones si el factor del balance es 2 o -2.
     * ! Precondición: n no es null.
     * 
     * * Convención de signos (según apunte):
     * - balance = altura(HI) - altura(HD)
     * - balance = 2  → caído a izquierda
     * - balance = -2 → caído a derecha
     * 
     * Tabla de rotaciones (Figura 4.18):
     * | Balance padre | Balance hijo  | Rotación           |
     * |---------------|---------------|--------------------|
     * | 2             | >= 0          | Simple derecha     |
     * | 2             | -1            | Doble izq-derecha  |
     * | -2            | <= 0          | Simple izquierda   |
     * | -2            | 1             | Doble der-izquierda|
     * 
     * @param n el nodo a balancear (no null)
     * @return la nueva raíz del subárbol balanceado
     */
    private NodoAVL<T> balancear(NodoAVL<T> n) {
        NodoAVL<T> resultado = n;

        // Recalcular altura del nodo actual (muy importante antes de sacar el balance)
        n.recalcularAltura();

        // Calcular el factor de balance (Altura HI - Altura HD)
        int balance = obtenerBalance(n);

        if (balance == 2) {
            // Desbalanceado a izquierda
            int balanceHijo = obtenerBalance(n.getIzquierdo());

            if (balanceHijo >= 0) {
                // Mismo signo: rotación simple a derecha
                resultado = rotarDerecha(n);
            } else {
                // Signo distinto: rotación doble izquierda-derecha
                NodoAVL<T> nuevaIzq = rotarIzquierda(n.getIzquierdo());
                n.setIzquierdo(nuevaIzq);
                // Ahora el nodo nuevaIzq tiene el mismo signo que el padre y se puede aplicar
                // rotación simple a derecha
                resultado = rotarDerecha(n);
            }
        } else if (balance == -2) {
            // Desbalanceado a derecha
            int balanceHijo = obtenerBalance(n.getDerecho());

            if (balanceHijo <= 0) {
                // Mismo signo: rotación simple a izquierda
                resultado = rotarIzquierda(n);
            } else {
                // Signo distinto: rotación doble derecha-izquierda
                NodoAVL<T> nuevaDer = rotarDerecha(n.getDerecho());
                n.setDerecho(nuevaDer);
                // Ahora el nodo nuevaDer tiene el mismo signo que el padre y se puede aplicar
                // rotación simple a izquierda
                resultado = rotarIzquierda(n);
            }
        }

        return resultado;
    }

    /**
     * Método privado para obtener balance del nodo (altura HI - altura HD). Por convención el hijo
     * nulo tiene altura -1.
     * 
     * @param n el nodo a evaluar
     * @return factor de balance en rango [-2, 2]
     */
    private int obtenerBalance(NodoAVL<T> n) {
        int altIzq = (n.getIzquierdo() == null) ? -1 : n.getIzquierdo().getAltura(); // -1 si nulo
        int altDer = (n.getDerecho() == null) ? -1 : n.getDerecho().getAltura();
        return altIzq - altDer;
    }

    /**
     * Rotación simple a izquierda. Algoritmo 4.10 del apunte. Se aplica cuando el nodo está caído a
     * la derecha (balance -2) y su hijo derecho está caído hacia el mismo lado.
     * 
     * @param r el pivote (nodo desbalanceado)
     * @return la nueva raíz del subárbol
     */
    private NodoAVL<T> rotarIzquierda(NodoAVL<T> r) {
        NodoAVL<T> h = r.getDerecho(); // hijo derecho será nueva raíz
        NodoAVL<T> temp = h.getIzquierdo(); // subárbol que cambia de padre

        // Realizar rotación (Figura 4.19 y 4.20 del apunte)
        h.setIzquierdo(r);
        r.setDerecho(temp);

        // Se recalcula la altura primero de los hijos, luego del padre
        r.recalcularAltura();
        h.recalcularAltura();

        return h; // nueva raíz del subárbol
    }

    /**
     * Rotación simple a derecha. Algoritmo 4.11 del apunte. Se aplica cuando el nodo está caído a
     * la izquierda (balance 2) y su hijo izquierdo está caído hacia el mismo lado.
     * 
     * @param r el pivote (nodo desbalanceado)
     * @return la nueva raíz del subárbol
     */
    private NodoAVL<T> rotarDerecha(NodoAVL<T> r) {
        NodoAVL<T> h = r.getIzquierdo(); // hijo izquierdo será nueva raíz
        NodoAVL<T> temp = h.getDerecho(); // subárbol que cambia de padre

        // Realizar rotación (Figura 4.21 y 4.22 del apunte)
        h.setDerecho(r);
        r.setIzquierdo(temp);

        // Recalcular alturas (primero de hijos y luego del padre)
        r.recalcularAltura();
        h.recalcularAltura();

        return h; // nueva raíz del subárbol
    }

    /**
     * Elimina un elemento del árbol AVL manteniendo el balance. La eliminación es O(log n) porque
     * el árbol permanece balanceado.
     *
     * @param elem el elemento a eliminar
     * @return true si se eliminó, false si no se encontró
     */
    public boolean eliminar(T elem) {
        Resultado<T> resultado = eliminarAux(this.raiz, elem);
        this.raiz = resultado.nodo;
        return resultado.exito;
    }

    /**
     * Elimina un elemento del árbol AVL manteniendo el balance.
     * 
     * Estrategia (igual que inserción): 
     * 1. Descender recursivamente hasta encontrar el elemento 
     * 2. Al encontrarlo, aplicar los 3 casos de eliminación ABB 
     * 3. A la vuelta de la recursión: reconectar, recalcular altura y balancear
     * 
     * @param n nodo actual del subárbol
     * @param elem elemento a eliminar
     * @return Resultado con nueva raíz del subárbol y true si se eliminó
     */
    private Resultado<T> eliminarAux(NodoAVL<T> n, T elem) {
        Resultado<T> res;

        if (n == null) {
            // Caso base: elemento no encontrado
            res = new Resultado<>(null, false);
        } else {
            int comparacion = elem.compareTo(n.getElem());

            if (comparacion == 0) {
                // Elemento encontrado: aplicar casos de eliminación
                res = eliminarNodo(n);

            } else if (comparacion < 0) {
                // Buscar en izquierda
                res = eliminarAux(n.getIzquierdo(), elem);
                n.setIzquierdo(res.nodo);

                // A la vuelta: balancear si hubo eliminación
                if (res.exito) {
                    res.nodo = balancear(n);
                } else {
                    res.nodo = n;
                }

            } else {
                // Buscar en derecha
                res = eliminarAux(n.getDerecho(), elem);
                n.setDerecho(res.nodo);

                // A la vuelta: balancear si hubo eliminación
                if (res.exito) {
                    res.nodo = balancear(n);
                } else {
                    res.nodo = n;
                }
            }
        }

        return res;
    }

    /**
     * Elimina el nodo dado aplicando los 3 casos de eliminación ABB. Retorna la nueva raíz del
     * subárbol que reemplaza a n.
     * 
     * Casos: 
     * 1. Hoja: retorna null 
     * 2. Un hijo: retorna ese hijo 
     * 3. Dos hijos: reemplaza por el menor del derecho, elimina recursivamente ese candidato 
     * y retorna n (modificado)
     * 
     * @param n el nodo a eliminar (no null)
     * @return Resultado con nueva raíz del subárbol y true (siempre se elimina este nodo)
     */
    private Resultado<T> eliminarNodo(NodoAVL<T> n) {
        Resultado<T> res;

        if (n.getIzquierdo() == null && n.getDerecho() == null) {
            // Caso 1: nodo hoja
            res = new Resultado<>(null, true);

        } else if (n.getIzquierdo() == null) {
            // Caso 2: sólo tiene hijo derecho -> sube el hijo derecho
            res = new Resultado<>(n.getDerecho(), true);

        } else if (n.getDerecho() == null) {
            // Caso 2: sólo tiene hijo izquierdo -> sube el hijo izquierdo
            res = new Resultado<>(n.getIzquierdo(), true);

        } else {
            // Caso 3: dos hijos. Estrategia: reemplazar por el MENOR del subárbol derecho

            // Optimización para extraer el mínimo del subárbol derecho en un sólo recorrido
            ResultadoMinimo<T> extraido = extraerMinimo(n.getDerecho());
            
            // El mínimo pasa a ser el valor de n
            n.setElem(extraido.minimo);

            // El derecho ahora es el subárbol sin el mínimo (ya viene balanceado)
            n.setDerecho(extraido.nuevaRaiz);

            // Último paso: balancear el resultado
            res = new Resultado<>(balancear(n), true);

            // Paso 3: eliminar recursivamente el menor del derecho.
            // Como el candidato puede estar a cualquier nivel del subárbol derecho, la recursión
            // garantiza encontrarlo y eliminarlo aplicando los casos 1 o 2 (porque tiene a lo sumo
            // 1 hijo derecho), balanceando todo el camino de vuelta.
            // Resultado<T> resCandidato = eliminarAux(n.getDerecho(), candidato);
            // n.setDerecho(resCandidato.nodo);
        }

        return res;
    }

    // /**
    //  * Método privado. Recorre el subárbol hacia la izquierda hasta encontrar el nodo más pequeño y
    //  * retorna su elemento. Se usa en el Caso 3 de eliminación para encontrar el candidato sucesor.
    //  * Precondición: n no es null.
    //  *
    //  * @param n raíz del subárbol donde buscar el mínimo
    //  * @return el elemento mínimo del subárbol
    //  */
    // private T obtenerMinimo(NodoAVL<T> n) {
    //     NodoAVL<T> actual = n;
    //     while (actual.getIzquierdo() != null) {
    //         actual = actual.getIzquierdo();
    //     }
    //     return actual.getElem();
    // }
    
    /*
     * Se implementó esta clase para optimizar la búsqueda y eliminación del nodo mínimo en el caso
     * 3 de eliminación, evitando recorrer dos veces el subárbol derecho (una para encontrar el
     * mínimo con obtenerMinimo() y otra para eliminarlo con eliminarAux()) 
     */

    /**
     * Estructura utilizada para retornar el valor mínimo y la nueva raíz del subárbol después de
     * eliminar el nodo mínimo. Se usa en el Caso 3 de eliminación.
     */
    private static class ResultadoMinimo<T> {
        T minimo; // el valor mínimo encontrado
        NodoAVL<T> nuevaRaiz; // nueva raíz del subárbol sin el mínimo

        ResultadoMinimo(T minimo, NodoAVL<T> nuevaRaiz) {
            this.minimo = minimo;
            this.nuevaRaiz = nuevaRaiz;
        }
    }

    /**
     * Extrae el mínimo del subárbol: lo encuentra, lo elimina (caso 1 o 2), y retorna su valor
     * junto con la nueva raíz del subárbol. Balancea en el camino de vuelta.
     * 
     * @param n el nodo raíz del subárbol donde buscar el mínimo (no null)
     * @return Resultado con el valor mínimo encontrado y la nueva raíz del subárbol sin ese mínimo
     */
    private ResultadoMinimo<T> extraerMinimo(NodoAVL<T> n) {
        ResultadoMinimo<T> res;
        if (n.getIzquierdo() == null) {
            // Encontramos el mínimo: es n
            // Caso 1 o 2: su nueva raíz es su hijo derecho (puede ser null)
            res = new ResultadoMinimo<>(n.getElem(), n.getDerecho());
        } else {
            // seguir buscando a la izquierda
            res = extraerMinimo(n.getIzquierdo());
            n.setIzquierdo(res.nuevaRaiz);

            // Balancear nuevaRaiz y actualizar resultado
            res.nuevaRaiz = balancear(n);
        }
        return res;
    }

    /*
     * Devuelve verdadero si el elemento recibido por parámetro está en el árbol y falso en caso
     * contrario.
     */
    public boolean pertenece(T elem) {
        return perteneceAux(this.raiz, elem);
    }

    /*
     * Método auxiliar y privado, que recorre la estructura de forma recursiva.
     */
    private boolean perteneceAux(NodoAVL<T> n, T elemento) {
        boolean exito = false;
        if (n != null) {
            int comparacion = elemento.compareTo(n.getElem());
            if ((comparacion == 0)) {
                // Elemento encontrado
                exito = true;
            } else if (comparacion < 0) {
                // elemento es menor que n.getElem()
                // busca a la izquierda de n
                exito = perteneceAux(n.getIzquierdo(), elemento);
            } else {
                // elemento es mayor que n.getElem()
                // busca a la derecha de n
                exito = perteneceAux(n.getDerecho(), elemento);
            }
        }
        return exito;
    }

    /*
     * Implementación iterativa del método pertenece(). Devuelve verdadero si el elemento recibido
     * por parámetro está en el árbol, falso en caso contrario.
     */
    public boolean perteneceIterativo(T elemento) {
        NodoAVL<T> actual = this.raiz;
        boolean exito = false;

        while (actual != null && !exito) {
            int comparacion = elemento.compareTo(actual.getElem());

            if (comparacion == 0) {
                // Elemento encontrado
                exito = true;
            } else if (comparacion < 0) {
                // Busca en el subárbol izquierdo
                actual = actual.getIzquierdo();
            } else {
                // Busca en el subárbol derecho
                actual = actual.getDerecho();
            }
        }

        return exito;
    }

    /*
     * Recorre el árbol completo y devuelve una lista ordenada con los elementos que se encuentran
     * almacenados en él
     */
    public Lista listar() {
        Lista lis = new Lista();
        NodoAVL<T> aux = this.raiz;
        if (aux != null) {
            int posInicial = 0; // posicion inicial al insertar en la lista
            this.listarAux(lis, aux, posInicial);
        }
        return lis;
    }

    /*
     * Método auxiliar y privado, que recorre la estructura de forma recursiva. list: lista a
     * manipular n: nodo pos: posición del último elemento insertado Retorna entero: posición del
     */
    private int listarAux(Lista list, NodoAVL<T> n, int pos) {
        int aux = pos;
        if (n != null) {
            // Si tiene HI, sigo recorriendo por la rama izquierda
            if (n.getIzquierdo() != null) {
                aux = this.listarAux(list, n.getIzquierdo(), pos);
            }
            aux++; // Incremento la posición
            list.insertar(n.getElem(), aux);
            if (n.getDerecho() != null) {
                aux = this.listarAux(list, n.getDerecho(), aux);
            }
        }
        return aux;
    }

    /*
     * Recorre parte del árbol (sólo lo necesario) y devuelve una lista ordenada con los elementos
     * que se encuentran almacenados en él.
     */
    public Lista listarRango(T minElem, T maxElem) {
        Lista lis = new Lista();
        NodoAVL<T> raiz = this.raiz;
        if (raiz != null) {
            this.listarRangoAux(lis, raiz, minElem, maxElem);
        }
        return lis;
    }

    /*
     * Método auxiliar y privado, que recorre la estructura de forma recursiva. list: lista a
     * manipular n: nodo
     */
    private void listarRangoAux(Lista list, NodoAVL<T> n, T min, T max) {
        if (n != null) {
            T valorNodo = n.getElem();

            // Si n es mayor a min, recorrer HI
            if (valorNodo.compareTo(min) > 0) {
                this.listarRangoAux(list, n.getIzquierdo(), min, max);
            }

            // Si n está dentro del rango [min, max], insertar n
            if (valorNodo.compareTo(min) >= 0 && valorNodo.compareTo(max) <= 0) {
                list.insertar(valorNodo, list.longitud() + 1);
            }

            // Si n es menor a max, recorrer HD
            if (valorNodo.compareTo(max) < 0) {
                this.listarRangoAux(list, n.getDerecho(), min, max);
            }
        }
    }

    /*
     * Recorre la rama correspondiente y devuelve el elemento más pequeño almacenado en el árbol. Si
     * el árbol está vacío, devuelve null
     */
    public T minimoElem() {
        T elem = null;
        // Como es un árbol ordenado, el menor de los elementos es el que se encuentra más a la
        // izquierda
        NodoAVL<T> n = this.raiz;

        while (n != null) {
            elem = n.getElem();
            n = n.getIzquierdo();
        }

        return elem;
    }

    /*
     * Recorre la rama correspondiente y devuelve el elemento más grande almacenado en el árbol.
     */
    public T maximoElem() {
        T elem = null;
        // Como es un árbol ordenado, el mayor de los elementos es el que se encuentra más a la
        // derecha
        NodoAVL<T> n = this.raiz;

        while (n != null) {
            elem = n.getElem();
            n = n.getDerecho();
        }

        return elem;
    }

    /*
     * Devuelve falso si hay al menos un elemento en el árbol. Verdadero en caso contrario.
     */
    public boolean esVacio() {
        return this.raiz == null;
    }

    /*
     * Vacía el árbol.
     */
    public void vaciar() {
        this.raiz = null;
    }

    /**
     * Recorre la rama correspondiente y devuelve el elemento almacenado en el árbol.
     * 
     * @param elem el elemento a buscar
     * @return el elemento encontrado o null si no se encuentra en el árbol
     */
    public T obtenerElemento(T elem) {
        return obtenerElementoAux(this.raiz, elem);
    }

    private T obtenerElementoAux(NodoAVL<T> nodo, T elem) {
        T resultado = null;

        if (nodo != null) {
            int comp = elem.compareTo(nodo.getElem());
            if (comp == 0) {
                // Caso base: lo encontramos
                resultado = nodo.getElem();
            } else if (comp < 0) {
                // Es menor: buscamos solo en el subárbol izquierdo
                resultado = obtenerElementoAux(nodo.getIzquierdo(), elem);
            } else {
                // Es mayor: buscamos solo en el subárbol derecho
                resultado = obtenerElementoAux(nodo.getDerecho(), elem);
            }
        }

        return resultado;
    }

    // No comento toString() porque se utilizará para la clase que imprime logs del sistema
    /**
     * Genera y devuelve una cadena de caracteres que indica cuál es la raíz del árbol y quiénes son
     * los hijos de cada nodo.
     */
    @Override
    public String toString() {
        String cadena;
        if (this.raiz != null) {
            cadena = toStringAux(this.raiz);
        } else {
            cadena = "Árbol vacío";
        }
        return cadena;
    }

    private String toStringAux(NodoAVL<T> nodo) {
        // método Privado que recorre el árbol por niveles y va guardando los
        // elementos de cada nodo y sus hijos en un String para luego retornarlo
        String cadena = "";
        // si el arbol está vacío, esto no se ejecuta y devuelve una cadena vacía
        if (nodo != null) {
            int elementosEnNivel = 1; // Número de elementos en el nivel actual
            Cola cola = new Cola();
            cola.poner(this.raiz);

            // Mientras la cola no sea vacía
            while (!cola.esVacia()) {
                int elementosSigNivel = 0; // Número de elementos en el siguiente nivel
                // Recorremos todos los nodos del nivel actual y los insertamos en la lista
                for (int i = 0; i < elementosEnNivel; i++) {
                    // Obtengo el nodo actual de la cola
                    @SuppressWarnings("unchecked")
                    NodoAVL<T> actual = (NodoAVL<T>) cola.obtenerFrente();
                    // Sacamos el nodo actual de la cola
                    cola.sacar();
                    cadena += actual.getElem();
                    // Agregamos los hijos del nodo actual a la cola, si existen
                    if (actual.getIzquierdo() != null) {
                        cola.poner(actual.getIzquierdo());
                        cadena += " HI: " + actual.getIzquierdo().getElem();
                        elementosSigNivel++;
                    } else {
                        cadena += " HI: -";
                    }
                    if (actual.getDerecho() != null) {
                        cola.poner(actual.getDerecho());
                        cadena += " HD: " + actual.getDerecho().getElem() + "\n";
                        elementosSigNivel++;
                    } else {
                        cadena += " HD: - \n";
                    }
                }
                // Actualizamos el número de elementos
                elementosEnNivel = elementosSigNivel;
            }
        }
        return cadena;
    }
}
