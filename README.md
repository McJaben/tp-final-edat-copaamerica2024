# 🏆 Copa América 2024 — Sistema de Información

**Trabajo Práctico Final · Estructuras de Datos**  
Licenciatura en Ciencias de la Computación · FAI · Universidad Nacional del Comahue

---

## Descripción

Sistema de información para la Copa América 2024 (EEUU) que permite gestionar equipos, ciudades sede y resultados de partidos. Incluye un motor de consultas de viajes que sugiere rutas aéreas entre ciudades usando algoritmos de búsqueda sobre grafos.

---

## Estructuras de datos utilizadas

| Estructura | Uso en el sistema |
|---|---|
| **Grafo etiquetado** (lista de adyacencia) | Mapa de ciudades y rutas aéreas (etiqueta = minutos de vuelo) |
| **Árbol AVL** | Tabla de equipos, ordenada alfabéticamente por nombre del país |
| **HashMap de Java** | Partidos disputados, indexados por par de equipos |
| **Lista enlazada** | Resultados de partidos por par de equipos; caminos del grafo |
| **Cola** | Auxiliar en BFS (camino con mínimas escalas) |
| **HeapMin** | Auxiliar en ordenamiento de equipos por goles a favor |

Todas las estructuras (Grafo, AVL, Lista, Cola, HeapMin) están implementadas desde cero con código propio. La excepción es el uso de HashMap propio de Java.

---

## Funcionalidades del menú

```
1.  ABM Ciudades            — Alta, baja y modificación de ciudades y rutas aéreas
2.  ABM Equipos             — Alta, baja y modificación de equipos participantes
3.  Alta de partidos        — Registro de resultados (actualiza estadísticas automáticamente)
4.  Consultas sobre equipos — Info de un país · Equipos en rango alfabético [min, max]
5.  Consultas de partidos   — Historial de enfrentamientos entre dos equipos
6.  Consultas de viajes     — Camino con mínimas escalas (DFS + backtracking)
                            — Camino de menor tiempo (DFS + backtracking)
                            — (*) Camino más corto evitando una ciudad
                            — (*) Todos los caminos + filtro por alojamiento disponible
7.  Ranking por goles       — Lista descendente por goles a favor (HeapMin auxiliar)
8.  Mostrar sistema         — Volcado completo de las tres estructuras (debug)
9.  Cargar datos desde archivo
0.  Salir
```

---

## Estructura del proyecto

```
tp-final-edat-copaamerica2024/
│
├── src/
│   ├── estructuras/
│   │   ├── lineales/
│   │   │   ├── Nodo.java
│   │   │   ├── Lista.java
│   │   │   └── Cola.java
│   │   ├── conjuntistas/
│   │   │   ├── NodoABB.java
│   │   │   ├── NodoAVL.java
│   │   │   ├── ArbolAVL.java
│   │   │   └── HeapMin.java
│   │   └── grafo/
│   │       ├── NodoVert.java
│   │       ├── NodoAdy.java
│   │       └── Grafo.java
│   │
│   ├── sistema/
│   |   ├── Ciudad.java
│   |   ├── Equipo.java
│   |   ├── ClavePartido.java
│   |   ├── DatosPartido.java
│   |   ├── CopaAmerica.java
│   |   ├── CargadorArchivo.java
│   |   ├── Logger.java
│   |   └── Menu.java          ← punto de entrada (main)
│   |
|   └── logs/
|       ├── sistema.log        ← generado en ejecución (estado inicial, operaciones, estado final)
|       └── caminos.log        ← generado al mostrar todos los caminos en opción 4 de consulta de viajes
├── cargaInicial.txt           ← dataset con 16 equipos, 32 partidos, 20 ciudades, 40 rutas
└── README.md
```

---

## Carga inicial desde archivo

El sistema lee un único archivo de texto con el siguiente formato (una línea por elemento, separador `;`). Las líneas vacías y las que comienzan con `#` se ignoran.

```
# Equipos: nombre; director técnico; grupo (A/B/C/D)
E: ARGENTINA; SCALONI; A
E: BRASIL; DORIVAL; D

# Ciudades: nombre; tieneAlojamiento; esSede
C: MIAMI; TRUE; TRUE
C: CHICAGO; TRUE; FALSE

# Rutas aéreas: origen; destino; minutos de vuelo
R: MIAMI; ATLANTA; 110
R: ATLANTA; CHICAGO; 115

# Partidos: eq1; eq2; ronda; ciudad; estadio; golesEq1; golesEq2
P: ARGENTINA; CANADA; GRUPO; ATLANTA; MERCEDES_BENZ; 2; 0
```

