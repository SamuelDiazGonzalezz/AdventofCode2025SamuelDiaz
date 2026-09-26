# Día 9: Cine - Parte B

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
