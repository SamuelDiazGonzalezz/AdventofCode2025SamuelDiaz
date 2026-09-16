package software.ulpgc.day06.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductList implements OperatorList {
    final private List<Long> productList;

    public static ProductList create() {
        return new ProductList();
    }

    private ProductList() {
        this.productList = new ArrayList<>();
    }

    @Override
    public int size() {
        return productList.size();
    }

    @Override
    public ProductList add(long number) {
        productList.add(number);
        return this;
    }

    @Override
    public ProductList addAll(Long[] numbers) {
        productList.addAll(Arrays.asList(numbers));
        return this;
    }

    @Override
    public ProductList addAll(List<Long> numbers) {
        productList.addAll(numbers);
        return this;
    }

    @Override
    public long compute() {
        return productList.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
    }
}
