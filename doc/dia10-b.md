# Día 10: Fábrica - Parte B

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
