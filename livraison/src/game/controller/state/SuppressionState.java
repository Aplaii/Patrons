package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import game.command.DeleteShapeCommand;

/**
 * État de suppression de formes.
 * <p>
 * Permet à l'utilisateur de supprimer une forme bleue en cliquant dessus.
 * La suppression est enregistrée comme une {@link DeleteShapeCommand}
 * dans l'historique pour permettre l'annulation (undo).
 * </p>
 *
 * @see ControllerState
 * @see DeleteShapeCommand
 */
public class SuppressionState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;

    /**
     * Construit un état de suppression.
     *
     * @param controller Le contrôleur principal.
     * @param model      Le modèle de jeu.
     */
    public SuppressionState(MouseController controller, GameModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Si une forme bleue est cliquée, elle est supprimée via
     * une {@link DeleteShapeCommand}.
     * </p>
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point2D p = e.getPoint();
        IShape clickedShape = model.getShapeAt(p);
        
        if (clickedShape != null) {
            controller.getCommandManager().executeCommand(new DeleteShapeCommand(model, clickedShape));
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
