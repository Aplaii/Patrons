/**
 * Package contenant les vues de l'application (Design Pattern MVC - Vue).
 * <p>
 * Les vues observent le {@link game.model.GameModel} et se mettent à jour
 * automatiquement via le pattern Observer :
 * <ul>
 *     <li>{@link game.view.MainFrame} : fenêtre principale assemblant les composants.</li>
 *     <li>{@link game.view.GamePanel} : panneau de dessin des formes.</li>
 *     <li>{@link game.view.ControlPanel} : panneau de contrôle avec les boutons.</li>
 * </ul>
 * </p>
 *
 * @see game.view.MainFrame
 * @see game.model.ModelListener
 */
package game.view;
