# Día 9: Cine - Parte A

## Principios SOLID

### S - Single Responsibility

`Position` solo representa una coordenada `(x, y)` y `Rectangle` solo calcula el área de un rectángulo a partir de dos esquinas (`area()`); ninguna de las dos clases sabe nada de parsing ni de cómo se generan combinaciones. `TilesManager`, en cambio, mezcla en una sola clase el parseo de la entrada (`parse`), la generación de todas las combinaciones de rectángulos (`rectangles`) y la búsqueda del área máxima (`largestArea`): son tres responsabilidades distintas conviviendo en el mismo tipo, por lo que el principio solo se cumple parcialmente.

### D - Dependency Inversion

`TilesManager` declara sus campos como `List<Position>` y `List<Rectangle>` (abstracciones de la interfaz `List`), no como `ArrayList` directamente, aunque internamente los instancie así. Es un ejemplo modesto pero real de depender de la abstracción del JDK en vez de la implementación concreta.

### No se aplica de forma clara en este día

-   **O (Open/Closed)**: no hay ningún punto de extensión (estrategias, estados calculables por subtipos, etc.); añadir un nuevo criterio de "rectángulo válido" obligaría a modificar `TilesManager` directamente.
-   **L (Liskov Substitution)**: no existe ninguna jerarquía de herencia ni subtipos intercambiables.
-   **I (Interface Segregation)**: no se define ninguna interfaz propia en este día.

## Enunciado

En el cine del Polo Norte, los elfos quieren encontrar el rectángulo más grande que pueda formarse usando dos tiles rojos como esquinas opuestas. Tienes una lista de posiciones de tiles rojos en una cuadrícula. Debes encontrar el área del rectángulo más grande posible que tenga dos tiles rojos en esquinas opuestas.

## Patrones de diseño

### Factory Method

`TilesManager.create()` para encapsular creación.

### Fluent API

Interfaz fluida para parsing y procesamiento:

```java
TilesManager.create()
    .parse(input)
    .rectangles()
    .largestArea()
```

### Records

-   `record Position(int x, int y)` - Posición 2D
-   `record Rectangle(Position p1, Position p2)` - Rectángulo definido por dos esquinas

### Clean Code

-   **Pipeline de transformaciones**: Parsing por capas
-   **Métodos descriptivos**: `rectangles`, `largestArea`

## Estructuras de datos

### List<Position>

Almacena todas las posiciones parseadas.

### List<Rectangle>

Almacena todos los rectángulos posibles ordenados por área.

### Stream con flatMap

Generación de combinaciones con `flatMap`:

```java
IntStream.range(0, size).boxed()
    .flatMap(i -> IntStream.range(i + 1, size)
        .mapToObj(j -> new Rectangle(...)))
```

## Algoritmos aplicados

### Generación de Todas las Combinaciones

Algoritmo **O(n²)** para generar todos los pares de posiciones:

```java
IntStream.range(0, positionList.size())
    .flatMap(i -> IntStream.range(i + 1, size)
        .mapToObj(j -> new Rectangle(pos[i], pos[j])))
```

### Ordenamiento por Área

```java
.sorted(Comparator.comparingLong(Rectangle::area).reversed())
```

Ordena rectángulos de mayor a menor área para encontrar el máximo fácilmente.

### Selección del Máximo

`rectangleList.getFirst().area()` obtiene el área del primer elemento (el mayor) gracias al ordenamiento descendente.

## Interfaces

**No se utilizan interfaces**.

**Justificación**:

-   Implementación única
-   No hay polimorfismo necesario

## El por qué de esas elecciones

### FlatMap para Combinaciones

El uso de `flatMap` para generar pares es funcional y elegante:

-   Evita bucles anidados
-   Declarativo (expresa qué, no cómo)
-   Componible con otras operaciones de stream

### Ordenamiento Completo vs Selección

Se ordena la lista completa en lugar de usar `max()`:

-   **Ventaja**: Acceso a los N mayores si se necesita
-   **Desventaja**: Más costoso que simplemente encontrar el máximo (O(n log n) vs O(n))

Para AoC con datasets pequeños, el costo adicional es insignificante.

### Records para Geometría

Position y Rectangle como records es perfecto:

-   Son value objects matemáticos
-   Inmutabilidad previene bugs
-   `equals` basado en valor es crucial para geometría

### Separación parse/rectangles

Dos pasos claros:

1. `parse`: Procesa input a posiciones
2. `rectangles`: Calcula rectángulos

Esto separa I/O de lógica de negocio.

## Datos Interesantes

### Método area en Rectangle

Rectangle debe implementar `area()` para calcular el área. Esto encapsula la lógica geométrica en el record, respetando SRP.

### sorted().toList()

El sorting usa:

```java
.sorted(...).toList()
```

`toList()` materializa el stream en una lista, necesario para acceder después con `getFirst()`.

### Comparator.comparingLong

Uso de `comparingLong` en lugar de `comparingInt` sugiere que las áreas pueden ser grandes (overflow con int).

### Reversed para Descendente

`.reversed()` invierte el comparator para obtener orden descendente, más limpio que negar la comparación.
