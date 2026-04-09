/**
 * Package racine de l'application "Jeu des Formes 2D".
 * <p>
 * Contient le point d'entrée ({@link game.Main}) qui initialise le modèle,
 * crée les obstacles aléatoires et lance l'interface graphique.
 * </p>
 * <p>
 * <b>Design Patterns utilisés :</b>
 * <ul>
 *     <li><b>MVC</b> : Séparation Modèle ({@link game.model}), Vue ({@link game.view}),
 *         Contrôleur ({@link game.controller}).</li>
 *     <li><b>Command</b> : Historique d'actions avec undo/redo ({@link game.command}).</li>
 *     <li><b>State</b> : Gestion des modes d'interaction ({@link game.controller.state}).</li>
 *     <li><b>Strategy</b> : Règles de placement interchangeables
 *         ({@link game.model.IPlacementStrategy}).</li>
 *     <li><b>Factory + Singleton</b> : Création centralisée des formes
 *         ({@link game.shapes.ShapeFactory}).</li>
 *     <li><b>Observer</b> : Notification des vues par le modèle
 *         ({@link game.model.ModelListener}, {@link game.shapes.ShapeListener}).</li>
 *     <li><b>Prototype</b> : Clonage des formes pour l'undo/redo
 *         ({@link game.shapes.IShape#clone()}).</li>
 * </ul>
 * </p>
 */
package game;
