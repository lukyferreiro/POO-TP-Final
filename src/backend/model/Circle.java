package backend.model;

public class Circle extends Ellipse {

    private final double radius;

    public Circle(Point topLeft, Point bottomRight){
        super(topLeft, bottomRight);
        this.radius = getxAxis();
        setyAxis(radius);
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", centerPoint, radius);
    }

    @Override
    public boolean pointBelongs(Point p){
        return true;
    }

}
