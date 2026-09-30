# Día 7: Laboratorios - Parte B

## Principios SOLID

### S - Single Responsibility Principle

Igual que en la parte A, `Beam` conserva una responsabilidad enfocada: ahora, además de las posiciones, lleva la cuenta de cuántos caminos (`pathCounts`) llegan a cada una, pero toda esa lógica sigue perteneciendo al mismo concepto de "estado de un haz en una fila" (ver `generateNextPositions`, `createSplitPositions`, `createStraightPosition`).

### O - Open/Closed Principle

No se aplica de forma clara, por la misma razón que en la parte A: el comportamiento ante un divisor (`isSplitter`) está codificado directamente en `Beam`, sin ninguna abstracción que permita variarlo sin modificar la clase.

### L - Liskov Substitution Principle

`PositionList extends HashSet<Position>` vuelve a ser un ejemplo válido de Liskov, igual que en la parte A: añade `positionExists` sin alterar el contrato heredado de `HashSet`, por lo que sigue siendo sustituible por un `Set<Position>` normal.

### I - Interface Segregation Principle

No se aplica: no hay interfaces propias en este día.

### D - Dependency Inversion Principle

No se aplica de forma clara: `BeamManager` sigue dependiendo directamente de la clase concreta `Beam`.

## Enunciado

Es un manifold de tachyones cuántico: una sola partícula toma ambos caminos (izquierda y derecha) en cada divisor. Según la interpretación de muchos mundos, cada división crea una nueva línea temporal. Debes calcular el número total de líneas temporales activas después de que una sola partícula complete todos sus posibles viajes a través del manifold.

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Singleton** con lazy initialization
-   **Factory Method**
-   **Fluent API**

## Estructuras de datos

Estructuras compartidas con la Parte A:

-   `List<Beam>` para almacenamiento de beams
-   IntStream para procesamiento

## Algoritmos aplicados

Mismos algoritmos de construcción iterativa y procesamiento por niveles.

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura compartida con la Parte A.

## Datos Interesantes

### Mismo Singleton

La Parte B reutiliza el mismo singleton, lo que significa que debe llamarse `resetInstance()` entre partes o usar instancias separadas.
