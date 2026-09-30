# Día 10: Fábrica - Parte B

## Principios SOLID

### S - Single Responsibility

`PatternInfo` es un buen ejemplo de responsabilidad única: solo empaqueta el resultado, la paridad y el coste de aplicar una combinación de botones, sin ninguna lógica adicional. `Machine` vuelve a concentrar varias responsabilidades como en la parte A: parsea la entrada (`parseButton`, `parseJoltage`), precalcula todas las combinaciones de botones agrupadas por paridad (`buildParityMaps`, `computePattern`) y resuelve la búsqueda recursiva memoizada (`findMinimumPresses`, `reduceState`). Son tres bloques de lógica muy distintos en una sola clase.

### D - Dependency Inversion

`buildParityMaps` devuelve un `Map<List<Integer>, Map<List<Integer>, Integer>>` y `findMinimumPresses` recibe la caché como `Map<List<Integer>, Long>`: siempre se trabaja contra la interfaz `Map`/`List` del JDK, nunca contra `HashMap`/`ArrayList` directamente, igual que ocurre con el mapa de memoización de `ServerRack` en el día 11 (parte b).

### No se aplica de forma clara en este día

-   **O (Open/Closed)**: la técnica "meet in the middle" está totalmente codificada dentro de `Machine`; no hay ningún punto para extenderla sin tocar la clase.
-   **L (Liskov Substitution)**: no hay jerarquías de tipos.
-   **I (Interface Segregation)**: no se definen interfaces propias.

## Enunciado

Ahora debes configurar los niveles de voltaje (ignorando las luces indicadoras). Cada máquina tiene contadores de voltaje que comienzan en 0. Al presionar un botón, incrementas en 1 los contadores especificados. Debes encontrar el mínimo número total de presiones de botones necesarias para configurar los contadores de voltaje de todas las máquinas a los valores requeridos.

## Patrones de diseño

Mismos patrones que Parte A:

-   **Factory Method**
-   **Clean Code**
-   **Immutability** con records

## Estructuras de datos

Estructuras compartidas:

-   `Set<Button>` para buttons
-   `Set<Integer>` para estados de luces
-   `Set<Set<Integer>>` para visited states
-   Regex patterns para parsing

## Algoritmos aplicados

Mismo **BFS recursivo** para búsqueda de camino más corto en espacio de estados.

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura completamente compartida con Parte A, demostrando que el algoritmo BFS es suficientemente genérico.

## Datos Interesantes

### Mismo Código BFS

El algoritmo BFS funciona para ambas partes, validando que la representación de estado como Set es la correcta abstracción.
