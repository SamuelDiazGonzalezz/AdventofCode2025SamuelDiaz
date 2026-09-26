# D铆a 3 - Parte B

## Enunciado

Ahora necesitas m醩 voltaje. En lugar de encender exactamente dos bater韆s, debes encender exactamente doce bater韆s dentro de cada banco. El voltaje de salida es el n鷐ero formado por los 12 d韌itos de las bater韆s encendidas. Encuentra el m醲imo voltaje posible de cada banco y suma todos los voltajes.

## Patrones de dise帽o

Mismos patrones que la Parte A:

-   **Factory Method** con `create()`
-   **Fluent API** para method chaining
-   **Immutability** con records
-   **Clean Code** con m茅todos cohesivos

## Estructuras de datos

Estructuras id茅nticas a la Parte A:

-   `List<BatteryBank>` como contenedor
-   `List<Integer>` dentro de cada BatteryBank
-   Stream API para procesamiento funcional

## Algoritmos aplicados

El mismo **algoritmo greedy** que la Parte A para seleccionar y combinar d铆gitos m谩ximos.

## Interfaces

**No se utilizan interfaces** por las mismas razones que la Parte A.

## El por qu茅 de esas elecciones

Las elecciones arquitect贸nicas son id茅nticas a la Parte A.

### Reutilizaci贸n de Dise帽o

La Parte B reutiliza completamente la arquitectura de la Parte A, indicando un dise帽o robusto y gen茅rico.

## Datos Interesantes

### C贸digo Compartido

Ambas partes comparten el mismo c贸digo, sugiriendo que:

-   El problema var铆a en datos de entrada, no en l贸gica
-   El dise帽o inicial previ贸 las necesidades correctamente
-   No se requiri贸 refactoring entre partes

### Validaci贸n del Algoritmo Greedy

Que el algoritmo greedy funcione para ambas partes valida que la estrategia de optimizaci贸n local es correcta para este tipo de problema.

