package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CanvasState {

    private final List<Figure> canvasFigure = new ArrayList<>();

    public void addFigure(Figure figure) {
        canvasFigure.add(figure);
    }

    public Iterable<Figure> figures() {
        return new ArrayList<>(canvasFigure);
    }

    public List<Figure> getFigures() {
        return canvasFigure;
    }

    public void removeFigures(List<Figure> selectedFigures){
        for(Figure figure : selectedFigures){
            canvasFigure.remove(figure);
        }
    }
}
