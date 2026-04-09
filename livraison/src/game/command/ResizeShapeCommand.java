package game.command;

import java.awt.geom.Point2D;

import game.shapes.*;
import game.model.GameModel;

/**
 * Commande concrète pour redimensionner (scale) une forme bleue existante.
 * <p>
 * Stocke le facteur d'échelle et le centre de mise à l'échelle pour permettre
 * l'annulation par application du facteur inverse (1/factor).
 * </p>
 *
 * @see Command
 * @see IShape#scale(double, Point2D)
 */
public class ResizeShapeCommand implements Command {
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme à redimensionner. */
    private IShape shape;
    /** Le facteur d'échelle appliqué. */
    private double factor;
    /** Le centre de la mise à l'échelle. */
    private Point2D center;

    /**
     * Construit une commande de redimensionnement de forme.
     *
     * @param model  Le modèle de jeu.
     * @param shape  La forme à redimensionner.
     * @param factor Le facteur d'échelle (ex : 1.5 pour agrandir de 50%).
     * @param center Le point central autour duquel effectuer la mise à l'échelle.
     */
    public ResizeShapeCommand(GameModel model, IShape shape, double factor, Point2D center) {
        this.model = model;
        this.shape = shape;
        this.factor = factor;
        this.center = center;
    }

    /**
     * {@inheritDoc}
     * Applique le facteur d'échelle à la forme autour du centre spécifié.
     */
    @Override
    public void execute() {
        shape.scale(factor, center);
        model.notifyListeners();
    }

    /**
     * {@inheritDoc}
     * Applique le facteur d'échelle inverse (1/factor) pour annuler le redimensionnement.
     */
    @Override
    public void undo() {
        shape.scale(1.0 / factor, center);
        model.notifyListeners();
    }
}
