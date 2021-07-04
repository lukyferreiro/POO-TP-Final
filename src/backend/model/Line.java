package backend.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.Arrays;
import java.util.List;

public class Line extends Figure{

    private final Point start;
    private final Point end;
    private static final int ERROR = 10;

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
        if(Double.compare (start.getX(), end.getX()) == 0){
            return ((point.getY() < start.getY() && point.getY() > end.getY()) ||
                    (point.getY() > start.getY() && point.getY() < end.getY()) &&
                            Double.compare(point.getX(), start.getX()) == 0);
        }
        if((point.getX() < start.getX() && point.getX() > end.getX()) ||
                (point.getX() > start.getX() && point.getX() < end.getX()) ){
            double aux = (start.getY() - end.getY()) / (start.getX() - end.getX());
            return Math.abs(point.getY() - aux * point.getX() - start.getY() - aux * start.getX()) < ERROR;
        }
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
