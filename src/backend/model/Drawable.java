package backend.model;

import javafx.scene.canvas.GraphicsContext;

// Interfaz para permitir dibujar a un objeto (Figure)
public interface Drawable {

    void draw(GraphicsContext gc);

}
