package game.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import game.controller.MouseController;
import game.model.GameModel;
import game.model.IShape;
import game.model.ModelListener;

/**
 * Panneau principal gérant l'affichage graphique (Vue).
 * Il écoute les changements du Modèle pour se redessiner.
 */
public class GamePanel extends JPanel implements ModelListener {
    private GameModel model;

    public GamePanel(GameModel model) {
        this.model = model;
        this.model.addListener(this);
        this.setBackground(Color.WHITE);
    }

    public void setController(MouseController controller) {
        this.addMouseListener(controller);
        this.addMouseMotionListener(controller);
    }

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

    @Override
    public void modelChanged() {
        repaint();
    }
}
