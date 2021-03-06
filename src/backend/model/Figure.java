package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

// Clase abstracta para modelar de una figura
public abstract class Figure implements Movable, Colorable, Drawable{

    private static final double DEFAULT_EDGE_WIDTH = 1;
    private static final Color DEFAULT_EDGE_COLOR = Color.BLACK;
    private static final Color DEFAULT_FILL_COLOR = Color.YELLOW;

    private double edgeWidth = DEFAULT_EDGE_WIDTH;
    private Color edgeColor = DEFAULT_EDGE_COLOR;
    private Color fillColor = DEFAULT_FILL_COLOR;

    // Metodo para determinar si un punto pertenece a la figura
    public abstract boolean pointBelongs(Point p);

    // Se determina si la figura completa se encuentra dentro del rectangulo imaginario de seleccion multiple
    public abstract boolean isEnclosedBy(Rectangle container);

    protected abstract List<Point> getPoints();

    public void checkPoints(Point topLeft, Point bottomRight){
        if(topLeft == null || bottomRight == null) {
            throw new NullPointerException("Puntos nulos");
        }
        if((bottomRight.getX() <= topLeft.getX()) || bottomRight.getY() <= topLeft.getY()) {
            throw new IllegalArgumentException("No se pudo crear la figura");
        }
    }

    public double getEdgeWidth() {
        return edgeWidth;
    }

    public void setEdgeWidth(double edgeWidth) {
        this.edgeWidth = edgeWidth;
    }

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

    @Override
    public void move(double deltaX, double deltaY){
        for(Point point : this.getPoints()) { point.move(deltaX, deltaY); }
    }

    @Override
    public abstract void draw(GraphicsContext gc);

    @Override
    public abstract String toString();
}
