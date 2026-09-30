package dia6.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Calculadora del día 6 (parte b): interpreta la entrada como una cuadrícula de caracteres
 * en la que los números se escriben en vertical (dígito a dígito, de arriba a abajo) y la
 * última fila contiene el operador ('+' o '*') de cada problema. Recorre la cuadrícula por
 * columnas, de derecha a izquierda, reconstruyendo cada número y agrupando los problemas
 * separados por columnas vacías, para finalmente sumar o multiplicar cada grupo.
 */
public class CephalopodMathCalculator {
    final private List<ProductList> productLists;
    final private SumList sumList;

    /**
     * Crea una nueva instancia vacía de {@link CephalopodMathCalculator}.
     *
     * @return una nueva instancia lista para parsear datos
     */
    public static CephalopodMathCalculator create() {
        return new CephalopodMathCalculator();
    }

    /**
     * Constructor privado: inicializa la lista de listas de productos y la lista de sumas.
     * Se usa el método estático {@link #create()} para instanciar la clase.
     */
    private CephalopodMathCalculator() {
        this.productLists = new ArrayList<>();
        this.sumList = SumList.create();
    }

    /**
     * Parsea la entrada completa, dividiéndola en líneas.
     *
     * @param input texto completo de entrada
     * @return la propia instancia, con los datos ya parseados
     */
    public CephalopodMathCalculator parse(String input) {
        return parse(input.split("\n"));
    }

    /**
     * Construye una cuadrícula de caracteres a partir de las líneas de entrada y la recorre
     * columna a columna (de derecha a izquierda) para extraer los números verticales y el
     * operador de cada problema, agrupándolos por columnas separadoras vacías.
     *
     * @param lines líneas de texto de la entrada
     * @return la propia instancia, con todos los problemas ya procesados
     */
    private CephalopodMathCalculator parse(String[] lines) {
        // Si no hay líneas, no hay nada que procesar.
        if (lines.length == 0) return this;

        int height = lines.length;
        // El ancho de la cuadrícula es la longitud de la línea más larga, para que quepan todas.
        int width = Arrays.stream(lines)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);

        // Se construye una matriz de caracteres rellena de espacios, y se copia cada línea en su fila.
        char[][] grid = new char[height][width];
        for (int row = 0; row < height; row++) {
            Arrays.fill(grid[row], ' ');
            char[] original = lines[row].toCharArray();
            System.arraycopy(original, 0, grid[row], 0, original.length);
        }

        // La última fila de la cuadrícula contiene los operadores de cada problema.
        int operatorRow = height - 1;
        List<Long> currentNumbers = new ArrayList<>();
        Character currentOperator = null;
        // Indica si actualmente estamos dentro de un bloque de columnas con contenido (un problema).
        boolean inProblem = false;

        // Se recorre la cuadrícula de derecha a izquierda, columna por columna.
        for (int col = width - 1; col >= 0; col--) {
            StringBuilder numberBuilder = new StringBuilder();
            boolean columnHasContent = false;

            // Se recorren las filas de dígitos (todas menos la del operador) para reconstruir el número de esta columna.
            for (int row = 0; row < operatorRow; row++) {
                char digit = grid[row][col];
                if (digit != ' ') {
                    numberBuilder.append(digit);
                    columnHasContent = true;
                }
            }

            // Si en la fila de operadores hay un '+' o '*', se registra como el operador del problema actual.
            char operatorChar = grid[operatorRow][col];
            if (operatorChar == '+' || operatorChar == '*') {
                currentOperator = operatorChar;
                columnHasContent = true;
            }

            // Si se ha formado un número en esta columna, se añade a la lista de números del problema actual.
            if (!numberBuilder.isEmpty()) {
                currentNumbers.add(Long.parseLong(numberBuilder.toString()));
            }

            if (columnHasContent) {
                inProblem = true;
            }

            // Una columna sin contenido actúa como separador entre problemas.
            boolean isSeparatorColumn = !columnHasContent;
            boolean isLastColumn = col == 0;

            // Al llegar a una columna separadora o a la última columna, se cierra el problema actual
            // (si había uno en curso) y se reinician los acumuladores para el siguiente.
            if ((isSeparatorColumn && inProblem) || (isLastColumn && inProblem)) {
                finalizeProblem(currentNumbers, currentOperator);
                currentNumbers = new ArrayList<>();
                currentOperator = null;
                inProblem = false;
            }
        }
        return this;
    }

    /**
     * Cierra un problema acumulado, añadiéndolo a la lista de productos o de sumas
     * según cuál sea su operador.
     *
     * @param numbers  números acumulados del problema
     * @param operator operador del problema ('+' o '*'), puede ser null
     */
    private void finalizeProblem(List<Long> numbers, Character operator) {
        // Si no hay números o no se detectó operador, no hay nada válido que procesar.
        if (numbers.isEmpty() || operator == null) return;

        if (operator == '*') {
            productLists.add(ProductList.create().addAll(numbers));
        } else if (operator == '+') {
            sumList.addAll(numbers);
        }
    }

    /**
     * Calcula el resultado total: la suma de todos los sumandos más la suma de los
     * resultados de cada lista de productos.
     *
     * @return el resultado total combinando sumas y productos
     */
    public long compute() {
        long sumResult = sumList.compute();
        // Se calcula el producto de cada grupo de productos y se suman todos los resultados.
        long productResult = productLists.stream()
                .map(ProductList::compute)
                .reduce(0L, Long::sum);

        return sumResult + productResult;
    }
}