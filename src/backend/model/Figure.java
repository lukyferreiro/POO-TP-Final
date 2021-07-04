package backend.model;

import javafx.scene.paint.Color;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public abstract class Figure implements Movable, Drawable, Colorable {
    private static final double DEFAULT_EDGE_WIDTH = 1;
    private static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
    private static final Color DEFAULT_FILL_COLOR = Color.YELLOW;

    private double edgeWidth = DEFAULT_EDGE_WIDTH;
    private Color edgeColor = DEFAULT_EDGE_COLOR;
    private Color fillColor = DEFAULT_FILL_COLOR;

    @Override
    public void setEdgeColor(Color edgeColor) {
        this.edgeColor = edgeColor;
    }
    @Override
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    @Override
    public Color getEdgeColor() {
        return edgeColor;
    }
    @Override
    public Color getFillColor() {
        return fillColor;
    }

    public double getEdgeWidth() {
        return edgeWidth;
    }
    public void setEdgeWidth(double edgeWidth) {
        this.edgeWidth = edgeWidth;
    }

    @Override
    public void move(double deltaX, double deltaY) {
        for(Point point : this.getPoints()) {
            point.move(deltaX, deltaY);
        }
    }

    public abstract void draw(GraphicsContext gc);

    // Metodo para determinar si la figura completa se encuentra dentro del rectangulo imaginario de seleccion multiple
    public abstract boolean isEnclosedBy(Point tl, Point br);

    // Metodo para determinar si un punto pertenece a la figura
    public abstract boolean pointBelongs(Point point);

    protected abstract List<Point> getPoints();

    @Override
    public abstract String toString();

}
