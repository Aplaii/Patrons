package game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import game.command.CommandManager;
import game.controller.MouseController;
import game.model.GameModel;

/**
 * Fenêtre principale de l'application (Design Pattern MVC - Vue).
 * <p>
 * Assemble les composants graphiques : le panneau de jeu ({@link GamePanel}),
 * le panneau de contrôle ({@link ControlPanel}) et un label d'affichage
 * de l'état courant du contrôleur. Configure le {@link MouseController}
 * comme gestionnaire des interactions souris.
 * </p>
 *
 * @see GamePanel
 * @see ControlPanel
 * @see MouseController
 */
public class MainFrame extends JFrame {
    /** Le modèle de jeu. */
    private GameModel model;
    /** Le gestionnaire de commandes. */
    private CommandManager commandManager;
    /** Le panneau d'affichage du jeu. */
    private GamePanel gamePanel;
    /** Le panneau de contrôle des boutons. */
    private ControlPanel controlPanel;
    /** Le contrôleur de souris. */
    private MouseController mouseController;

    /**
     * Construit la fenêtre principale de l'application.
     * Initialise et assemble tous les composants graphiques.
     *
     * @param model          Le modèle de jeu.
     * @param commandManager Le gestionnaire de commandes pour l'undo/redo.
     */
    public MainFrame(GameModel model, CommandManager commandManager) {
        super("Jeu des Formes 2D");
        this.model = model;
        this.commandManager = commandManager;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        gamePanel = new GamePanel(model);
        mouseController = new MouseController(model, commandManager);
        gamePanel.setController(mouseController);

        controlPanel = new ControlPanel(model, commandManager, mouseController);

        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        JLabel debugLabel = new JLabel("State: SelectionState");
        debugLabel.setHorizontalAlignment(SwingConstants.CENTER);
        debugLabel.setForeground(Color.RED);
        debugLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(debugLabel, BorderLayout.NORTH);

        mouseController.setStateObserver(state -> debugLabel.setText("Active State: " + state));

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Retourne le panneau de jeu.
     *
     * @return Le {@link GamePanel} de cette fenêtre.
     */
    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
