package backend.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import java.util.ArrayList;

public class Rectangle extends Figure {

    private final Point topLeft;
    private final Point bottomRight;

    public Rectangle(Point topLeft, Point bottomRight) {
        checkPoints(topLeft, bottomRight);
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public Point getBottomRight() {
        return bottomRight;
    }
    public Point getTopLeft() {
        return topLeft;
    }

    @Override
    public boolean pointBelongs(Point point) {
        return point.getX() > topLeft.getX() && point.getY() > topLeft.getY() &&
                point.getX() < bottomRight.getX() && point.getY() < bottomRight.getY();
    }

    @Override
    public boolean isEnclosedBy(Rectangle container) {
        return container.pointBelongs(topLeft) && container.pointBelongs(bottomRight);
    }

    @Override
    protected List<Point> getPoints() {
        List<Point> toReturn = new ArrayList<>();
        toReturn.add(bottomRight);
        toReturn.add(topLeft);
        return toReturn;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double width = bottomRight.horizontalDistToPoint(topLeft);
        double height = bottomRight.verticalDistToPoint(topLeft);
        gc.strokeRect(topLeft.getX(), topLeft.getY(), width, height);
        gc.fillRect(topLeft.getX(), topLeft.getY(), width, height);
    }

    @Override
    public String toString() {
        return String.format("Rectángulo [ %s , %s ]", topLeft, bottomRight);
    }

}
