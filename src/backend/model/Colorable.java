package backend.model;

import javafx.scene.paint.Color;

// Interfaz para establecer los colores de un objeto (en este caso Figure)
public interface Colorable {

    void setEdgeColor(Color edgeColor);
    void setFillColor(Color fillColor);

    Color getEdgeColor();
    Color getFillColor();

}
