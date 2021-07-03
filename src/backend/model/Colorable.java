package backend.model;

import javafx.scene.paint.Color;

// Interfaz para establecer los colores y el ancho
// del borde de un objeto (en ese caso Figure)
public interface Colorable {

    default void setColorProp(Color edgeColor, Color fillColor, double edgeWidth) {
        setEdgeColor(edgeColor);    // Color del borde
        setFillColor(fillColor);    // Color del relleno
        setEdgeWidth(edgeWidth);    // Ancho del borde
    }

    void setEdgeColor(Color edgeColor);
    void setFillColor(Color fillColor);
    void setEdgeWidth(double edgeWidth);

    Color getEdgeColor();
    Color getFillColor();

    double getEdgeWidth();

}
