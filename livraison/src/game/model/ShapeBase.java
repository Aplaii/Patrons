package game.model;

import java.awt.Color;

/**
 * Classe abstraite de base pour factoriser le code commun aux formes,
 * telles que la couleur et l'état de sélection.
 */
public abstract class ShapeBase implements IShape {
    protected Color color;
    protected boolean selected = false;

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
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public abstract IShape clone();
}
