package game.command;

/**
 * Interface du patron de conception Command.
 * Permet d'encapsuler une requête sous forme d'objet.
 */
public interface Command {
    void execute();

    void undo();
}
