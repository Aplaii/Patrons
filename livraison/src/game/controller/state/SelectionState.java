package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * État de sélection - état par défaut du contrôleur.
 * <p>
 * Lorsque l'utilisateur clique sur une forme bleue, le contrôleur
 * bascule automatiquement en mode modification ({@link ModificationState}).
 * Un clic sur une zone vide désélectionne toutes les formes.
 * </p>
 *
 * @see ControllerState
 * @see ModificationState
 */
public class SelectionState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;

    /**
     * Construit un état de sélection.
     *
     * @param controller Le contrôleur principal.
     * @param model      Le modèle de jeu.
     */
    public SelectionState(MouseController controller, GameModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Si une forme bleue est cliquée, bascule en mode modification.
     * Sinon, désélectionne toutes les formes.
     * </p>
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point2D p = e.getPoint();
        IShape clickedShape = model.getShapeAt(p);
        
        if (clickedShape != null) {
            controller.setModeModification(clickedShape);
            controller.mousePressed(e);
        } else {
            model.deselectAll();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void mouseDragged(MouseEvent e) {}

    /** {@inheritDoc} */
    @Override
    public void mouseReleased(MouseEvent e) {}

    /** {@inheritDoc} */
    @Override
    public void draw(Graphics2D g2d) {}
}
