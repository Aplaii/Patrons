package game.shapes;


/**
 * Interface d'écoute des changements d'une forme individuelle (Design Pattern Observer).
 * <p>
 * Permet au {@link game.model.GameModel} d'être notifié automatiquement
 * lorsqu'une forme est modifiée (déplacement, redimensionnement, changement de couleur, etc.).
 * </p>
 *
 * @see IShape#addShapeListener(ShapeListener)
 * @see IShape#removeShapeListener(ShapeListener)
 */
public interface ShapeListener {

    /**
     * Appelée lorsqu'une forme a été modifiée.
     *
     * @param shape La forme qui a changé.
     */
    void shapeChanged(IShape shape);
}
