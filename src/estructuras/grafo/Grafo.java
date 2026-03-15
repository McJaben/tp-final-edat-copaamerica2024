package estructuras.grafo;

import estructuras.lineales.Lista;
import estructuras.lineales.Cola;

/**
 * @author Benjamín Morales <benjamin.morales at est.fi.uncoma.edu.ar>
 * TDA Grafo etiquetado NO dirigido — Implementación con listas de adyacencia.
 *
 * Dominio: Mapa de ciudades de EEUU con rutas aéreas para Copa América 2024.
 *   - Vértices : objetos Ciudad (nombre único, alojamiento, esSede)
 *   - Etiquetas: tiempo de vuelo directo en minutos (tipo E, ej. Integer)
 *   - No dirigido: insertar A→B también inserta B→A con la misma etiqueta
 *   - No se permiten arcos duplicados entre el mismo par de vértices.
 *
 * Todas las estructuras auxiliares usan ÚNICAMENTE los TDAs propios:
 *   Lista  (estructuras.lineales.Lista)
 *   Cola   (estructuras.lineales.Cola)
 *
 * API de Lista usada internamente:
 *   insertar(Object elem, int pos)  → boolean
 *   eliminar(int pos)               → boolean
 *   recuperar(int pos)              → Object
 *   localizar(Object elem)          → int   (-1 si no está)
 *   longitud()                      → int
 *   esVacia()                       → boolean
 *   vaciar()                        → void
 *   clone()                         → Lista
 *
 * API de Cola usada internamente:
 *   poner(Object elem)              → boolean
 *   sacar()                         → boolean
 *   obtenerFrente()                 → Object
 *   esVacia()                       → boolean
 *
 * ═══════════════════════════════════════════════════════════════
 *  OPERACIONES DEL TDA (según Apunte 5 + enunciado TPO)
 * ═══════════════════════════════════════════════════════════════
 * Básicas:
 *  insertarVertice(T)                              → boolean
 *  eliminarVertice(T)                              → boolean
 *  existeVertice(T)                                → boolean
 *  insertarArco(T origen, T destino, E etiqueta)   → boolean
 *  eliminarArco(T origen, T destino)               → boolean
 *  existeArco(T origen, T destino)                 → boolean
 *  esVacio()                                       → boolean
 *  getVertice(T)                                   → T
 *
 * Recorridos:
 *  listarEnProfundidad()                           → Lista
 *  listarEnAnchura()                               → Lista
 * 
 * Caminos:
 *  existeCamino(T origen, T destino)               → boolean
 *  caminoMenosVertices(T origen, T destino)         → Lista  [mínima cantidad de ciudades]
 *  caminoMasLiviano(T origen, T destino)         → Lista  [menor tiempo de vuelo]
 *  caminoMasLivianoSinPasar(T, T, T evitar)      → Lista  [(*) sin pasar por ciudad C]
 *  todosLosCaminos(T origen, T destino)            → Lista  [(*) todos los caminos]
 *
 * Debug:
 *  toString()                                      → String
 *
 * @param <T> Tipo de elemento en los vértices (debe implementar equals)
 * @param <E> Tipo de etiqueta en los arcos (ej. Integer p/ minutos)
 */
public class Grafo<T, E> {

    private NodoVert<T, E> inicio;

    //  Constructor
    public Grafo() {
        this.inicio = null;
    }

    //  Métodos auxiliares privados

    /**
     * Busca y devuelve el NodoVert cuyo elem.equals(buscado).
     * Devuelve null si no existe.
     */
    private NodoVert<T, E> ubicarVertice(T buscado) {
        NodoVert<T, E> aux = this.inicio;
        while (aux != null && !aux.getElem().equals(buscado)) {
            aux = aux.getSigVertice();
        }
        return aux;
    }

    /**
     * Agrega un NodoAdy al FINAL de la lista de adyacentes de 'origen',
     * apuntando a 'destino' con la etiqueta dada.
     * Insertar al final mantiene el orden de inserción y simplifica el código.
     */
    private void enlazarAdyacente(NodoVert<T, E> origen, NodoVert<T, E> destino, E etiqueta) {
        NodoAdy<T, E> nuevo = new NodoAdy<>(destino, etiqueta);
        NodoAdy<T, E> aux = origen.getPrimerAdy();
        if (aux == null) {
            origen.setPrimerAdy(nuevo);
        } else {
            while (aux.getSigAdyacente() != null) {
                aux = aux.getSigAdyacente();
            }
            aux.setSigAdyacente(nuevo);
        }
    }

