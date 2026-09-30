package dia5.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gestiona el inventario del día 5 (parte a): parsea los rangos válidos y los
 * ingredientes de la entrada, determina el estado de cada ingrediente según
 * si cae dentro de algún rango, y permite contar cuántos son frescos.
 * Implementa un patrón "builder" fluido devolviendo {@code this} en cada método.
 */
public class InventoryManagement {
    private final List<Range> ranges;
    private final List<Ingredient> ingredients;
    // Expresión regular que detecta una o más líneas vacías (separador entre el bloque de rangos y el de ingredientes).
    private final static String EMPTY_LINE_REGEX = "(?m)^\\s*$\\R+";

    /**
     * Constructor privado: inicializa las listas vacías de rangos e ingredientes.
     * Se usa el método estático {@link #create()} para instanciar la clase (patrón factoría estática).
     */
    private InventoryManagement() {
        this.ranges = new ArrayList<>();
        this.ingredients = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de {@link InventoryManagement}.
     *
     * @return una nueva instancia lista para parsear datos
     */
    public static InventoryManagement create() {
        return new InventoryManagement();
    }

    /**
     * Comprueba si un id de ingrediente está contenido en al menos uno de los rangos almacenados.
     *
     * @param ingredientId id del ingrediente a comprobar
     * @return true si el id cae dentro de algún rango válido
     */
    private boolean rangeChecker(long ingredientId) {
        // anyMatch recorre los rangos y se detiene en cuanto encuentra uno que contenga el id.
        return this.ranges.stream().anyMatch(range -> range.isInRange(ingredientId));
    }

    /**
     * Añade un nuevo rango válido al inventario.
     *
     * @param start inicio (inclusive) del rango
     * @param end   fin (inclusive) del rango
     * @return la propia instancia, para encadenar llamadas
     */
    public InventoryManagement addRange(long start, long end) {
        ranges.add(new Range(start, end));
        return this;
    }

    /**
     * Añade un rango válido a partir de su representación en texto, con formato "inicio-fin".
     *
     * @param range cadena con el rango en formato "inicio-fin"
     * @return la propia instancia, para encadenar llamadas
     */
    public InventoryManagement addRange(String range) {
        this.addRange(getRangeValue(range, 0), getRangeValue(range, 1));
        return this;
    }

    /**
     * Parsea la entrada completa del problema, separando el bloque de rangos del bloque de ingredientes.
     *
     * @param input texto completo de entrada
     * @return la propia instancia, con los rangos e ingredientes ya cargados
     */
    public InventoryManagement parse(String input) {
        // Divide la entrada en dos bloques usando el separador de línea(s) vacía(s).
        return parse(input.split(EMPTY_LINE_REGEX));
    }

    /**
     * Parsea el array resultante de dividir la entrada: el primer bloque son los rangos
     * y el segundo los ingredientes.
     *
     * @param split array con [bloque de rangos, bloque de ingredientes]
     * @return la propia instancia, tras encadenar el parseo de rangos e ingredientes
     */
    private InventoryManagement parse(String[] split) {
        return parseRanges(split[0]).parseIngredients(split[1]);
    }

    /**
     * Parsea el bloque de texto de ingredientes, una línea por ingrediente.
     *
     * @param s bloque de texto con los ids de ingredientes, uno por línea
     * @return la propia instancia
     */
    private InventoryManagement parseIngredients(String s) {
        // Por cada línea del bloque, se añade el ingrediente correspondiente.
        Arrays.stream(s.split("\n")).forEach(this::addIngredient);
        return this;
    }

    /**
     * Parsea el bloque de texto de rangos, una línea por rango.
     *
     * @param s bloque de texto con los rangos, uno por línea
     * @return la propia instancia
     */
    private InventoryManagement parseRanges(String s) {
        // Se recorta cada línea (trim) antes de convertirla en un rango, por si hay espacios sobrantes.
        Arrays.stream(s.split("\n")).map(String::trim).forEach(this::addRange);
        return this;
    }

    /**
     * Añade un ingrediente a partir de su id en formato texto.
     *
     * @param ingredientId id del ingrediente como cadena
     * @return la propia instancia
     */
    public InventoryManagement addIngredient(String ingredientId) {
        return this.addIngredient(parseNumber(ingredientId));
    }

    /**
     * Añade un ingrediente al inventario, calculando su estado (fresco o estropeado)
     * en función de si su id cae dentro de algún rango válido.
     *
     * @param ingredientId id numérico del ingrediente
     * @return la propia instancia
     */
    public InventoryManagement addIngredient(long ingredientId) {
        // Se determina el estado con el operador ternario según el resultado de rangeChecker.
        ingredients.add(new Ingredient(ingredientId, rangeChecker(ingredientId) ? IngredientStatus.fresh : IngredientStatus.spoiled));
        return this;
    }

    /**
     * Extrae uno de los dos valores numéricos (inicio o fin) de una cadena de rango "inicio-fin".
     *
     * @param range cadena con el rango en formato "inicio-fin"
     * @param x     índice del valor a extraer (0 = inicio, 1 = fin)
     * @return el valor numérico extraído
     */
    private long getRangeValue(String range, int x) {
        // Se divide la cadena por el guion y se toma el elemento en la posición x.
        return parseNumber(range.split("-")[x]);
    }

    /**
     * Convierte una cadena de texto en un número long.
     *
     * @param str cadena a convertir
     * @return el valor numérico correspondiente
     */
    private long parseNumber(String str) {
        return Long.parseLong(str);
    }

    /**
     * Cuenta cuántos ingredientes del inventario están en estado fresco.
     *
     * @return número de ingredientes frescos
     */
    public long count() {
        return ingredients
                .stream()
                // Se filtran solo los ingredientes cuyo estado es "fresh".
                .filter(ing -> ing.status().equals(IngredientStatus.fresh))
                .count();
    }
}
