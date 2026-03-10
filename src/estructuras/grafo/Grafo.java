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
 *  caminoMenosEscalas(T origen, T destino)         → Lista  [mínima cantidad de ciudades]
 *  caminoMenosMinutos(T origen, T destino)         → Lista  [menor tiempo de vuelo]
 *  caminoMenosMinutosSinPasar(T, T, T evitar)      → Lista  [(*) sin pasar por ciudad C]
 *  todosLosCaminos(T origen, T destino)            → Lista  [(*) todos los caminos]
 *
 * Debug:
 *  toString()                                      → String
 *
 * @param <T> Tipo de elemento en los vértices (debe implementar equals y Comparable)
 * @param <E> Tipo de etiqueta en los arcos (debe implementar Comparable, ej. Integer p/ minutos)
 */
public class Grafo<T extends Comparable<T>, E extends Comparable<E>> {

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
        if (origen.getPrimerAdy() == null) {
            origen.setPrimerAdy(nuevo);
        } else {
            NodoAdy<T, E> aux = origen.getPrimerAdy();
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
        // la comparación por referencia es intencional: el NodoVert destino viene siempre
        // de ubicarVertice(), que devuelve la referencia exacta al nodo del grafo.
        while (actual != null && !actual.getVertice().equals(destino)) {
            anterior = actual;
            actual = actual.getSigAdyacente();
        }
        if (actual != null) {
            if (anterior == null) {
                origen.setPrimerAdy(actual.getSigAdyacente());
            } else {
                anterior.setSigAdyacente(actual.getSigAdyacente());
            }
        }
    }

    //  Operaciones básicas

    /**
     * Inserta 'ciudad' como nuevo vértice si no existe ya uno igual.
     * 
     * @param ciudad el elemento a insertar en el vértice
     * @return true si se insertó correctamente, false si ya existía un vértice igual
     */
    public boolean insertarVertice(T ciudad) {
        boolean exito = false;
        if (this.ubicarVertice(ciudad) == null) {
            this.inicio = new NodoVert<>(ciudad, this.inicio);
            exito = true;
        }
        return exito;
    }

    /**
     * Elimina el vértice igual a 'ciudad' y todos los arcos que lo involucran.
     * Paso 1: recorre todos los demás vértices y les quita el arco hacia 'ciudad'.
     * Paso 2: desconecta el propio NodoVert de la lista de vértices.
     * @param ciudad el elemento del vértice a eliminar
     * @return true si se eliminó correctamente, false si no existía el vértice
     */
    public boolean eliminarVertice(T ciudad) {
        boolean exito = false;
        NodoVert<T, E> aBorrar = this.ubicarVertice(ciudad);
        if (aBorrar != null) {
            // Paso 1 — eliminar arcos entrantes desde el resto de vértices
            NodoVert<T, E> aux = this.inicio;
            while (aux != null) {
                if (!aux.equals(aBorrar)) {
                    this.desenlazarAdyacente(aux, aBorrar);
                }
                aux = aux.getSigVertice();
            }
            // Paso 2 — quitar el nodo de la lista de vértices
            if (this.inicio.equals(aBorrar)) {
                this.inicio = this.inicio.getSigVertice();
            } else {
                NodoVert<T, E> prev = this.inicio;
                while (prev.getSigVertice() != null && !prev.getSigVertice().equals(aBorrar)) {
                    prev = prev.getSigVertice();
                }
                if (prev.getSigVertice() != null) {
                    prev.setSigVertice(aBorrar.getSigVertice());
                }
            }
            exito = true;
        }
        return exito;
    }

    /**
     * Devuelve true si existe un vértice con elem igual a 'ciudad'.
     * @param ciudad el elemento a buscar en los vértices
     * @return true si existe el vértice, false en caso contrario.
     */
    public boolean existeVertice(T ciudad) {
        return this.ubicarVertice(ciudad) != null;
    }

