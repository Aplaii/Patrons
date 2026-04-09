package game.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import game.shapes.*;

/**
 * Modèle central du jeu (Design Pattern MVC - Modèle).
 * <p>
 * Gère l'état du jeu : les formes rouges (obstacles), les formes bleues
 * (créées par le joueur), le mode de difficulté et les stratégies de placement.
 * Implémente le pattern Observer pour notifier les vues des changements,
 * et le pattern Strategy pour déléguer les règles de placement.
 * </p>
 *
 * @see ModelListener
 * @see IPlacementStrategy
 * @see ShapeListener
 */
public class GameModel implements ShapeListener {
    /** Liste des formes rouges (obstacles). */
    private List<IShape> redShapes;
    /** Liste des formes bleues (créées par le joueur). */
    private List<IShape> blueShapes;
    /** Indicateur du mode difficile. */
    private boolean hardModeEnabled;
    /** Indicateur de visibilité des formes rouges. */
    private boolean redShapesVisible;
    /** Liste des observateurs du modèle. */
    private List<ModelListener> listeners;
    /** Stratégie de placement courante. */
    private IPlacementStrategy strategy;

    /**
     * Construit un nouveau modèle de jeu avec les paramètres par défaut :
     * aucune forme, mode standard, obstacles visibles.
     */
    public GameModel() {
        redShapes = new ArrayList<>();
        blueShapes = new ArrayList<>();
        listeners = new ArrayList<>();
        hardModeEnabled = false;
        redShapesVisible = true;
        strategy = new StandardPlacementStrategy();
    }

    /**
     * Ajoute un observateur au modèle.
     *
     * @param listener L'observateur à ajouter.
     */
    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifie tous les observateurs d'un changement dans le modèle.
     */
    public void notifyListeners() {
        for (ModelListener l : listeners) {
            l.modelChanged();
        }
    }

    /**
     * Ajoute une forme rouge (obstacle) au modèle.
     * La couleur de la forme est forcée à rouge.
     *
     * @param shape La forme à ajouter comme obstacle.
     */
    public void addRedShape(IShape shape) {
        shape.setColor(Color.RED);
        redShapes.add(shape);
        notifyListeners();
    }

    /**
     * Supprime toutes les formes rouges du modèle.
     */
    public void clearRedShapes() {
        redShapes.clear();
        notifyListeners();
    }

    /**
     * Vérifie si une forme bleue peut être ajoutée.
     * Délègue la vérification à la stratégie de placement actuelle.
     *
     * @param shape La forme à tester.
     * @return {@code true} si l'ajout est possible, {@code false} sinon.
     */
    public boolean canAddBlueShape(IShape shape) {
        return strategy.canAddBlueShape(shape, blueShapes, redShapes);
    }

    /**
     * Vérifie si une forme bleue peut être déplacée ou redimensionnée.
     * Délègue la vérification à la stratégie de placement actuelle.
     *
     * @param shape La forme à tester.
     * @return {@code true} si la modification est possible, {@code false} sinon.
     */
    public boolean canMoveOrResizeBlueShape(IShape shape) {
        return strategy.canMoveOrResizeBlueShape(shape, redShapes);
    }

    /**
     * Ajoute une forme bleue (créée par le joueur) au modèle.
     * La couleur est forcée à bleu et le modèle s'enregistre comme
     * écouteur des changements de la forme (pattern Observer).
     *
     * @param shape La forme bleue à ajouter.
     */
    public void addBlueShape(IShape shape) {
        shape.setColor(Color.BLUE);
        blueShapes.add(shape);
        shape.addShapeListener(this);
        notifyListeners();
    }

    /**
     * Supprime une forme bleue du modèle et retire l'écoute de ses changements.
     *
     * @param shape La forme bleue à supprimer.
     */
    public void removeBlueShape(IShape shape) {
        shape.removeShapeListener(this);
        blueShapes.remove(shape);
        notifyListeners();
    }

    /**
     * Retourne la liste des formes rouges (obstacles).
     *
     * @return La liste des formes rouges.
     */
    public List<IShape> getRedShapes() {
        return redShapes;
    }

    /**
     * Retourne la liste des formes bleues (créées par le joueur).
     *
     * @return La liste des formes bleues.
     */
    public List<IShape> getBlueShapes() {
        return blueShapes;
    }

