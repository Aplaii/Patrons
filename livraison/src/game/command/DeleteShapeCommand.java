package game.command;

import game.model.GameModel;
import game.model.IShape;

/**
 * Commande concrète pour supprimer une forme bleue du modèle.
 */
public class DeleteShapeCommand implements Command {
    private GameModel model;
    private IShape shape;

    public DeleteShapeCommand(GameModel model, IShape shape) {
        this.model = model;
        this.shape = shape;
    }

    @Override
    public void execute() {
        model.removeBlueShape(shape);
    }

    @Override
    public void undo() {
        model.addBlueShape(shape);
    }
}
