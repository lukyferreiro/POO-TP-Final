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
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PaintPane extends BorderPane {

	// BackEnd
	private final CanvasState canvasState;

	// Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();

	Color edgeColor = Color.BLACK;
	Color fillColor = Color.YELLOW;

	// Botones Barra Izquierda
	ToggleButton selectionButton = new ToggleButton("Seleccionar");
	ToggleButton deleteButton = new ToggleButton("Borrar");
	ToggleButton sendFrontButton = new ToggleButton("Al fondo");
	ToggleButton sendBackButton = new ToggleButton("Al frente");
	//Labels
	Label edgeLabel=new Label("Borde");
	Label fillLabel=new Label("Relleno");

	//Border slider
	Slider slider = new Slider(1, 30, 1);

	//Color Picker
	ColorPicker edgeColorPicker = new ColorPicker(edgeColor);
	ColorPicker fillColorPicker = new ColorPicker(fillColor);

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	List<Figure> selectedFigures = new ArrayList<>();	

	// StatusBar
	StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		List<ToggleButton> toolsList = new ArrayList<>();
		toolsList.add(selectionButton);
		//toolsList.addAll(Arrays.asList(FigureButton.values());
		toolsList.addAll(Arrays.stream(FigureButton.values()).map(FigureButton::getButton).collect(Collectors.toList()));
		toolsList.add(deleteButton);
		toolsList.add(sendFrontButton);
		toolsList.add(sendBackButton);

		ToggleGroup tools = new ToggleGroup();
		for (ToggleButton tool : toolsList) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		VBox buttonsBox = new VBox(10);
		
		//Agrego los botones de la izquierda
		buttonsBox.getChildren().addAll(toolsList);
		//Permito que se vea donde esta posicionado
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		//agrego los labels, slider y colorPick
		buttonsBox.getChildren().add(edgeLabel);
		buttonsBox.getChildren().add(slider);
		buttonsBox.getChildren().add(edgeColorPicker);
		buttonsBox.getChildren().add(fillLabel);
		buttonsBox.getChildren().add(fillColorPicker);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		// Creacion de una figura
		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			StringBuilder label = new StringBuilder();
			try {
				Figure newFigure = FigureButton.findButton(startPoint, endPoint);
				if(newFigure != null) {
					newFigure.setEdgeWidth(slider.getValue());
					newFigure.setFillColor(fillColorPicker.getValue());
					newFigure.setEdgeColor(edgeColorPicker.getValue());
					canvasState.addFigure(newFigure);
				}
				if(selectionButton.isSelected()) {
					for (Figure figure : canvasState.figures()) {
						if (figure.isEnclosedBy(startPoint, endPoint)) {
							selectedFigures.add(figure);
							label.append(figure.toString());
							statusPane.updateStatus(label.toString());
						}
					}
				}
			} catch(IllegalArgumentException ex) {

			}
			redrawCanvas();
			selectedFigures.clear();
		});

		// Informacion relevante de la figura
		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState.figures()) {
				if(figure.pointBelongs(eventPoint)) {
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
				selectedFigures.clear();
				//click en un punto sobre una figura
				if(startPoint.compareTo(eventPoint) == 0) {
					for(Figure figure : canvasState.figures()) {
						if (figure.pointBelongs(eventPoint)) {
							selectedFigures.add(figure);
						}
					}
				}
				//hago un rectangulo imaginario
				else{
					for (Figure figure : canvasState.figures()) {
						if(figure.isEnclosedBy(startPoint, eventPoint)) {
							selectedFigures.add(figure);
						}
					}
				}

				StringBuilder label = new StringBuilder();
				label.append("Se selecciono: ");

				if (!selectedFigures.isEmpty()) {
					for(Figure figure : selectedFigures){
						label.append(figure.toString());
					}
					statusPane.updateStatus(label.toString());
				}
				redrawCanvas();
				selectedFigures.clear();
			}

		// TODO mover multiples figuras
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double diffX = (eventPoint.getX() - startPoint.getX()) / 100;
				double diffY = (eventPoint.getY() - startPoint.getY()) / 100;
				if(!selectedFigures.isEmpty()) {
					for (Figure figure : selectedFigures){
						figure.move(diffX, diffY);
						redrawCanvas();
					}
				}
			}
		});
		// TODO no lo probamos 
		deleteButton.setOnAction(event -> {
			canvasState.removeFigures(selectedFigures);
			selectedFigures.clear();
			deleteButton.setSelected(false);
			redrawCanvas();
		});
		//Se cambia el borde de las figuras
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				for (Figure figure : selectedFigures) {
					figure.setEdgeWidth(slider.getValue());
					redrawCanvas();
				}
			}
		});
		//Cambia el color del borde
		edgeColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				for(Figure figure: selectedFigures){
					figure.setEdgeColor(newValue);
					redrawCanvas();
				}
			}
		});
		//Cambia el color del relleno
		fillColorPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				for(Figure figure : selectedFigures){
					figure.setFillColor(newValue);
					redrawCanvas();
				}
			}
		});
		
		setLeft(buttonsBox);
		setRight(canvas);
	}

		// TODO algo anda mal. Se crea en canvasState pero no sale al frontend
	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState.figures()) {
			if (!selectedFigures.isEmpty()) {
				if (selectedFigures.contains(figure))
				figure.setEdgeColor(Color.RED);
			}
			gc.setFill(figure.getFillColor());
			gc.setStroke(figure.getEdgeColor());
			gc.setLineWidth(figure.getEdgeWidth());
			figure.draw(gc);

		}
	}

//	void redrawCanvas() {
//		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//		for(Figure figure : canvasState.figures()) {
//			if(figure == selectedFigure) {
//				gc.setStroke(Color.RED);
//			} else {
//				gc.setStroke(lineColor);
//			}
//			gc.setFill(fillColor);
//			if(figure instanceof Rectangle) {
//				Rectangle rectangle = (Rectangle) figure;
//				gc.fillRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
//						Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
//				gc.strokeRect(rectangle.getTopLeft().getX(), rectangle.getTopLeft().getY(),
//						Math.abs(rectangle.getTopLeft().getX() - rectangle.getBottomRight().getX()), Math.abs(rectangle.getTopLeft().getY() - rectangle.getBottomRight().getY()));
//			} else if(figure instanceof Circle) {
//				Circle circle = (Circle) figure;
//				double diameter = circle.getRadius() * 2;
//				gc.fillOval(circle.getCenterPoint().getX() - circle.getRadius(), circle.getCenterPoint().getY() - circle.getRadius(), diameter, diameter);
//				gc.strokeOval(circle.getCenterPoint().getX() - circle.getRadius(), circle.getCenterPoint().getY() - circle.getRadius(), diameter, diameter);
//			}
//		}
//	}
}
