package game.command;

import game.model.GameModel;
import game.shapes.*;

/**
 * Commande concrète pour supprimer une forme bleue du modèle.
 * <p>
 * L'annulation (undo) restaure la forme bleue dans le modèle.
 * </p>
 *
 * @see Command
 * @see CreateShapeCommand
 */
public class DeleteShapeCommand implements Command {
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme à supprimer. */
    private IShape shape;

    /**
     * Construit une commande de suppression de forme.
     *
     * @param model Le modèle de jeu.
     * @param shape La forme bleue à supprimer.
     */
    public DeleteShapeCommand(GameModel model, IShape shape) {
        this.model = model;
        this.shape = shape;
    }

    /**
     * {@inheritDoc}
     * Supprime la forme bleue du modèle.
     */
    @Override
    public void execute() {
        model.removeBlueShape(shape);
    }

    /**
     * {@inheritDoc}
     * Restaure la forme bleue dans le modèle.
     */
    @Override
    public void undo() {
        model.addBlueShape(shape);
    }
}
