package backend.model;

public class Square extends Rectangle {

    private final double side;

    public Square(Point topLeft, Point bottomRight) {
        super(topLeft, new Point(topLeft.getX() +  (topLeft.getX() - bottomRight.getX()),
                topLeft.getY() +  (topLeft.getX() - bottomRight.getX()) ));
        this.side = topLeft.getX() - bottomRight.getX();
    }

    @Override
    public String toString() {
        return String.format("Cuadrado [ %s , %s, Lado: %s ]", topLeft, bottomRight, side);
    }
}
