package game.view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.command.CommandManager;
import game.controller.MouseController;
import game.controller.ShapeType;
import game.model.GameModel;
import game.model.ModelListener;

/**
 * Panneau de contrôle (Vue) contenant les boutons d'interaction.
 * Permet de sélectionner la forme à dessiner, de supprimer,
 * et d'annuler/rétablir des actions.
 */
public class ControlPanel extends JPanel implements ModelListener {
    private GameModel model;
    private CommandManager commandManager;
    private MouseController controller;

    private JButton circleBtn;
    private JButton rectBtn;
    private JButton deleteBtn;
    private JButton undoBtn;
    private JButton redoBtn;
    private JCheckBox hardModeCheck;
    private JLabel scoreLabel;

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

        circleBtn.addActionListener(e -> controller.setCurrentShapeType(ShapeType.CIRCLE));
        rectBtn.addActionListener(e -> controller.setCurrentShapeType(ShapeType.RECTANGLE));
        deleteBtn.addActionListener(e -> controller.deleteSelectedShape());
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
            // Re-trigger game logic for hard mode if necessary
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

    public void updateButtons() {
        undoBtn.setEnabled(commandManager.canUndo());
        redoBtn.setEnabled(commandManager.canRedo());
    }

    @Override
    public void modelChanged() {
        scoreLabel.setText(String.format("Score (Aire): %.2f", model.getTotalBlueArea()));
        updateButtons();
    }
}
