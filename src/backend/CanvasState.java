package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.List;

public class CanvasState {

    private final List<Figure> canvasFigure = new ArrayList<>();

    public void addFigure(Figure figure) {
        canvasFigure.add(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(canvasFigure);
    }

}
