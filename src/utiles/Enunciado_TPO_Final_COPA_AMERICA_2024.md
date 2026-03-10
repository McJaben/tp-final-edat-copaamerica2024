**Trabajo Final de Estructuras de Datos – FAI – UNCOMA**

**COPA AMERICA 2024**

Se desea desarrollar un sistema que brinde información de la **Copa América de Fútbol 2024** a disputarse en EEUU. 

El principal objetivo del sistema es brindar a los hinchas información sobre el evento y sugerir caminos para viajar de una sede a otra a ver a su equipo favorito.

Dado que los partidos se llevarán a cabo en distintas ciudades, se desea que el sistema almacene un mapa de las ciudades de EEUU y de las rutas aéreas entre ellas. Este mapa de ciudades deberá incluir las sedes de los partidos y otras ciudades en las que los pasajeros puedan hacer escala. Para cada ciudad se almacenará un nombre único, si hay alojamiento disponible y si es sede o no de la copa. Para las rutas se almacenará el tiempo de vuelo directo estimado en minutos, entre cada par de ciudades.

Además, se guardará información de los equipos de cada país, incluyendo el nombre del director técnico, el grupo en el que el país juega en primera ronda (A, B, C, D), los puntos ganados y la cantidad de goles a favor y en contra hasta el momento.

Por otro lado, se almacenarán los resultados de los partidos, indicando los dos equipos participantes (equipo1 y equipo2, considerando el orden alfabético de sus nombres, equipo1\<equipo2), ronda por la que se enfrentan (grupo, cuartos, semifinales, final), ciudad del evento, nombre del estadio y la cantidad de goles de cada equipo.

Para la implementación se deberá utilizar un grafo etiquetado para el mapa de las ciudades.  Los equipos de fútbol se guardarán en una tabla de búsqueda implementada con un árbol AVL, ordenada alfabéticamente por el nombre del país. Además se utilizará un mapeo (implementado con hash abierto o HashMap de Java) para almacenar los partidos, donde como dominio se utilizará una clase con los atributos eq1 y eq2 (donde siempre se guardará como eq1 el de nombre menor alfabéticamente que eq2), y como rango los datos de goles de eq1, goles de eq2 y la ronda del partido que disputaron. Atención: dos equipos pueden ser adversarios en más de una oportunidad, en caso de cruzarse en el grupo y luego en instancias finales. 

Desarrollar la clase CopaAmerica2024 con un menú de opciones para realizar las siguientes tareas:

1. Altas, bajas y modificaciones de ciudades  
2. Altas, bajas y modificaciones de equipos  
3. Altas de partidos   
4. Consulta sobre equipos:  
* Dado un país, mostrar puntos ganados, goles a favor y en contra y diferencia de goles  
* Dadas dos cadenas (min y max) devolver todos los equipos cuyo nombre esté alfabéticamente en el rango \[min, max\].  
5. Consultas sobre partidos:  
* Dados 2 equipos, si jugaron algún partido entre sí, mostrar los resultados.


6. Consultas sobre viajes: Dada una ciudad A y una ciudad B:  
* Obtener el camino que llegue de A a B pasando por la mínima cantidad de ciudades  
* Obtener el camino que llegue de A a B de menor tiempo  
* (\*) El camino más corto en minutos de vuelo para llegar de A a B y que no pase por una ciudad C dada  
* (\*) Obtener todos los caminos posibles para llegar de A a B, mostrarlos y luego filtrar y mostrar solo los que haya posibilidad de conseguir alojamiento en la ciudad destino o en alguna ciudad por la que tenga que pasar camino a ella  
7. Generar y mostrar una lista de los equipos ordenados por cantidad de goles a favor (utilice una estructura de datos auxiliar que considere apropiada para asegurar la eficiencia al momento de la consulta. No debe guardar el listado después de mostrarlo porque es una estructura que cambia el orden de acuerdo al momento de la consulta).  
8. Mostrar sistema: es una operación de debugging que permite ver todas las estructuras utilizadas con su contenido (grafo, AVL y Mapeo) para verificar, en cualquier momento de la ejecución del sistema, que se encuentren cargadas correctamente.

