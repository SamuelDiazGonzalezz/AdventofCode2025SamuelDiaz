# Día 10: Fábrica - Parte A

## Enunciado

Las máquinas de la fábrica están apagadas y necesitas inicializarlas. Cada máquina tiene luces indicadoras ([.##.]), botones que togglean luces específicas ((0,3,4)), y requerimientos de voltaje ({3,5,4,7}). Para configurar las luces, presionas botones; cada botón puede presionarse múltiples veces. Debes encontrar el mínimo número de presiones de botones necesarias para configurar correctamente todas las máquinas.

## Patrones de diseño

### Factory Method

`Machine.create(String input)` combina creación con parsing.

### Clean Code

-   **Métodos pequeños** con responsabilidad única
-   **Nombres descriptivos**: `desiredStateSteps`, `bfsMinSteps`, `applyButton`
-   **Separación de parsing**: Regex patterns como constantes, múltiples métodos parse

### Immutability

-   `record Button(Set<Integer> lights)` - Configuración de button inmutable
-   Sets inmutables para estados

## Estructuras de datos

### Set<Button>

HashSet de buttons para búsqueda eficiente y eliminación de duplicados.

### Set<Integer> para Estados

Cada estado del sistema se representa como **Set de índices** de luces encendidas:

-   Compacto (solo almacena índices de luces ON)
-   `equals` y `hashCode` eficientes para detección de estados visitados
-   Operaciones de set para aplicar buttons

### Set<Set<Integer>> para Visited

Conjunto de estados visitados para evitar reexploración en BFS.

### List<Set<Integer>> para Frontier

Lista de estados en la frontera actual del BFS.

### Pattern (Regex)

Constantes `Pattern` para parsing eficiente:

```java
private static final Pattern LIGHTS_PATTERN = Pattern.compile("\\[(.*?)]");
private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");
```

## Algoritmos aplicados

### BFS (Breadth-First Search)

Implementación **recursiva** de BFS para encontrar el camino más corto:

```java
private int bfsMinSteps(Set<Set<Integer>> visited,
                        List<Set<Integer>> frontier,
                        Set<Integer> target,
                        int depth) {
    return frontier.isEmpty() ? -1 :
           frontier.contains(target) ? depth :
           bfsMinSteps(
               mergeVisitedAndFrontier(visited, frontier),
               generateNextFrontier(visited, frontier),
               target,
               depth + 1
           );
}
```

**Características**:

-   **Recursión de cola**: Podría optimizarse a iteración
-   **Breadth-first**: Explora por niveles, garantiza camino más corto
-   **Early termination**: Retorna apenas encuentra el target

### Generación de Estados Vecinos

```java
frontier.stream()
    .flatMap(state -> buttons.stream()
        .map(button -> applyButton(state, button.lights())))
    .filter(state -> !visited.contains(state))
    .distinct()
```

Para cada estado en la frontera, genera todos los estados alcanzables aplicando cada button.

### Aplicación de Button con XOR

```java
private Set<Integer> applyButton(Set<Integer> currentState,
                                   Set<Integer> buttonLights) {
    return Stream.concat(
        currentState.stream().filter(l -> !buttonLights.contains(l)),
        buttonLights.stream().filter(l -> !currentState.contains(l))
    ).collect(Collectors.toSet());
}
```

Esto implementa **XOR de sets**: toggle las luces especificadas.

-   Luces ON que el button afecta → OFF (no se incluyen)
-   Luces OFF que el button afecta → ON (se incluyen)
-   Luces no afectadas → mantienen su estado

### Parsing con Regex y Matcher

```java
while (matcher.find()) {
    // Procesa matcher.group(1)
}
```

Extrae múltiples matches de patterns en el input.

## Interfaces

**No se utilizan interfaces**.

**Justificación**:

-   Machine es implementación única
-   El problema usa clases concretas
-   Records para data objects

## El por qué de esas elecciones

### BFS Recursivo vs Iterativo

La implementación usa **recursión** en lugar de un while loop:

-   **Ventaja**: Más funcional y declarativo
-   **Ventaja**: Cada nivel es una llamada, facilitando debugging
-   **Desventaja**: Stack overflow con profundidades grandes
-   **Desventaja**: Menos eficiente que iteración

Para AoC, donde la profundidad suele ser limitada, la recursión es aceptable.

### Set<Integer> para Estados

Representar estados como sets de índices:

-   **Compacto**: Solo almacena luces ON
-   **Eficiente**: equals/hashCode en O(n)
-   **Functional**: Permite operaciones de set

Alternativa sería BitSet o boolean[], pero Set es más expresivo.

### Distinct en generateNextFrontier

```java
.distinct()
```

Elimina estados duplicados en la nueva frontera antes de explorarlos, optimizando el BFS.

### Stream.concat para XOR

El uso de `concat` para implementar XOR es elegante pero no obvio. Podría beneficiarse de un comentario explicativo.

### Regex Patterns Precompilados

```java
private static final Pattern LIGHTS_PATTERN = ...
```

Precompilar patterns es una **optimización importante**: la compilación de regex es costosa.

## Datos Interesantes

### BFS Garantiza Camino Más Corto

BFS encuentra el **camino más corto** en grafos no ponderados, lo cual es perfecto para este problema donde cada button press tiene costo 1.

### Estado como Set

La representación de estado como Set de índices es brillante:

-   Ignora orden (no importa en qué orden están las luces)
-   Permite usar HashSet para visited (O(1) lookup)
-   Facilita comparaciones de estados

### Merging Visited y Frontier

```java
Stream.concat(visited.stream(), frontier.stream())
    .collect(Collectors.toSet())
```

Combina estados visitados con la frontera actual para la siguiente iteración.

### Filter de Visited

Filtrar estados ya visitados previene ciclos y reduce el espacio de búsqueda exponencialmente.

### Complejidad Exponencial Potencial

El número de estados posibles es 2^n donde n es el número de luces. BFS explora el espacio de estados sistemáticamente pero puede ser exponencial en el peor caso.

### Design Pattern: Command

Los `Button` objetos podrían verse como implementaciones del patrón **Command**, donde cada button "ejecuta" una transformación de estado.
