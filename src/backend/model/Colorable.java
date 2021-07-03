package backend.model;

import javafx.scene.paint.Color;

// Interfaz para establecer los colores y el ancho
// del borde de un objeto (en ese caso Figure)
public interface Colorable {


    void setEdgeColor(Color edgeColor);
    void setFillColor(Color fillColor);

    Color getEdgeColor();
    Color getFillColor();

}
