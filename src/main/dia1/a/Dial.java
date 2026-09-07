package software.ulpgc.day01.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Dial {
    private final List<Order> orders;
    private Dial() {
        this.orders = new ArrayList<>();
    }
    public static Dial create() {
        return new Dial();
    }

    public Dial add(String... orders) {
        Arrays.stream(orders)
                .map(this::parse)
                .forEach(this::add);
        return this;
    }

    private void add(Order order) {
        orders.add(order);
    }

    private Order parse(String order) {
        return new Order(signOf(order) * valueOf(order));
    }

    private int signOf(String order) {
        return order.charAt(0) == 'L' ? -1 : 1;
    }

    private int valueOf(String order) {
        return Integer.parseInt(order.substring(1));
    }

    public int position() {
        return normalize(sumAll());
    }

    private int sumAll() {
        return sum(orders.stream());
    }

    public int count() {
        return (int) iterate()
                .map(this::sumPartial)
                .filter(s -> s == 0)
                .count();
    }

    private IntStream iterate() {
        return IntStream.rangeClosed(1, orders.size()).parallel();
    }

    private int sumPartial(int size) {
        return normalize(sum(orders.stream().limit(size)));
    }

    private static int sum(Stream<Order> orders) {
        return (orders.mapToInt(Order::step).sum() + 50) % 100;
    }

    private int normalize(int value) {
        return ((value % 100) + 100) % 100;
    }

    public Dial execute(String orders) {
        if (orders.isEmpty()) return this;
        return add(orders.split("\n"));
    }
}