    /**
     * Inserta un arco NO dirigido entre origen y destino con la etiqueta dada. Crea A→B y B→A con
     * la misma etiqueta. Se evita que se inserten arcos duplicados, dado que el dominio menciona
     * más aerolíneas, tipos de vuelo o frecuencias entre las mismas ciudades.
     * 
     * @param origen ciudad de origen del arco
     * @param destino ciudad de destino del arco
     * @param etiqueta tiempo de vuelo directo en minutos (debe ser >= 0)
     * @return true si se insertó correctamente, false si no existen los vértices o si ya existía el
     *         arco
     */
    public boolean insertarArco(T origen, T destino, E etiqueta) {
        boolean exito = false;
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null && !this.existeArco(origen, destino)) {
            this.enlazarAdyacente(nOrigen, nDestino, etiqueta);
            this.enlazarAdyacente(nDestino, nOrigen, etiqueta); // no dirigido
            exito = true;
        }
        return exito;
    }

    /**
     * Elimina el arco entre origen y destino (en ambas direcciones).
     * @param origen nombre de la ciudad de origen del arco a eliminar
     * @param destino nombre de la ciudad de destino del arco a eliminar
     * @return true si se eliminó, false en caso contrario
     */
    public boolean eliminarArco(T origen, T destino) {
        boolean exito = false;
        NodoVert<T, E> nOrigen  = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            this.desenlazarAdyacente(nOrigen,  nDestino);
            this.desenlazarAdyacente(nDestino, nOrigen);
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
     * Devuelve el objeto T almacenado en el vértice igual a 'ciudad'. Útil para modificar atributos
     * de Ciudad (alojamiento, esSede) sin eliminar y reinsertar el vértice. Devuelve null si no
     * existe el vértice.
     * @param ciudad la ciudad a obtener
     * @return objeto T almacenado en el vértice
     */
    public T getVertice(T ciudad) {
        T resultado = null;
        NodoVert<T, E> nodo = this.ubicarVertice(ciudad);
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
        NodoVert<T, E> aux = this.inicio;
        while (aux != null) {
            if (visitados.localizar(aux.getElem()) < 0) {
                this.listarEnProfundidadAux(aux, visitados);
            }
            aux = aux.getSigVertice();
        }
        return visitados;
    }

    private void listarEnProfundidadAux(NodoVert<T, E> n, Lista visitados) {
        visitados.insertar(n.getElem(), visitados.longitud() + 1);
        NodoAdy<T, E> ady = n.getPrimerAdy();
        while (ady != null) {
            if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                this.listarEnProfundidadAux(ady.getVertice(), visitados);
            }
            ady = ady.getSigAdyacente();
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
        NodoVert<T, E> nOrigen  = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            Lista visitados = new Lista();
            existe = this.existeCaminoAux(nOrigen, destino, visitados);
        }
        return existe;
    }

    private boolean existeCaminoAux(NodoVert<T, E> actual, T destino, Lista visitados) {
        boolean encontrado = false;
        if (actual.getElem().equals(destino)) {
            encontrado = true;
        } else {
            visitados.insertar(actual.getElem(), visitados.longitud() + 1);
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null && !encontrado) {
                if (visitados.localizar(ady.getVertice().getElem()) < 0) {
                    encontrado = this.existeCaminoAux(ady.getVertice(), destino, visitados);
                }
                ady = ady.getSigAdyacente();
            }
        }
        return encontrado;
    }

    //  TPO consigna 6.a — CAMINO CON MENOS ESCALAS (BFS)

    /**
     * Devuelve el camino de A a B que pasa por la mínima cantidad de ciudades.
     *
     * Algoritmo: BFS — el primer camino completo encontrado en BFS es el de menos saltos, por la
     * propiedad de exploración nivel a nivel.
     *
     * La Cola guarda objetos Lista, donde cada Lista es el camino recorrido hasta ese punto. Cuando
     * se llega al destino se devuelve esa Lista.
     *
     * Devuelve Lista vacía si algún vértice no existe o no hay camino.
     */
    public Lista caminoMenosEscalas(T origen, T destino) {
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

                // Último elemento del camino parcial = ciudad actual
                @SuppressWarnings("unchecked") // Suprimo este warning porque estoy seguro que estoy
                                               // encolando objetos T (Ciudad en TPO)
                T ultimoElem = (T) caminoActual.recuperar(caminoActual.longitud());
                NodoVert<T, E> nActual = this.ubicarVertice(ultimoElem);

                NodoAdy<T, E> ady = nActual.getPrimerAdy();
                while (ady != null) {
                    T elemAdy = ady.getVertice().getElem();
                    if (visitados.localizar(elemAdy) < 0) {
                        Lista nuevoCamino = caminoActual.clone();
                        nuevoCamino.insertar(elemAdy, nuevoCamino.longitud() + 1);
                        if (elemAdy.equals(destino)) {
                            resultado = nuevoCamino; // primer camino completo en BFS
                            break;
                        }
                        visitados.insertar(elemAdy, visitados.longitud() + 1);
                        colaCaminos.poner(nuevoCamino);
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
     *   - 'caminoActual' crece al avanzar y se recorta al retroceder.
     *   - 'menosMinutos[0]' es la referencia mutable al mejor tiempo hallado.
     *   - Poda: si el tiempo acumulado ya iguala o supera el mejor conocido,
     *     no se sigue explorando esa rama.
     *
     * NOTA: E debe ser Integer cuando el grafo se instancia como Grafo<Ciudad, Integer>.
     *
     * Devuelve Lista vacía si no hay camino o algún vértice no existe.
     */
    public Lista caminoMenosMinutos(T origen, T destino) {
        Lista mejorCamino = new Lista();
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            Lista caminoActual = new Lista();
            caminoActual.insertar(origen, 1);
            int[] menosMinutos = {Integer.MAX_VALUE};
            this.caminoMenosMinutosAux(nOrigen, destino, caminoActual, 0, menosMinutos, mejorCamino);
        }
        return mejorCamino;
    }

    private void caminoMenosMinutosAux(NodoVert<T, E> actual, T destino, Lista caminoActual,
            int minutosAcum, int[] menosMinutos, Lista mejorCamino) {
        if (actual.getElem().equals(destino)) {
            if (minutosAcum < menosMinutos[0]) {
                menosMinutos[0] = minutosAcum;
                mejorCamino.vaciar();
                for (int i = 1; i <= caminoActual.longitud(); i++) {
                    mejorCamino.insertar(caminoActual.recuperar(i), i);
                }
            }
        } else {
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null) {
                T elemAdy = ady.getVertice().getElem();
                int tiempoAdy = (Integer) ady.getEtiqueta();
                int nuevoTiempo = minutosAcum + tiempoAdy;
                if (caminoActual.localizar(elemAdy) < 0 && nuevoTiempo < menosMinutos[0]) {
                    caminoActual.insertar(elemAdy, caminoActual.longitud() + 1);
                    this.caminoMenosMinutosAux(ady.getVertice(), destino, caminoActual, nuevoTiempo,
                            menosMinutos, mejorCamino);
                    caminoActual.eliminar(caminoActual.longitud()); // backtracking
                }
                ady = ady.getSigAdyacente();
            }
        }
    }

    //  TPO consigna 6.c (*) — MENOR TIEMPO SIN PASAR POR CIUDAD C

    /**
     * (*) Camino de menor tiempo de A a B que no pase por la ciudad C.
     *
     * Estrategia: se pre-inserta 'ciudadEvitar' en caminoActual antes de
     * comenzar el DFS. El auxiliar nunca agrega un nodo ya presente en
     * caminoActual, por lo que C queda efectivamente bloqueado.
     * El mejorCamino resultante nunca contiene a ciudadEvitar.
     *
     * Devuelve Lista vacía si no hay camino que evite C,
     * o si algún vértice no existe.
     */
    public Lista caminoMenosMinutosSinPasar(T origen, T destino, T ciudadEvitar) {
        Lista mejorCamino = new Lista();
        NodoVert<T, E> nOrigen = this.ubicarVertice(origen);
        NodoVert<T, E> nDestino = this.ubicarVertice(destino);
        if (nOrigen != null && nDestino != null) {
            Lista caminoActual = new Lista();
            caminoActual.insertar(origen, 1);

            // Lista de bloqueados SEPARADA: solo se usa para el chequeo de visita,
            // nunca se copia al resultado. Contiene origen + la ciudad a evitar.
            Lista bloqueados = new Lista();
            bloqueados.insertar(origen, 1);
            bloqueados.insertar(ciudadEvitar, 2);

            int[] menosMinutos = {Integer.MAX_VALUE};
            this.caminoMenosMinutosSinPasarAux(nOrigen, destino, caminoActual, bloqueados, 0,
                    menosMinutos, mejorCamino);
        }
        return mejorCamino;
    }

    private void caminoMenosMinutosSinPasarAux(NodoVert<T, E> actual, T destino, Lista caminoActual,
            Lista bloqueados, int minutosAcum, int[] menosMinutos, Lista mejorCamino) {
        if (actual.getElem().equals(destino)) {
            if (minutosAcum < menosMinutos[0]) {
                menosMinutos[0] = minutosAcum;
                mejorCamino.vaciar();
                for (int i = 1; i <= caminoActual.longitud(); i++) {
                    mejorCamino.insertar(caminoActual.recuperar(i), i);
                }
            }
        } else {
            NodoAdy<T, E> ady = actual.getPrimerAdy();
            while (ady != null) {
                T elemAdy = ady.getVertice().getElem();
                int tiempoAdy = (Integer) ady.getEtiqueta();
                int nuevoTiempo = minutosAcum + tiempoAdy;
                // Chequeo sobre 'bloqueados', no sobre 'caminoActual'
                if (bloqueados.localizar(elemAdy) < 0 && nuevoTiempo < menosMinutos[0]) {
                    caminoActual.insertar(elemAdy, caminoActual.longitud() + 1);
                    bloqueados.insertar(elemAdy, bloqueados.longitud() + 1);
                    this.caminoMenosMinutosSinPasarAux(ady.getVertice(), destino, caminoActual,
                            bloqueados, nuevoTiempo, menosMinutos, mejorCamino);
                    // Backtracking en ambas listas
                    caminoActual.eliminar(caminoActual.longitud());
                    bloqueados.eliminar(bloqueados.longitud());
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
            this.todosLosCaminosAux(nOrigen, destino, caminoActual, todos);
        }
        return todos;
    }

    private void todosLosCaminosAux(NodoVert<T, E> actual, T destino, Lista caminoActual,
            Lista todos) {
        if (actual.getElem().equals(destino)) {
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