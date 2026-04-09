package game.model;

import game.shapes.IShape;
import java.util.List;

/**
 * Stratégie de placement pour le mode difficile (Hard Mode).
 * <p>
 * Hérite de {@link StandardPlacementStrategy} et ajoute une contrainte
 * supplémentaire : interdire tout ajout de forme bleue tant que les formes
 * rouges (obstacles) sont visibles. Une fois les obstacles masqués,
 * les règles standard s'appliquent.
 * </p>
 *
 * @see StandardPlacementStrategy
 * @see IPlacementStrategy
 */
public class HardModePlacementStrategy extends StandardPlacementStrategy {

    /** Indicateur de visibilité des formes rouges. */
    private boolean redShapesVisible;

    /**
     * Définit l'état de visibilité des formes rouges.
     *
     * @param redShapesVisible {@code true} si les formes rouges sont visibles.
     */
    public void setRedShapesVisible(boolean redShapesVisible) {
        this.redShapesVisible = redShapesVisible;
    }

    /**
     * {@inheritDoc}
     * <p>
     * En mode difficile, si les formes rouges sont visibles,
     * aucun ajout de forme bleue n'est autorisé.
     * Sinon, les règles de la stratégie standard s'appliquent.
     * </p>
     */
    @Override
    public boolean canAddBlueShape(IShape shape, List<IShape> blueShapes, List<IShape> redShapes) {
        if (redShapesVisible) {
            return false;
        }
        
        return super.canAddBlueShape(shape, blueShapes, redShapes);
    }
}
