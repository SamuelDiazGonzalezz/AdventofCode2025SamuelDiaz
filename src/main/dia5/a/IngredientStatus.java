package dia5.a;

/**
 * Estados posibles de un {@link Ingredient} dentro del inventario:
 * fresco (dentro de algún rango válido) o estropeado (fuera de todos los rangos).
 */
public enum IngredientStatus {
    // El ingrediente está dentro de al menos un rango válido.
    fresh,
    // El ingrediente no está dentro de ningún rango válido.
    spoiled
}
