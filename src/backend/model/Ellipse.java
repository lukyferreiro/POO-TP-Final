package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
public class Ellipse extends Figure {

    private Point centerPoint;
    private double xAxis;
    private double yAxis;

    public Ellipse(Point topLeft, Point bottomRight) {
        checkPoints(topLeft, bottomRight);
        this.xAxis = topLeft.horizontalDistToPoint(bottomRight);
        this.yAxis = topLeft.verticalDistToPoint(bottomRight);
        this.centerPoint = new Point(topLeft.getX() + xAxis / 2, topLeft.getY() + yAxis / 2);
    }

    public void setCenterPoint(Point centerPoint){
        this.centerPoint = centerPoint;
    }
    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }
    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public Point getCenterPoint(){
        return centerPoint;
    }

    // Metodo para calcular el Point topLeft del rectangulo que contiene a la elipse
    private Point getTopLeft() {
        return new Point(centerPoint.getX() - xAxis / 2,
                centerPoint.getY() - yAxis / 2);
    }

    // Metodo para calcular el Point bottomRight del rectangulo que contiene a la elipse
    private Point getBottomRight() {
        return new Point(centerPoint.getX() + xAxis / 2,
                centerPoint.getY() + yAxis / 2);
    }

    @Override
    public boolean pointBelongs(Point point) {
        double diffX = point.horizontalDistToPoint(centerPoint);
        double diffY = point.verticalDistToPoint(centerPoint);
        double a = xAxis / 2;
        double b = yAxis / 2;
        return ((Math.pow(diffX,2) / Math.pow(a,2)) + (Math.pow(diffY,2) / Math.pow(b,2))) <= 1;
    }

    @Override
    public boolean isEnclosedBy(Rectangle container) {
        return container.pointBelongs(getTopLeft()) && container.pointBelongs(getBottomRight());
    }

    @Override
    protected List<Point> getPoints() {
        List<Point> toReturn = new ArrayList<>();
        toReturn.add(centerPoint);
        return toReturn;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeOval(getTopLeft().getX(), getTopLeft().getY(), xAxis, yAxis);
        gc.fillOval(getTopLeft().getX(), getTopLeft().getY(), xAxis, yAxis);
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, Eje mayor: %.2f, Eje menor: %.2f]",centerPoint, xAxis, yAxis);
    }
}
