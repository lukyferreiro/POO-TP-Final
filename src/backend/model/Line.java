package backend.model;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Line extends Figure{

    private final static int ERROR = 5;
    private final Point start;
    private final Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean pointBelongs(Point point) {
        if(Double.compare(start.getX(), end.getX()) == 0){
            return ((point.getY() < start.getY() && point.getY() > end.getY()) ||
                    (point.getY() > start.getY() && point.getY() < end.getY())
                            && Double.compare(point.getX(), start.getX()) == 0);
        }
        if((point.getX() < start.getX() && point.getX() > end.getX()) ||
                (point.getX() > start.getX() && point.getX() < end.getX()) ){
            double m = (start.getY() - end.getY()) / (start.getX() - end.getX());
            return Math.abs(point.getY() - m * point.getX() - start.getY() - m * start.getX()) < ERROR;
        }
        return false;
    }

    @Override
    public boolean isEnclosedBy(Rectangle container) {
        return container.pointBelongs(start) && container.pointBelongs(end);
    }

    @Override
    protected List<Point> getPoints() {
        List<Point> toReturn = new ArrayList<>();
        toReturn.add(start);
        toReturn.add(end);
        return toReturn;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    @Override
    public String toString() {
        return String.format("Línea [ %s, %s ]", start, end);
    }
}
