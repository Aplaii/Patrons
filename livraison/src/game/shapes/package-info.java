/**
 * Package contenant les formes géométriques du jeu.
 * <p>
 * Définit la hiérarchie des formes via l'interface {@link game.shapes.IShape}
 * et la classe abstraite {@link game.shapes.ShapeBase}. Les formes concrètes
 * ({@link game.shapes.Circle}, {@link game.shapes.RectangleShape}) sont créées
 * par la fabrique {@link game.shapes.ShapeFactory} (Design Patterns Factory et Singleton).
 * </p>
 * <p>
 * Le pattern Prototype ({@link game.shapes.IShape#clone()}) permet le clonage
 * des formes pour le mécanisme d'undo/redo.
 * </p>
 *
 * @see game.shapes.IShape
 * @see game.shapes.ShapeFactory
 */
package game.shapes;
