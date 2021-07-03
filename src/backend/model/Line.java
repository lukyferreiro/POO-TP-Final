package backend.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;
import java.util.List;

public class Line extends Figure{

    private final Point start;
    private final Point end;

    public Line(Point start, Point end){
        this.start = start;
        this.end = end;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeLine(start.getX(), start.getY(),end.getX(), end.getY());
    }

    @Override
    public boolean isEnclosedBy(Point tl, Point br) {
        Point topLeft = new Point(Math.min(start.getX(),end.getX()), Math.max(start.getY(), end.getY()));
        Point bottomRight = new Point(Math.max(start.getX(),end.getX()), Math.min(start.getY(), end.getY()));
        return new Rectangle(topLeft,bottomRight).isEnclosedBy(tl, br);
    }

    @Override
    public boolean pointBelongs(Point point) {
        return false;
    }

    @Override
    protected List<Point> getPoints() {
        return Arrays.asList(start, end);
    }

    @Override
    public String toString() {
        return String.format("Línea [ %s, %s ]", start, end);
    }
}
