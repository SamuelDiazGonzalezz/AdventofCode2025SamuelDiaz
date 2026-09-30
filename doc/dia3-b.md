# Día 3 - Parte B

## Principios SOLID

### S (Responsabilidad Única)

`BatteryBank` (`dia3/b/BatteryBank.java`) mantiene una única responsabilidad —encontrar la mejor combinación de dígitos— repartida en métodos privados muy acotados: `maxCombination` elige recursivamente el índice del siguiente dígito óptimo, `concat` solo combina una cabeza con una cola, y `joinChars` solo convierte la lista final en un número. `BatteryMaximizer` (`dia3/b/BatteryMaximizer.java`) conserva su única responsabilidad de acumular bancos y sumar sus resultados, igual que en la Parte A.

No se aplica de forma clara en este día:

-   **O (Abierto/Cerrado)**: el cambio de "elegir 2 dígitos" (Parte A) a "elegir hasta `MAX_COMBINATION_LENGTH` dígitos mediante recursión" (Parte B) se hizo sustituyendo por completo el método `sum()`/`maxCombination` dentro de la misma clase `BatteryBank`, no extendiéndolo sin modificar código existente.
-   **L (Sustitución de Liskov)**: no hay herencia en este día.
-   **I (Segregación de Interfaces)**: no se definen interfaces propias.
-   **D (Inversión de Dependencias)**: `BatteryMaximizer` sigue dependiendo directamente del tipo concreto `BatteryBank`.

## Enunciado

Ahora necesitas m�s voltaje. En lugar de encender exactamente dos bater�as, debes encender exactamente doce bater�as dentro de cada banco. El voltaje de salida es el n�mero formado por los 12 d�gitos de las bater�as encendidas. Encuentra el m�ximo voltaje posible de cada banco y suma todos los voltajes.

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Factory Method** con `create()`
-   **Fluent API** para method chaining
-   **Immutability** con records
-   **Clean Code** con métodos cohesivos

## Estructuras de datos

Estructuras idénticas a la Parte A:

-   `List<BatteryBank>` como contenedor
-   `List<Integer>` dentro de cada BatteryBank
-   Stream API para procesamiento funcional

## Algoritmos aplicados

El mismo **algoritmo greedy** que la Parte A para seleccionar y combinar dígitos máximos.

## Interfaces

**No se utilizan interfaces** por las mismas razones que la Parte A.

## El por qué de esas elecciones

Las elecciones arquitectónicas son idénticas a la Parte A.

### Reutilización de Diseño

La Parte B reutiliza completamente la arquitectura de la Parte A, indicando un diseño robusto y genérico.

## Datos Interesantes

### Código Compartido

Ambas partes comparten el mismo código, sugiriendo que:

-   El problema varía en datos de entrada, no en lógica
-   El diseño inicial previó las necesidades correctamente
-   No se requirió refactoring entre partes

### Validación del Algoritmo Greedy

Que el algoritmo greedy funcione para ambas partes valida que la estrategia de optimización local es correcta para este tipo de problema.

