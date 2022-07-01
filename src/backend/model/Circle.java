package backend.model;

public class Circle extends Ellipse {

    private final double radius;

    public Circle(Point topLeft, Point bottomRight){
        super(topLeft, bottomRight);
        setCenterPoint(topLeft);
        this.radius = topLeft.distanceToPoint(bottomRight) * 2;
        setyAxis(radius);
        setxAxis(radius);
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public boolean pointBelongs(Point point) {
        //Vemos si el punto pertenece al circulo
        return (getCenterPoint().distanceToPoint(point))*2 < getRadius();
    }

    @Override
    public String toString() {
        return String.format("Círculo [Centro: %s, Radio: %.2f]", getCenterPoint(), radius);
    }
}