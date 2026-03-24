package game.command;

import game.model.GameModel;
import game.model.IShape;

/**
 * Commande concrète pour créer et ajouter une forme bleue au modèle.
 */
public class CreateShapeCommand implements Command {
    private GameModel model;
    private IShape shape;

    public CreateShapeCommand(GameModel model, IShape shape) {
        this.model = model;
        this.shape = shape;
    }

    @Override
    public void execute() {
        model.addBlueShape(shape);
    }

    @Override
    public void undo() {
        model.removeBlueShape(shape);
    }
}
