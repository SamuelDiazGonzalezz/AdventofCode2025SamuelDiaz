# Día 4 - Parte B

## Principios SOLID

### S (Responsabilidad Única)

`PaperRoll` (`dia4/b/PaperRoll.java`) añade el conocimiento de su propia `Position` (`row()`, `col()`) pero mantiene una responsabilidad acotada: gestionar sus vecinos y saber dónde está. `PrintingDepartment` (`dia4/b/PrintingDepartment.java`), en cambio, concentra aún más responsabilidades que en la Parte A: parseo de la entrada, recálculo iterativo del grafo de adyacencias (`processOne`), eliminación de nodos accesibles (`removeAdjacents`), control del bucle de eliminación (`process`, con `Stream.iterate`), conteo de rollos eliminados (`deletedPaperRolls`/`removedPaperRolls`) e impresión por consola (`print()`). Este día ilustra, más que un buen ejemplo positivo, una violación todavía más marcada del SRP en `PrintingDepartment` respecto a la Parte A.

No se aplica de forma clara en este día:

-   **O (Abierto/Cerrado)**: el comportamiento iterativo de eliminación (`process`, `removeAdjacents`, `processOne`) se añadió modificando y ampliando directamente la clase `PrintingDepartment` de la Parte A, sin ninguna abstracción que quedara cerrada a modificación.
-   **L (Sustitución de Liskov)**: no hay herencia en este día.
-   **I (Segregación de Interfaces)**: no se definen interfaces propias.
-   **D (Inversión de Dependencias)**: `PrintingDepartment` sigue dependiendo directamente de la clase concreta `PaperRoll` (`new PaperRoll(position)`), no de ninguna abstracción.

## Enunciado

<!-- Rellenar con el enunciado del problema -->

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Factory Method**
-   **Fluent API**
-   **Immutability** con records
-   **Clean Code**

## Estructuras de datos

Estructuras idénticas a la Parte A:

-   Array 2D para la grid
-   Lists para vecinos
-   Streams para procesamiento

## Algoritmos aplicados

Mismos algoritmos de búsqueda de vecinos y construcción de grafo que la Parte A.

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura compartida con la Parte A, demostrando reutilización efectiva del diseño.

## Datos Interesantes

### Código Reutilizado

La implementación es idéntica a la Parte A, variando solo en los datos de entrada o el criterio de conteo final.
