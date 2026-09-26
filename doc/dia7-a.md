# Día 7: Laboratorios - Parte A

## Enunciado

Te encuentras en un laboratorio de teletransportadores que tiene un problema con un manifold de tachyones. Los haces de tachyones (que siempre se mueven hacia abajo) se dividen al encontrar divisores (^). Cuando un haz encuentra un divisor, se detiene y dos nuevos haces continúan desde la izquierda y derecha inmediatas del divisor. Debes analizar cuántas veces se dividirá el haz en total.

## Patrones de diseño

### Singleton

`BeamManager` implementa el patrón **Singleton**:

```java
private static BeamManager instance;

public static BeamManager getInstance() {
    if (instance == null) instance = new BeamManager();
    return instance;
}
```

Este es un **lazy singleton** (se crea bajo demanda).

### Factory Method

`Beam.create(int, int)` encapsula la creación de beams.

### Fluent API

El manager usa interfaz fluida para parsing.

### Clean Code

-   **Métodos pequeños** con responsabilidades claras
-   **Separación de parsing**: Múltiples métodos `parse` sobrecargados

## Estructuras de datos

### List<Beam>

Lista de beams que crece dinámicamente conforme se procesa cada línea del input.

### IntStream

Uso de streams para generar nuevos beams from existentes:

```java
IntStream.range(0, strings.size())
    .mapToObj(i -> beamList.get(i).next(strings.get(i)))
    .forEach(beamList::add)
```

## Algoritmos aplicados

### Construcción Iterativa

El parsing construye la lista iterativamente:

1. Crea el primer beam con la posición de 'S'
2. Para cada línea subsiguiente, genera el siguiente beam basado en el anterior
3. Agrega cada nuevo beam a la lista

### Procesamiento por Niveles

El método `parse(List<String>)` procesa líneas nivel por nivel, donde cada nivel genera el siguiente mediante `beam.next()`.

## Interfaces

**No se utilizan interfaces**.

**Justificación**:

-   Singleton: solo una instancia, no necesita abstracción
-   No hay polimorfismo requerido

## El por qué de esas elecciones

### Singleton para BeamManager

Razones para usar Singleton:

-   **Estado compartido global**: Todos acceden a la misma lista de beams
-   **Punto único de control**: Centraliza la gestión de beams
-   **Convenience**: Fácil acceso desde cualquier lugar

Desventajas aceptadas:

-   **Testing difícil**: El estado global complica tests unitarios
-   **Concurrencia**: No es thread-safe (podría usarse lazy initialization con doble-checked locking)
-   **Tight coupling**: El código se acopla al singleton

### resetInstance para Testing

El método `resetInstance()` sugiere que se reconoció el problema de testing del singleton y se agregó una manera de resetear el estado entre tests. Esto es un buen compromiso.

### Parse Encadenado

El parsing está dividido en pasos claros:

```java
parse(String) -> parse(String[]) -> parse(List<String>)
```

Cada paso agrega processing adicional.

## Datos Interesantes

### Singleton No Thread-Safe

El singleton usa "check-then-act" sin sincronización:

```java
if (instance == null) instance = new BeamManager();
```

En entornos multihilo, esto podría crear múltiples instancias. Para AoC esto es aceptable (single-threaded), pero en producción sería un bug.

### Método getLast()

Uso de `beamList.getLast()` para acceder al último beam, sugiriendo que la respuesta final siempre está en el último elemento.

### Parsing del Primer Beam

El primer beam se trata especialmente:

```java
.add(new Position(0, split[0].indexOf('S')))
```

Busca la posición de 'S' en la primera línea para inicializar.

### Trade-off: Singleton vs Dependency Injection

En un diseño más testeable, BeamManager sería inyectado en lugar de ser singleton. El singleton favorece **convenience sobre testability**.
