package backend.model;

import java.util.Objects;

public class Point implements Movable {

    public double x, y;

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
    public void move(double deltaX, double deltaY){
        this.x += deltaX;
        this.y += deltaY;
    }

    // Distancia entre dos puntos
    public double distanceTo(Point point) {
        return Math.sqrt(Math.pow(this.x - point.getX(), 2) + Math.pow(this.y - point.getY(), 2));
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
        if ( !(o instanceof Point) ){    //  if (o == null || getClass() != o.getClass()){
            return false;
        }
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
