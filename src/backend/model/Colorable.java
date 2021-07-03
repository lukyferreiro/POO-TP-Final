package backend.model;

import javafx.scene.paint.Color;

// Interfaz para establecer los colores de un objeto
public interface Colorable {

    Color getEdgeColor();   // Get color del borde
    Color getFillColor();   // Get color de relleno
    
    void setEdgeColor(Color edgeColor);    // Set color borde
    void setFillColor(Color fillColor);    // Set color relleno

}
