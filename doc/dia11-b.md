# Día 11: Reactor - Parte B

## Principios SOLID

### S - Single Responsibility

`VisitedSet` tiene una responsabilidad única y bien delimitada: llevar la cuenta de qué dispositivos se han visitado y cuántos de los obligatorios (`dac`, `fft`) ya se han pasado (`isValid`, `requiredCount`, `addNew`). Esto queda separado de la búsqueda en profundidad propiamente dicha, que vive en `ServerRack.pathsTo`/`total`. Es una mejora real de diseño respecto a la parte A, donde esa lógica no estaba extraída.

### D - Dependency Inversion

El campo `mem` de `ServerRack` se declara como `Map<CacheKey, Long>` (abstracción `Map`, no `HashMap`), y `CacheKey` a su vez se apoya en `VisitedSet.requiredCount()` en lugar de comparar el conjunto completo de visitados, reduciendo el acoplamiento de la clave de caché a un detalle interno mínimo y estable.

### No se aplica de forma clara en este día

-   **O (Open/Closed)**: la lista fija de dispositivos obligatorios (`MUST_PASS_DEVICES`) está codificada como constante dentro de `VisitedSet`; para soportar otro conjunto de nodos obligatorios habría que modificar la clase, no extenderla.
-   **L (Liskov Substitution)**: no hay jerarquías de herencia en este código.
-   **I (Interface Segregation)**: no se define ninguna interfaz propia.

## Enunciado

Los elfos han descubierto que el problema está en caminos que pasan por dos dispositivos específicos: `dac` (convertidor digital-analógico) y `fft` (transformada rápida de Fourier). Ahora debes encontrar todos los caminos desde `svr` (server rack) hasta `out` que visiten tanto `dac` como `fft` en cualquier orden.

## Patrones de diseño

Mismos patrones que Parte A:

-   **Factory Method**
-   **Memoization** para optimización
-   **Clean Code**
-   **Records** para value objects

## Estructuras de datos

Estructuras compartidas:

-   `Set<Device>` para dispositivos
-   `Map<Device, Long>` para cache
-   Grafo implícito a través de outputs

## Algoritmos aplicados

Mismo **DFS recursivo con memoization** para conteo de caminos.

Posiblemente con **cache adicional** (`CacheKey`, `VisitedSet`) para optimizaciones más avanzadas.

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura compartida con Parte A.

### Optimizaciones Adicionales

La Parte B introduce `CacheKey` y `VisitedSet`, sugiriendo:

-   Cache más sofisticado
-   Posible tracking de estados visitados
-   Optimizaciones para problemas más grandes

## Datos Interesantes

### Extensión delMemoization

La introducción de estructuras adicionales muestra cómo el patrón Memoization puede extenderse para problemas más complejos.
