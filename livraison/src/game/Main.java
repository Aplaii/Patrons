package game;

import java.awt.Color;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.command.CommandManager;
import game.model.Circle;
import game.model.GameModel;
import game.model.RectangleShape;
import game.view.MainFrame;

/**
 * Classe de lancement de l'application.
 * Initialise le Modèle, le CommandManager, et la Fenêtre principale.
 * Gère également le timer du mode Difficile.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel();
            CommandManager commandManager = new CommandManager();

            // Generate some random red shapes
            generateRandomRedShapes(model, 5);

            MainFrame frame = new MainFrame(model, commandManager);
            frame.setVisible(true);

            Timer hardModeTimer = new Timer(10000, e -> {
                if (model.isHardModeEnabled()) {
                    model.setRedShapesVisible(false);
                }
            });
            hardModeTimer.setRepeats(false);

            model.addListener(() -> {
                if (model.isHardModeEnabled() && !hardModeTimer.isRunning() && model.isRedShapesVisible()) {
                    hardModeTimer.restart();
                } else if (!model.isHardModeEnabled()) {
                    hardModeTimer.stop();
                    model.setRedShapesVisible(true);
                }
            });

            if (model.isHardModeEnabled()) {
                hardModeTimer.start();
            }
        });
    }

    private static void generateRandomRedShapes(GameModel model, int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            boolean isCircle = rand.nextBoolean();
            double x = 100 + rand.nextInt(600);
            double y = 100 + rand.nextInt(350);

            if (isCircle) {
                double radius = 30 + rand.nextInt(40);
                model.addRedShape(new Circle(x, y, radius, Color.RED));
            } else {
                double width = 50 + rand.nextInt(60);
                double height = 50 + rand.nextInt(60);
                model.addRedShape(new RectangleShape(x, y, width, height, Color.RED));
            }
        }
    }
}
