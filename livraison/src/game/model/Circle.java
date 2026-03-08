package game.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * Implémentation concrète d'un Cercle.
 */
public class Circle extends ShapeBase {
    private double x, y, radius;

    public Circle(double x, double y, double radius, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        Ellipse2D.Double ellipse = new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
        g2d.fill(ellipse);
        if (selected) {
            g2d.setColor(Color.YELLOW);
            g2d.draw(ellipse);
        }
    }

    @Override
    public boolean contains(Point2D p) {
        return p.distance(x, y) <= radius;
    }

    @Override
    public boolean intersects(IShape other) {
        Area area1 = new Area(this.getAwtShape());
        Area area2 = new Area(other.getAwtShape());
        area1.intersect(area2);
        return !area1.isEmpty();
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
    }

    @Override
    public void scale(double factor, Point2D center) {
        // Simple scaling
        x = center.getX() + (x - center.getX()) * factor;
        y = center.getY() + (y - center.getY()) * factor;
        radius *= factor;
    }

    @Override
    public Shape getAwtShape() {
        return new Ellipse2D.Double(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public IShape clone() {
        return new Circle(x, y, radius, color);
    }
}
