package dia5.a;

/**
 * Representa un ingrediente del inventario del día 5, identificado por su id
 * numérico y su estado actual (fresco o estropeado).
 *
 * @param id     identificador único del ingrediente
 * @param status estado del ingrediente ({@link IngredientStatus})
 */
public record Ingredient(long id, IngredientStatus status) {}
