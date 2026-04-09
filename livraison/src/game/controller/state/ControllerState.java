package game.controller.state;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Interface représentant un état du contrôleur (Design Pattern State).
 * <p>
 * Chaque état définit le comportement des interactions souris
 * (pression, glissement, relâchement) et le dessin optionnel
 * d'éléments visuels temporaires (prévisualisation, etc.).
 * </p>
 *
 * @see game.controller.MouseController
 * @see SelectionState
 * @see CircleCreationState
 * @see RectangleCreationState
 * @see ModificationState
 * @see SuppressionState
 */
public interface ControllerState {

    /**
     * Gère l'événement de pression du bouton de la souris.
     *
     * @param e L'événement souris.
     */
    void mousePressed(MouseEvent e);

    /**
     * Gère l'événement de glissement de la souris (bouton maintenu enfoncé).
     *
     * @param e L'événement souris.
     */
    void mouseDragged(MouseEvent e);

    /**
     * Gère l'événement de relâchement du bouton de la souris.
     *
     * @param e L'événement souris.
     */
    void mouseReleased(MouseEvent e);

    /**
     * Dessine des éléments visuels optionnels liés à cet état
     * (ex : prévisualisation d'une forme en cours de création).
     *
     * @param g2d Le contexte graphique 2D.
     */
    void draw(Graphics2D g2d);
}