    /**
     * Elimina de la lista de adyacentes de 'origen' el primer NodoAdy que apunte exactamente al
     * NodoVert 'destino' (comparación de referencia).
     */
    private void desenlazarAdyacente(NodoVert<T, E> origen, NodoVert<T, E> destino) {
        NodoAdy<T, E> actual = origen.getPrimerAdy();
        NodoAdy<T, E> anterior = null;
        // Se compara por referencia, dado que los NodoVert entrantes son el puntero exacto al nodo
        // en el grafo, no una copia. Esto es O(1) por comparación en lugar de invocar equals().
        while (actual != null && actual.getVertice() != destino) {
            anterior = actual;
            actual = actual.getSigAdyacente();
        }
        if (actual != null) {
            if (anterior == null) {
                // actual no nulo y anterior nulo: el destino es el primer adyacente del origen
                origen.setPrimerAdy(actual.getSigAdyacente());
            } else {
                anterior.setSigAdyacente(actual.getSigAdyacente());
            }
        }
    }

    //  Operaciones básicas

    /**
     * Inserta 'elem' como nuevo vértice si no existe ya uno igual.
     * 
     * @param elem el elemento a insertar en el vértice
     * @return true si se insertó correctamente, false si ya existía un vértice igual
     */
    public boolean insertarVertice(T elem) {
        boolean exito = false;
        if (this.ubicarVertice(elem) == null) {
            this.inicio = new NodoVert<>(elem, this.inicio);
            exito = true;
        }
        return exito;
    }

    /**
     * Elimina el vértice igual a 'elem' y todos los arcos que lo involucran.
     * 
     * Estrategia (para grafo no dirigido):
     * 1. Recorrer la lista de vértices una sola vez para ubicar 'aBorrar' y
     *    al mismo tiempo mantener 'vertPrevio' para poder desconectarlo.
     * 2. Recorrer los adyacentes de 'aBorrar': cada adyacente es exactamente
     *    un vértice que tiene un arco inverso hacia 'aBorrar'. Desenlazarlo
     *    directamente desde la referencia ya disponible, sin recorrer toda
     *    la lista de vértices.
     * 3. Desconectar 'aBorrar' de la lista de vértices usando 'vertPrevio'.
     * 
     * @param elem el elemento del vértice a eliminar
     * @return true si se eliminó, false si no existía el vértice
     */
    public boolean eliminarVertice(T elem) {
        boolean exito = false;
        NodoVert<T, E> aBorrar = null;
        NodoVert<T, E> vertPrevio = null; // usado para desconectar aBorrar de lista de vértices
        NodoVert<T, E> aux = this.inicio;

        // 1. recorrer lista de vértices una vez para ubicar 'aBorrar' y guardar anterior
        while (aux != null && aBorrar == null) {
            if (aux.getElem().equals(elem)) {
                aBorrar = aux;
            } else {
                vertPrevio = aux;
                aux = aux.getSigVertice();
            }
        }

        if (aBorrar != null) {
            // 2. Recorrer los adyacentes de 'aBorrar' para eliminar los arcos inversos (grafo no
            // dirigido). Es más eficiente aprovechar los enlaces que recorrer toda la lista de
            // vértices buscando quién apunta a 'aBorrar'.
            NodoAdy<T, E> ady = aBorrar.getPrimerAdy();
            while (ady != null) {
                this.desenlazarAdyacente(ady.getVertice(), aBorrar);
                ady = ady.getSigAdyacente();
            }

            // 3. quitar 'aBorrar' de la lista de vértices usando 'vertPrevio'
            if (vertPrevio == null) {
                // aBorrar era el primer vértice de la lista
                this.inicio = aBorrar.getSigVertice();
            } else {
                vertPrevio.setSigVertice(aBorrar.getSigVertice());
            }

            exito = true;
        }
        return exito;
    }

