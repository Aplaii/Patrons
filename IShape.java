package game.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.Shape;

/**
 * Interface représentant une forme géométrique dans le jeu.
 * Protocole commun (Operations) que toutes les formes devront implémenter.
 */
public interface IShape extends Cloneable {
    void draw(Graphics2D g2d);

    boolean contains(Point2D p);

    boolean intersects(IShape other);

    double getArea();

    void translate(double dx, double dy);

    void scale(double factor, Point2D center);

    Color getColor();

    void setColor(Color color);

    boolean isSelected();

    void setSelected(boolean selected);

    Shape getAwtShape();

    IShape clone();
}
