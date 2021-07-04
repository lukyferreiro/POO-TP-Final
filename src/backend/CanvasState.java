package backend;

import backend.model.Figure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CanvasState {

    private final LinkedList<Figure> canvasFigures = new LinkedList<>();

    public void addFigure(Figure figure) {
        canvasFigures.add(figure);
    }

    // Metodo para retornar iterar por las Figures
    public Iterable<Figure> figures() {
        return canvasFigures;
    }

    // Metodo para remover las figuras seleccionadas
    public void removeFigures(Collection<Figure> selectedFigures){
        for(Figure figure : selectedFigures){
            canvasFigures.remove(figure);
        }
    }

    // Metodo para mover a la figuras hacia adelante (utilizando el orden de aparicion)
    public void moveForward(Collection<Figure> selectedFigures) {
        removeFigures(selectedFigures);
        LinkedList<Figure> aux = new LinkedList<>(selectedFigures);
        aux.descendingIterator().forEachRemaining(canvasFigures::addFirst);
    }

    // Metodo para mover a la figuras hacia atras (utilizando el orden de aparicion)
    public void moveBackwards(Collection<Figure> selectedFigures) {
        removeFigures(selectedFigures);
        selectedFigures.forEach(canvasFigures::addLast);
    }

    public List<Figure> getFigures() {
        return canvasFigures;
    }
}
