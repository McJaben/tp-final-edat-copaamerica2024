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

Todas las estructuras (Grafo, AVL, Lista, Cola, HeapMin) están implementadas desde cero con código propio.

---

## Funcionalidades del menú

```
1.  ABM Ciudades            — Alta, baja y modificación de ciudades y rutas aéreas
2.  ABM Equipos             — Alta, baja y modificación de equipos participantes
3.  Alta de partidos        — Registro de resultados (actualiza estadísticas automáticamente)
4.  Consultas sobre equipos — Info de un país · Equipos en rango alfabético [min, max]
5.  Consultas de partidos   — Historial de enfrentamientos entre dos equipos
6.  Consultas de viajes     — Camino con mínimas escalas (BFS)
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
│   └── sistema/
│       ├── Ciudad.java
│       ├── Equipo.java
│       ├── ClavePartido.java
│       ├── DatosPartido.java
│       ├── CopaAmerica.java
│       ├── CargadorArchivo.java
│       ├── Logger.java
│       └── Menu.java          ← punto de entrada (main)
│
├── cargaInicial.txt           ← dataset con 16 equipos, 32 partidos, 20 ciudades, 40 rutas
├── sistema.log                ← generado en ejecución (estado inicial, operaciones, estado final)
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

> _Próximamente: diagrama del Grafo de ciudades y diagrama del AVL de equipos generados con la carga inicial._

---

## Algoritmos de caminos implementados

**BFS — Mínima cantidad de ciudades**  
La cola gestiona caminos parciales. El primer camino que alcanza el destino es necesariamente el de menos saltos.

**DFS + backtracking — Menor tiempo de vuelo**  
Se exploran todos los caminos simples acumulando tiempos. Una poda descarta ramas cuyo tiempo acumulado ya supera el mejor encontrado.

**DFS con bloqueo preventivo — Evitar ciudad C**  
La ciudad a evitar se pre-inserta en la lista de visitados antes de comenzar el DFS, bloqueándola sin modificar el grafo.

**DFS exhaustivo — Todos los caminos**  
Se guardan clones del camino actual cada vez que se alcanza el destino. El resultado se puede filtrar por alojamiento disponible en ciudades intermedias o destino.

---

## Requisitos

- Java 17 o superior (se usan switch expressions con `->`)
- Sin dependencias externas

---

## Autor

**Benjamín Morales**  
Licenciatura en Ciencias de la Computación · FAI · Universidad Nacional del Comahue  
`benjamin.morales at est.fi.uncoma.edu.ar`