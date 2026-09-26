# Día 6: Compactador de Basura - Parte A

## Enunciado

Ayudas a una familia de cefalópodos con matemáticas mientras esperan a abrir una puerta. Los problemas están dispuestos horizontalmente en columnas donde cada columna es un problema vertical. Los números están alineados verticalmente y al final de cada columna hay un símbolo (+ o \*) indicando la operación. Debes resolver cada problema y sumar todos los resultados para obtener el gran total.

## Patrones de diseño

### Factory Method

`CephalopodMathCalculator.create()` y `SumList.create()`, `ProductList.create()` proporcionan control sobre la creación.

### Strategy Pattern

**Patrón Strategy** implementado mediante la interfaz `OperatorList`:

```java
public interface OperatorList {
    OperatorList add(long number);
    OperatorList addAll(List<Long> numbers);
    long compute();
    int size();
}
```

Implementaciones concretas:

-   `SumList`: Estrategia de suma
-   `ProductList`: Estrategia de producto

Este patrón permite tratar diferentes operaciones de forma polimórfica.

### Composition

`CephalopodMathCalculator` **compone** diferentes operators:

-   Un `SumList` para sumas
-   Múltiples `ProductList` para productos

### Fluent API

Tanto las implementaciones de `OperatorList` como el calculator usan interfaz fluida.

### Clean Code

-   **Parsing complejo separado**: Múltiples métodos de parsing con responsabilidades claras
-   **Nombres descriptivos**: `toLongList`, `compute`
-   **Transformaciones pipeline**: Processing funcional de datos

## Estructuras de datos

### List<ProductList>

El calculator mantiene una lista de `ProductList` porque puede haber múltiples columnas con operador `*`.

### SumList (singular)

Solo hay un `SumList` porque todas las columnas con `+` se acumulan en una única lista.

### Stream Processing

Uso extensivo de streams para transformaciones:

```java
problems.stream()
    .map(row -> row[i])
    .toArray(String[]::new)
```

### Transposición de Matriz

El parsing transpone la matriz de entrada:

```java
IntStream.range(0, problems.getFirst().length)
    .mapToObj(i -> problems.stream()
        .map(row -> row[i])
        .toArray(String[]::new))
```

## Algoritmos aplicados

### Transposición de Matriz

El algoritmo convierte filas en columnas:

-   Input: Filas de números
-   Output: Columnas (arrays verticales)
-   Permite procesar cada columna independientemente

### Clasificación por Operador

```java
if (row[row.length - 1].equals("*")) {
    productLists.add(...);
} else {
    sumList.addAll(...);
}
```

Clasifica cada columna según su último elemento (operador).

### Reduce para Agregación

```java
sumList.compute() + productLists.stream()
    .map(ProductList::compute)
    .reduce(0L, Long::sum)
```

Combina resultados de múltiples operators usando reduce.

### Compute en SumList y ProductList

-   **SumList**: `stream().mapToLong().sum()`
-   **ProductList**: `stream().mapToLong().reduce(1, (a, b) -> a * b)`

Diferentes estrategias de reducción.

## Interfaces

**Sí se utilizan interfaces**: `OperatorList`

### Justificación para usar interfaz

La interfaz `OperatorList` es necesaria porque:

1. **Polimorfismo necesario**: Diferentes implementaciones (suma vs producto) con comportamiento distinto
2. **Común contract**: Todas las operaciones comparten métodos `add`, `addAll`, `compute`
3. **Extensibilidad**: Fácil agregar nuevas operaciones (división, módulo, etc.)
4. **Open/Closed Principle**: El calculator está abierto para extensión (nuevos operators) pero cerrado para modificación

### Beneficios Concretos

-   El código cliente (`CephalopodMathCalculator`) no necesita saber qué tipo de operator está usando
-   Facilita testing con mocks
-   Permite composición flexible

## El por qué de esas elecciones

### Strategy Pattern sobre Conditionals

En lugar de:

```java
if (operator == "+") sum else product
```

Se usa Strategy pattern, que:

-   Elimina condicionales del codigo principal
-   Facilita agregar operaciones sin modificar existing code
-   Encapsula cada estrategia en su propia clase

### Una SumList vs Múltiples ProductLists

Diseño asimétrico por necesidad:

-   **ProductList**: cada columna con `*` se multiplica independientemente, luego se suman
-   **SumList**: todas las sumas se acumulan directamente

Esta asimetría refleja la semántica del problema.

### Transposición de Matriz

Transponer permite:

-   Procesar columnas como si fueran filas
-   Usar streams para parsing elegante
-   Separar extracción de clasificación

### Parsing por Capas

El parsing tiene múltiples niveles:

```
String -> String[] -> Stream<String[]> -> transposed -> classified -> operators
```

Cada capa es una transformación pura y testeable.

## Datos Interesantes

### Retorno null en SumList.addAll

```java
public SumList addAll(List<Long> numbers) {
    sumList.addAll(numbers);
    return null; // Bug!
}
```

Esto parece un **bug**: debería retornar `this` para mantener la interfaz fluida. Probablemente no causó problemas porque este método no se encadena en el código actual.

### IntStream para Transposición

La transposición usa `IntStream.range` para generar índices de columnas, demostrando cómo los streams pueden reemplazar bucles anidados con código más declarativo.

### Type Safety con InterfazEl uso de `OperatorList` como tipo en lugar de clases concretas proporciona:

-   Desacoplamiento
-   Flexibilidad para cambiar implementaciones
-   Contrato explícito

### Reduce con Identidad Correcta

-   Suma: identidad = 0
-   Producto: identidad = 1

El código usa las identidades matemáticas correctas para cada operación.

### Posible Mejora: Eliminar Duplicación

`SumList` y `ProductList` tienen mucha duplicación. Podrían compartir una clase base abstracta con solo el método `compute()` diferente.
