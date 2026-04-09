package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;

/**
 * État de modification d'une forme sélectionnée.
 * <p>
 * Agit comme un état composite qui délègue les interactions à un sous-état :
 * <ul>
 *     <li>{@link MoveState} : déplacement par clic-glissement gauche sur la forme.</li>
 *     <li>{@link ScaleState} : mise à l'échelle par clic-droit ou Shift+clic sur la forme.</li>
 *     <li>{@link ResizeState} : redimensionnement via les poignées de contrôle.</li>
 * </ul>
 * Cliquer sur une autre forme change la sélection. Cliquer sur une zone vide
 * revient au mode sélection.
 * </p>
 *
 * @see ControllerState
 * @see MoveState
 * @see ScaleState
 * @see ResizeState
 */
public class ModificationState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme actuellement sélectionnée pour modification. */
    private IShape selectedShape;
    /** Le sous-état actif (Move, Resize ou Scale). */
    private ControllerState subState;

    /**
     * Construit un état de modification pour une forme donnée.
     *
     * @param controller    Le contrôleur principal.
     * @param model         Le modèle de jeu.
     * @param selectedShape La forme sélectionnée à modifier.
     */
    public ModificationState(MouseController controller, GameModel model, IShape selectedShape) {
        this.controller = controller;
        this.model = model;
        this.selectedShape = selectedShape;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Détermine le sous-état approprié en fonction de la cible du clic :
     * <ul>
     *     <li>Poignée de contrôle → {@link ResizeState}</li>
     *     <li>Autre forme → change la sélection</li>
     *     <li>Zone vide → retour au mode sélection</li>
     *     <li>Même forme + clic droit ou Shift → {@link ScaleState}</li>
     *     <li>Même forme + clic gauche → {@link MoveState}</li>
     * </ul>
     * </p>
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point2D p = e.getPoint();

        int handleIndex = selectedShape.hitControlPoint(p);
        if (handleIndex != -1) {
            subState = new ResizeState(controller, model, selectedShape, handleIndex);
            controller.notifyStateChange(subState.getClass().getSimpleName());
            subState.mousePressed(e);
            return;
        }

        IShape clickedShape = model.getShapeAt(p);
        if (clickedShape != null && clickedShape != selectedShape) {
            controller.setModeModification(clickedShape);
            controller.mousePressed(e);
            return;
        } else if (clickedShape == null) {
            controller.setModeSelection();
            return;
        }

        if (SwingUtilities.isRightMouseButton(e) || e.isShiftDown()) {
            subState = new ScaleState(controller, model, selectedShape);
        } else {
            subState = new MoveState(controller, model, selectedShape);
        }
        controller.notifyStateChange(subState.getClass().getSimpleName());
        subState.mousePressed(e);
    }

    /**
     * {@inheritDoc}
     * Délègue au sous-état actif.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (subState != null) {
            subState.mouseDragged(e);
        }
    }

    /**
     * {@inheritDoc}
     * Délègue au sous-état actif puis réinitialise le sous-état.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (subState != null) {
            subState.mouseReleased(e);
            subState = null;
            controller.notifyStateChange(this.getClass().getSimpleName());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Graphics2D g2d) {
    }
}
