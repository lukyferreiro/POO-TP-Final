package backend.model;

import java.util.Objects;

public class Point implements Movable, Comparable<Point> {

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
    public int compareTo(Point o) {
        int ans= Double.compare(x, o.getX());
        if(ans == 0)
           ans= Double.compare(y, o.getY());
        return ans;
    }

}
