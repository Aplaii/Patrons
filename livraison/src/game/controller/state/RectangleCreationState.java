package game.controller.state;

import game.controller.MouseController;
import game.model.GameModel;
import game.shapes.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import game.command.CreateShapeCommand;

/**
 * État de création de rectangle.
 * <p>
 * Permet à l'utilisateur de créer un rectangle bleu par clic-glissement :
 * <ul>
 *     <li>Le point de clic définit un coin du rectangle.</li>
 *     <li>Le glissement définit le coin opposé.</li>
 *     <li>Un simple clic (sans glissement) crée un carré de 60x60 pixels centré sur le point.</li>
 * </ul>
 * Utilise l'interpolation par pixel (axes X et Y séparés) pour bloquer la forme
 * contre les obstacles et les limites du plateau. À la fin de la création,
 * bascule en mode sélection.
 * </p>
 *
 * @see ControllerState
 * @see CircleCreationState
 */
public class RectangleCreationState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;
    /** Position de départ (premier coin du rectangle). */
    private Point2D startPos;
    /** Forme de prévisualisation pendant le glissement. */
    private IShape previewShape;
    /** Dernière coordonnée X valide du coin opposé. */
    private double lastValidX;
    /** Dernière coordonnée Y valide du coin opposé. */
    private double lastValidY;

    /**
     * Construit un état de création de rectangle.
     *
     * @param controller Le contrôleur principal.
     * @param model      Le modèle de jeu.
     */
    public RectangleCreationState(MouseController controller, GameModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Si une forme existante est cliquée, bascule en mode modification.
     * Sinon, commence la création d'un rectangle au point cliqué.
     * </p>
     */
    @Override
    public void mousePressed(MouseEvent e) {
        Point2D p = e.getPoint();
        IShape clickedShape = model.getShapeAt(p);
        if (clickedShape != null) {
            controller.setModeModification(clickedShape);
            controller.mousePressed(e);
            return;
        }

        startPos = p;
        lastValidX = p.getX();
        lastValidY = p.getY();
        previewShape = ShapeFactory.getInstance().createRectangle(startPos.getX(), startPos.getY(), 1, 1, Color.BLUE);
        if (model.canAddBlueShape(previewShape)) {
            model.addBlueShape(previewShape);
        } else {
            previewShape = null;
        }
    }

    /**
     * Crée un rectangle à partir de deux points (coins opposés).
     * Gère automatiquement les coordonnées négatives (coins inversés).
     *
     * @param p1 Premier coin.
     * @param p2 Coin opposé (diagonale).
     * @return Une nouvelle forme rectangle.
     */
    private IShape createRect(Point2D p1, Point2D p2) {
        double x = Math.min(p1.getX(), p2.getX());
        double y = Math.min(p1.getY(), p2.getY());
        double w = Math.abs(p1.getX() - p2.getX());
        double h = Math.abs(p1.getY() - p2.getY());
        return ShapeFactory.getInstance().createRectangle(x, y, w, h, Color.BLUE);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Redimensionne la prévisualisation du rectangle par interpolation pixel par pixel.
     * Les axes X et Y sont traités séparément pour permettre le glissement
     * le long d'un obstacle (blocage sur un axe, libre sur l'autre).
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (previewShape == null) return;
        model.removeBlueShape(previewShape);
        
        Point2D p = e.getPoint();
        double dx = p.getX() - lastValidX;
        double dy = p.getY() - lastValidY;
        
        int steps = (int) Math.ceil(Math.max(Math.abs(dx), Math.abs(dy)));
        if (steps > 0) {
            double stepX = dx / steps;
            double stepY = dy / steps;
            
            double currentX = lastValidX;
            double currentY = lastValidY;

            for (int i = 0; i < steps; i++) {
                IShape testX = createRect(startPos, new Point2D.Double(currentX + stepX, currentY));
                if (model.canAddBlueShape(testX)) {
                    currentX += stepX;
                }
                
                IShape testY = createRect(startPos, new Point2D.Double(currentX, currentY + stepY));
                if (model.canAddBlueShape(testY)) {
                    currentY += stepY;
                }
            }
            
            lastValidX = currentX;
            lastValidY = currentY;
            previewShape = createRect(startPos, new Point2D.Double(currentX, currentY));
        }

        model.addBlueShape(previewShape);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Finalise la création du rectangle. Si le glissement est très court (&lt;5px),
     * un carré de 60x60 pixels centré est créé. La création est enregistrée
     * comme commande pour l'historique undo/redo, puis le contrôleur bascule
     * en mode sélection.
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (previewShape != null) {
            model.removeBlueShape(previewShape);
            if (startPos.distance(e.getPoint()) < 5) {
                previewShape = ShapeFactory.getInstance().createRectangle(startPos.getX() - 30, startPos.getY() - 30, 60, 60, Color.BLUE);
            }
            if (model.canAddBlueShape(previewShape)) {
                controller.getCommandManager().executeCommand(new CreateShapeCommand(model, previewShape));
            }
            previewShape = null;
        }
        controller.setModeSelection();
    }

    /** {@inheritDoc} */
    @Override
    public void draw(Graphics2D g2d) {}
}