    /**
     * Devuelve true si existe un vértice con elem igual a 'elem'.
     * @param elem el elemento a buscar en los vértices
     * @return true si existe el vértice, false en caso contrario.
     */
    public boolean existeVertice(T elem) {
        return this.ubicarVertice(elem) != null;
    }

    /**
     * Inserta un arco NO dirigido entre origen y destino con la etiqueta dada. Crea A→B y B→A con
     * la misma etiqueta.
     * 
     * @param origen elem de origen del arco
     * @param destino elem de destino del arco
     * @param etiqueta tiempo de vuelo directo en minutos (debe ser >= 0)
     * @return true si se insertó correctamente, false si no existen los vértices o si ya existía el
     *         arco
     */
    public boolean insertarArco(T origen, T destino, E etiqueta) {
        boolean exito = false;
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        boolean existeArco = false;
        if (nOrigen != null && nDestino != null) {
            NodoAdy<T, E> ady = nOrigen.getPrimerAdy();
            while (!existeArco && ady != null) {
                if (ady.getVertice().getElem().equals(destino)) {
                    existeArco = true;
                }
                ady = ady.getSigAdyacente();
            }
            if (!existeArco) {
                this.enlazarAdyacente(nOrigen, nDestino, etiqueta);
                this.enlazarAdyacente(nDestino, nOrigen, etiqueta); // no dirigido
                exito = true;
            }
        }
        return exito;
    }

    /**
     * Elimina el arco entre origen y destino (en ambas direcciones).
     * @param origen nombre de la elem de origen del arco a eliminar
     * @param destino nombre de la elem de destino del arco a eliminar
     * @return true si se eliminó, false en caso contrario
     */
    public boolean eliminarArco(T origen, T destino) {
        boolean exito = false;
        NodoVert<T, E> auxO = null;
        NodoVert<T, E> auxD = null;
        NodoVert<T, E> aux = this.inicio;
        
        while((auxO == null || auxD == null) && aux != null) {
            // * recorre la lista de vértices una sola vez hasta determinar si existen ambos
            // * porque es más eficiente que llamar a ubicarVertice() para ambos objetos
            if (aux.getElem().equals(origen)) auxO = aux;
            if (aux.getElem().equals(destino)) auxD = aux;
            aux = aux.getSigVertice();
        }
        
        if (auxO != null && auxD != null) {
            this.desenlazarAdyacente(auxO, auxD);
            this.desenlazarAdyacente(auxD, auxO);
            exito = true;
        }
        return exito;
    }

    /**
     * Devuelve true si existe al menos un arco directo entre origen y destino.
     */
    public boolean existeArco(T origen, T destino) {
        boolean existe = false;
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        if (nOrigen != null) {
            NodoAdy<T, E> ady = nOrigen.getPrimerAdy();
            while (ady != null && !existe) {
                if (ady.getVertice().getElem().equals(destino)) {
                    existe = true;
                }
                ady = ady.getSigAdyacente();
            }
        }
        return existe;
    }

    public boolean esVacio() {
        return this.inicio == null;
    }

    /**
     * Devuelve el objeto T almacenado en el vértice igual a 'elem'. Útil para modificar atributos
     * de elem (alojamiento, esSede) sin eliminar y reinsertar el vértice. Devuelve null si no
     * existe el vértice.
     * @param elem la elem a obtener
     * @return objeto T almacenado en el vértice
     */
    public T getVertice(T elem) {
        T resultado = null;
        NodoVert<T, E> nodo = this.ubicarVertice(elem);
        if (nodo != null) {
            resultado = nodo.getElem();
        }
        return resultado;
    }

    //  Recorridos