**Clases para investigar**

* Leer sobre StringTokenizer o split de String (para fraccionar un String con un carácter separador)   
* Abrir archivo de texto para lectura (FileReader, BufferedReader)  
* Escribir en archivo de texto (FileWritter)

**Requisitos importantes**

* El programa debe permitir la ejecución por separado de cada una de las operaciones especificadas mediante un menú.  
* El programa debe ser eficiente: Debe recorrer todas las estructuras sólo lo necesario y haciendo buen uso de la memoria.  
* Las estructuras deben estar implementadas de forma genérica para elementos de tipo Object o Comparable de Java, según el propósito de la estructura, a menos que se indique lo contrario.  
* La carga inicial del sistema debe hacerse en forma de lote, a partir de un archivo de texto. Por ejemplo, la información se puede ingresar en un único archivo, de a un elemento por línea, indicando el tipo (E:Equipo, P:Partido, C:Ciudad, R:Ruta), o en archivos diferentes para cada colección. 

  FORMATO DE ARCHIVO DE TEXTO PARA LA CARGA (EJEMPLO)

  Equipo: nombre del país, apellido del técnico, grupo inicial (los puntos se cargarán después a partir de los resultados de los partidos)

  E: ARGENTINA; SCALONI; A

  E: CHILE; GARECA; A


  Partido: nombre eq1, nombre eq2, instancia, ciudad, estadio, goles eq1, goles eq2

  P: ARGENTINA; CANADA; GRUPO; ATLANTA; MERCEDES-BENZ; 2; 0

  P: ARGENTINA; CHILE; GRUPO; NEW YORK; METLIFE; 1; 0


  Ciudad: nombre, disponibilidad de alojamiento, sede de la copa

  C: LOS ANGELES; TRUE; FALSE

  C: LAS VEGAS; FALSE; TRUE

  C: MIAMI; TRUE; TRUE


  Ruta: ciudad origen, ciudad destino, tiempo estimado de vuelo directo

  R: LOS ANGELES; LAS VEGAS; 90

  R: LOS ANGELES; MIAMI; 320


PARA LA ENTREGA, LA CARGA INICIAL DEBERÁ CONTAR CON **AL MENOS** 16 EQUIPOS, 20 PARTIDOS, 20 ELEMENTOS DE TIPO CIUDAD Y 40 RUTAS EN EL MAPA.

SE DEBE ASEGURAR QUE EN EL SET DE CARGA INICIAL LOS EQUIPOS SE LISTEN EN FORMA DESORDENADA PARA QUE SE PRODUZCAN **TODAS LAS ROTACIONES POSIBLES** AL INSERTARLOS EN EL AVL.

* Mantener un archivo .LOG (archivo de texto) para guardar la siguiente información:  
  * Estado del sistema al momento de terminar la carga inicial (todas las estructuras)  
  * Listado de operaciones que se realizan a lo largo de la ejecución (Ej: “Se insertó la ciudad X”, “Se eliminó el equipo Y”, “Se cargaron los datos del partido eq1-eq2”, etc)  
  * Estado del sistema al momento de terminar de ejecutarse (todas las estructuras)

**Condiciones y fechas de entrega**

* El programa debe realizarse de manera individual y debe presentarse personalmente a alguno de los docentes que indicará si está aprobado. Una vez aprobado deberá subirlo a la plataforma PEDCO para su archivo, en el curso llamado “para alumnos preparando final”.  
* Al momento de la defensa, deberá presentar un dibujo (en papel o digital) del diagrama del mapa de ciudades (grafo) y de la tabla de equipos (AVL) subido en la carga inicial desde archivo de texto.  
* Los estudiantes que promocionan la materia tendrán tiempo para entregarlo hasta el viernes 2 de agosto de 2024 pero NO DEBEN realizar los módulos marcados con (\*)  
* Los estudiantes que no promocionen podrán entregarlo en cualquier momento, pero como mínimo 2 semanas antes de presentarse a rendir el final regular.

