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
 * État de création de cercle.
 * <p>
 * Permet à l'utilisateur de créer un cercle bleu par clic-glissement :
 * <ul>
 *     <li>Le point de clic définit le centre du cercle.</li>
 *     <li>Le glissement définit le rayon (distance du centre au curseur).</li>
 *     <li>Un simple clic (sans glissement) crée un cercle de rayon par défaut (30px).</li>
 * </ul>
 * Utilise l'interpolation par pixel pour bloquer la forme contre les obstacles
 * et les limites du plateau. À la fin de la création, bascule en mode sélection.
 * </p>
 *
 * @see ControllerState
 * @see RectangleCreationState
 */
public class CircleCreationState implements ControllerState {
    /** Le contrôleur principal. */
    private MouseController controller;
    /** Le modèle de jeu. */
    private GameModel model;
    /** Position de départ (centre du cercle). */
    private Point2D startPos;
    /** Forme de prévisualisation pendant le glissement. */
    private IShape previewShape;
    /** Dernier rayon valide trouvé lors de l'interpolation. */
    private double lastValidRadius = 1;

    /**
     * Construit un état de création de cercle.
     *
     * @param controller Le contrôleur principal.
     * @param model      Le modèle de jeu.
     */
    public CircleCreationState(MouseController controller, GameModel model) {
        this.controller = controller;
        this.model = model;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Si une forme existante est cliquée, bascule en mode modification.
     * Sinon, commence la création d'un cercle au point cliqué.
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
        lastValidRadius = 1;
        previewShape = ShapeFactory.getInstance().createCircle(startPos.getX(), startPos.getY(), 1, Color.BLUE);
        if (model.canAddBlueShape(previewShape)) {
            model.addBlueShape(previewShape);
        } else {
            previewShape = null;
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Redimensionne la prévisualisation du cercle par interpolation pixel par pixel.
     * Le rayon est augmenté progressivement jusqu'à la position du curseur,
     * en s'arrêtant au premier obstacle rencontré.
     * </p>
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (previewShape == null) return;
        model.removeBlueShape(previewShape);
        double targetRadius = startPos.distance(e.getPoint());
        
        double dr = targetRadius - lastValidRadius;
        int steps = (int) Math.ceil(Math.abs(dr));
        
        if (steps > 0) {
            double step = dr / steps;
            double currentRadius = lastValidRadius;
            
            for (int i = 0; i < steps; i++) {
                IShape testShape = ShapeFactory.getInstance().createCircle(startPos.getX(), startPos.getY(), currentRadius + step, Color.BLUE);
                if (model.canAddBlueShape(testShape)) {
                    currentRadius += step;
                } else {
                    break;
                }
            }
            lastValidRadius = currentRadius;
            previewShape = ShapeFactory.getInstance().createCircle(startPos.getX(), startPos.getY(), currentRadius, Color.BLUE);
        }
        
        model.addBlueShape(previewShape);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Finalise la création du cercle. Si le glissement est très court (&lt;5px),
     * un cercle de rayon par défaut (30px) est créé. La création est enregistrée
     * comme commande pour l'historique undo/redo, puis le contrôleur bascule
     * en mode sélection.
     * </p>
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (previewShape != null) {
            model.removeBlueShape(previewShape);
            if (startPos.distance(e.getPoint()) < 5) {
                previewShape = ShapeFactory.getInstance().createCircle(startPos.getX(), startPos.getY(), 30, Color.BLUE);
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
