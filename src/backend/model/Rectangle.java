package backend.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.ArrayList;

public class Rectangle extends Figure {

    protected final Point topLeft, bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public boolean isEnclosedBy(Point tl, Point br) {
        Rectangle rect = new Rectangle(tl, br);
        return rect.pointBelongs(topLeft) && rect.pointBelongs(bottomRight);
    }

    @Override
    public boolean pointBelongs(Point point) {
        return point.getX() >= topLeft.getX() && point.getX() <= bottomRight.getX()
                && point.getY() >= topLeft.getY() && point.getY() <= bottomRight.getY();
    }

    @Override
    public List<Point> getPoints() {
        List<Point> toReturn = new ArrayList<>();
        toReturn.add(topLeft);
        toReturn.add(bottomRight);
        return toReturn;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(topLeft.getX(), topLeft.getY(),
                Math.abs(topLeft.getX() - bottomRight.getX()), Math.abs(topLeft.getY() - bottomRight.getY()));

        gc.strokeRect(topLeft.getX(), topLeft.getY(),
                Math.abs(topLeft.getX() - bottomRight.getX()), Math.abs(topLeft.getY() - bottomRight.getY()));
    }

    @Override
    public String toString() {
        return String.format("Rectangulo [ %s , %s ]", topLeft, bottomRight);
    }
}
