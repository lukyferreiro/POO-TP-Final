package backend.model;

public class Circle extends Ellipse {

    private final double radius;

    public Circle(Point topLeft, Point bottomRight){
        super(topLeft, bottomRight);
        this.radius = xAxis;
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", centerPoint, radius);
    }

    public double getRadius() {
        return radius;
    }

}
