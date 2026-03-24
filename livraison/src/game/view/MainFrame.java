package game.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import game.command.CommandManager;
import game.controller.MouseController;
import game.model.GameModel;

/**
 * Fenêtre principale de l'application Swing.
 * Assemble le modèle, la vue (GamePanel, ControlPanel) et le contrôleur.
 */
public class MainFrame extends JFrame {
    private GameModel model;
    private CommandManager commandManager;
    private GamePanel gamePanel;
    private ControlPanel controlPanel;
    private MouseController mouseController;

    public MainFrame(GameModel model, CommandManager commandManager) {
        super("Jeu des Formes 2D");
        this.model = model;
        this.commandManager = commandManager;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        gamePanel = new GamePanel(model);
        mouseController = new MouseController(model, gamePanel, commandManager);
        gamePanel.setController(mouseController);

        controlPanel = new ControlPanel(model, commandManager, mouseController);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
