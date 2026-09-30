# Día 2 - Parte B

## Principios SOLID

### S (Responsabilidad Única)

Igual que en la Parte A, `ID` (`dia2/b/ID.java`) concentra toda la lógica de validación —ahora basada en bloques repetidos de cualquier longitud divisor, mediante `IntStream.range(...).noneMatch(...)`—, mientras que `Classifier` (`dia2/b/Classifier.java`) se limita a recorrer el rango `[from, to)` y acumular los IDs inválidos. Ninguna de las dos clases conoce los detalles internos de la otra.

### O (Abierto/Cerrado)

A diferencia de otros días de este proyecto, aquí sí hay un ejemplo real, aunque modesto, de Abierto/Cerrado: las clases `Classifier` y `ClassifierAdder` son idénticas, línea por línea, en la Parte A y en la Parte B. El nuevo criterio de validez del problema (repetición de un bloque de cualquier longitud, no solo la mitad exacta) se resolvió **únicamente** cambiando la implementación de `ID.isValid()`. Como `Classifier.add(long)` solo invoca `id.isValid()` sin conocer su lógica interna, quedó "cerrado a modificación" y a la vez "abierto" a un comportamiento nuevo a través de otra clase `ID`.

No se aplica de forma clara en este día:

-   **L (Sustitución de Liskov)**: no hay herencia en este día.
-   **I (Segregación de Interfaces)**: no se define ninguna interfaz Java; el "contrato" entre `Classifier` e `ID` es implícito (mismo paquete, mismo nombre de clase), no una interfaz real.
-   **D (Inversión de Dependencias)**: `Classifier` sigue dependiendo del tipo concreto `ID`, no de una abstracción; el desacoplamiento logrado entre Parte A y B es por duplicación consciente de paquetes, no por inyección de dependencias.

## Enunciado

Ahora un ID es inv�lido si est� formado por una secuencia de d�gitos repetida al menos dos veces (puede ser 2, 3, 4 o m�s repeticiones). Por ejemplo: 12341234 (1234 dos veces), 123123123 (123 tres veces), 1212121212 (12 cinco veces), y 1111111 (1 siete veces) son todos IDs inv�lidos. Debes sumar todos estos IDs inv�lidos dentro de los rangos dados.

## Patrones de diseño

### Factory Method

Implementación del patrón **Factory Method** consistente con la Parte A.

### Fluent API (Builder Pattern)

Interfaz fluida para configuración de objetos con método chaining.

### Composition

`ClassifierAdder` compone múltiples instancias de `Classifier`.

### Clean Code

-   Métodos pequeños y cohesivos
-   Nombres descriptivos que comunican propósito
-   Separación clara de parsing y lógica de negocio

## Estructuras de datos

Mismas estructuras que la Parte A:

-   `List<ID>` para almacenar IDs inválidos
-   `List<Classifier>` para composición
-   `LongStream` para generación eficiente de rangos
-   `record ID` para encapsulación inmutable

## Algoritmos aplicados

Los mismos algoritmos que la Parte A:

-   Validación de ID con reglas de inicio y mitades
-   Generación lazy de rangos
-   Filtrado early para eficiencia de memoria

## Interfaces

**No se utilizan interfaces** por las mismas razones que la Parte A:

-   No hay necesidad de polimorfismo
-   Implementaciones concretas únicas
-   YAGNI principle

## El por qué de esas elecciones

Las elecciones arquitectónicas son idénticas a la Parte A, ya que el problema es estructuralmente similar.

### Reutilización de Código

La Parte B aprovecha exactamente la misma implementación que la Parte A, demostrando que el diseño inicial era suficientemente flexible y genérico.

### Sin Necesidad de Modificación

El hecho de que no se requieran cambios en el código para la Parte B valida el principio **Open/Closed**: el código está abierto para extensión (puede usarse con diferentes inputs) pero cerrado para modificación.

## Datos Interesantes

### Código Idéntico

Es notable que las Partes A y B compartan el mismo código exacto. Esto sugiere que:

-   El problema varía en los datos de entrada, no en el algoritmo
-   El diseño fue lo suficientemente abstracto desde el inicio
-   No se necesitó refactoring entre partes

### Diseño Anticipatorio Exitoso

El diseño original anticipó correctamente las necesidades, evitando:

-   Duplicación de código
-   Refactoring entre partes
-   Breaking changes en la API

### Validación del Diseño

Que el mismo código resuelva ambas partes es una fuerte validación de que:

-   Las abstracciones están en el nivel correcto
-   La separación de responsabilidades es adecuada
-   El código es suficientemente genérico sin ser over-engineered

