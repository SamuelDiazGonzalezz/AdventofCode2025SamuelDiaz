# Día 5 - Parte A

## Enunciado

<!-- Rellenar con el enunciado del problema -->

## Patrones de diseño

### Factory Method

`InventoryManagement.create()` proporciona punto de creación controlado.

### Builder/Fluent API

Interfaz fluida completa:

```java
InventoryManagement.create()
    .addRange(start, end)
    .parse(input)
    .addIngredient(id)
    .count()
```

### Enum para Estados

`enum IngredientStatus { fresh, spoiled }` proporciona type-safety y claridad semántica.

### Records para Value Objects

-   `record Ingredient(long id, IngredientStatus status)`
-   `record Range(long start, long end)`

Ambos son value objects inmutables que encapsulan datos relacionados.

### Clean Code

-   **Métodos de parsing privados** separados de lógica de negocio
-   **Nombres descriptivos**: `rangeChecker`, `getRangeValue`
-   **Constante para regex**: `EMPTY_LINE_REGEX`

## Estructuras de datos

### List<Range>

Almacena rangos válidos para verificación eficiente de ingredientes.

### List<Ingredient>

Almacena todos los ingredientes procesados con sus estados.

### Stream API

Uso de streams para:

-   Parsing: `Arrays.stream(input.split(...)).forEach(...)`
-   Validación: `ranges.stream().anyMatch(...)`
-   Conteo: `ingredients.stream().filter(...).count()`

## Algoritmos aplicados

### Verificación de Rango

```java
private boolean rangeChecker(long ingredientId) {
    return ranges.stream().anyMatch(range -> range.isInRange(ingredientId));
}
```

Busca si un ID está en **cualquier** rango usando `anyMatch`:

-   **O(n)** donde n es el número de rangos
-   Short-circuiting: se detiene al encontrar match

### Parsing por Secciones

```java
private InventoryManagement parse(String[] split) {
    return parseRanges(split[0]).parseIngredients(split[1]);
}
```

Divide el input en secciones separadas por líneas vacías usando regex `"(?m)^\\s*$\\R+"`.

### Clasificación Eager

Los ingredientes se clasifican inmediatamente al agregarlos:

```java
.addIngredient(ingredientId)
new Ingredient(id, rangeChecker(id) ? fresh : spoiled)
```

Evita reprocesar datos más tarde.

### Long para IDs

Uso de `long` en lugar de `int` para soportar rangos grandes.

## Interfaces

**No se utilizan interfaces**.

**Justificación**:

-   `InventoryManagement` es implementación única
-   Los records no necesitan interfaces
-   No hay polimorfismo requerido

## El por qué de esas elecciones

### Records para Ingredient y Range

Los records son ideales porque:

-   **Ingredient**: Tupla inmutable (id + status)
-   **Range**: Intervalo matemático inmutable
-   Ambos son value objects sin comportamiento complejo

### Enum para IngredientStatus

Enum sobre String/boolean:

-   **Type-safe**: No puede tener valores inválidos
-   **Autodocumentado**: Los valores posibles están claros
-   **Extensible**: Puede agregar métodos o valores fácilmente

### Separación de Parsing

Los métodos de parsing están claramente separados:

-   `parse(String)`: Entry point que maneja input completo
-   `parseRanges`: Procesa rangos
-   `parseIngredients`: Procesa IDs
-   Cada uno tiene una responsabilidad (SRP)

### Ranges List vs. Otras Opciones

Alternativas consideradas:

-   **Set de todos los IDs válidos**: Prohibitivo en memoria para rangos grandes
-   **Intervaltree**: Overcomplicated para este caso
-   **List simple**: Suficiente para pocos rangos

La elección de `List<Range>` con verificación linear es:

-   Simple
-   Suficiente para el problema
-   Fácil de entender y mantener

### Clasificación Eager vs Lazy

Se clasifica al agregar en lugar de al contar:

-   **Ventaja**: No reprocesar
-   **Ventaja**: Puede consultar múltiples veces sin recomputar
-   **Desventaja**: Requiere más memoria (guarda todos los ingredientes)

## Datos Interesantes

### Regex para Líneas Vacías

```java
private static final String EMPTY_LINE_REGEX = "(?m)^\\s*$\\R+";
```

-   `(?m)`: Multiline mode
-   `^\\s*$`: Línea solo con whitespace
-   `\\R+`: Uno o más line breaks

Este regex es más robusto que un simple `\\n\\n` porque maneja:

-   Diferentes line endings (\\r\\n, \\n, \\r)
-   Líneas con espacios
-   Múltiples líneas vacías consecutivas

### Uso de long Everywhere

El uso consistente de `long` para IDs previene overflow en rangos grandes, mostrando previsión en el diseño.

### Method Chaining en parse

```java
parseRanges(split[0]).parseIngredients(split[1])
```

Encadena el parsing en una expresión fluida, compartiendo `this` entre secciones.

### Sobrecarga de addRange

Dos versiones:

-   `addRange(long, long)`: Usar programáticamente
-   `addRange(String)`: Parsear de input

Esto separa la lógica de parsing de la lógica de negocio, respetando Interface Segregation Principle.
