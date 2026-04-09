package game.command;

import java.util.Stack;

/**
 * Gestionnaire des commandes pour l'historique d'actions.
 * <p>
 * Implémente le mécanisme d'annulation (Undo) et de rétablissement (Redo)
 * via deux piles (stacks). Chaque nouvelle commande exécutée est empilée
 * sur la pile Undo, et la pile Redo est vidée pour maintenir la cohérence.
 * </p>
 *
 * @see Command
 */
public class CommandManager {
    /** Pile des commandes pouvant être annulées. */
    private Stack<Command> undoStack;
    /** Pile des commandes pouvant être rétablies. */
    private Stack<Command> redoStack;

    /**
     * Construit un nouveau gestionnaire de commandes avec des piles vides.
     */
    public CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    /**
     * Exécute une commande et l'ajoute à la pile Undo.
     * La pile Redo est vidée car une nouvelle action
     * invalide l'historique de rétablissement.
     *
     * @param command La commande à exécuter.
     */
    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    /**
     * Annule la dernière commande exécutée.
     * La commande est transférée de la pile Undo vers la pile Redo.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    /**
     * Rétablit la dernière commande annulée.
     * La commande est transférée de la pile Redo vers la pile Undo.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    /**
     * Vérifie s'il est possible d'annuler une commande.
     *
     * @return {@code true} si la pile Undo n'est pas vide, {@code false} sinon.
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    /**
     * Vérifie s'il est possible de rétablir une commande.
     *
     * @return {@code true} si la pile Redo n'est pas vide, {@code false} sinon.
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
}
