# Día 12: Granja de Árboles de Navidad

## Principios SOLID

### S - Single Responsibility

Cada clase tiene una única responsabilidad: `Position` solo suma coordenadas; `Shape` solo conoce la geometría de una forma; `Region` solo agrega ancho, alto y regalos requeridos; `PresentFitterParser` solo parsea el texto de entrada; `LongMaskRegionSolver`/`BitSetRegionSolver` solo resuelven el backtracking con una representación concreta de bits; `RegionSolverFactory` solo decide qué solver usar; y `PresentFitter` solo orquesta (delega el parseo y delega la resolución), quedando como la clase más simple de todo el día.

### O - Open/Closed

La interfaz `RegionSolver` permite añadir una nueva estrategia de resolución (por ejemplo, un solver basado en otra técnica de poda) creando una clase nueva que la implemente, sin modificar `PresentFitter` ni las implementaciones existentes. Solo haría falta actualizar `RegionSolverFactory` para poder elegirla, que es precisamente el único punto del sistema diseñado para conocer las implementaciones concretas.

### L - Liskov Substitution

`LongMaskRegionSolver` y `BitSetRegionSolver` son completamente intercambiables en cualquier sitio donde se espera un `RegionSolver`: ambas cumplen el mismo contrato (`canFit(Region) -> boolean`) sin sorpresas, a pesar de usar representaciones internas distintas (`long` vs `BitSet`). `PresentFitter` ni siquiera sabe cuál de las dos está usando.

### I - Interface Segregation

`RegionSolver` expone un único método (`canFit`), el mínimo necesario para que `PresentFitter` pueda delegar la comprobación de encaje. No obliga a sus implementaciones a exponer detalles internos (como los métodos privados `precomputePlacements` o `tryFit`, que permanecen ocultos en cada solver).

### D - Dependency Inversion

`PresentFitter.canFitPresents(Region)` depende únicamente de la abstracción `RegionSolver` (obtenida a través de `RegionSolverFactory.forRegion(region)`), no de `LongMaskRegionSolver` ni `BitSetRegionSolver` directamente. Además, sigue declarando sus colecciones contra interfaces del JDK (`List<Shape>`, `List<Region>`), nunca contra `ArrayList`/`HashMap` directamente.

## Enunciado