**Material de estudio**

Además de las estructuras vistas hasta el momento, tienen el siguiente material en forma de apuntes y videos, para usar durante el desarrollo del trabajo final:

* [Apunte 5 \- Grafos.pdf](https://pedco.uncoma.edu.ar/pluginfile.php/217771/mod_folder/content/0/Apuntes%20obligatorios/Apunte%205%20-%20Grafos.pdf?forcedownload=1)

  * ### [EDAT \- Video 5.1 \- Grafos (Parte 1\)](https://www.youtube.com/watch?v=xzXYkfM71bA&list=PLEikxmM8Dt1hcl15StVfmPedIjG9LudFg&index=21&pp=iAQB)

  * ### [EDAT \- Video 5.2 \- Grafos (Parte 2\)](https://www.youtube.com/watch?v=Sk6L1ocrJoo&list=PLEikxmM8Dt1hcl15StVfmPedIjG9LudFg&index=22&pp=iAQB)

* [Apunte 6 \- TDAs de Propósito Específico.pdf](https://pedco.uncoma.edu.ar/pluginfile.php/217771/mod_folder/content/0/Apuntes%20obligatorios/Apunte%206%20-%20TDAs%20de%20Prop%C3%B3sito%20Espec%C3%ADfico.pdf?forcedownload=1)

  * ### [EDAT \- Video 6.1 \- TDAs especiales](https://www.youtube.com/watch?v=SbraLQJtdWc&list=PLEikxmM8Dt1hcl15StVfmPedIjG9LudFg&index=23&t=9s&pp=iAQB)

* [Apéndice B \- Cómo implementar estructuras para un tipo de elemento genérico.pdf](https://pedco.uncoma.edu.ar/pluginfile.php/217771/mod_folder/content/0/Apuntes%20obligatorios/Ap%C3%A9ndice%20B%20-%20C%C3%B3mo%20implementar%20estructuras%20para%20un%20tipo%20de%20elemento%20gen%C3%A9rico.pdf?forcedownload=1)

  * ### [EDAT \- Video 4.2 \- Clase especial \- Cómo crear clases comparables](https://www.youtube.com/watch?v=at-qzndfflE&list=PLEikxmM8Dt1hcl15StVfmPedIjG9LudFg&index=16&pp=iAQB)

* [Apéndice C \- Como devolver más de un resultado en un método Java.pdf](https://pedco.uncoma.edu.ar/pluginfile.php/217771/mod_folder/content/0/Apuntes%20opcionales/Apendice%20C%20-%20Como%20devolver%20m%C3%A1s%20de%20un%20resultado%20en%20un%20m%C3%A9todo%20Java.pdf?forcedownload=1)

  * ### [Video extra: estructuras provistas por Java](https://www.youtube.com/watch?v=dhcvV2J3fnA&list=PLEikxmM8Dt1hcl15StVfmPedIjG9LudFg&index=24&pp=iAQB)

* [Apéndice D \- Cómo implementa Java las estructuras de datos vistas en la materia.pdf](https://pedco.uncoma.edu.ar/pluginfile.php/217771/mod_folder/content/0/Apuntes%20opcionales/Ap%C3%A9ndice%20D%20-%20C%C3%B3mo%20implementa%20Java%20las%20estructuras%20de%20datos%20vistas%20en%20la%20materia.pdf?forcedownload=1)  
* PORQUE NUNCA ESTA DE MÁS REPETIRLO  
  [Apéndice A \- Buenas Prácticas de Programación.pdf](https://pedco.uncoma.edu.ar/pluginfile.php/217771/mod_folder/content/0/Apuntes%20obligatorios/Apendice%20A%20-%20Buenas%20Pr%C3%A1cticas%20de%20Programaci%C3%B3n.pdf?forcedownload=1)
