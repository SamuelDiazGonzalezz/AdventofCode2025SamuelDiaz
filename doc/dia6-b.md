# Día 6: Compactador de Basura - Parte B

## Principios SOLID

### S - Single Responsibility Principle

Igual que en la parte A, `SumList` y `ProductList` mantienen una responsabilidad única (sumar o multiplicar sus operandos). `CephalopodMathCalculator` añade aquí la reconstrucción de números a partir de una cuadrícula de caracteres (`parse`, `finalizeProblem`), pero esa lógica de "leer la cuadrícula" queda separada de la lógica de cálculo, que sigue delegándose en `SumList`/`ProductList`.

### O - Open/Closed Principle

Misma situación que en la parte A: `OperatorList` permitiría añadir una implementación nueva sin tocar `SumList` ni `ProductList`, pero `finalizeProblem` decide entre suma y producto con un `if/else` sobre el carácter `'+'`/`'*'`, por lo que un tercer operador seguiría obligando a modificar `CephalopodMathCalculator`.

### L - Liskov Substitution Principle

A diferencia de la parte A, aquí `SumList.addAll(List<Long>)` sí devuelve `this` correctamente, por lo que el contrato fluido de `OperatorList` se respeta: tanto `SumList` como `ProductList` pueden sustituirse entre sí sin sorpresas. Es un buen ejemplo de que el bug de Liskov detectado en la parte A fue corregido en esta parte.

### I - Interface Segregation Principle

Igual que en la parte A, la interfaz `OperatorList` sigue siendo pequeña y con métodos que ambas implementaciones usan por completo (aquí `addAll` recibe un array `Long[]` en vez de un único `Long`, pero mantiene la misma cohesión).

### D - Dependency Inversion Principle

Misma observación que en la parte A: los campos de `CephalopodMathCalculator` siguen tipados como `List<ProductList>` y `SumList` concretos, no como `OperatorList`, así que la inversión de dependencias sigue siendo solo parcial pese a existir la interfaz.

## Enunciado

Los cefalópodos olvidaron explicar que las matemáticas se leen de derecha a izquierda en columnas. Cada número se da en su propia columna con el dígito más significativo arriba y el menos significativo abajo. Debes releer los problemas de derecha a izquierda y calcular el nuevo gran total.

## Patrones de diseño

Mismos patrones que la Parte A:

-   **Factory Method**
-   **Strategy Pattern** con interfaz `OperatorList`
-   **Composition**
-   **Fluent API**

## Estructuras de datos

Estructuras compartidas con la Parte A:

-   `List<ProductList>`
-   `SumList`
-   Stream processing para transformaciones

## Algoritmos aplicados

Mismos algoritmos:

-   Transposición de matriz
-   Clasificación por operador
-   Reduce para agregación

## Interfaces

**Sí se usa la interfaz `OperatorList`** por las mismas razones que la Parte A.

## El por qué de esas elecciones

Arquitectura completamente compartida con la Parte A, demostrando que el patrón Strategy y la interfaz `OperatorList` fueron diseñados correctamente desde el inicio.

## Datos Interesantes

### Mismo Código, Mismas Ventajas

La Parte B hereda todas las ventajas arquitectónicas de la Parte A, incluido el polimorfismo y la extensibilidad del Strategy pattern.

### Validación del Diseño con Interfaces

El hecho de que la misma interfaz funcione para ambas partes valida que la abstracción estaba en el nivel correcto.
