package game.command;

import game.model.GameModel;
import game.shapes.*;

/**
 * Commande concrète pour créer et ajouter une forme bleue au modèle.
 * <p>
 * L'annulation (undo) supprime la forme bleue du modèle.
 * </p>
 *
 * @see Command
 * @see DeleteShapeCommand
 */
public class CreateShapeCommand implements Command {
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme à créer. */
    private IShape shape;

    /**
     * Construit une commande de création de forme.
     *
     * @param model Le modèle de jeu.
     * @param shape La forme bleue à ajouter.
     */
    public CreateShapeCommand(GameModel model, IShape shape) {
        this.model = model;
        this.shape = shape;
    }

    /**
     * {@inheritDoc}
     * Ajoute la forme bleue au modèle.
     */
    @Override
    public void execute() {
        model.addBlueShape(shape);
    }

    /**
     * {@inheritDoc}
     * Supprime la forme bleue du modèle.
     */
    @Override
    public void undo() {
        model.removeBlueShape(shape);
    }
}
