# Día 8: Zona de Juegos - Parte A

## Enunciado

En una zona de juegos subterránea, los elfos están configurando decoraciones navideñas conectando cajas de conexión eléctricas en el espacio 3D. Para ahorrar luces, quieren conectar los pares más cercanos primero. Debes conectar los 1000 pares de cajas más cercanas y luego multiplicar los tamaños de los tres circuitos más grandes formados.

## Patrones de diseño

### Factory Method

`ChristmasDecorationManager.create(int maxConnectJunction)` acepta parámetro de configuración.

### Fluent API

Interfaz fluida para construcción y procesamiento:

```java
ChristmasDecorationManager.create(maxJunctions)
    .parse(input)
    .startConnections()
    .calculate()
```

### Records para Value Objects

-   `record Position(int x, int y, int z)` - Coordenadas 3D inmutables
-   `record JunctionBoxPair(Position box1, Position box2, long distance)` - Par de junctions con distancia

### Clean Code

-   **Métodos pequeños**: Cada uno con responsabilidad única
-   **Nombres descriptivos**: `computeJunctionPairs`, `getThreeLargest`
-   **Transformaciones pipeline**: Múltiples métodos `parse` sobrecargados

## Estructuras de datos

### List<Position>

Lista de posiciones (junction boxes) en espacio 3D.

### List<JunctionBoxPair>

Lista de todos los pares posibles de junctions con sus distancias, ordenada por distancia.

### CircuitSet

Estructura personalizada (no mostrada but referenced) que:

-   Mantiene las posiciones
-   Agrupa conexiones
-   Calcula los tres grupos más grandes

### Stream API

Transformaciones funcionales para parsing:

```java
Arrays.stream(input.split("\n"))
    .map(s -> toIntList(s.trim().split(",")))
    .map(iL -> new Position(...))
```

## Algoritmos aplicados

### Generación de Todos los Pares

Algoritmo **O(n²)** para generar todos los pares posibles:

```java
for (int i = 0; i < boxList.size(); i++) {
    for (int j = i + 1; j < boxList.size(); j++) {
        pairList.add(new JunctionBoxPair(...));
    }
}
```

El bucle evita duplicados usando `j = i + 1`.

### Ordenamiento por Distancia

```java
pairList.sort(JunctionBoxPair::compareTo)
```

Ordena pares de menor a mayor distancia, permitiendo seleccionar las conexiones más cortas primero.

### Algoritmo Greedy para Conexiones

`startConnections()` selecciona los primeros N pares (los de menor distancia):

```java
IntStream.range(0, MAX_JUNCTION_CONNECT_COUNT)
    .mapToObj(i -> pairList.get(i))
    .forEach(circuitSet::add)
```

Este es un **algoritmo greedy**: assume que las conexiones más cortas son óptimas.

### Cálculo de Producto

```java
circuitSet.getThreeLargest()
    .stream()
    .mapToLong(Set::size)
    .reduce(1, (a, b) -> a * b)
```

Multiplica los tamaños de los tres grupos más grandes.

## Interfaces

**No se utilizan interfaces explícitas**.

**Justificación**:

-   `CircuitSet` parece ser una clase concreta sin necesidad de abstracción
-   No hay variaciones de comportamiento que requieran polimorfismo

## El porque de esas elecciones

### Records para Position y JunctionBoxPair

Ambos son **value objects** perfectos:

-   Position: Coordenadas 3D inmutables
-   JunctionBoxPair: Tupla inmutable de dos positions más distancia

Beneficios:

-   Inmutability thread-safe
-   `equals` basado en valor (crucial para comparaciones)
-   Código conciso

### Generación Completa de Pares

Genera **todos** los pares antes de filtrar/ordenar, en lugar de quedar lazy. Esto es:

-   **Simple**: Fácil de entender y debuggear
-   **Correcto**: Garantiza encontrar los mínimos globales
-   **Memory-intensive**: O(n²) pares en memoria

Para problemas de AoC con n pequeño, esto es aceptable.

### Bucles for Tradicionales para Pares

Usa bucles `for` en lugar de streams para la generación de pares:

```java
for (int i...) for (int j = i+1...)
```

Esto es más legible que el equivalente con `flatMap` y evita complejidad innecesaria.

### Algoritmo Greedy

La elección de un algoritmo greedy (seleccionar las N conexiones más cortas) sugiere que:

-   El problema permite optimización local
-   No se requiere programación dinámica o backtracking
-   La solución es eficiente: O(n² log n) por el sorting

### MAX_JUNCTION_CONNECT_COUNT Configurable

Se pasa como parámetro del constructor en lugar de hardcodear, demostrando **parametrización** y flexibilidad.

## Datos Interesantes

### Position 3D

El uso de coordenadas 3D (`x, y, z`) sugiere un problema espacial tridimensional, lo cual es relativamente raro en AoC.

### Método distanceTo

`Position` debe implementar `distanceTo(Position)` para calcular distancias (probablemente euclidiana 3D). Esto está encapsulado en el record, respetando el principio de **Tell, Don't Ask**.

### CircuitSet como Abstracción

`CircuitSet` parece encapsular la lógica de:

-   Gestionar conexiones
-   Detectar componentes conectados (grupos)
-   Encontrar los tres grupos más grandes

Esto es un buen ejemplo de **extracción de clase** para lógica compleja.

### Código Imperativo Mezclado con Funcional

El código mezcla estilos:

-   Imperative: bucles for para pares
-   Functional: streams para parsing y cálculo final

Esta mezcla pragmática usa el mejor estilo para cada situación.

### IntStream.range vs Loop

Para seleccionar los primeros N elementos, usa `IntStream.range`:

```java
IntStream.range(0, MAX).mapToObj(i -> list.get(i))
```

Esto podría simplificarse con `list.stream().limit(MAX)`, pero la forma actual es más explícita sobre los índices.
