package game.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameModel implements ShapeListener {
    private List<IShape> redShapes;
    private List<IShape> blueShapes;
    private boolean hardModeEnabled;
    private boolean redShapesVisible;
    private List<ModelListener> listeners;

    public GameModel() {
        redShapes = new ArrayList<>();
        blueShapes = new ArrayList<>();
        listeners = new ArrayList<>();
        hardModeEnabled = false;
        redShapesVisible = true;
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (ModelListener l : listeners) {
            l.modelChanged();
        }
    }

    public void addRedShape(IShape shape) {
        shape.setColor(Color.RED);
        redShapes.add(shape);
        notifyListeners();
    }

    public void clearRedShapes() {
        redShapes.clear();
        notifyListeners();
    }

    public boolean canAddBlueShape(IShape shape) {
        java.awt.geom.Rectangle2D bounds = shape.getAwtShape().getBounds2D();
        if (bounds.getMinX() < 0 || bounds.getMinY() < 0 ||
                bounds.getMaxX() > 1024 || bounds.getMaxY() > 800) {
            return false;
        }

        if (blueShapes.size() >= 4) {
            return false;
        }

        if (hardModeEnabled && redShapesVisible) {
            return false;
        }

        for (IShape redShape : redShapes) {
            if (shape.intersects(redShape)) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveOrResizeBlueShape(IShape shape) {
        java.awt.geom.Rectangle2D bounds = shape.getAwtShape().getBounds2D();
        if (bounds.getMinX() < 0 || bounds.getMinY() < 0 ||
                bounds.getMaxX() > 1024 || bounds.getMaxY() > 735) {
            return false;
        }

        for (IShape redShape : redShapes) {
            if (shape.intersects(redShape)) {
                return false;
            }
        }
        return true;
    }

    public void addBlueShape(IShape shape) {
        shape.setColor(Color.BLUE);
        blueShapes.add(shape);
        shape.addShapeListener(this);
        notifyListeners();
    }

    public void removeBlueShape(IShape shape) {
        shape.removeShapeListener(this);
        blueShapes.remove(shape);
        notifyListeners();
    }

    public List<IShape> getRedShapes() {
        return redShapes;
    }

    public List<IShape> getBlueShapes() {
        return blueShapes;
    }

    public void setHardModeEnabled(boolean hardModeEnabled) {
        if (this.hardModeEnabled != hardModeEnabled) {
            this.hardModeEnabled = hardModeEnabled;
            notifyListeners();
        }
    }

    public boolean isHardModeEnabled() {
        return hardModeEnabled;
    }

    public boolean isRedShapesVisible() {
        return redShapesVisible;
    }

    public void setRedShapesVisible(boolean redShapesVisible) {
        if (this.redShapesVisible != redShapesVisible) {
            this.redShapesVisible = redShapesVisible;
            notifyListeners();
        }
    }

    public double getTotalBlueArea() {
        if (blueShapes.isEmpty())
            return 0;

        java.awt.geom.Area union = new java.awt.geom.Area();
        for (IShape shape : blueShapes) {
            union.add(new java.awt.geom.Area(shape.getAwtShape()));
        }
        return calculateArea(union);
    }

    private double calculateArea(java.awt.geom.Area area) {
        double totalArea = 0;
        java.awt.geom.PathIterator iterator = area.getPathIterator(null, 1.0);
        double[] coords = new double[6];
        double startX = 0, startY = 0;
        double curX = 0, curY = 0;

        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            switch (type) {
                case java.awt.geom.PathIterator.SEG_MOVETO:
                    startX = coords[0];
                    startY = coords[1];
                    curX = startX;
                    curY = startY;
                    break;
                case java.awt.geom.PathIterator.SEG_LINETO:
                    totalArea += (curX * coords[1] - coords[0] * curY);
                    curX = coords[0];
                    curY = coords[1];
                    break;
                case java.awt.geom.PathIterator.SEG_CLOSE:
                    totalArea += (curX * startY - startX * curY);
                    break;
            }
            iterator.next();
        }
        return Math.abs(totalArea) / 2.0;
    }

    public IShape getShapeAt(java.awt.geom.Point2D p) {
        for (int i = blueShapes.size() - 1; i >= 0; i--) {
            if (blueShapes.get(i).contains(p)) {
                return blueShapes.get(i);
            }
        }
        return null;
    }

    public void deselectAll() {
        boolean changed = false;
        for (IShape shape : blueShapes) {
            if (shape.isSelected()) {
                shape.setSelected(false);
                changed = true;
            }
        }
        if (changed)
            notifyListeners();
    }

    @Override
    public void shapeChanged(IShape shape) {
        notifyListeners();
    }
}
