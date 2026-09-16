package software.ulpgc.day06.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CephalopodMathCalculator {
    final private List<ProductList> productLists;
    final private SumList sumList;

    public static CephalopodMathCalculator create() {
        return new CephalopodMathCalculator();
    }

    private CephalopodMathCalculator() {
        this.productLists = new ArrayList<>();
        this.sumList = SumList.create();
    }

    public CephalopodMathCalculator parse(String input) {
        return parse(input.split("\n"));
    }

    private CephalopodMathCalculator parse(String[] lines) {
        if (lines.length == 0) return this;

        int height = lines.length;
        int width = Arrays.stream(lines)
                .map(String::length)
                .max(Comparator.naturalOrder())
                .orElse(0);

        char[][] grid = new char[height][width];
        for (int row = 0; row < height; row++) {
            Arrays.fill(grid[row], ' ');
            char[] original = lines[row].toCharArray();
            System.arraycopy(original, 0, grid[row], 0, original.length);
        }

        int operatorRow = height - 1;
        List<Long> currentNumbers = new ArrayList<>();
        Character currentOperator = null;
        boolean inProblem = false;

        for (int col = width - 1; col >= 0; col--) {
            StringBuilder numberBuilder = new StringBuilder();
            boolean columnHasContent = false;

            for (int row = 0; row < operatorRow; row++) {
                char digit = grid[row][col];
                if (digit != ' ') {
                    numberBuilder.append(digit);
                    columnHasContent = true;
                }
            }

            char operatorChar = grid[operatorRow][col];
            if (operatorChar == '+' || operatorChar == '*') {
                currentOperator = operatorChar;
                columnHasContent = true;
            }

            if (!numberBuilder.isEmpty()) {
                currentNumbers.add(Long.parseLong(numberBuilder.toString()));
            }

            if (columnHasContent) {
                inProblem = true;
            }

            boolean isSeparatorColumn = !columnHasContent;
            boolean isLastColumn = col == 0;

            if ((isSeparatorColumn && inProblem) || (isLastColumn && inProblem)) {
                finalizeProblem(currentNumbers, currentOperator);
                currentNumbers = new ArrayList<>();
                currentOperator = null;
                inProblem = false;
            }
        }
        return this;
    }

    private void finalizeProblem(List<Long> numbers, Character operator) {
        if (numbers.isEmpty() || operator == null) return;

        if (operator == '*') {
            productLists.add(ProductList.create().addAll(numbers));
        } else if (operator == '+') {
            sumList.addAll(numbers);
        }
    }

    public long compute() {
        long sumResult = sumList.compute();
        long productResult = productLists.stream()
                .map(ProductList::compute)
                .reduce(0L, Long::sum);

        return sumResult + productResult;
    }
}