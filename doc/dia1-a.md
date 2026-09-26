# Día 1: Entrada Secreta - Parte A

## Enunciado

Debes abrir la entrada secreta del Polo Norte usando una caja fuerte con un dial circular numerado del 0 al 99. El dial comienza en 50 y debes seguir una secuencia de rotaciones (L=izquierda, R=derecha) dadas en el input. La contraseña real no es la posición final del dial, sino el número de veces que el dial apunta exactamente al 0 después de completar cada rotación de la secuencia.

## Patrones de diseño

### Factory Method

Se utiliza el patrón **Factory Method** mediante el método estático `Dial.create()` en lugar de exponer directamente el constructor. Esto proporciona flexibilidad para futuras extensiones y encapsula la lógica de creación.

### Fluent API (Builder Pattern)

La clase `Dial` implementa un **Fluent API** donde cada método retorna `this`, permitiendo encadenar llamadas como `Dial.create().add(...).execute(...)`. Esto mejora la legibilidad y hace el código más expresivo.

### Immutability

Se usa el tipo `record Order(int step)` para crear objetos inmutables que representan órdenes. Los records son ideales para DTOs (Data Transfer Objects) simples y garantizan thread-safety.

### Clean Code

-   **Métodos pequeños con responsabilidad única**: Cada método tiene una tarea clara (`parse`, `signOf`, `valueOf`,`normalize`)
-   **Nombres descriptivos**: Los nombres comunican intención (`sumPartial`, `iterate`)
-   **Encapsulación**: Los detalles de implementación están privados

## Estructuras de datos

### List<Order>

Se utiliza `ArrayList<Order>` como estructura principal para almacenar la secuencia de órdenes. Esta elección permite:

-   Acceso secuencial eficiente con `stream()`
-   Operaciones de límite con `limit(size)`
-   Acceso indexado para cálculos parciales

### Stream API

Uso extensivo de Streams para procesamiento funcional:

-   `Arrays.stream(orders).map().forEach()` - Transformación y acción
-   `IntStream.rangeClosed()` - Generación de rangos
-   `parallel()` - Paralelización para cálculos intensivos en el método `count()`

## Algoritmos aplicados

### Aritmética Modular

El algoritmo central usa **aritmética modular** para mantener valores en el rango [0, 100):

```java
private int normalize(int value) {
    return ((value % 100) + 100) % 100;
}
```

La doble normalización maneja correctamente valores negativos.

### Procesamiento de Streams Paralelo

En `count()`, se utiliza `IntStream.parallel()` para paralelizar el conteo de posiciones que cruzan el cero, optimizando el rendimiento.

## Interfaces

**No se utilizan interfaces** en esta implementación.

**Justificación**:

-   `Dial` es una clase concreta con una responsabilidad clara y no necesita abstracciones
-   No hay múltiples implementaciones alternativas del comportamiento
-   El diseño favorece YAGNI (You Aren't Gonna Need It)
-   La clase es suficientemente simple y cohesiva

## El por qué de esas elecciones

### Factory Method sobre Constructor Público

-   Permite nombrar el método de creación de forma más expresiva
-   Facilita futuras variantes sin romper el código existente
-   Sigue el principio de Open/Closed

### Fluent API

-   Mejora la legibilidad del código cliente
-   Reduce la necesidad de variables temporales
-   Hace el código más declarativo

### Streams sobre bucles tradicionales

-   Código más expresivo y funcional
-   Facilita paralelización con cambios mínimos
-   Reduce errores relacionados con índices y estado mutable

### Records para Order

-   Elimina boilerplate (constructor, getters, equals, hashCode)
-   Garantiza inmutabilidad por defecto
-   Comunica claramente que es un contenedor de datos

## Datos Interesantes

### Offset de +50

El método `sum` aplica un offset de `+50` antes del módulo, correspondiente al punto de inicio del dial.

### Método execute como Entry Point

El método `execute(String orders)` actúa como punto de entrada principal, manejando input vacío y delegando el parsing. Esto separa la validación de entrada de la lógica de negocio.

### Paralelización selectiva

Solo el método `count()` usa paralelización, indicando una optimización consciente donde el beneficio supera el overhead.
