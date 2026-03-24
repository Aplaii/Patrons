package game.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RectangleShape extends ShapeBase {
    private double x, y, width, height;

    public RectangleShape(double x, double y, double width, double height, Color color) {
        super(color);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        Rectangle2D.Double rect = new Rectangle2D.Double(x, y, width, height);
        g2d.fill(rect);
        if (selected) {
            g2d.setColor(Color.YELLOW);
            g2d.draw(rect);
        }
    }

    @Override
    public boolean contains(Point2D p) {
        return p.getX() >= x && p.getX() <= x + width && p.getY() >= y && p.getY() <= y + height;
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
        return width * height;
    }

    @Override
    public void translate(double dx, double dy) {
        x += dx;
        y += dy;
        notifyShapeListeners();
    }

    @Override
    public void scale(double factor, Point2D center) {
        x = center.getX() + (x - center.getX()) * factor;
        y = center.getY() + (y - center.getY()) * factor;
        width *= factor;
        height *= factor;
        notifyShapeListeners();
    }

    @Override
    public Shape getAwtShape() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public IShape clone() {
        return new RectangleShape(x, y, width, height, color);
    }
}
