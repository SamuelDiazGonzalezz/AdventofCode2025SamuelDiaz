# Día 1: Entrada Secreta - Parte B

## Enunciado

Ahora el método de contraseña cambia (método 0x434C49434B): debes contar el número de veces que el dial cruza por el 0 durante cualquier rotación, no solo al final de cada una. Por ejemplo, si el dial está en 50 y se hace una rotación R1000, cruzará el 0 diez veces antes de volver a 50. Esto incluye tanto los cruces durante el movimiento como los aterrizajes exactos en 0.

## Patrones de diseño

### Factory Method

Uso del patrón **Factory Method** con `Dial.create()`, manteniendo consistencia con la Parte A.

### Fluent API (Builder Pattern)

Implementación de **Fluent API** para encadenamiento de métodos, mejorando la expresividad del código.

### Immutability

Uso de `record Order(int step)` para objetos inmutables thread-safe.

### Clean Code

-   **Métodos pequeños con responsabilidad única**
-   **Nombres descriptivos** que comunican intención
-   **Encapsulación** de detalles de implementación

## Estructuras de datos

### List<Order>

`ArrayList<Order>` como estructura principal, permitiendo:

-   Acceso secuencial eficiente
-   Operaciones de stream
-   Acceso indexado para el algoritmo `zeros()`

### IntStream

Uso intensivo de `IntStream` para:

-   Generación de rangos con `IntStream.range()`
-   Operaciones map-filter-reduce
-   Procesamiento funcional de índices

## Algoritmos aplicados

### Algoritmo de Cruce de Cero

El algoritmo principal `zeros()` calcula cuántas veces el dial cruza el 0 entre posiciones:

```java
private int zerosBetween(int from, int to) {
    return from < to
        ? Math.floorDiv(to, 100) - Math.floorDiv(from, 100)
        : Math.floorDiv(from - 1, 100) - Math.floorDiv(to - 1, 100);
}
```

Este algoritmo usa **división entera** para contar cuántos múltiplos de 100 se cruzan en un rango, evitando la necesidad de iterar paso a paso.

### Cálculo de Sumas Parciales

`rawSum(index)` calcula la suma acumulada hasta un índice sin normalizar, manteniendo el valor "raw" para el análisis de cruces de cero.

### Diferenciación Direccional

El método `zerosBetween` maneja dos casos:

-   **Movimiento hacia adelante** (`from < to`): Cuenta cruces positivos
-   **Movimiento hacia atrás** (`from > to`): Cuenta cruces negativos ajustando con `- 1`

## Interfaces

**No se utilizan interfaces** por las mismas razones que en la Parte A:

-   Implementación concreta sin necesidad de abstracción
-   No hay variaciones de comportamiento
-   Principio YAGNI

## El por qué de esas elecciones

### Extensión sobre la Parte A

La Parte B **extiende** la funcionalidad de la Parte A agregando el método `zeros()` sin modificar el código existente, demostrando el principio **Open/Closed Principle**.

### División Entera para Eficiencia

En lugar de simular cada paso del dial, se usa `Math.floorDiv()` para calcular matemáticamente los cruces de cero. Esto es:

-   **O(1)** en lugar de O(n) por cada movimiento
-   Más preciso (sin acumulación de errores)
-   Más declarativo (expresa la intención matemática)

### Separación rawSum vs sumAll

-   `rawSum`: Mantiene valores no normalizados para detectar cruces
-   `sumAll`: Aplica normalización módulo 100
-   Esta separación respeta **Single Responsibility Principle**

### Filter con Optional

El uso de:

```java
IntStream.of(orders.get(index).step())
    .filter(step -> step != 0)
    .map(...)
    .findFirst()
    .orElse(0);
```

Maneja elegantemente el caso edge de step == 0 sin condicionales explícitos.

## Datos Interesantes

### Manejo de Movimientos Nulos

El método `zerosAt` filtra pasos de valor 0 antes de calcular cruces, evitando división por cero y casos edge.

### Precisión en Ajustes Direccionales

El ajuste `from - 1` en movimientos hacia atrás es crucial: cuando nos movemos de 100 a 99, no queremos contar el 0 que ya pasamos, solo los nuevos cruces.

### Composición Funcional

La solución compone varias operaciones funcionales (`IntStream.range().map().sum()`) en lugar de bucles imperativos, haciendo el código más testeable y componible.

### Comparación con Parte A

Mientras la Parte A cuenta **aterrizajes exactos** en 0, la Parte B cuenta **cruces** del 0, requiriendo un algoritmo completamente diferente (conteo matemático vs. comparación exacta).
