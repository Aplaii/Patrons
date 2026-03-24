package game.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Le Modèle principal (Architecture MVC) qui gère l'état global du jeu.
 * Il maintient les listes des formes rouges et bleues, le score,
 * et s'occupe de notifier la Vue via les ModelListeners.
 */
public class GameModel {
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
        if (blueShapes.size() >= 4) {
            return false;
        }

        if (hardModeEnabled && redShapesVisible) {
            return false; // Wait until they disappear
        }

        for (IShape redShape : redShapes) {
            if (shape.intersects(redShape)) {
                return false;
            }
        }
        return true;
    }

    public boolean canMoveOrResizeBlueShape(IShape shape) {
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
        notifyListeners();
    }

    public void removeBlueShape(IShape shape) {
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
        double total = 0;
        for (IShape shape : blueShapes) {
            total += shape.getArea();
        }
        return total;
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
}
