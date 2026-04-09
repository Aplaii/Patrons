package game.model;

import game.shapes.IShape;
import java.util.List;

/**
 * Stratégie de placement standard.
 * <p>
 * Vérifie les conditions suivantes avant d'autoriser l'ajout ou la modification
 * d'une forme bleue :
 * <ul>
 *     <li>La forme doit rester dans les limites du plateau (1024x735 pixels).</li>
 *     <li>Le nombre maximal de formes bleues est de 4.</li>
 *     <li>La forme ne doit pas entrer en collision avec les formes rouges (obstacles).</li>
 * </ul>
 * </p>
 *
 * @see IPlacementStrategy
 * @see HardModePlacementStrategy
 */
public class StandardPlacementStrategy implements IPlacementStrategy {

    /**
     * {@inheritDoc}
     * <p>
     * Vérifie les limites du plateau, le nombre maximal de formes bleues (4)
     * et l'absence de collision avec les obstacles rouges.
     * </p>
     */
    @Override
    public boolean canAddBlueShape(IShape shape, List<IShape> blueShapes, List<IShape> redShapes) {
        java.awt.geom.Rectangle2D bounds = shape.getAwtShape().getBounds2D();
        
        if (bounds.getMinX() < 0 || bounds.getMinY() < 0 ||
                bounds.getMaxX() > 1024 || bounds.getMaxY() > 735) {
            return false;
        }

        if (blueShapes.size() >= 4) {
            return false;
        }

        for (IShape redShape : redShapes) {
            if (shape.intersects(redShape)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Vérifie les limites du plateau et l'absence de collision
     * avec les obstacles rouges.
     * </p>
     */
    @Override
    public boolean canMoveOrResizeBlueShape(IShape shape, List<IShape> redShapes) {
        java.awt.geom.Rectangle2D bounds = shape.getAwtShape().getBounds2D();
        
        if (bounds.getMinX() < 0 || bounds.getMinY() < 0 ||
                bounds.getMaxX() > 1024 || bounds.getMaxY() > 735) {
            return false;
        }

        for (IShape redShape : redShapes) {
            if (shape.intersects(redShape)) {
                return false;
            }
        }
        
        return true;
    }
}
