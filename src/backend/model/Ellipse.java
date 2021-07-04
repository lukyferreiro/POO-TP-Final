package backend.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Ellipse extends Figure{

    protected Point centerPoint;
    public double xAxis, yAxis;
    protected Point topLeft;
    protected Point bottomRight;

    public Ellipse(Point startPoint, Point endPoint) {
        checkTopLeftBottomRight(startPoint, endPoint);
        this.topLeft = startPoint;
        this.bottomRight = endPoint;
        this.xAxis = Math.abs(bottomRight.getX() - topLeft.getX());
        this.yAxis = Math.abs(topLeft.getY() - bottomRight.getY());
        this.centerPoint = new Point(topLeft.getX() + xAxis/2 , topLeft.getY() + yAxis/2 );
    }

    public Point getCenterPoint() {
        return centerPoint;
    }
    public double getxAxis() {
        return xAxis;
    }
    public double getyAxis() {
        return yAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }
    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }


    @Override
    public boolean isEnclosedBy(Point tl, Point br) {
        Rectangle rect = new Rectangle(tl, br);
        return rect.pointBelongs(topLeft) && rect.pointBelongs(bottomRight);
    }

    @Override
    public boolean pointBelongs(Point point) {
        double h = centerPoint.getX();
        double k = centerPoint.getY();
        return ((Math.pow(point.getX() - h , 2)) / (Math.pow(xAxis, 2)) ) + (Math.pow( point.getY() - k, 2) / Math.pow(yAxis, 2)) <= 1;
    }

    @Override
    public List<Point> getPoints() {
        List<Point> toReturn = new ArrayList<>();
        toReturn.add(centerPoint);
        return toReturn;
    }


    @Override
    public void draw(GraphicsContext gc) {
        gc.fillOval(topLeft.getX(), topLeft.getY(), xAxis, yAxis);
        gc.strokeOval(topLeft.getX(), topLeft.getY(), xAxis, yAxis);
    }

    @Override
    public String toString() {
        return String.format("Elipse [Centro: %s, DMayor: %.2f, DMenor: %.2f]",
                centerPoint, xAxis, yAxis);
    }
}

