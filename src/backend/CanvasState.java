package backend;

import backend.model.Figure;

import java.util.*;

public class CanvasState extends LinkedList<Figure>{

    // Metodo para remover las figuras seleccionadas
    public void removeFigures(Collection<Figure> selectedFigures){
        for(Figure figure : selectedFigures){
            this.remove(figure);
        }
    }

    // Metodo para mover a la figuras hacia adelante (utilizando el orden de aparicion)
    public void moveForward(Collection<Figure> selectedFigures) {
        removeFigures(selectedFigures);
        LinkedList<Figure> aux = new LinkedList<>(selectedFigures);
        aux.descendingIterator().forEachRemaining(this::addFirst);
    }

    // Metodo para mover a la figuras hacia atras (utilizando el orden de aparicion)
    public void moveBackwards(Collection<Figure> selectedFigures) {
        removeFigures(selectedFigures);
        for(Figure figure : selectedFigures){
            this.addLast(figure);
        }
    }
}