En la granja de árboles de Navidad, los elfos necesitan colocar regalos bajo los árboles. Los regalos vienen en formas estándar (mostradas como grids con # representando partes sólidas). Cada región bajo un árbol tiene un tamaño específico y necesita fit un número determinado de regalos de cada forma. Los regalos pueden rotarse y voltearse, pero deben colocarse perfectamente en la cuadrícula sin overlap. Debes determinar cuántas regiones pueden fit todos los regalos listados para esa región.

## Patrones de diseño

### Factory Method

`PresentFitter.create()` encapsula la creación de la instancia vacía. `RegionSolverFactory.forRegion(region)` es también un Factory Method, pero además actúa como **Strategy selector**: decide en tiempo de ejecución qué implementación de `RegionSolver` instanciar según el tamaño de la región.

### Strategy Pattern

La interfaz `RegionSolver` (con las implementaciones `LongMaskRegionSolver` y `BitSetRegionSolver`) es un ejemplo de **Strategy**: encapsula el algoritmo de backtracking detrás de una interfaz común, permitiendo que `PresentFitter` use una u otra sin conocer los detalles de cada una. Ver [Principios SOLID](#principios-solid) para el detalle de cómo esto habilita O, L, I y D.

### Separación Parseo / Resolución

`PresentFitterParser` encapsula todo el parseo de texto, y `PresentFitter` delega en ella (`new PresentFitterParser().parse(input)`), manteniendo separadas ambas responsabilidades.

### Record como Clase Principal

`PresentFitter` es un **record** sencillo: solo contiene los datos (`shapes`, `regions`) y unos pocos métodos de orquestación (`parse`, `fittableRegions`, `canFitPresents`) que delegan el trabajo pesado en `PresentFitterParser` y `RegionSolver`.

```java
public record PresentFitter(List<Shape> shapes, List<Region> regions) {
    // Solo orquesta: delega parseo y resolución
}
```

### Backtracking con Memoization

Implementación de **backtracking** optimizado con cache para el problema de fitting, ahora encapsulada dentro de cada `RegionSolver` (`LongMaskRegionSolver`/`BitSetRegionSolver`) en vez de estar en `PresentFitter`.

### Clean Code

-   **Parsing complejo separado** en múltiples métodos
-   **Métodos pequeños**: cada uno con responsabilidad única
-   **Nombres descriptivos**: `canFitPresents`, `tryFit`, `precomputePlacements`

## Estructuras de datos

### List<Shape> y List<Region>

Listas inmutables de shapes y regions.

### BitSet para Ocupación

`BitSet` para representar celdas ocupadas en la grid:

-   **Eficiente en memoria**: Un bit por celda
-   **Operaciones rápidas**: AND, OR para intersecciones
-   **Para regions grandes** (> 64 celdas)

### Long para Ocupación (Bitmask)

Para regions pequeñas (≤ 64 celdas), usa `long` como bitmask:

-   **Más rápido**: Operaciones de bits nativas
-   **Limitado a 64 bits**: Solo para grids pequeñas

### Maps para Memoization

-   `Map<Shape, List<BitSet>>`: Cache de placements por shape
-   `Map<String, Boolean>`: Cache de estados ya computados (BitSet)
-   `Map<Long, Boolean>`: Cache de estados (long bitmask)

### IdentityHashMap

```java
Map<Shape, List<BitSet>> cache = new IdentityHashMap<>();
```

Usa **identidad de objeto** en lugar de equals, evitando comparaciones costosas.

## Algoritmos aplicados

### Backtracking Recursivo

```java
private boolean tryFit(BitSet occupied,
                       List<List<BitSet>> placements,
                       int index,
                       Map<String, Boolean> memo) {
    if (index >= placements.size()) return true; // Todas colocadas

    return placements.get(index).stream()
        .filter(mask -> !mask.intersects(occupied))
        .anyMatch(mask -> {
            BitSet next = (BitSet) occupied.clone();
            next.or(mask);
            return tryFit(next, placements, index + 1, memo);
        });
}
```

**Características**:

-   **Caso base**: Si todas las shapes están colocadas, éxito
-   **Prueba todas las orientaciones**: Para cada shape, testa todas las posiciones/rotaciones
-   **Backtracking**: Si falla, retrocede y prueba otra posición
-   **Short-circuiting**: `anyMatch` se detiene al encontrar solución

### Memoization de Estados

```java
String key = index + ":" + Base64.encode(occupied);
if (memo.containsKey(key)) return memo.get(key);
```

Cachea resultados para (índice, estado) evitando recomputación.

### Precomputación de Colocaciones

```java
List<BitSet> precomputePlacements(Shape shape, Region region) {
    return shape.allOrientations().stream()
        .flatMap(variant -> /* todas las posiciones */)
        .distinct()
        .toList();
}
```

**Optimización eager**: Precalcula todas las posiciones válidas para cada shape antes del backtracking, convirtiendo tiempo de búsqueda en tiempo de setup.

### Optimización por Tamaño

```java
if (regionSize <= 64) {
    // Usa long bitmask
} else {
    // Usa BitSet
}
```

Selecciona representación basándose en tamaño para optimizar rendimiento.

### Orden Heurístico

```java
ordered.sort(Comparator.comparingInt(List::size));
```

Ordena shapes por número de placements posibles (menos primero), reduciendo el espacio de búsqueda.

### Operaciones de Bitmask

-   `(occupied & mask) == 0L`: Verifica si no hay intersección (con long)
-   `!mask.intersects(occupied)`: Verifica intersección (con BitSet)
-   `occupied | mask`: Une bitmasks (con long)
-   `occupied.or(mask)`: Une BitSets

## Interfaces

**Sí se utiliza una interfaz propia: `RegionSolver`.**

```java
public interface RegionSolver {
    boolean canFit(Region region);
}
```

Con dos implementaciones intercambiables, `LongMaskRegionSolver` (representación `long`, para regiones ≤ 64 celdas) y `BitSetRegionSolver` (representación `BitSet`, sin límite de tamaño), elegidas en tiempo de ejecución por `RegionSolverFactory`. Es el mismo patrón que la interfaz `OperatorList` del día 6: una abstracción mínima (un único método) con implementaciones intercambiables, que permite añadir una tercera estrategia de resolución en el futuro sin tocar `PresentFitter`.

Los records `Shape` y `Region` siguen siendo value objects sin interfaz propia, ya que no tienen implementaciones alternativas.

## El por qué de esas elecciones

### Record para PresentFitter

`PresentFitter` es un record que no mezcla datos con lógica compleja: esa lógica vive en `PresentFitterParser` y en las implementaciones de `RegionSolver`:

-   **Ventaja**: Inmutabilidad automática de las listas `shapes`/`regions`
-   **Ventaja**: Sintaxis concisa
-   **Ventaja**: al quedar solo con métodos de orquestación, es fácil de leer de principio a fin

El constructor público (usado por `PresentFitterParser.parseLines`) sigue siendo necesario para construir la instancia final con los datos ya parseados.

### BitSet vs Long Bitmask

La dualidad BitSet/Long es brillante:

-   **Long**: Operaciones extremadamente rápidas para grids ≤ 8x8
-   **BitSet**: Flexible para cualquier tamaño

Este **polimorfismo manual** optimiza el caso común sin complicar el código.

### Memoization de Estado Completo

Cachea `(index, occupied)` en lugar de solo `index`:

-   Evita recalcular sub-problemas idénticos
-   Crítico para performance: sin esto sería exponencial

### IdentityHashMap para Shape

Usa identidad porque:

-   Las shapes son reutilizadas (misma instancia)
-   Evita llamar equals/hashCode en shapes complejas
-   Más rápido: comparación de punteros

### Precomputación vs Lazy

Precalcula **todas** las orientaciones y posiciones:

-   **Ventaja**: Simplifica el backtracking
-   **Ventaja**: distinct() elimina duplicados
-   **Desventaja**: Usa más memoria

Para AoC,el trade-off es correcto.

### Ordering Heuristic

Ordenar por `List::size` (menos opciones primero) es **heurística de fallo rápido**:

-   Si una shape tiene pocas opciones, mejor fallar temprano
-   Reduce el árbol de búsqueda significativamente

## Datos Interesantes

### Cache Key para BitSet

```java
Base64.getEncoder().encodeToString(occupied.toByteArray())
```

Serializa BitSet a String para usar como key. Esto es:

-   **Costoso**: Conversión + encoding
-   **Necesario**: BitSet no es hashable directamente de forma segura

### XOR para Long Key

```java
long key = occupied ^ ((long) index << 56);
```

Combina state + index en un solo long usando XOR y shift, evitando crear objetos.

###Complexity del Problema
Este es un problema **NP-completo** (packing/tiling):

-   Backtracking con memoization es approach razonable
-   No hay algoritmo polynomial (que sepamos)
-   Las optimizaciones son cruciales

### Dual Algorithms

Tener `tryFit` y `tryFitLong` es duplicación, pero:

-   Permite optimizar cada caso independientemente
-   El beneficio de performance justifica la duplicación
-   No se puede abstraer fácilmente (tipos diferentes: BitSet vs long)

### distinct() en Precompute

`distinct()` elimina colocaciones duplicadas (e.g., rotaciones simétricas que resultan iguales).

### Clone de BitSet

```java
BitSet next = (BitSet) occupied.clone();
```

Clona para backtracking sin mutar el original. Esto es **inmutabilidad parcial**: las listas son inmutables pero los BitSets internos se clonan.

### Design Sofisticado

Este es el código más complejo de todos los días:

-   Múltiples optimizaciones (memoization, precompute, ordering)
-   Doble implementación (BitSet/long)
-   Backtracking recursivo
-   Cacheo sofisticado

Demuestra expertise en algoritmos y optimization.
