package game.controller;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.SwingUtilities;

import game.command.CommandManager;
import game.command.CreateShapeCommand;
import game.command.MoveShapeCommand;
import game.command.ResizeShapeCommand;
import game.model.Circle;
import game.model.GameModel;
import game.model.IShape;
import game.model.RectangleShape;
import game.view.GamePanel;

/**
 * Contrôleur principal gérant les interactions à la souris (MVC).
 * Il intercepte les clics pour créer, sélectionner, déplacer
 * ou redimensionner les formes, puis instancie les Commandes appropriées.
 */
public class MouseController extends MouseAdapter {
    private GameModel model;
    private GamePanel view;
    private CommandManager commandManager;

    private ShapeType currentShapeType = ShapeType.CIRCLE;
    private IShape selectedShape = null;
    private Point2D lastMousePos;

    private boolean isDragging = false;
    private boolean isResizing = false;
    private boolean isCreating = false;

    private Point2D resizeCenter;
    private Point2D creationStartPos;
    private IShape previewShape = null;

    private double accumulatedDx = 0;
    private double accumulatedDy = 0;
    private double accumulatedScale = 1.0;

    public MouseController(GameModel model, GamePanel view, CommandManager commandManager) {
        this.model = model;
        this.view = view;
        this.commandManager = commandManager;
    }

    public void setCurrentShapeType(ShapeType type) {
        this.currentShapeType = type;
    }

    public IShape getSelectedShape() {
        return selectedShape;
    }

    public void deleteSelectedShape() {
        if (selectedShape != null) {
            commandManager.executeCommand(new game.command.DeleteShapeCommand(model, selectedShape));
            selectedShape = null;
            model.deselectAll();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point2D p = e.getPoint();
        lastMousePos = p;

        IShape clickedShape = model.getShapeAt(p);

        if (clickedShape != null) {
            model.deselectAll();
            selectedShape = clickedShape;
            selectedShape.setSelected(true);
            model.notifyListeners();

            if (SwingUtilities.isRightMouseButton(e) || e.isShiftDown()) {
                isResizing = true;
                accumulatedScale = 1.0;
                resizeCenter = new Point2D.Double(selectedShape.getAwtShape().getBounds2D().getCenterX(),
                        selectedShape.getAwtShape().getBounds2D().getCenterY());
            } else {
                isDragging = true;
                accumulatedDx = 0;
                accumulatedDy = 0;
            }
        } else {
            model.deselectAll();
            selectedShape = null;

            isCreating = true;
            creationStartPos = p;

            if (currentShapeType == ShapeType.CIRCLE) {
                previewShape = new Circle(p.getX(), p.getY(), 1, Color.BLUE);
            } else {
                previewShape = new RectangleShape(p.getX(), p.getY(), 1, 1, Color.BLUE);
            }

            if (model.canAddBlueShape(previewShape)) {
                model.addBlueShape(previewShape);
            } else {
                previewShape = null;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point2D p = e.getPoint();
        double dx = p.getX() - lastMousePos.getX();
        double dy = p.getY() - lastMousePos.getY();

        if (isCreating && previewShape != null) {
            model.removeBlueShape(previewShape);

            IShape newPreview;
            if (currentShapeType == ShapeType.CIRCLE) {
                double radius = creationStartPos.distance(p);
                newPreview = new Circle(creationStartPos.getX(), creationStartPos.getY(), radius, Color.BLUE);
            } else {
                double x = Math.min(creationStartPos.getX(), p.getX());
                double y = Math.min(creationStartPos.getY(), p.getY());
                double w = Math.abs(creationStartPos.getX() - p.getX());
                double h = Math.abs(creationStartPos.getY() - p.getY());
                newPreview = new RectangleShape(x, y, w, h, Color.BLUE);
            }

            if (model.canAddBlueShape(newPreview)) {
                previewShape = newPreview;
            }
            model.addBlueShape(previewShape);
        } else if (isDragging && selectedShape != null) {
            // Tentative de déplacement combiné (dx, dy)
            selectedShape.translate(dx, dy);
            if (!model.canMoveOrResizeBlueShape(selectedShape)) {
                // Annuler le déplacement combiné et revenir à la position d'origine
                selectedShape.translate(-dx, -dy);

                // Glissement axial : tester X seul via un clone
                IShape testX = selectedShape.clone();
                testX.translate(dx, 0);
                boolean canX = model.canMoveOrResizeBlueShape(testX);

                // Glissement axial : tester Y seul via un clone (depuis position d'origine)
                IShape testY = selectedShape.clone();
                testY.translate(0, dy);
                boolean canY = model.canMoveOrResizeBlueShape(testY);

                // Appliquer les mouvements validés
                if (canX) {
                    selectedShape.translate(dx, 0);
                    accumulatedDx += dx;
                }
                if (canY) {
                    selectedShape.translate(0, dy);
                }
                if (canX || canY) {
                    model.notifyListeners();
                }
            } else {
                accumulatedDx += dx;
                model.notifyListeners();
            }
            lastMousePos = p; // toujours mis à jour pour continuer à tracker la souris
        } else if (isResizing && selectedShape != null) {
            double factor = 1.0 + (dx + dy) * 0.01;
            if (factor <= 0.1)
                factor = 0.1;

            selectedShape.scale(factor, resizeCenter);
            if (!model.canMoveOrResizeBlueShape(selectedShape)) {
                // Collision : la forme reste bloquée mais on suit quand même la souris
                selectedShape.scale(1.0 / factor, resizeCenter);
            } else {
                accumulatedScale *= factor;
                model.notifyListeners();
            }
            lastMousePos = p; // toujours mis à jour pour continuer à tracker la souris
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isCreating) {
            if (previewShape != null) {
                model.removeBlueShape(previewShape);

                // If just a quick click, apply a default size if possible
                if (creationStartPos.distance(e.getPoint()) < 5) {
                    IShape defaultShape;
                    if (currentShapeType == ShapeType.CIRCLE) {
                        defaultShape = new Circle(creationStartPos.getX(), creationStartPos.getY(), 30, Color.BLUE);
                    } else {
                        defaultShape = new RectangleShape(creationStartPos.getX() - 30, creationStartPos.getY() - 30,
                                60, 60, Color.BLUE);
                    }
                    if (model.canAddBlueShape(defaultShape)) {
                        previewShape = defaultShape;
                    } else {
                        previewShape = null; // Cannot inject default size due to collision
                    }
                }

                if (previewShape != null) {
                    commandManager.executeCommand(new CreateShapeCommand(model, previewShape));
                }
            }
            isCreating = false;
        } else if (selectedShape != null) {
            if (isDragging && (accumulatedDx != 0 || accumulatedDy != 0)) {
                selectedShape.translate(-accumulatedDx, -accumulatedDy);
                commandManager.executeCommand(new MoveShapeCommand(model, selectedShape, accumulatedDx, accumulatedDy));
            } else if (isResizing && accumulatedScale != 1.0) {
                selectedShape.scale(1.0 / accumulatedScale, resizeCenter);
                commandManager
                        .executeCommand(new ResizeShapeCommand(model, selectedShape, accumulatedScale, resizeCenter));
            }
        }
        isDragging = false;
        isResizing = false;
        accumulatedDx = 0;
        accumulatedDy = 0;
        accumulatedScale = 1.0;
    }
}
