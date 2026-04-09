package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import game.command.MoveShapeCommand;

/**
 * Sous-état de déplacement d'une forme.
 * <p>
 * Permet de déplacer une forme bleue par clic-glissement.
 * Utilise l'interpolation pixel par pixel avec gestion
 * séparée des axes X et Y pour permettre le glissement
 * le long des obstacles et des bords du plateau.
 * Le déplacement total est enregistré comme une commande
 * unique pour l'historique undo/redo.
 * </p>
 *
 * @see ModificationState
 * @see MoveShapeCommand
 */
public class MoveState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme à déplacer. */
    private IShape shape;
    /** Dernière position connue du curseur. */
    private Point2D lastPos;
    /** Déplacement horizontal accumulé pour la commande. */
    private double accDx = 0;
    /** Déplacement vertical accumulé pour la commande. */
    private double accDy = 0;

    /**
     * Construit un sous-état de déplacement.
     *
     * @param controller Le contrôleur principal.
     * @param model      Le modèle de jeu.
     * @param shape      La forme à déplacer.
     */
    public MoveState(MouseController controller, GameModel model, IShape shape) {
        this.controller = controller;
        this.model = model;
        this.shape = shape;
    }

    /**
     * {@inheritDoc}
     * Initialise la position de référence et remet les accumulateurs à zéro.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        lastPos = e.getPoint();
        accDx = 0;
        accDy = 0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Déplace la forme pixel par pixel en testant chaque micro-déplacement
     * séparément sur X et Y. Les mouvements invalides (collision, hors limites)
     * sont annulés individuellement, permettant le glissement le long des obstacles.
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D p = e.getPoint();
        double dx = p.getX() - lastPos.getX();
        double dy = p.getY() - lastPos.getY();

        int steps = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)));
        if (steps > 0) {
            double stepX = dx / steps;
            double stepY = dy / steps;
            
            double movedX = 0;
            double movedY = 0;

            for (int i = 0; i < steps; i++) {
                shape.translate(stepX, 0);
                if (model.canMoveOrResizeBlueShape(shape)) {
                    movedX += stepX;
                } else {
                    shape.translate(-stepX, 0);
                }

                shape.translate(0, stepY);
                if (model.canMoveOrResizeBlueShape(shape)) {
                    movedY += stepY;
                } else {
                    shape.translate(0, -stepY);
                }
            }

            accDx += movedX;
            accDy += movedY;
        }
        lastPos = p;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Annule le déplacement visuel et enregistre le mouvement total
     * comme une {@link MoveShapeCommand} dans l'historique.
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (accDx != 0 || accDy != 0) {
            shape.translate(-accDx, -accDy);
            controller.getCommandManager().executeCommand(new MoveShapeCommand(model, shape, accDx, accDy));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Graphics2D g2d) {}
}
