# Día 8: Zona de Juegos - Parte B

## Enunciado

Los elfos no tienen suficientes cables de extensión. Debes continuar conectando pares de cajas hasta que todas estén en un solo circuito grande. Encuentra qué par de cajas conectas al final y multiplica sus coordenadas X.

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Factory Method**
-   **Fluent API**
-   **Records** para value objects

## Estructuras de datos

Estructuras compartidas:

-   `List<Position>` (3D)
-   `List<JunctionBoxPair>`
-   CircuitSet para gestión de conexiones

## Algoritmos aplicados

Mismos algoritmos:

-   Generación de pares O(n²)
    -Ordenamiento por distancia
-   Algoritmo greedy para selección

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura compartida con la Parte A.

## Datos Interesantes

### Código Reutilizado

Implementación idéntica, variando parámetros o criterios de cálculo final.
