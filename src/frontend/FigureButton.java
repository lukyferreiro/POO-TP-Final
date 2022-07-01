package frontend;

import backend.model.*;
import javafx.scene.control.ToggleButton;

public enum FigureButton {

    RECTANGLE(new ToggleButton("Rectángulo")){
        @Override
        public Figure createFigure(Point startPoint, Point endPoint) {
            return new Rectangle(startPoint, endPoint);
        }
    },
    CIRCLE(new ToggleButton("Círculo")){
        @Override
        public Figure createFigure(Point startPoint, Point endPoint) {
            return new Circle(startPoint, endPoint);
        }
    },
    SQUARE(new ToggleButton("Cuadrado")){
        @Override
        public Figure createFigure(Point startPoint, Point endPoint) {
            return new Square(startPoint, endPoint);
        }
    },
    ELLIPSE(new ToggleButton("Elipse")){
        @Override
        public Figure createFigure(Point startPoint, Point endPoint) {
            return new Ellipse(startPoint, endPoint);
        }
    },
    LINE(new ToggleButton("Línea")){
        @Override
        public Figure createFigure(Point startPoint, Point endPoint) {
            return new Line(startPoint, endPoint);
        }
    };

    private final ToggleButton button;

    FigureButton(ToggleButton button) {
        this.button = button;
    }

    public ToggleButton getButton() {
        return button;
    }

    // Metodo para buscar el boton de la Figure y devuelve una instancia de dicha Figure
    public static Figure findButtons(Point startPoint, Point endPoint){
        for (FigureButton b : values()) {
            if(b.button.isSelected()){
                return b.createFigure(startPoint, endPoint);
            }
        }
        return null;
    }

    // Metodo abstracto que sobreescribe cada enum para devulver su respectiva instancia
    public abstract Figure createFigure(Point startPoint, Point endPoint);

}
