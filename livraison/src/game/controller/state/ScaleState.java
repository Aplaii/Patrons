package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import game.command.ResizeShapeCommand;

/**
 * Sous-état de mise à l'échelle (scale) d'une forme.
 * <p>
 * Activé par un clic droit ou Shift+clic sur une forme sélectionnée.
 * Le facteur d'échelle est calculé à partir du déplacement horizontal
 * et vertical du curseur. La mise à l'échelle est effectuée autour
 * du centre géométrique de la forme. Le facteur total est enregistré
 * comme une {@link ResizeShapeCommand} pour l'historique undo/redo.
 * </p>
 *
 * @see ModificationState
 * @see ResizeShapeCommand
 * @see IShape#scale(double, Point2D)
 */
public class ScaleState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme à mettre à l'échelle. */
    private IShape shape;
    /** Dernière position connue du curseur. */
    private Point2D lastPos;
    /** Centre géométrique de la forme (point fixe de la mise à l'échelle). */
    private Point2D center;
    /** Facteur d'échelle accumulé pour la commande. */
    private double accScale = 1.0;

    /**
     * Construit un sous-état de mise à l'échelle.
     *
     * @param controller Le contrôleur principal.
     * @param model      Le modèle de jeu.
     * @param shape      La forme à mettre à l'échelle.
     */
    public ScaleState(MouseController controller, GameModel model, IShape shape) {
        this.controller = controller;
        this.model = model;
        this.shape = shape;
    }

    /**
     * {@inheritDoc}
     * Initialise la position de référence et calcule le centre de la forme.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        lastPos = e.getPoint();
        center = new Point2D.Double(shape.getAwtShape().getBounds2D().getCenterX(),
                shape.getAwtShape().getBounds2D().getCenterY());
        accScale = 1.0;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Calcule le facteur d'échelle à partir du déplacement souris
     * (1% par pixel de déplacement). Un facteur minimum de 0.1 est garanti.
     * Si la mise à l'échelle résulte en une collision ou un dépassement,
     * elle est annulée.
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D p = e.getPoint();
        double dx = p.getX() - lastPos.getX();
        double dy = p.getY() - lastPos.getY();
        double factor = 1.0 + (dx + dy) * 0.01;
        if (factor <= 0.1) factor = 0.1;

        shape.scale(factor, center);
        if (!model.canMoveOrResizeBlueShape(shape)) {
            shape.scale(1.0 / factor, center);
        } else {
            accScale *= factor;
            lastPos = p;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Annule la mise à l'échelle visuelle et enregistre le facteur total
     * comme une {@link ResizeShapeCommand} dans l'historique.
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (accScale != 1.0) {
            shape.scale(1.0 / accScale, center);
            controller.getCommandManager().executeCommand(new ResizeShapeCommand(model, shape, accScale, center));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Graphics2D g2d) {}
}
