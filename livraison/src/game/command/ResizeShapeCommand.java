package game.command;

import java.awt.geom.Point2D;

import game.model.IShape;
import game.model.GameModel;

/**
 * Commande concrète pour redimensionner une forme bleue existante.
 */
public class ResizeShapeCommand implements Command {
    private GameModel model;
    private IShape shape;
    private double factor;
    private Point2D center;

    public ResizeShapeCommand(GameModel model, IShape shape, double factor, Point2D center) {
        this.model = model;
        this.shape = shape;
        this.factor = factor;
        this.center = center;
    }

    @Override
    public void execute() {
        shape.scale(factor, center);
        model.notifyListeners();
    }

    @Override
    public void undo() {
        shape.scale(1.0 / factor, center);
        model.notifyListeners();
    }
}
