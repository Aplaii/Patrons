package game.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Forme concrète représentant un rectangle.
 * <p>
 * Défini par son coin supérieur gauche (x, y), sa largeur et sa hauteur.
 * Le rectangle est dessiné avec les poignées de contrôle aux quatre coins
 * lorsqu'il est sélectionné.
 * </p>
 *
 * @see ShapeBase
 * @see Circle
 */
public class RectangleShape extends ShapeBase {
    /** Coordonnée X du coin supérieur gauche. */
    private double x;
    /** Coordonnée Y du coin supérieur gauche. */
    private double y;
    /** Largeur du rectangle. */
    private double width;
    /** Hauteur du rectangle. */
    private double height;

    /**
     * Construit un rectangle avec les paramètres spécifiés.
     *
     * @param x      Coordonnée X du coin supérieur gauche.
     * @param y      Coordonnée Y du coin supérieur gauche.
     * @param width  Largeur du rectangle.
     * @param height Hauteur du rectangle.
     * @param color  Couleur du rectangle.
     */
    public RectangleShape(double x, double y, double width, double height, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dessine le rectangle rempli. Si sélectionné, ajoute un contour jaune
     * et des poignées de contrôle noires aux quatre coins.
     * </p>
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
        g2d.fill(rect);
        if (selected) {
            g2d.setColor(Color.YELLOW);
            g2d.draw(rect);
            drawHandles(g2d);
        }
    }

    /**
     * Dessine les poignées de contrôle (carrés noirs) aux points de contrôle.
     *
     * @param g2d Le contexte graphique 2D.
     */
    private void drawHandles(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        for (Point2D p : getControlPoints()) {
            g2d.fillRect((int) p.getX() - 3, (int) p.getY() - 3, 6, 6);
        }
    }

    /**
     * {@inheritDoc}
     * Vérifie si le point est dans les bornes du rectangle.
     */
    @Override
    public boolean contains(Point2D p) {
        return p.getX() >= x && p.getX() <= x + width && p.getY() >= y && p.getY() <= y + height;
    }

    /**
     * {@inheritDoc}
     * Utilise les zones géométriques AWT pour une détection précise.
     */
    @Override
    public boolean intersects(IShape other) {
        Area area1 = new Area(this.getAwtShape());
        Area area2 = new Area(other.getAwtShape());
        area1.intersect(area2);
        return !area1.isEmpty();
    }

    /**
     * {@inheritDoc}
     * Calcule l'aire avec la formule largeur × hauteur.
     */
    @Override
    public double getArea() {
        return width * height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     * Le coin et les dimensions sont mis à l'échelle par rapport au point central donné.
     */
    @Override
    public void scale(double factor, Point2D center) {
        x = center.getX() + (x - center.getX()) * factor;
        y = center.getY() + (y - center.getY()) * factor;
        width *= factor;
        height *= factor;
        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     * Retourne les quatre coins du rectangle :
     * haut-gauche, haut-droite, bas-droite, bas-gauche.
     */
    @Override
    public Point2D[] getControlPoints() {
        return new Point2D[] {
                new Point2D.Double(x, y),
                new Point2D.Double(x + width, y),
                new Point2D.Double(x + width, y + height),
                new Point2D.Double(x, y + height)
        };
    }

    /**
     * {@inheritDoc}
     * Tolérance de détection de 5 pixels autour du point de contrôle.
     */
    @Override
    public int hitControlPoint(Point2D p) {
        Point2D[] points = getControlPoints();
        for (int i = 0; i < points.length; i++) {
            if (p.distance(points[i]) <= 5) {
                return i;
            }
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Le redimensionnement recalcule les coordonnées et dimensions du rectangle
     * en fonction du coin déplacé. Le coin opposé reste fixe.
     * Les valeurs min/max sont utilisées pour garantir des dimensions positives.
     * </p>
     */
    @Override
    public void moveControlPoint(int index, Point2D newPos) {
        double newX1 = x, newY1 = y, newX2 = x + width, newY2 = y + height;
        switch (index) {
            case 0:
                newX1 = newPos.getX();
                newY1 = newPos.getY();
                break;
            case 1:
                newX2 = newPos.getX();
                newY1 = newPos.getY();
                break;
            case 2:
                newX2 = newPos.getX();
                newY2 = newPos.getY();
                break;
            case 3:
                newX1 = newPos.getX();
                newY2 = newPos.getY();
                break;
        }
        x = Math.min(newX1, newX2);
        y = Math.min(newY1, newY2);
        width = Math.abs(newX1 - newX2);
        height = Math.abs(newY1 - newY2);
        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getAwtShape() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    /**
     * {@inheritDoc}
     * Crée une copie indépendante du rectangle avec les mêmes coordonnées, dimensions et état.
     */
    @Override
    public IShape clone() {
        RectangleShape r = new RectangleShape(x, y, width, height, color);
        r.setSelected(this.selected);
        return r;
    }

    /**
     * {@inheritDoc}
     * Copie l'état d'un autre rectangle dans cet objet.
     *
     * @param other L'autre forme (doit être une instance de {@link RectangleShape}).
     */
    @Override
    public void copyStateFrom(IShape other) {
        if (other instanceof RectangleShape) {
            RectangleShape o = (RectangleShape) other;
            this.x = o.x;
            this.y = o.y;
            this.width = o.width;
            this.height = o.height;
            this.color = o.color;
            this.selected = o.selected;
            notifyShapeListeners();
        }
    }
}
