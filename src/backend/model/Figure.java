package backend.model;

import javafx.scene.paint.Color;
import java.util.List;

public abstract class Figure implements Movable, Drawable, Colorable {

    private double edgeWidth = 1;
    private Color edgeColor = Color.BLACK;
    private Color fillColor = Color.YELLOW;

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

    // Metodo para determinar si la figura completa se encuentra dentro
    // del rectangulo imaginario de seleccion multiple

    //public abstract boolean inEnclosedBy(Rectangle rectangle);
    public abstract boolean isEnclosedBy(Point tl, Point br);

    // Metodo para determinar si un punto pertenece a la figura
    public abstract boolean pointBelongs(Point point);

    protected abstract List<Point> getPoints();

    @Override
    public abstract String toString();

}
