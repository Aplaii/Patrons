package game.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class ShapeBase implements IShape {
    protected Color color;
    protected boolean selected = false;
    protected List<ShapeListener> listeners = new ArrayList<>();

    public ShapeBase(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        notifyShapeListeners();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyShapeListeners();
    }

    @Override
    public void addShapeListener(ShapeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeShapeListener(ShapeListener listener) {
        listeners.remove(listener);
    }

    protected void notifyShapeListeners() {
        for (ShapeListener l : listeners) {
            l.shapeChanged(this);
        }
    }

    @Override
    public abstract IShape clone();
}
