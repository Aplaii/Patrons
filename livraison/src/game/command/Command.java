package game.command;

/**
 * Interface du patron de conception Command.
 * <p>
 * Permet d'encapsuler une requête sous forme d'objet,
 * rendant possible l'annulation (undo) et le rétablissement (redo)
 * des actions effectuées par l'utilisateur.
 * </p>
 *
 * @see CommandManager
 */
public interface Command {

    /**
     * Exécute la commande.
     */
    void execute();

    /**
     * Annule l'exécution de la commande (opération inverse).
     */
    void undo();
}
