# Día 8: Zona de Juegos - Parte B

## Principios SOLID

### S - Single Responsibility Principle

Igual que en la parte A, `CircuitSet` mantiene una única responsabilidad: fusionar circuitos de cajas conectadas. `ChristmasDecorationManager` añade aquí la lógica de "seguir conectando hasta que quede un único circuito" (`calculate`), pero delega toda la gestión de grupos en `CircuitSet`, sin duplicar esa responsabilidad.

### O - Open/Closed Principle

No se aplica de forma clara, por la misma razón que en la parte A: no hay ningún punto de extensión para variar el criterio de conexión o de parada sin modificar directamente `ChristmasDecorationManager.calculate()`.

### L - Liskov Substitution Principle

No se aplica: no hay ninguna jerarquía de tipos propia en este código.

### I - Interface Segregation Principle

Igual que en la parte A: `JunctionBoxPair implements Comparable<JunctionBoxPair>` ilustra una interfaz mínima y enfocada, aunque proviene de la librería estándar y no es una interfaz propia del diseño.

### D - Dependency Inversion Principle

Igual que en la parte A: `CircuitSet` sigue tipando su campo interno como `List<Set<Position>>`, usando abstracciones de colección en lugar de implementaciones concretas, lo que sigue siendo una aplicación modesta pero real del principio.

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
