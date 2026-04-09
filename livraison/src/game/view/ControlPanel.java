package game.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.command.CommandManager;
import game.controller.MouseController;
import game.model.GameModel;
import game.model.ModelListener;

/**
 * Panneau de contrôle (Design Pattern MVC - Vue) contenant les boutons d'interaction.
 * <p>
 * Permet de :
 * <ul>
 *     <li>Sélectionner le type de forme à créer (Cercle, Rectangle).</li>
 *     <li>Basculer en mode suppression.</li>
 *     <li>Annuler (Undo) et rétablir (Redo) des actions.</li>
 *     <li>Activer/désactiver le mode difficile (Hard Mode).</li>
 *     <li>Afficher le score courant (aire totale des formes bleues).</li>
 * </ul>
 * Écoute les changements du modèle pour mettre à jour dynamiquement
 * le score et l'état des boutons Undo/Redo.
 * </p>
 *
 * @see GameModel
 * @see CommandManager
 * @see MouseController
 */
public class ControlPanel extends JPanel implements ModelListener {
    /** Le modèle de jeu. */
    private GameModel model;
    /** Le gestionnaire de commandes. */
    private CommandManager commandManager;
    /** Le contrôleur de souris. */
    private MouseController controller;

    /** Bouton de création de cercle. */
    private JButton circleBtn;
    /** Bouton de création de rectangle. */
    private JButton rectBtn;
    /** Bouton de suppression. */
    private JButton deleteBtn;
    /** Bouton d'annulation (Undo). */
    private JButton undoBtn;
    /** Bouton de rétablissement (Redo). */
    private JButton redoBtn;
    /** Case à cocher du mode difficile. */
    private JCheckBox hardModeCheck;
    /** Label affichant le score (aire totale). */
    private JLabel scoreLabel;

    /**
     * Construit le panneau de contrôle avec tous les boutons et écoute les événements.
     *
     * @param model          Le modèle de jeu.
     * @param commandManager Le gestionnaire de commandes pour l'undo/redo.
     * @param controller     Le contrôleur de souris.
     */
    public ControlPanel(GameModel model, CommandManager commandManager, MouseController controller) {
        this.model = model;
        this.commandManager = commandManager;
        this.controller = controller;
        this.model.addListener(this);

        setLayout(new FlowLayout());

        circleBtn = new JButton("Cercle");
        rectBtn = new JButton("Rectangle");
        deleteBtn = new JButton("Supprimer");
        undoBtn = new JButton("Undo");
        redoBtn = new JButton("Redo");
        hardModeCheck = new JCheckBox("Hard Mode");
        scoreLabel = new JLabel("Score (Aire): 0");

        circleBtn.addActionListener(e -> controller.setModeCircleCreation());
        rectBtn.addActionListener(e -> controller.setModeRectangleCreation());
        deleteBtn.addActionListener(e -> controller.setModeSuppression());
        undoBtn.addActionListener(e -> {
            commandManager.undo();
            updateButtons();
        });
        redoBtn.addActionListener(e -> {
            commandManager.redo();
            updateButtons();
        });

        hardModeCheck.addActionListener(e -> {
            boolean enabled = hardModeCheck.isSelected();
            model.setHardModeEnabled(enabled);
        });

        add(circleBtn);
        add(rectBtn);
        add(deleteBtn);
        add(undoBtn);
        add(redoBtn);
        add(hardModeCheck);
        add(scoreLabel);

        updateButtons();
    }

    /**
     * Met à jour l'état activé/désactivé des boutons Undo et Redo
     * en fonction de l'historique de commandes.
     */
    public void updateButtons() {
        undoBtn.setEnabled(commandManager.canUndo());
        redoBtn.setEnabled(commandManager.canRedo());
    }

    /**
     * {@inheritDoc}
     * Met à jour le score affiché et l'état des boutons lorsque le modèle change.
     */
    @Override
    public void modelChanged() {
        scoreLabel.setText(String.format("Score (Aire): %.2f", model.getTotalBlueArea()));
        updateButtons();
    }
}
