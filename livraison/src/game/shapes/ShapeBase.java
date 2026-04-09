package game.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe abstraite de base pour toutes les formes géométriques.
 * <p>
 * Fournit l'implémentation commune de la gestion de la couleur,
 * de la sélection et du mécanisme Observer (notification des écouteurs).
 * Les classes concrètes ({@link Circle}, {@link RectangleShape}) héritent
 * de cette classe et implémentent les méthodes géométriques spécifiques.
 * </p>
 *
 * @see IShape
 * @see Circle
 * @see RectangleShape
 */
public abstract class ShapeBase implements IShape {
    /** Couleur de la forme. */
    protected Color color;
    /** État de sélection de la forme. */
    protected boolean selected = false;
    /** Liste des écouteurs de changements de cette forme. */
    protected List<ShapeListener> listeners = new ArrayList<>();

    /**
     * Construit une forme avec la couleur spécifiée.
     *
     * @param color La couleur initiale de la forme.
     */
    public ShapeBase(Color color) {
        this.color = color;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * {@inheritDoc}
     * Notifie les écouteurs du changement de couleur.
     */
    @Override
    public void setColor(Color color) {
        this.color = color;
        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSelected() {
        return selected;
    }

    /**
     * {@inheritDoc}
     * Notifie les écouteurs du changement de sélection.
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyShapeListeners();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addShapeListener(ShapeListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeShapeListener(ShapeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Notifie tous les écouteurs que cette forme a changé.
     */
    protected void notifyShapeListeners() {
        for (ShapeListener l : listeners) {
            l.shapeChanged(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract IShape clone();
}
