package game.model;

import game.shapes.IShape;
import java.util.List;

/**
 * Interface pour la stratégie de placement des formes (Design Pattern Strategy).
 * <p>
 * Définit les règles pour ajouter ou modifier des formes bleues en fonction
 * du contexte du jeu (formes rouges obstacles, limites du plateau, etc.).
 * </p>
 *
 * @see StandardPlacementStrategy
 * @see HardModePlacementStrategy
 */
public interface IPlacementStrategy {

    /**
     * Vérifie si une forme bleue peut être ajoutée au plateau.
     *
     * @param shape      La forme à tester.
     * @param blueShapes Liste des formes bleues actuelles.
     * @param redShapes  Liste des formes rouges (obstacles) actuelles.
     * @return {@code true} si l'ajout est permis, {@code false} sinon.
     */
    boolean canAddBlueShape(IShape shape, List<IShape> blueShapes, List<IShape> redShapes);

    /**
     * Vérifie si une forme bleue peut être déplacée ou redimensionnée.
     *
     * @param shape     La forme à tester.
     * @param redShapes Liste des formes rouges (obstacles) actuelles.
     * @return {@code true} si la modification est permise, {@code false} sinon.
     */
    boolean canMoveOrResizeBlueShape(IShape shape, List<IShape> redShapes);
}
