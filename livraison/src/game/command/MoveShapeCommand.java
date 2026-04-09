package game.command;

import game.model.GameModel;
import game.shapes.*;

/**
 * Commande concrète pour déplacer une forme bleue existante.
 * <p>
 * Stocke le déplacement total (dx, dy) pour permettre l'annulation
 * par translation inverse.
 * </p>
 *
 * @see Command
 * @see IShape#translate(double, double)
 */
public class MoveShapeCommand implements Command {
    /** La forme à déplacer. */
    private IShape shape;
    /** Déplacement horizontal total. */
    private double dx;
    /** Déplacement vertical total. */
    private double dy;
    /** Le modèle de jeu. */
    private GameModel model;

    /**
     * Construit une commande de déplacement de forme.
     *
     * @param model Le modèle de jeu.
     * @param shape La forme à déplacer.
     * @param dx    Le déplacement horizontal (en pixels).
     * @param dy    Le déplacement vertical (en pixels).
     */
    public MoveShapeCommand(GameModel model, IShape shape, double dx, double dy) {
        this.model = model;
        this.shape = shape;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * {@inheritDoc}
     * Applique la translation (dx, dy) à la forme.
     */
    @Override
    public void execute() {
        shape.translate(dx, dy);
        model.notifyListeners();
    }

    /**
     * {@inheritDoc}
     * Applique la translation inverse (-dx, -dy) à la forme.
     */
    @Override
    public void undo() {
        shape.translate(-dx, -dy);
        model.notifyListeners();
    }
}
