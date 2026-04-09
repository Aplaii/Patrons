package game.shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Forme concrète représentant un cercle.
 * <p>
 * Défini par son centre (x, y) et son rayon. Le cercle est dessiné avec
 * les poignées de contrôle aux quatre coins de son bounding box carré
 * lorsqu'il est sélectionné.
 * </p>
 *
 * @see ShapeBase
 * @see RectangleShape
 */
public class Circle extends ShapeBase {
    /** Coordonnée X du centre du cercle. */
    private double x;
    /** Coordonnée Y du centre du cercle. */
    private double y;
    /** Rayon du cercle. */
    private double radius;

    /**
     * Construit un cercle avec les paramètres spécifiés.
     *
     * @param x      Coordonnée X du centre.
     * @param y      Coordonnée Y du centre.
     * @param radius Rayon du cercle.
     * @param color  Couleur du cercle.
     */
    public Circle(double x, double y, double radius, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Dessine le cercle rempli. Si sélectionné, ajoute un contour jaune
     * et des poignées de contrôle noires aux quatre coins.
     * </p>
     */
    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        Ellipse2D.Double ellipse = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
        g2d.fill(ellipse);
        if (selected) {
            g2d.setColor(Color.YELLOW);
            g2d.draw(ellipse);
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
     * Vérifie si le point est à une distance inférieure ou égale au rayon du centre.
     */
    @Override
    public boolean contains(Point2D p) {
        return p.distance(x, y) <= radius;
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
     * Calcule l'aire avec la formule π × r².
     */
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
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
     * Le centre et le rayon sont mis à l'échelle par rapport au point central donné.
     */
    @Override
    public void scale(double factor, Point2D center) {
        x = center.getX() + (x - center.getX()) * factor;
        y = center.getY() + (y - center.getY()) * factor;
        radius *= factor;
        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     * Retourne les quatre coins du bounding box carré du cercle :
     * haut-gauche, haut-droite, bas-droite, bas-gauche.
     */
    @Override
    public Point2D[] getControlPoints() {
        return new Point2D[] {
                new Point2D.Double(x - radius, y - radius),
                new Point2D.Double(x + radius, y - radius),
                new Point2D.Double(x + radius, y + radius),
                new Point2D.Double(x - radius, y + radius)
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
     * Pour un cercle, le redimensionnement conserve un rapport d'aspect 1:1
     * (le bounding box reste un carré). Le point de contrôle opposé reste fixe.
     * </p>
     */
    @Override
    public void moveControlPoint(int index, Point2D newPos) {
        Point2D[] points = getControlPoints();
        Point2D opposite = points[(index + 2) % 4];

        double newX1 = newPos.getX();
        double newY1 = newPos.getY();
        double newX2 = opposite.getX();
        double newY2 = opposite.getY();

        double side = Math.max(Math.abs(newX1 - newX2), Math.abs(newY1 - newY2));

        if (newX1 > newX2)
            newX1 = newX2 + side;
        else
            newX1 = newX2 - side;
        if (newY1 > newY2)
            newY1 = newY2 + side;
        else
            newY1 = newY2 - side;

        radius = side / 2.0;
        x = (newX1 + newX2) / 2.0;
        y = (newY1 + newY2) / 2.0;

        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape getAwtShape() {
        return new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
    }

    /**
     * {@inheritDoc}
     * Crée une copie indépendante du cercle avec les mêmes coordonnées, rayon et état.
     */
    @Override
    public IShape clone() {
        Circle c = new Circle(x, y, radius, color);
        c.setSelected(this.selected);
        return c;
    }

    /**
     * {@inheritDoc}
     * Copie l'état d'un autre cercle dans cet objet.
     *
     * @param other L'autre forme (doit être une instance de {@link Circle}).
     */
    @Override
    public void copyStateFrom(IShape other) {
        if (other instanceof Circle) {
            Circle o = (Circle) other;
            this.x = o.x;
            this.y = o.y;
            this.radius = o.radius;
            this.color = o.color;
            this.selected = o.selected;
            notifyShapeListeners();
        }
    }
}
