# Día 5 - Parte B

## Principios SOLID

### S - Single Responsibility Principle

`Range.count()` conserva una única responsabilidad (calcular cuántos valores cubre el rango), y el `Collector` construido en `mergingRanges()` aísla en un único punto la lógica de fusión de rangos solapados. `InventoryManagement` sigue mezclando parsing, fusión de rangos y conteo en la misma clase, igual que en la parte A.

### O - Open/Closed Principle

No se aplica de forma clara: la fusión de rangos (`overlaps`, `merge`) está implementada como lógica fija dentro de `InventoryManagement`, sin ningún punto de extensión para aplicar otros criterios de fusión sin modificar la clase.

### L - Liskov Substitution Principle

No se aplica: no hay ninguna jerarquía de tipos en el código de esta parte.

### I - Interface Segregation Principle

No se aplica: no se define ninguna interfaz propia en este día.

### D - Dependency Inversion Principle

No se aplica de forma clara: `InventoryManagement` trabaja directamente con el tipo concreto `Range`, sin depender de ninguna abstracción.

## Enunciado

<!-- Rellenar con el enunciado del problema -->

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Factory Method**
-   **Fluent API**
-   **Enum para estados**
-   **Records para value objects**

## Estructuras de datos

Estructuras compartidas con la Parte A:

-   `List<Range>` para rangos
-   `List<Ingredient>` para ingredientes
-   Streams para procesamiento

## Algoritmos aplicados

Mismos algoritmos de verificación de rangos y clasificación que la Parte A.

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura compartida con la Parte A, demostrando diseño robusto y reutilizable.

## Datos Interesantes

### Reutilización Total

La Parte B reutiliza completamente el diseño de la Parte A, validando las decisiones arquitectónicas iniciales.
