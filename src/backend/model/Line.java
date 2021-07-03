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

    public Point getStart() {
        return start;
    }
    public Point getEnd() {
        return end;
    }


    @Override
    public boolean pointBelongs(Point point) {
        return false;
    }

    @Override
    protected List<Point> getPoints() {
        return Arrays.asList(getStart(), getEnd());
    }

    @Override
    public boolean isEnclosedBy(Point tl, Point br) {
        Rectangle rect = new Rectangle(tl, br);
        return rect.pointBelongs(start) && rect.pointBelongs(end);
    }

    @Override
    public String toString() {
        return String.format("Linea [%.2f , %.2f]", start, end);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }
}
