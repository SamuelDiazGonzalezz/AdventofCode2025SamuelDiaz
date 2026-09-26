# Día 4 - Parte A

## Enunciado

<!-- Rellenar con el enunciado del problema -->

## Patrones de diseño

### Factory Method

`PrintingDepartment.create(Position maxPosition)` encapsula la creación con validación del tamaño de la grid.

### Fluent API

Encadenamiento de métodos para construcción y procesamiento:

```java
PrintingDepartment.create(maxPos)
    .addRows(rows)
    .process()
    .accessibleRollsCount()
```

### Immutability con Records

-   `record Position(int col, int row)` - Coordenadas inmutables
-   `record PaperRoll()` con `List<PaperRoll>` interna

### Clean Code

-   **Métodos pequeños** con responsabilidad única
-   **Nombres descriptivos**: `getAdjacentRolls`, `isValidNeighbor`, `isInBounds`
-   **Separación de concerns**: Parsing, procesamiento de grid, y cálculo están separados

## Estructuras de datos

### Array 2D: PaperRoll[][]

Matriz bidimensional para representar la grid espacial:

-   **Acceso O(1)** por coordenadas
-   **Memory-efficient** para grids densas
-   Permite `null` para celdas vacías

### List<PaperRoll> (dentro de PaperRoll)

Cada roll mantiene sus vecinos:

-   Grafo implícito donde nodos mantienen sus conexiones
-   Evita estructuras de grafo explícitas

### IntStream para iteración 2D

```java
IntStream.range(0, paperRolls.length)
    .forEach(i -> IntStream.range(0, paperRolls[i].length)
        .forEach(j -> ...))
```

### Stream<PaperRoll> para filtrado

`getAllRolls()` convierte la matriz en stream para operaciones funcionales.

## Algoritmos aplicados

### Búsqueda de Vecinos en Grid 2D

```java
IntStream.rangeClosed(pos.row() - 1, pos.row() + 1)
    .flatMap(i -> IntStream.rangeClosed(pos.col() - 1, pos.col() + 1)
        .mapToObj(j -> new Position(i, j)))
```

Genera todos los 8 vecinos (incluyendo diagonales) usando producto cartesiano.

### Filtrado de Vecinos Válidos

Aplica dos filtros:

1. `isValidNeighbor`: No es la posición actual
2. `isInBounds`: Está dentro de los límites de la grid

### Construcción de Grafo Implícito

El método `process()` construye el grafo de adyacencias:

-   Itera sobre todas las celdas
-   Para cada roll, encuentra y almacena vecinos
-   Construcción O(n _ m _ 8) donde n,m son dimensiones

### Conteo con Predicado

`accessibleRollsCount()` usa filtro para contar rolls con menos de 4 vecinos.

## Interfaces

**No se utilizan interfaces**.

**Justificación**:

-   `PrintingDepartment` es la única implementación de gestión de grid
-   `PaperRoll` es un record simple
-   No hay polimorfismo necesario
-   YAGNI principle

## El por qué de esas elecciones

### Array 2D sobre Otras Estructuras

Ventajas del array 2D:

-   **Acceso directo O(1)** vs. Map<Position, Roll> que sería O(log n)
-   **Memory locality** mejor para cache
-   **Simple** y directo para grids rectangulares

Desventajas aceptadas:

-   Debe conocerse el tamaño de antemano
-   No es sparse-friendly (desperdicia memoria si hay pocas celdas)

### Construcción Lazy vs Eager de Grafo

Se elige construcción **eager** (llamada explícita a `process()`):

-   **Ventaja**: Separación clara entre parsing y procesamiento
-   **Ventaja**: Puede verificarse el estado antes de procesar
-   **Desventaja**: Requiere llamada explícita (podría olvidarse)

### FlatMap para Producto Cartesiano

El uso de `flatMap` para generar vecinos:

```java
.flatMap(i -> IntStream.rangeClosed(...))
```

Es elegante y funcional, creando el producto cartesiano de rangos de forma declarativa.

### Records para Position

Position es **value object** perfecto:

-   Solo coordenadas, sin identidad
-   Inmutable por naturaleza
-   `equals` basado en valor (automático con record)

## Datos Interesantes

### Constant MAX_ADJACENT_PAPER_ROLLS

La constante `MAX_ADJACENT_PAPER_ROLLS = 4` está hardcoded. Podría parameterizarse en el constructor para mayor flexibilidad.

### Método print() para Debugging

El método `print()` visualiza la grid:

```java
System.out.print(roll.size() < MAX ? "x" : "@")
```

Útil para debugging visual, aunque mezcla lógica de negocio con presentación.

### Uso de Objects.nonNull como Filtro

```java
.filter(Objects::nonNull)
```

Manejo elegante de celdas vacías sin condicionales explícitos.

### Búsqueda de 8-vecindad

El algoritmo busca los 8 vecinos (incluyendo diagonales), no solo los 4 ortogonales. Esto es menos común y sugiere un problema específico del dominio.

### Posible Mejora: Builder Separado

Los métodos de construcción (`addRow`, `addRows`, `process`) podrían separarse en un Builder dedicado para mejor separación de concerns.
