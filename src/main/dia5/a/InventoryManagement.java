package software.ulpgc.day05.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InventoryManagement {
    private final List<Range> ranges;
    private final List<Ingredient> ingredients;
    private final static String EMPTY_LINE_REGEX = "(?m)^\\s*$\\R+";

    private InventoryManagement() {
        this.ranges = new ArrayList<>();
        this.ingredients = new ArrayList<>();
    }

    public static InventoryManagement create() {
        return new InventoryManagement();
    }

    private boolean rangeChecker(long ingredientId) {
        return this.ranges.stream().anyMatch(range -> range.isInRange(ingredientId));
    }

    public InventoryManagement addRange(long start, long end) {
        ranges.add(new Range(start, end));
        return this;
    }

    public InventoryManagement addRange(String range) {
        this.addRange(getRangeValue(range, 0), getRangeValue(range, 1));
        return this;
    }

    public InventoryManagement parse(String input) {
        return parse(input.split(EMPTY_LINE_REGEX));
    }

    private InventoryManagement parse(String[] split) {
        return parseRanges(split[0]).parseIngredients(split[1]);
    }

    private InventoryManagement parseIngredients(String s) {
        Arrays.stream(s.split("\n")).forEach(this::addIngredient);
        return this;
    }

    private InventoryManagement parseRanges(String s) {
        Arrays.stream(s.split("\n")).map(String::trim).forEach(this::addRange);
        return this;
    }

    public InventoryManagement addIngredient(String ingredientId) {
        return this.addIngredient(parseNumber(ingredientId));
    }

    public InventoryManagement addIngredient(long ingredientId) {
        ingredients.add(new Ingredient(ingredientId, rangeChecker(ingredientId) ? IngredientStatus.fresh : IngredientStatus.spoiled));
        return this;
    }

    private long getRangeValue(String range, int x) {
        return parseNumber(range.split("-")[x]);
    }

    private long parseNumber(String str) {
        return Long.parseLong(str);
    }

    public long count() {
        return ingredients
                .stream()
                .filter(ing -> ing.status().equals(IngredientStatus.fresh))
                .count();
    }
}
