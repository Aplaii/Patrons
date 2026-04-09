package game.model;

/**
 * Interface d'écoute des changements du modèle (Design Pattern Observer).
 * <p>
 * Les classes implémentant cette interface seront notifiées
 * lorsque l'état du {@link GameModel} change.
 * </p>
 *
 * @see GameModel#addListener(ModelListener)
 * @see GameModel#notifyListeners()
 */
public interface ModelListener {

    /**
     * Appelée lorsque le modèle de jeu a été modifié.
     */
    void modelChanged();
}