    /**
     * Recorrido en PROFUNDIDAD (DFS) sobre todo el grafo.
     * Devuelve Lista con los elem de cada vértice en orden de visita.
     */
    public Lista listarEnProfundidad() {
        Lista visitados = new Lista();
        // define un vértice donde comenzar a recorrer
        NodoVert<T, E> aux = this.inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                // si el vértice no fue visitado aún, avanza en profundidad
                this.listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert<T, E> n, Lista vis) {
        if (n != null) {
            // marca al vértice n como visitado
            vis.insertar(n.getElem(), vis.longitud() + 1);
            NodoAdy<T, E> ady = n.getPrimerAdy();
            while (ady != null) {
                // visita en profundidad los adyacentes de n aún no visitados
                if (vis.localizar(ady.getVertice().getElem()) < 0) {
                    this.listarEnProfundidadAux(ady.getVertice(), vis);
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    /**
     * Recorrido en ANCHURA (BFS) sobre todo el grafo.
     * Devuelve Lista con los elem de cada vértice en orden de visita.
     * Usa el TDA Cola propio para gestionar el orden de exploración.
     */
    public Lista listarEnAnchura() {
        Lista visitados = new Lista();
        NodoVert<T, E> aux = this.inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                this.anchuraAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void anchuraAux(NodoVert<T, E> nodoInicio, Lista visitados) {
        Cola cola = new Cola();
        visitados.insertar(nodoInicio.getElem(), visitados.longitud() + 1);
        cola.poner(nodoInicio);
        while (!cola.esVacia()) {
            @SuppressWarnings("unchecked") // Suprimo este warning porque estoy seguro que estoy encolando instancias de NodoVert
            // Si la clase Cola fuera genérica (<T>), no haría falta este cast ni se lanzaría el warning en el IDE
            NodoVert<T, E> actual = (NodoVert<T, E>) cola.obtenerFrente();
            cola.sacar();
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null) {
                if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                    visitados.insertar(ady.getVertice().getElem(), visitados.longitud() + 1);
                    cola.poner(ady.getVertice());
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    //  Existencia de camino

    /**
     * Devuelve true si existe al menos un camino de origen a destino
     * (directo o con escalas). Usa DFS.
     */
    public boolean existeCamino(T origen, T destino) {
        boolean existe = false;
        // verifica si ambos vértices existen
        NodoVert<T, E> auxO  = null;
        NodoVert<T, E> auxD = null;
        NodoVert<T, E> aux = this.inicio;

        while((auxO == null || auxD == null) && aux != null) {
            if (aux.getElem().equals(origen)) auxO = aux;
            if (aux.getElem().equals(destino)) auxD = aux;
            aux = aux.getSigVertice();
        }

        if (auxO != null && auxD != null) {
            // si ambos vértices existen, busca si existe camino entre ambos
            Lista visitados = new Lista();
            existe = this.existeCaminoAux(auxO, destino, visitados);
        }
        return existe;
    }

    private boolean existeCaminoAux(NodoVert<T, E> actual, T destino, Lista vis) {
        boolean encontrado = false;
        if (actual != null) {
            // si vértice actual es el destino: ¡hay camino!
            if (actual.getElem().equals(destino)) {
                encontrado = true;
            } else {
                // si no es el destino, verifica si hay camino entre n y destino
                vis.insertar(actual.getElem(), vis.longitud() + 1);
                NodoAdy<T, E> ady = actual.getPrimerAdy();
                while (ady != null && !encontrado) {
                    // si el nodo adyacente no fue visitado, llama recursivamente con adyacente
                    if (vis.localizar(ady.getVertice().getElem()) < 0) {
                        encontrado = this.existeCaminoAux(ady.getVertice(), destino, vis);
                    }
                    ady = ady.getSigAdyacente();
                }
            }
        }
        return encontrado;
    }

    //  TPO consigna 6.a — CAMINO CON MENOS ESCALAS (BFS) (menos vértices)
    // TODO: probar implementar camino menos escalas con DFS en lugar de recorrido en anchura

    /**
     * Devuelve el camino de A a B que pasa por la mínima cantidad de vértices.
     *
     * Algoritmo: BFS — el primer camino completo encontrado en BFS es el de menos saltos, por la
     * propiedad de exploración nivel a nivel.
     *
     * La Cola guarda objetos Lista, donde cada Lista es el camino recorrido hasta ese punto. Cuando
     * se llega al destino se devuelve esa Lista.
     *
     * Devuelve Lista vacía si algún vértice no existe o no hay camino.
     */
    public Lista caminoMenosVertices(T origen, T destino) {
        Lista resultado = new Lista();
        NodoVert<T, E> nOrigen = ubicarVertice(origen);
        NodoVert<T, E> nDestino = ubicarVertice(destino);
        
        if (nOrigen != null && nDestino != null) {
            Lista visitados = new Lista();
            visitados.insertar(origen, 1);

            // La Cola almacena Listas; cada Lista es un camino parcial
            Cola colaCaminos = new Cola();
            Lista caminoInicial = new Lista();
            caminoInicial.insertar(origen, 1);
            colaCaminos.poner(caminoInicial);

            while (!colaCaminos.esVacia() && resultado.esVacia()) {
                Lista caminoActual = (Lista) colaCaminos.obtenerFrente();
                colaCaminos.sacar();

                int longCamino = caminoActual.longitud(); // evitar llamadas repetidas
                // Último elemento del camino parcial = vértice actual
                @SuppressWarnings("unchecked") // Suprimo este warning porque estoy seguro que estoy
                                               // encolando objetos T (Ciudad en TPO)
                T ultimoElem = (T) caminoActual.recuperar(longCamino);
                NodoVert<T, E> nActual = this.ubicarVertice(ultimoElem);

                NodoAdy<T, E> ady = nActual.getPrimerAdy();
                while (ady != null && resultado.esVacia()) {
                    T elemAdy = ady.getVertice().getElem();
                    if (visitados.localizar(elemAdy) < 0) {
                        Lista nuevoCamino = caminoActual.clone();
                        nuevoCamino.insertar(elemAdy, nuevoCamino.longitud() + 1);
                        if (elemAdy.equals(destino)) {
                            resultado = nuevoCamino; // primer camino completo en BFS
                        } else {
                            visitados.insertar(elemAdy, visitados.longitud() + 1);
                            colaCaminos.poner(nuevoCamino);
                        }
                    }
                    ady = ady.getSigAdyacente();
                }
            }
        }

        return resultado;
    }

    //  TPO consigna 6.b — CAMINO DE MENOR TIEMPO (DFS + backtracking)

    /**
     * Devuelve el camino de A a B con el menor tiempo total de vuelo
     * (suma de etiquetas de los arcos recorridos).
     *
     * Algoritmo: DFS con backtracking.
     *   - 'caminoActual' cumple dos roles: lleva el recorrido construido y sirve
     *     como lista de visitados para evitar ciclos (localizar() sobre ella).
     *   - 'menosPeso[0]' es la referencia mutable al mejor tiempo hallado.
     *   - Poda: si el tiempo acumulado ya iguala o supera el mejor conocido,
     *     no se sigue explorando esa rama.
     *   - Al llegar al destino, mejorCamino.copiarDesde(caminoActual) actualiza
     *     el resultado en O(n) en lugar del for con recuperar(i) que era O(n²).
     * Devuelve Lista vacía si no hay camino o algún vértice no existe.
     */
    public Lista caminoMasLiviano(T origen, T destino) {
        Lista mejorCamino = new Lista();
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            Lista caminoActual = new Lista();
            caminoActual.insertar(origen, 1);
            int[] menosPeso = {Integer.MAX_VALUE}; // puede representar tiempo/longitud/etc
            this.caminoMasLivianoAux(nOrigen, nDestino, caminoActual, 0, menosPeso, mejorCamino);
        }
        return mejorCamino;
    }

    private void caminoMasLivianoAux(NodoVert<T, E> actual, NodoVert<T, E> destino, Lista caminoActual,
            int pesoAcum, int[] menosPeso, Lista mejorCamino) {
        if (actual == destino) { // comparo por referencia O(1) en lugar de usar equals()
            menosPeso[0] = pesoAcum;
            // clone() es O(n) y evita el for de copia que era O(n) con longitud() O(n) adentro = O(n²)
            mejorCamino.copiarDesde(caminoActual);
        } else {
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null) {
                T elemAdy = ady.getVertice().getElem();
                int pesoAdy = (Integer) ady.getEtiqueta();
                // Orden de condiciones deliberado: de menor a mayor costo.
                // 1. Poda de peso: O(1). Descarta ramas que no pueden mejorar el mejor camino.
                // Si falla, se evita pagar el costo de localizar().
                // 2. localizar() sobre caminoActual para evitar ciclos: O(n).
                if ((pesoAcum + pesoAdy) < menosPeso[0] && caminoActual.localizar(elemAdy) < 0) {
                    caminoActual.insertar(elemAdy, caminoActual.longitud() + 1);
                    this.caminoMasLivianoAux(ady.getVertice(), destino, caminoActual, (pesoAcum + pesoAdy),
                            menosPeso, mejorCamino);
                    caminoActual.eliminar(caminoActual.longitud()); // backtracking
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    //  TPO consigna 6.c (*) — MENOR TIEMPO SIN PASAR POR VÉRTICE C
    /**
     ** Camino de menor tiempo de A a B que no pase por el vértice C.
     *
     * Devuelve Lista vacía si no hay camino que evite C, o si algún vértice no existe.
     */
    public Lista caminoMasLivianoSinPasar(T origen, T destino, T verticeEvitar) {
        Lista mejorCamino = new Lista();
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            Lista caminoActual = new Lista();
            caminoActual.insertar(origen, 1);
            int[] menosPeso = {Integer.MAX_VALUE};
            this.caminoMasLivianoSinPasarAux(nOrigen, nDestino, caminoActual, 0, menosPeso,
                    mejorCamino, verticeEvitar);
        }
        return mejorCamino;
    }

    private void caminoMasLivianoSinPasarAux(NodoVert<T, E> actual, NodoVert<T, E> destino,
            Lista caminoActual, int pesoAcum, int[] menosPeso, Lista mejorCamino, T verticeEvitar) {
        if (actual == destino) {
            // La poda antes del llamado recursivo asegura que al llegar acá, pesoAcum es
            // menor a menosPeso[0], por lo que la comparación es redundante y se omite.
            menosPeso[0] = pesoAcum;
            mejorCamino.copiarDesde(caminoActual); // O(n), sin usar una lista auxiliar
        } else {
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null) {
                T elemAdy = ady.getVertice().getElem();
                int pesoAdy = (Integer) ady.getEtiqueta();
                // Orden de condiciones deliberado: de menor a mayor costo.
                // 1. Descarte directo del vértice a evitar: O(1).
                // 2. Poda de peso: O(1). Descarta ramas que no pueden mejorar el mejor camino.
                // Si alguna de las dos anteriores falla, se evita pagar el costo de localizar().
                // 3. localizar() sobre caminoActual para evitar ciclos: O(n).
                if (!elemAdy.equals(verticeEvitar) && (pesoAcum + pesoAdy) < menosPeso[0]
                        && caminoActual.localizar(elemAdy) < 0) {
                    caminoActual.insertar(elemAdy, caminoActual.longitud() + 1);
                    this.caminoMasLivianoSinPasarAux(ady.getVertice(), destino, caminoActual,
                            pesoAcum + pesoAdy, menosPeso, mejorCamino, verticeEvitar);
                    caminoActual.eliminar(caminoActual.longitud()); // backtracking
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    //  TPO consigna 6.d (*) — TODOS LOS CAMINOS DE A A B

    /**
     * (*) Devuelve TODOS los caminos simples (sin ciclos) de A a B.
     * El resultado es una Lista de Listas: cada elemento es un camino completo.
     *
     * Uso en el TPO: obtener todos los caminos y luego filtrar los que tienen
     * alojamiento en alguna ciudad intermedia o en el destino.
     *
     * Devuelve Lista vacía si no hay caminos o algún vértice no existe.
     */
    public Lista todosLosCaminos(T origen, T destino) {
        Lista todos = new Lista();
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            Lista caminoActual = new Lista();
            caminoActual.insertar(origen, 1);
            this.todosLosCaminosAux(nOrigen, nDestino, caminoActual, todos);
        }
        return todos;
    }

    private void todosLosCaminosAux(NodoVert<T, E> actual, NodoVert<T, E> destino, Lista caminoActual,
            Lista todos) {
        if (actual == destino) {
            todos.insertar(caminoActual.clone(), todos.longitud() + 1);
        } else {
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null) {
                T elemAdy = ady.getVertice().getElem();
                if (caminoActual.localizar(elemAdy) < 0) {
                    caminoActual.insertar(elemAdy, caminoActual.longitud() + 1);
                    this.todosLosCaminosAux(ady.getVertice(), destino, caminoActual, todos);
                    caminoActual.eliminar(caminoActual.longitud()); // backtracking
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    //  toString() — consigna 8 del TPO (debugging)

    // /**
    //  * Muestra todas las ciudades y sus rutas con el tiempo en minutos.
    //  * Ejemplo:
    //  *   MIAMI [SEDE] [Alojamiento] --> [90 min → LAS VEGAS] [320 min → LOS ANGELES]
    //  */
    // @Override
    // public String toString() {
    //     StringBuilder sb = new StringBuilder();
    //     if (this.inicio == null) {
    //         sb.append("Grafo vacío\n");
    //     } else {
    //         sb.append("=== Mapa de Ciudades (Grafo) ===\n");
    //         NodoVert<T, E> nv = this.inicio;
    //         while (nv != null) {
    //             sb.append(nv.getElem().toString()).append(" --> ");
    //             NodoAdy<T, E> ady = nv.getPrimerAdy();
    //             if (ady == null) {
    //                 sb.append("[sin rutas]");
    //             }
    //             while (ady != null) {
    //                 sb.append("[")
    //                   .append(ady.getEtiqueta())
    //                   .append(" min → ")
    //                   .append(ady.getVertice().getElem().toString())
    //                   .append("] ");
    //                 ady = ady.getSigAdyacente();
    //             }
    //             sb.append("\n");
    //             nv = nv.getSigVertice();
    //         }
    //     }
    //     return sb.toString();
    // }

    //  toString() — Versión mejorada para el TPO (debugging y visualización)

    /**
     * Muestra todas las ciudades y sus rutas aéreas en un formato jerárquico y legible.
     * Cada ciudad se lista como un encabezado, y sus rutas salientes se muestran
     * indentadas, una por línea, con el tiempo de vuelo y el destino.
     * También indica si la ciudad es sede y si tiene alojamiento.
     *
     * Ejemplo de salida:
     * === Mapa de Ciudades (Grafo) ===
     * ATLANTA [SEDE] [Alojamiento]
     *   --> [115 min → CHICAGO]
     *   --> [130 min → DALLAS]
     *   --> [85 min → ORLANDO]
     *   --> [75 min → CHARLOTTE]
     *   --> [110 min → MIAMI]
     *   --> [130 min → AUSTIN]
     *
     * AUSTIN [SEDE] [Alojamiento]
     *   --> [50 min → DALLAS]
     *
     * CHICAGO [Alojamiento]
     *   --> [165 min → DENVER]
     *   --> [240 min → SEATTLE]
     *   --> [145 min → BOSTON]
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.inicio == null) {
            sb.append("Grafo vacío\n");
        } else {
            sb.append("=== Mapa de Ciudades (Grafo) ===\n");
            NodoVert<T, E> verticeActual = this.inicio;

            // Recorremos todos los vértices (ciudades) del grafo
            while (verticeActual != null) {
                T elementoCiudad = verticeActual.getElem(); // Asumo que T es tu clase Ciudad

                // --- 1. Mostrar la ciudad con sus propiedades ---
                // Aprovechamos el toString() de Ciudad para mostrar los tags de SEDE/ALOJAMIENTO.
                sb.append(elementoCiudad.toString()).append("\n");

                // --- 2. Mostrar las rutas (adyacentes) indentadas ---
                NodoAdy<T, E> adyacenteActual = verticeActual.getPrimerAdy();

                if (adyacenteActual == null) {
                    sb.append("\t[sin rutas salientes]\n");
                } else {
                    while (adyacenteActual != null) {
                        // Indentación con tabulador para claridad visual
                        sb.append("\t--> [")
                        .append(adyacenteActual.getEtiqueta()) // El peso (minutos)
                        .append(" min → ")
                        .append(adyacenteActual.getVertice().getElem().toString()) // Nombre ciudad destino
                        .append("]\n");

                        adyacenteActual = adyacenteActual.getSigAdyacente();
                    }
                }
                // Línea en blanco opcional para separar ciudades visualmente
                // sb.append("\n");

                // Avanzamos al siguiente vértice en la lista
                verticeActual = verticeActual.getSigVertice();
            }
        }
        return sb.toString();
    }
}