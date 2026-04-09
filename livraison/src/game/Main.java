package game;

import java.awt.Color;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.command.CommandManager;
import game.model.GameModel;
import game.shapes.*;
import game.view.MainFrame;

/**
 * Point d'entrée de l'application "Jeu des Formes 2D".
 * <p>
 * Initialise le modèle de jeu, génère des obstacles rouges aléatoires,
 * crée la fenêtre principale et configure le mode difficile (Hard Mode)
 * avec un timer de disparition des formes rouges.
 * </p>
 *
 * @author Groupe H
 * @version 1.0
 */
public class Main {

    /**
     * Méthode principale de l'application.
     * <p>
     * Crée le modèle, le gestionnaire de commandes, génère les obstacles rouges,
     * affiche la fenêtre principale et met en place le timer du mode difficile
     * (les formes rouges disparaissent après 10 secondes).
     * </p>
     *
     * @param args Arguments de la ligne de commande (non utilisés).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel();
            CommandManager commandManager = new CommandManager();

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

    /**
     * Génère un nombre donné de formes rouges aléatoires (cercles ou rectangles)
     * et les ajoute au modèle en tant qu'obstacles.
     *
     * @param model Le modèle de jeu auquel ajouter les formes rouges.
     * @param count Le nombre de formes rouges à générer.
     */
    private static void generateRandomRedShapes(GameModel model, int count) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            boolean isCircle = rand.nextBoolean();
            double x = 100 + rand.nextInt(600);
            double y = 100 + rand.nextInt(350);

            if (isCircle) {
                double radius = 30 + rand.nextInt(40);
                model.addRedShape(ShapeFactory.getInstance().createCircle(x, y, radius, Color.RED));
            } else {
                double width = 50 + rand.nextInt(60);
                double height = 50 + rand.nextInt(60);
                model.addRedShape(ShapeFactory.getInstance().createRectangle(x, y, width, height, Color.RED));
            }
        }
    }
}