    /**
     * Active ou désactive le mode difficile.
     * En mode difficile, la stratégie de placement est remplacée par
     * {@link HardModePlacementStrategy}. En mode normal, la stratégie
     * {@link StandardPlacementStrategy} est utilisée.
     *
     * @param hardModeEnabled {@code true} pour activer le mode difficile.
     */
    public void setHardModeEnabled(boolean hardModeEnabled) {
        if (this.hardModeEnabled != hardModeEnabled) {
            this.hardModeEnabled = hardModeEnabled;
            if (hardModeEnabled) {
                HardModePlacementStrategy hmStrategy = new HardModePlacementStrategy();
                hmStrategy.setRedShapesVisible(this.redShapesVisible);
                this.strategy = hmStrategy;
            } else {
                this.strategy = new StandardPlacementStrategy();
            }
            notifyListeners();
        }
    }

    /**
     * Indique si le mode difficile est activé.
     *
     * @return {@code true} si le mode difficile est actif.
     */
    public boolean isHardModeEnabled() {
        return hardModeEnabled;
    }

    /**
     * Indique si les formes rouges sont actuellement visibles.
     *
     * @return {@code true} si les formes rouges sont visibles.
     */
    public boolean isRedShapesVisible() {
        return redShapesVisible;
    }

    /**
     * Définit la visibilité des formes rouges.
     * Met également à jour la stratégie de placement en mode difficile.
     *
     * @param redShapesVisible {@code true} pour rendre les formes rouges visibles.
     */
    public void setRedShapesVisible(boolean redShapesVisible) {
        if (this.redShapesVisible != redShapesVisible) {
            this.redShapesVisible = redShapesVisible;
            if (this.strategy instanceof HardModePlacementStrategy) {
                ((HardModePlacementStrategy) this.strategy).setRedShapesVisible(redShapesVisible);
            }
            notifyListeners();
        }
    }

    /**
     * Calcule l'aire totale couverte par les formes bleues (union géométrique).
     * Les zones de chevauchement ne sont comptées qu'une seule fois.
     *
     * @return L'aire totale de l'union des formes bleues en pixels carrés.
     */
    public double getTotalBlueArea() {
        if (blueShapes.isEmpty())
            return 0;

        java.awt.geom.Area union = new java.awt.geom.Area();
        for (IShape shape : blueShapes) {
            union.add(new java.awt.geom.Area(shape.getAwtShape()));
        }
        return calculateArea(union);
    }

    /**
     * Calcule l'aire d'une zone géométrique AWT en utilisant la formule du lacet
     * (Shoelace formula) sur les segments linéaires du contour.
     *
     * @param area La zone AWT dont calculer l'aire.
     * @return L'aire en pixels carrés.
     */
    private double calculateArea(java.awt.geom.Area area) {
        double totalArea = 0;
        java.awt.geom.PathIterator iterator = area.getPathIterator(null, 1.0);
        double[] coords = new double[6];
        double startX = 0, startY = 0;
        double curX = 0, curY = 0;

        while (!iterator.isDone()) {
            int type = iterator.currentSegment(coords);
            switch (type) {
                case java.awt.geom.PathIterator.SEG_MOVETO:
                    startX = coords[0];
                    startY = coords[1];
                    curX = startX;
                    curY = startY;
                    break;
                case java.awt.geom.PathIterator.SEG_LINETO:
                    totalArea += (curX * coords[1] - coords[0] * curY);
                    curX = coords[0];
                    curY = coords[1];
                    break;
                case java.awt.geom.PathIterator.SEG_CLOSE:
                    totalArea += (curX * startY - startX * curY);
                    break;
            }
            iterator.next();
        }
        return Math.abs(totalArea) / 2.0;
    }

    /**
     * Recherche la forme bleue située à un point donné.
     * Les formes sont parcourues dans l'ordre inverse (du dessus vers le dessous)
     * pour respecter l'ordre d'affichage (z-order).
     *
     * @param p Le point à tester.
     * @return La forme bleue contenant le point, ou {@code null} si aucune ne le contient.
     */
    public IShape getShapeAt(java.awt.geom.Point2D p) {
        for (int i = blueShapes.size() - 1; i >= 0; i--) {
            if (blueShapes.get(i).contains(p)) {
                return blueShapes.get(i);
            }
        }
        return null;
    }

    /**
     * Désélectionne toutes les formes bleues.
     * Notifie les observateurs uniquement si au moins une forme était sélectionnée.
     */
    public void deselectAll() {
        boolean changed = false;
        for (IShape shape : blueShapes) {
            if (shape.isSelected()) {
                shape.setSelected(false);
                changed = true;
            }
        }
        if (changed)
            notifyListeners();
    }

    /**
     * {@inheritDoc}
     * Réagit aux changements d'une forme individuelle en notifiant les vues.
     */
    @Override
    public void shapeChanged(IShape shape) {
        notifyListeners();
    }
}
