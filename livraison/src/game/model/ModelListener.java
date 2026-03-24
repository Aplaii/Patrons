package game.model;

/**
 * Interface d'écoute pour l'architecture MVC.
 * Permet à la Vue de s'abonner aux changements du Modèle.
 */
public interface ModelListener {
    void modelChanged();
}
