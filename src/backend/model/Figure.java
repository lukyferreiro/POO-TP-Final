package backend.model;

import java.awt.*;
import java.util.List;

public abstract class Figure implements Movable {

   public abstract boolean pointBelongs(Point point);

   public void setEdgeWidth(double newWidth){
       this.edgeWidth = edgeWidth;
   }

    // Get ancho del borde
    public double getEdgeWidth(){
        return edgeWidth;
    }

    @Override
   public abstract String toString();

   protected abstract List<Point> getPoints();

   @Override
   public void move(double deltaX, double deltaY) {
       for(Point point : this.getPoints()) {
           point.move(deltaX, deltaY);
       }
   }


}

