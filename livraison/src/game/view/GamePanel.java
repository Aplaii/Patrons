package game.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import game.controller.MouseController;
import game.model.GameModel;
import game.model.ModelListener;
import game.shapes.*;

/**
 * Panneau principal gérant l'affichage graphique des formes (Design Pattern MVC - Vue).
 * <p>
 * Écoute les changements du {@link GameModel} via le pattern Observer et
 * se redessine automatiquement. Dessine les formes rouges (si visibles)
 * puis les formes bleues, avec l'antialiasing activé.
 * </p>
 *
 * @see GameModel
 * @see ModelListener
 * @see MainFrame
 */
public class GamePanel extends JPanel implements ModelListener {
    /** Le modèle de jeu à afficher. */
    private GameModel model;

    /**
     * Construit le panneau de jeu avec le modèle spécifié.
     * S'enregistre comme écouteur du modèle et configure le fond blanc
     * et la taille du plateau (1024x735 pixels).
     *
     * @param model Le modèle de jeu.
     */
    public GamePanel(GameModel model) {
        this.model = model;
        this.model.addListener(this);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new java.awt.Dimension(1024, 735));
    }

    /**
     * Attache un contrôleur de souris à ce panneau.
     * Le contrôleur recevra les événements de clic et de glissement.
     *
     * @param controller Le contrôleur de souris à attacher.
     */
    public void setController(MouseController controller) {
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }

    /**
     * Dessine le contenu du panneau : formes rouges (obstacles, si visibles)
     * puis formes bleues (créées par le joueur).
     *
     * @param g Le contexte graphique.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (model.isRedShapesVisible()) {
            for (IShape shape : model.getRedShapes()) {
                shape.draw(g2d);
            }
        }

        for (IShape shape : model.getBlueShapes()) {
            shape.draw(g2d);
        }
    }

    /**
     * {@inheritDoc}
     * Redessine le panneau lorsque le modèle change.
     */
    @Override
    public void modelChanged() {
        repaint();
    }
}
