package game.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import game.command.CommandManager;
import game.model.GameModel;
import game.shapes.*;
import game.controller.state.*;

/**
 * Contrôleur principal de la souris (Design Pattern State).
 * <p>
 * Gère les interactions souris de l'utilisateur en déléguant le traitement
 * des événements à l'état courant ({@link ControllerState}).
 * Permet de basculer entre les différents modes : sélection, création
 * de cercle, création de rectangle, suppression et modification.
 * </p>
 *
 * @see ControllerState
 * @see SelectionState
 * @see CircleCreationState
 * @see RectangleCreationState
 * @see SuppressionState
 * @see ModificationState
 */
public class MouseController extends MouseAdapter {
    /** Le modèle de données du jeu. */
    private GameModel model;
    /** Le gestionnaire de commandes pour l'historique undo/redo. */
    private CommandManager commandManager;
    /** L'état courant du contrôleur. */
    private ControllerState currentState;
    /** Observateur notifié lors des changements d'état. */
    private Consumer<String> stateObserver;

    /**
     * Construit un nouveau contrôleur de souris.
     * L'état initial est {@link SelectionState}.
     *
     * @param model          Le modèle de jeu.
     * @param commandManager Le gestionnaire de commandes.
     */
    public MouseController(GameModel model, CommandManager commandManager) {
        this.model = model;
        this.commandManager = commandManager;
        this.currentState = new SelectionState(this, model);
    }

    /**
     * Définit un observateur qui sera notifié à chaque changement d'état.
     *
     * @param observer Le consommateur recevant le nom de l'état courant.
     */
    public void setStateObserver(Consumer<String> observer) {
        this.stateObserver = observer;
        updateObserver();
    }

    /**
     * Notifie l'observateur d'un changement d'état avec un nom d'état spécifique.
     *
     * @param stateName Le nom de l'état à notifier.
     */
    public void notifyStateChange(String stateName) {
        if (stateObserver != null) {
            stateObserver.accept(stateName);
        }
    }

    /**
     * Met à jour l'observateur avec le nom de l'état courant.
     */
    private void updateObserver() {
        if (stateObserver != null && currentState != null) {
            stateObserver.accept(currentState.getClass().getSimpleName());
        }
    }

    /**
     * Bascule le contrôleur en mode création de cercle.
     * Désélectionne toutes les formes et notifie l'observateur.
     */
    public void setModeCircleCreation() {
        this.currentState = new CircleCreationState(this, model);
        model.deselectAll();
        updateObserver();
    }

    /**
     * Bascule le contrôleur en mode création de rectangle.
     * Désélectionne toutes les formes et notifie l'observateur.
     */
    public void setModeRectangleCreation() {
        this.currentState = new RectangleCreationState(this, model);
        model.deselectAll();
        updateObserver();
    }

    /**
     * Bascule le contrôleur en mode suppression.
     * Désélectionne toutes les formes et notifie l'observateur.
     */
    public void setModeSuppression() {
        this.currentState = new SuppressionState(this, model);
        model.deselectAll();
        updateObserver();
    }

    /**
     * Bascule le contrôleur en mode sélection.
     * Désélectionne toutes les formes et notifie l'observateur.
     */
    public void setModeSelection() {
        this.currentState = new SelectionState(this, model);
        model.deselectAll();
        updateObserver();
    }

    /**
     * Bascule le contrôleur en mode modification pour une forme donnée.
     * La forme est sélectionnée visuellement (avec les poignées de contrôle)
     * et le modèle est notifié du changement.
     *
     * @param shape La forme à modifier.
     */
    public void setModeModification(IShape shape) {
        model.deselectAll();
        shape.setSelected(true);
        this.currentState = new ModificationState(this, model, shape);
        model.notifyListeners();
        updateObserver();
    }

    /**
     * Retourne le gestionnaire de commandes associé à ce contrôleur.
     *
     * @return Le {@link CommandManager} utilisé pour l'historique d'actions.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * {@inheritDoc}
     * Délègue l'événement à l'état courant.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        currentState.mousePressed(e);
    }

    /**
     * {@inheritDoc}
     * Délègue l'événement à l'état courant.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        currentState.mouseDragged(e);
    }

    /**
     * {@inheritDoc}
     * Délègue l'événement à l'état courant.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        currentState.mouseReleased(e);
    }
}
