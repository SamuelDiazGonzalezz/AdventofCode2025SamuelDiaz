# Día 11: Reactor - Parte A

## Enunciado

En el reactor que alimenta la fábrica, hay un problema de comunicación con el nuevo server rack. Tienes una lista de dispositivos y sus conexiones de salida (formato: `dispositivo: salida1 salida2`). Los datos fluyen desde dispositivos a través de sus salidas (no pueden fluir hacia atrás). Debes encontrar cuántos caminos diferentes conducen desde el dispositivo `you` hasta el dispositivo `out`.

## Patrones de diseño

### Factory Method

`ServerRack.create()` proporciona creación controlada.

### Memoization

Uso del patrón **Memoization** para optimizar recursividad:

```java
private final Map<Device, Long> mem;
```

### Clean Code

-   **Métodos pequeños** con responsabilidad única
-   **Names descriptivos**: `pathsTo`, `getDevice`
-   **Separación de parsing complejo**

### Records

`record Device(String label, List<String> outputs)` - Dispositivo inmutable con salidas.

## Estructuras de datos

### Set<Device>

HashSet para almacenar dispositivos sin duplicados.

### Map<Device, Long> para Memoization

Cache de resultados computados para evitar recomputación en recursión.

### Map<String, List<String>> para Parsing

Estructura intermedia durante parsing (device label → outputs).

## Algoritmos aplicados

### DFS Recursivo con Memoization

Algoritmo de **búsqueda en profundidad** con cache:

```java
public long pathsTo(Device from, Device to) {
    if (from.equals(to)) return 1; // Caso base
    if (mem.containsKey(from)) return mem.get(from); // Cache hit

    long totalSteps = next(from).stream()
        .mapToLong(out -> pathsTo(getDevice(out), to))
        .sum();
    mem.put(from, totalSteps);
    return totalSteps;
}
```

**Características**:

-   **Caso base**: Si origen == destino, hay 1 camino
-   **Memoization**: Cachea resultados para evitar recomputación
-   **Recursión**: Suma caminos de todos los vecinos

### Parsing Pipeline Complejo

Transform multipass:

```java
split("\n") -> map(split(":")) ->
collect(toMap) -> entrySet() ->
map(toDevice) -> toList()
```

### Stream Processing

Uso de mapToLong().sum() para agregar caminos.

## Interfaces

**No se utilizan interfaces**.

**Justificación**:

-   Implementación única de ServerRack
-   Record para Device (no puede implementar interfaces con estado complejo)

## El por qué de esas elecciones

### Memoization para Optimización

Sin memoization, el algoritmo sería exponencial:

-   **Sin memo**: O(2^n) - recomputaría caminos repetidamente
-   **Con memo**: O(n) - cada device se calcula una vez

Esto es **crucial** para performance.

### Record para Device

Device es perfecto como record:

-   **Value object**: Label + outputs sin comportamiento complejo
-   **Immutable**: Seguro como key en Map
-   **Equals/Hash**: Automático, correcto para memoization

### Map Temporal para Parsing

Usa `Collectors.toMap()` como estructura intermedia, demostrando uso avanzado de collectors.

### Set vs List para Devices

`Set<Device>` elimina duplicados automáticamente, aunque el parsing probablemente ya garantiza unicidad.

## Datos Interesantes

### Grafo como Estructura Implícita

El código modela un **grafo dirigido**:

-   Nodos: Devices
-   Aristas: Outputs (device → lista de devices siguientes)

No usa una estructura de grafo explícita, sino que cada device conoce sus vecinos.

### Conteo de Caminos

El algoritmo cuenta **todos los caminos posibles** de un device a otro, no solo el más corto. Esto sugiere:

-   Puede haber múltiples rutas
-   El DAG (grafo acíclico dirigido) permite múltiples paths

### getDevice con OrElse

```java
.findFirst().orElse(new Device(label, List.of()))
```

Retorna un device vacío si no encuentra el label. Esto evita excepciones pero podría ocultar errores de input.

### Sobrecarga de pathsTo

Dos versiones:

-   `pathsTo(Device, Device)`: Interna, usa objetos
-   `pathsTo(String, String)`: Externa, usa labels convenientes

Esto separa la API pública (user-friendly) de la interna (type-safe).

### Parsing con Regex-like Split

Usa `.split("\\s+")` para manejar múlt iples espacios, mostrando robustez en parsing.

### Recursión sin Stack Overflow

La memoization previene recalcular, pero también **previene loops infinitos** en grafos cíclicos (aunque el problema probablemente es acíclico).

### Design: DAG Assumption

El algoritmo assume un **DAG** (Directed Acyclic Graph). En grafos con ciclos, la recursión sin memoization correcta caería en loop infinito.
