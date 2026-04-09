package game.command;

import game.model.GameModel;
import game.shapes.*;

/**
 * Commande concrète pour modifier une forme (déplacement de points de contrôle, etc.).
 * <p>
 * Stocke l'état initial et l'état final de la forme via le pattern Prototype (clone),
 * permettant l'annulation et le rétablissement de la modification.
 * </p>
 *
 * @see Command
 * @see IShape#clone()
 * @see IShape#copyStateFrom(IShape)
 */
public class ModifyShapeCommand implements Command {
    /** Le modèle de jeu. */
    private GameModel model;
    /** La forme cible de la modification. */
    private IShape shape;
    /** Clone de l'état avant modification. */
    private IShape oldState;
    /** Clone de l'état après modification. */
    private IShape newState;

    /**
     * Construit une commande de modification de forme.
     * Les états initial et final sont clonés pour garantir l'indépendance
     * des données sauvegardées.
     *
     * @param model    Le modèle de jeu.
     * @param shape    La forme à modifier.
     * @param oldState L'état de la forme avant modification.
     * @param newState L'état de la forme après modification.
     */
    public ModifyShapeCommand(GameModel model, IShape shape, IShape oldState, IShape newState) {
        this.model = model;
        this.shape = shape;
        this.oldState = oldState.clone();
        this.newState = newState.clone();
    }

    /**
     * {@inheritDoc}
     * Restaure la forme à son état final (après modification).
     */
    @Override
    public void execute() {
        shape.copyStateFrom(newState);
        model.notifyListeners();
    }

    /**
     * {@inheritDoc}
     * Restaure la forme à son état initial (avant modification).
     */
    @Override
    public void undo() {
        shape.copyStateFrom(oldState);
        model.notifyListeners();
    }
}
