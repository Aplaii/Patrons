package game.command;

import game.model.IShape;
import game.model.GameModel;

/**
 * Commande concrète pour déplacer une forme bleue existante.
 */
public class MoveShapeCommand implements Command {
    private IShape shape;
    private double dx;
    private double dy;
    private GameModel model;

    public MoveShapeCommand(GameModel model, IShape shape, double dx, double dy) {
        this.model = model;
        this.shape = shape;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void execute() {
        shape.translate(dx, dy);
        model.notifyListeners();
    }

    @Override
    public void undo() {
        shape.translate(-dx, -dy);
        model.notifyListeners();
    }
}
