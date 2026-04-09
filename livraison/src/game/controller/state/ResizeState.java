package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import game.command.ModifyShapeCommand;

/**
 * Sous-état de redimensionnement d'une forme via les poignées de contrôle.
 * <p>
 * Permet de redimensionner une forme bleue en déplaçant l'une de ses
 * quatre poignées de contrôle (coins du bounding box).
 * Utilise l'interpolation pixel par pixel avec gestion séparée des axes X et Y.
 * L'état initial est sauvegardé et une {@link ModifyShapeCommand} est créée
 * à la fin pour l'historique undo/redo.
 * </p>
 *
 * @see ModificationState
 * @see ModifyShapeCommand
 * @see IShape#moveControlPoint(int, Point2D)
 */
public class ResizeState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme à redimensionner. */
    private IShape shape;
    /** Index de la poignée de contrôle déplacée (0-3). */
    private int handleIndex;
    /** Clone de l'état initial avant redimensionnement. */
    private IShape initialState;

    /**
     * Construit un sous-état de redimensionnement.
     *
     * @param controller  Le contrôleur principal.
     * @param model       Le modèle de jeu.
     * @param shape       La forme à redimensionner.
     * @param handleIndex L'index de la poignée saisie (0 à 3).
     */
    public ResizeState(MouseController controller, GameModel model, IShape shape, int handleIndex) {
        this.controller = controller;
        this.model = model;
        this.shape = shape;
        this.handleIndex = handleIndex;
    }

    /**
     * {@inheritDoc}
     * Sauvegarde l'état initial de la forme pour l'undo.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        initialState = shape.clone();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Déplace la poignée pixel par pixel en testant chaque micro-déplacement
     * séparément sur X et Y. Les positions invalides (collision, hors limites)
     * sont annulées, permettant le redimensionnement le long des obstacles.
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        IShape backup = shape.clone();
        Point2D currentPos = shape.getControlPoints()[handleIndex];
        Point2D targetPos = e.getPoint();
        
        double dx = targetPos.getX() - currentPos.getX();
        double dy = targetPos.getY() - currentPos.getY();
        
        int steps = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)));
        if (steps > 0) {
            double stepX = dx / steps;
            double stepY = dy / steps;
            
            double testX = currentPos.getX();
            double testY = currentPos.getY();

            IShape tempShape = backup.clone();

            for (int i = 0; i < steps; i++) {
                tempShape.moveControlPoint(handleIndex, new Point2D.Double(testX + stepX, testY));
                if (model.canMoveOrResizeBlueShape(tempShape)) {
                    testX += stepX;
                } else {
                    tempShape.moveControlPoint(handleIndex, new Point2D.Double(testX, testY));
                }

                tempShape.moveControlPoint(handleIndex, new Point2D.Double(testX, testY + stepY));
                if (model.canMoveOrResizeBlueShape(tempShape)) {
                    testY += stepY;
                } else {
                    tempShape.moveControlPoint(handleIndex, new Point2D.Double(testX, testY));
                }
            }
            shape.copyStateFrom(tempShape);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sauvegarde l'état final, restaure l'état initial puis enregistre
     * une {@link ModifyShapeCommand} contenant les deux états pour l'undo/redo.
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        IShape finalState = shape.clone();
        shape.copyStateFrom(initialState);
        controller.getCommandManager().executeCommand(new ModifyShapeCommand(model, shape, initialState, finalState));
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Graphics2D g2d) {}
}