El conjunto de datos incluido en `cargaInicial.txt` contiene los 32 partidos reales de la Copa América 2024, con equipos listados en orden aleatorio para forzar todas las rotaciones posibles del AVL.

---

## Archivo de log

Cada ejecución genera entradas en `sistema.log` con el siguiente contenido:

- **Estado inicial** — volcado completo de las tres estructuras al terminar la carga
- **Operaciones** — cada alta, baja, modificación y consulta queda registrada con timestamp
- **Estado final** — volcado completo al cerrar el programa

```
============================================================
SESIÓN INICIADA: 2024-07-15 14:32:01
============================================================
[2024-07-15 14:32:01] === INICIO DE CARGA DESDE ARCHIVO: cargaInicial.txt ===
[2024-07-15 14:32:01] Se cargó el equipo: ARGENTINA (DT: SCALONI, Grupo: A)
[2024-07-15 14:32:01] Se cargó la ciudad: MIAMI (Alojamiento: true, Sede: true)
...
```

---

## Diagramas

### Diagrama del grafo de ciudades después de la carga inicial
<img width="1142" height="796" alt="grafo_mapa_ciudades" src="https://github.com/user-attachments/assets/d86333a5-2fb3-4e10-aee9-a828fc189535" />


### Diagrama del árbol AVL de equipos generado con la carga inicial
<img width="664" height="478" alt="avl_equipos" src="https://github.com/user-attachments/assets/7045d3f0-2240-411c-96d7-8a723694da2c" />


---

## Algoritmos de caminos implementados

**DFS + backtracking + poda — Mínima cantidad de ciudades**  
Se exploran todos los caminos simples desde el origen con backtracking. Un contador entero `cantActual` reemplaza las llamadas a `longitud()` para evaluar la poda en O(1). La poda descarta ramas cuya longitud ya iguala o supera el mínimo encontrado. Al llegar al destino se actualiza el mejor camino con `copiarDesde()` en O(n).

**DFS + backtracking + poda — Menor tiempo de vuelo**  
Se exploran todos los caminos simples acumulando tiempos. Una poda descarta ramas cuyo tiempo acumulado ya supera el mejor encontrado. Al llegar al destino se actualiza el mejor camino con `copiarDesde()` en O(n).

**DFS con descarte directo — Evitar ciudad C**  
Igual que el camino de menor tiempo, pero con una condición adicional antes del llamado recursivo: si el adyacente es la ciudad a evitar, se descarta en O(1) sin necesidad de estructuras auxiliares. El grafo no se modifica.

**DFS exhaustivo — Todos los caminos**  
Se guardan clones del camino actual cada vez que se alcanza el destino. El resultado se puede filtrar por alojamiento disponible en ciudades intermedias o destino.

---

## Requisitos

- Java 17 o superior (se usan switch expressions con `->`)
- Sin dependencias externas

---

## Notas o aclaraciones varias
- La mayoría de los TDA se implementaron usando genéricos (`<T>` o `<T extends Comparable<T>>`) para lograr **tipo seguro** y evitar los warnings que los IDEs generan en Java 17+ cuando se usa `Object` o el raw `Comparable` (Ej: _"Comparable is a raw type. References to generic type Comparable should be parameterized"_ o _"Type safety: The method compareTo(Object) belongs to the raw type Comparable. References to generic type Comparable should be parameterized"_).
  - En particular, `ArbolAVL`, `HeapMin` y `Grafo` son genéricos porque necesitan poder comparar y/o almacenar distintos tipos de elementos sin castear.
  - `Lista` y `Cola` mantienen una implementación más simple (basada en `Object`) debido a que así fueron implementadas durante la cursada y se puede ver cómo es necesario realizar casteos en varias partes del sistema por este tipo de implementación.

  Para aprender a implementar TDAs genéricos (sin usar sólo Object o Comparable) consulté recursos como StackOverflow, LLMs y estos videos del canal de YouTube "makigas":
  - [¿Qué es un genérico? (en Java, aunque es igual en todas partes)](https://youtu.be/ai4HH6CI_08?si=QJ5FEdP1eA-f9dfz)
  - [Genéricos en Java: cómo crear tu propia clase](https://youtu.be/OtSd9NCXdGc?si=eNgd8T_zNC1-UT_H)
  - [Genéricos de Java con restricción de tipos](https://youtu.be/Qf2dj-Xxiz8?si=88bAmSkgFmPsLuEm)

## Autor

**Benjamín Morales**  
Licenciatura en Ciencias de la Computación · FAI · Universidad Nacional del Comahue  
`benjamin.morales at est.fi.uncoma.edu.ar`
