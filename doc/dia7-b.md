# Día 7: Laboratorios - Parte B

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
