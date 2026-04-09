package game.shapes;

import java.awt.Color;

/**
 * Fabrique de formes (Design Pattern Factory &amp; Singleton).
 * <p>
 * Centralise la création des différentes formes du jeu et garantit
 * une instance unique via le pattern Singleton. Permet de créer
 * des cercles et des rectangles de manière uniforme.
 * </p>
 *
 * @see Circle
 * @see RectangleShape
 * @see IShape
 */
public class ShapeFactory {
    /** Instance unique de la fabrique (Singleton). */
    private static ShapeFactory instance;

    /**
     * Constructeur privé pour empêcher l'instanciation directe (Singleton).
     */
    private ShapeFactory() {
    }

    /**
     * Récupère l'instance unique de la fabrique.
     * Crée l'instance lors du premier appel (lazy initialization).
     *
     * @return L'instance Singleton de {@code ShapeFactory}.
     */
    public static ShapeFactory getInstance() {
        if (instance == null) {
            instance = new ShapeFactory();
        }
        return instance;
    }

    /**
     * Crée un cercle avec les paramètres spécifiés.
     *
     * @param x      Coordonnée X du centre.
     * @param y      Coordonnée Y du centre.
     * @param radius Rayon du cercle.
     * @param color  Couleur du cercle.
     * @return Une nouvelle instance de {@link Circle}.
     */
    public IShape createCircle(double x, double y, double radius, Color color) {
        return new Circle(x, y, radius, color);
    }

    /**
     * Crée un rectangle avec les paramètres spécifiés.
     *
     * @param x      Coordonnée X du coin supérieur gauche.
     * @param y      Coordonnée Y du coin supérieur gauche.
     * @param width  Largeur du rectangle.
     * @param height Hauteur du rectangle.
     * @param color  Couleur du rectangle.
     * @return Une nouvelle instance de {@link RectangleShape}.
     */
    public IShape createRectangle(double x, double y, double width, double height, Color color) {
        return new RectangleShape(x, y, width, height, color);
    }
}
