# Día 9: Cine - Parte B

## Principios SOLID

### S - Single Responsibility

Este día es el mejor ejemplo del lote: `Edge` solo conoce la geometría de un segmento (si corta un rectángulo, si contiene un punto, si un rayo lo cruza), `Rectangle` solo calcula sus propias esquinas y límites (`corners`, `minX`/`maxX`/`minY`/`maxY`), y `Polygon` se limita a orquestar el algoritmo de ray-casting apoyándose en `Edge` (`contains`, `anyEdgeCutsThrough`) sin conocer los detalles de cómo se calcula una intersección. Cada clase tiene un único motivo de cambio.

### D - Dependency Inversion

`TilesManager.rectangles()` filtra los rectángulos con `.filter(polygon::contains)`, es decir, depende únicamente del método público de `Polygon` (su abstracción de cara al exterior) sin conocer cómo implementa el ray-casting internamente. También declara sus colecciones como `List<Position>`/`List<Rectangle>` en vez de tipos concretos.

### No se aplica de forma clara en este día

-   **O (Open/Closed)**: no hay puntos de extensión; añadir un nuevo tipo de comprobación geométrica requeriría modificar `Edge` o `Polygon` directamente.
-   **L (Liskov Substitution)**: no hay jerarquías de herencia ni subtipos sustituibles.
-   **I (Interface Segregation)**: no se define ninguna interfaz propia.

## Enunciado

Ahora el rectángulo solo puede incluir tiles rojos o verdes (no otros tiles). Los tiles rojos de tu lista están conectados por líneas de tiles verdes formando un loop, y todos los tiles dentro del loop también son verdes. Debes encontrar el área del rectángulo más grande que use solo tiles rojos o verdes, con tiles rojos en las esquinas opuestas.

## Patrones de diseño

Mismos patrones que Parte A:

-   **Factory Method**
-   **Fluent API**
-   **Records** para geometría

## Estructuras de datos

Estructuras compartidas:

-   `List<Position>`
-   `List<Rectangle>` (o similar estructura geométrica)
-   Streams para combinaciones

## Algoritmos aplicados

Probablemente algoritmos similares de generación de combinaciones y cálculo geométrico.

## Interfaces

**No se utilizan interfaces**.

## El por qué de esas elecciones

Arquitectura compartida con Parte A para procesamiento geométrico.

## Datos Interesantes

### Posibles Variaciones

La Parte B puede usar polígonos (`Polygon`) o más puntos (`Edge`), sugiriendo extensión de la lógica geométrica.
