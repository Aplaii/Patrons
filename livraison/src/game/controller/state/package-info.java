/**
 * Package contenant les états du contrôleur (Design Pattern State).
 * <p>
 * Chaque état définit le comportement des interactions souris
 * dans un mode spécifique du jeu :
 * <ul>
 *     <li>{@link game.controller.state.SelectionState} : sélection de formes (état par défaut).</li>
 *     <li>{@link game.controller.state.CircleCreationState} : création de cercles.</li>
 *     <li>{@link game.controller.state.RectangleCreationState} : création de rectangles.</li>
 *     <li>{@link game.controller.state.ModificationState} : modification d'une forme sélectionnée.</li>
 *     <li>{@link game.controller.state.MoveState} : déplacement d'une forme.</li>
 *     <li>{@link game.controller.state.ResizeState} : redimensionnement via les poignées.</li>
 *     <li>{@link game.controller.state.ScaleState} : mise à l'échelle globale.</li>
 *     <li>{@link game.controller.state.SuppressionState} : suppression de formes.</li>
 * </ul>
 * </p>
 *
 * @see game.controller.state.ControllerState
 * @see game.controller.MouseController
 */
package game.controller.state;
