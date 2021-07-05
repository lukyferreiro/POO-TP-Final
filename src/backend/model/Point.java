package backend.model;

import java.util.Objects;

public class Point implements Movable{

    private double x, y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    @Override
    public void move(double moveInX, double moveInY) {
        this.x += moveInX;
        this.y += moveInY;
    }

    // Distancia entre dos puntos
    public double distanceToPoint(Point p){
        return Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
    }

    // Distancia horizontal entre dos puntos
    public double horizontalDistToPoint(Point p) {
        return Math.abs(p.getX() - x);
    }

    // Distancia vertical entre dos puntos
    public double verticalDistToPoint(Point p) {
        return Math.abs(p.getY() - y);
    }

    @Override
    public String toString() {
        return String.format("{%.2f , %.2f}", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if( !(o instanceof Point point)){
            return false;
        }
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
