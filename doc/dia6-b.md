# Día 6: Compactador de Basura - Parte B

## Enunciado

Los cefalópodos olvidaron explicar que las matemáticas se leen de derecha a izquierda en columnas. Cada número se da en su propia columna con el dígito más significativo arriba y el menos significativo abajo. Debes releer los problemas de derecha a izquierda y calcular el nuevo gran total.

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Factory Method**
-   **Strategy Pattern** con interfaz `OperatorList`
-   **Composition**
-   **Fluent API**

## Estructuras de datos

Estructuras compartidas con la Parte A:

-   `List<ProductList>`
-   `SumList`
-   Stream processing para transformaciones

## Algoritmos aplicados

Mismos algoritmos:

-   Transposición de matriz
-   Clasificación por operador
-   Reduce para agregación

## Interfaces

**Sí se usa la interfaz `OperatorList`** por las mismas razones que la Parte A.

## El por qué de esas elecciones

Arquitectura completamente compartida con la Parte A, demostrando que el patrón Strategy y la interfaz `OperatorList` fueron diseñados correctamente desde el inicio.

## Datos Interesantes

### Mismo Código, Mismas Ventajas

La Parte B hereda todas las ventajas arquitectónicas de la Parte A, incluido el polimorfismo y la extensibilidad del Strategy pattern.

### Validación del Diseño con Interfaces

El hecho de que la misma interfaz funcione para ambas partes valida que la abstracción estaba en el nivel correcto.
