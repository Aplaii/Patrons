package game.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.Shape;

/**
 * Interface représentant une forme géométrique du jeu.
 * <p>
 * Définit le contrat pour toutes les formes (cercles, rectangles, etc.)
 * incluant le dessin, la détection de collision, la transformation géométrique,
 * la gestion de la sélection et le support du pattern Prototype (clone).
 * </p>
 *
 * @see ShapeBase
 * @see Circle
 * @see RectangleShape
 */
public interface IShape extends Cloneable {

    /**
     * Dessine la forme sur le contexte graphique.
     * Si la forme est sélectionnée, les poignées de contrôle sont également affichées.
     *
     * @param g2d Le contexte graphique 2D.
     */
    void draw(Graphics2D g2d);

    /**
     * Vérifie si un point est contenu dans la forme.
     *
     * @param p Le point à tester.
     * @return {@code true} si le point est à l'intérieur de la forme.
     */
    boolean contains(Point2D p);

    /**
     * Vérifie si cette forme intersecte une autre forme.
     *
     * @param other L'autre forme à tester.
     * @return {@code true} si les deux formes se chevauchent.
     */
    boolean intersects(IShape other);

    /**
     * Calcule l'aire de la forme.
     *
     * @return L'aire en pixels carrés.
     */
    double getArea();

    /**
     * Translate la forme d'un déplacement donné.
     *
     * @param dx Le déplacement horizontal (en pixels).
     * @param dy Le déplacement vertical (en pixels).
     */
    void translate(double dx, double dy);

    /**
     * Applique une mise à l'échelle à la forme autour d'un point central.
     *
     * @param factor Le facteur d'échelle (ex : 2.0 pour doubler la taille).
     * @param center Le point central de la mise à l'échelle.
     */
    void scale(double factor, Point2D center);

    /**
     * Retourne la couleur actuelle de la forme.
     *
     * @return La couleur de la forme.
     */
    Color getColor();

    /**
     * Définit la couleur de la forme.
     *
     * @param color La nouvelle couleur.
     */
    void setColor(Color color);

    /**
     * Indique si la forme est actuellement sélectionnée.
     *
     * @return {@code true} si la forme est sélectionnée.
     */
    boolean isSelected();

    /**
     * Définit l'état de sélection de la forme.
     *
     * @param selected {@code true} pour sélectionner la forme.
     */
    void setSelected(boolean selected);

    /**
     * Retourne la représentation AWT de la forme pour les opérations géométriques.
     *
     * @return L'objet {@link Shape} AWT correspondant.
     */
    Shape getAwtShape();

    /**
     * Crée une copie indépendante de cette forme (Design Pattern Prototype).
     *
     * @return Un clone de cette forme.
     */
    IShape clone();

    /**
     * Ajoute un écouteur de changements de cette forme.
     *
     * @param listener L'écouteur à ajouter.
     */
    void addShapeListener(ShapeListener listener);

    /**
     * Supprime un écouteur de changements de cette forme.
     *
     * @param listener L'écouteur à supprimer.
     */
    void removeShapeListener(ShapeListener listener);

    /**
     * Retourne les points de contrôle de la forme (coins du bounding box).
     * Ces points servent de poignées pour le redimensionnement interactif.
     *
     * @return Un tableau de {@link Point2D} représentant les points de contrôle.
     */
    java.awt.geom.Point2D[] getControlPoints();

    /**
     * Détecte si un point se trouve sur un point de contrôle.
     *
     * @param p Le point à tester.
     * @return L'index du point de contrôle touché, ou {@code -1} si aucun.
     */
    int hitControlPoint(java.awt.geom.Point2D p);

    /**
     * Déplace un point de contrôle vers une nouvelle position.
     * La forme est redimensionnée en conséquence.
     *
     * @param index  L'index du point de contrôle à déplacer.
     * @param newPos La nouvelle position du point de contrôle.
     */
    void moveControlPoint(int index, java.awt.geom.Point2D newPos);

    /**
     * Copie l'état interne d'une autre forme dans cette forme.
     * Utilisé pour le mécanisme d'undo/redo.
     *
     * @param other La forme source dont copier l'état.
     */
    void copyStateFrom(IShape other);
}
