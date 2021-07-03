package frontend;

import backend.CanvasState;
import backend.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PaintPane extends BorderPane {

	// BackEnd
	private final CanvasState canvasState;

	// Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();

	Color lineColor = Color.BLACK;
	Color fillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton bringForwardButton = new ToggleButton("Al fondo");
	ToggleButton sendBackButton = new ToggleButton("Al frente");

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura

	List<Figure> selectedFigures;

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		List<ToggleButton> toolsList = new ArrayList<>();
		toolsList.add(selectionButton);
		toolsList.addAll(Arrays.stream(FigureButton.values()).map(FigureButton::getButton).collect(Collectors.toList()));
		toolsList.add(deleteButton);
		toolsList.add(bringForwardButton);
		toolsList.add(sendBackButton);

		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsList) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsList);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);
		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			if(startPoint == null) {
				return;
			}
			if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
				return ;
			}
			Figure newFigure = FigureButton.findButton(startPoint, endPoint);

			if(newFigure == null){
				throw new IllegalArgumentException("Figura mal creada");
			}
			canvasState.addFigure(newFigure);
			redrawCanvas();
		});
		// Informacion relevante de la figura
		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figureBelongs(figure, eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			if(found) {
				statusPane.updateStatus(label.toString());
			} else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder();
				if(!selectedFigures.isEmpty()) {
					for (Figure figure : canvasState.figures()) {
						if (figure.pointBelongs(eventPoint)) {
							found = true;
							selectedFigures.add(figure);
							label.append("Se seleccionó: " + figure.toString());
						}
					}
					if (found) {
						statusPane.updateStatus(label.toString());
					}
					redrawCanvas();
				}
			}
		})
		// TODO mover multiples figuras
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(!selectedFigures.isEmpty()) {
					for (Figure figure : canvasState.figures()){//selectedFigures
						figure.move(diffX, diffY);
						redrawCanvas();
					}
				}
			}
		});
		setLeft(buttonsBox);
		setRight(canvas);
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if(figure == selectedFigure) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(lineColor);
			}
			gc.setFill(fillColor);
			/*
			 figura.draw(gc){
			     gc.fillRect(...)
			     gc.strokeRect(...)
			 }
			 */
			if(figure instanceof Rectangle) {
				Rectangle rectangle = (Rectangle) figure;
				gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
						Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()),
						Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
				gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
						Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()),
						Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
			} else if(figure instanceof Ellipse) {
				Ellipse circle = (Ellipse) figure;
			//	double diameter = circle.getRadius() * 2;
				gc.fillOval(circle.getCenterPoint().getX() ,
						circle.getCenterPoint().getY(), circle.xAxis, circle.yAxis);
				gc.strokeOval(circle.getCenterPoint().getX(),
						circle.getCenterPoint().getY(), circle.xAxis, circle.yAxis);
			}
		}
	}

	boolean figureBelongs(Figure figure, Point eventPoint) {
		boolean found = false;
		if(figure instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) figure;
			found = eventPoint.getX() > rectangle.getTopLeft().getX() && eventPoint.getX() < rectangle.getBottomRight().getX() &&
					eventPoint.getY() > rectangle.getTopLeft().getY() && eventPoint.getY() < rectangle.getBottomRight().getY();
		} else if(figure instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) figure;
			return ellipse.pointBelongs(eventPoint);
//			found = Math.sqrt(Math.pow(ellipse.getCenterPoint().getX() - eventPoint.getX(), 2) +
//					Math.pow(ellipse.getCenterPoint().getY() - eventPoint.getY(), 2)) < ellipse.getRadius();
		}
		return found;
	}

}
