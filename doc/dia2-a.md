# Día 2: Tienda de Regalos - Parte A

## Enunciado

Un elfo joven ha añadido IDs de producto inválidos a la base de datos de la tienda del Polo Norte. Los IDs inválidos son aquellos formados por una secuencia de dígitos repetida exactamente dos veces (ej: 55, 6464, 123123). Los IDs no pueden empezar con cero. Dado un conjunto de rangos de IDs, debes identificar y sum ar todos los IDs inválidos que aparecen en esos rangos.

## Patrones de diseño

### Factory Method

Uso del patrón **Factory Method** en ambas clases:

-   `Classifier.create()` - Creación de clasificadores
-   `ClassifierAdder.create()` - Creación del agregador

### Fluent API (Builder Pattern)

Implementación completa del patrón **Builder** con interfaz fluida:

```java
Classifier.create()
    .from(start)
    .to(end)
    .calculate()
```

Este patrón hace el código autoexplicativo y facilita la configuración secuencial de objetos complejos.

### Composition over Inheritance

`ClassifierAdder` **compone** múltiples `Classifier` en lugar de heredar. Esto es más flexible y evita las limitaciones de herencia única en Java.

### Clean Code

-   **Separación de responsabilidades**: `Classifier` clasifica un rango, `ClassifierAdder` agrega múltiples rangos
-   **Métodos pequeños**: Cada método hace una cosa y la hace bien
-   **Parse separado de lógica**: Los métodos `parse` están aislados de la lógica de clasificación

## Estructuras de datos

### List<ID> y List<Classifier>

-   `Classifier` mantiene `List<ID>` de IDs inválidos
-   `ClassifierAdder` mantiene `List<Classifier>` para agregar resultados

Estas listas permiten:

-   Acumulación dinámica de resultados
-   Operaciones de stream para agregación
-   Memoria eficiente (solo se almacenan IDs inválidos, no todos)

### LongStream

Uso de `LongStream.range(from, to)` para generar rangos de números eficientemente sin materializar todos los valores en memoria.

### Record ID

El `record ID(String number)` encapsula:

-   El número como string (para análisis de patrones)
-   Métodos de validación (`isValid()`)
-   Conversión a long (`longNumber()`)

## Algoritmos aplicados

### Algoritmo de Validación de ID

El método `ID.isValid()` implementa dos reglas:

1. **No puede empezar con "0"** - Validación simple con `startsWith("0")`
2. **Regla de mitades**: Si tiene longitud par, las dos mitades no pueden ser iguales

```java
number.length() % 2 == 1 ||
    !number.substring(0, number.length()/2)
        .equals(number.substring(number.length()/2))
```

Este algoritmo es **O(n)** donde n es la longitud del número.

### Generación y Filtrado Lazy

El uso de `LongStream.range()` con `.forEach()` genera números bajo demanda, procesándolos inmediatamente. Esto es memory-efficient para rangos grandes.

### Parsing Recursivo en ClassifierAdder

El parsing sigue una cadena de transformaciones:

```
String -> String[] -> long[] -> Classifier
```

Cada paso es una función pura con responsabilidad única.

## Interfaces

**No se utilizan interfaces explícitas**.

**Justificación**:

-   Ambas clases tienen implementaciones concretas únicas
-   No hay necesidad de polimorfismo
-   La composición se logra a través de clases concretas
-   Principio YAGNI - no se anticipan implementaciones alternativas

Sin embargo, hay **interfaces implícitas**:

-   Ambas clases ofrecen método `sum()` - podría extraerse a interfaz si hubiera más clases similares
-   Pattern de `create()` es consistente - sugiere un contrato implícito

## El por qué de esas elecciones

### Separación Classifier vs ClassifierAdder

Esta separación sigue el **Single Responsibility Principle**:

-   `Classifier`: Responsable de UN rango
-   `ClassifierAdder`: Responsable de MÚLTIPLES rangos

Esto permite:

-   Testear cada clase independientemente
-   Reutilizar `Classifier` en otros contextos
-   Escalar a múltiples rangos sin modificar `Classifier`

### Almacenar solo IDs inválidos

En lugar de almacenar todos los IDs y filtrar después, se filtran durante la generación. Esto es:

-   **Memory-efficient**: No almacena millones de IDs válidos
-   **Early filtering**: Descarta datos innecesarios inmediatamente
-   **Performance**: Reduce operaciones posteriores

### String vs Long para ID

Se mantiene el ID como `String` en el record porque:

-   La validación requiere análisis de caracteres y subcadenas
-   Solo se convierte a `long` cuando es necesario (para sumar)
-   Mejor separación de concerns: validación (String) vs. cálculo (long)

### Fluent API con side effects

Aunque `calculate()` tiene side effects (llena la lista de IDs), mantiene la interfaz fluida. Esto es un **trade-off aceptable** porque:

-   La intención es clara (el nombre "calculate" sugiere computación)
-   El beneficio de legibilidad supera la impureza funcional
-   El estado está encapsulado

## Datos Interesantes

### Lazy Evaluation con forEach

Usar `forEach` en lugar de `collect` permite procesar elementos sin crear colecciones intermedias, optimizando memoria.

### Ajuste del rango con +1

```java
public Classifier to(long to) {
    this.to = to + 1; // Convierte rango inclusivo a exclusivo
    return this;
}
```

Este ajuste convierte semántica inclusiva (para el usuario) en exclusiva (para `LongStream.range`), mostrando atención al detalle en la API pública vs. implementación interna.

### Transformación funcional en compile

El método `compile` transforma strings a clasificadores usando:

```java
Arrays.stream(idRanges)
    .map(String::trim)
    .map(this::parse)
    .forEach(this::add);
```

Este pipeline funcional es declarativo, testeable y fácil de modificar.

### Composición de Sumas

`ClassifierAdder.sum()` usa composición:

```java
classifierList.stream().mapToLong(Classifier::sum).sum()
```

Esto demuestra cómo operaciones simples se componen para crear funcionalidad compleja.
