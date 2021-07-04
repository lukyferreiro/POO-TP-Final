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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ColorPicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.Collection;
import java.util.LinkedList;

public class PaintPane extends BorderPane {

	// BackEnd
	private final CanvasState canvasState;

	// Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();

	private final Color edgeColor = Color.BLACK;
	private final Color fillColor = Color.YELLOW;

	// Botones Barra Izquierda
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private final ToggleButton deleteButton = new ToggleButton("Borrar");
	private final ToggleButton sendFrontButton = new ToggleButton("Al fondo");
	private final ToggleButton sendBackButton = new ToggleButton("Al frente");

	//Border slider
	private final Slider slider = new Slider(1, 30, 1);

	//Color Picker
	private final ColorPicker edgeColorPicker = new ColorPicker(edgeColor);
	private final ColorPicker fillColorPicker = new ColorPicker(fillColor);

	//Labels
	Label edgeLabel = new Label("Borde");
	Label fillLabel = new Label("Relleno");

	// Dibujar una figura
	private Point startPoint;

	// Seleccionar una figura
	private final List<Figure> selectedFigures = new LinkedList<>();

	// StatusBar
	private final StatusPane statusPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {

		this.canvasState = canvasState;
		this.statusPane = statusPane;
		List<ToggleButton> toolsList = new ArrayList<>();
		toolsList.add(selectionButton);
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

		//Agrego los labels, slider y colorPick
		buttonsBox.getChildren().add(edgeLabel);
		buttonsBox.getChildren().add(slider);
		buttonsBox.getChildren().add(edgeColorPicker);
		buttonsBox.getChildren().add(fillLabel);
		buttonsBox.getChildren().add(fillColorPicker);

		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		canvas.setOnMousePressed(event -> {
			startPoint = new Point(event.getX(), event.getY());
		});

		//....
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

		//
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
		});

		//
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected() && !selectedFigures.isEmpty()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double deltaX = (eventPoint.getX() - startPoint.getX());
				double deltaY = (eventPoint.getY() - startPoint.getY());
				startPoint = eventPoint;
				for(Figure figure : selectedFigures){
					figure.move(deltaX, deltaY);
				}
				redrawCanvas();
			}
		});

		//Elimina todas las figuras seleccionadas
		deleteButton.setOnAction(event -> {
			canvasState.removeFigures(selectedFigures);
			selectedFigures.clear();
			deleteButton.setSelected(false);
			redrawCanvas();
		});

		//Lleva "Al Frente" todas las figuras seleccionadas
		sendFrontButton.setOnAction(event -> {
			canvasState.moveForward(selectedFigures);
			sendFrontButton.setSelected(false);	//Decimos que el boton dejo de estar pulsado
			redrawCanvas();
		});

		//LLeva "Al Fondo" todas las figuras selecciondas
		sendBackButton.setOnAction(event -> {
			canvasState.moveBackwards(selectedFigures);
			sendBackButton.setSelected(false);
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
}
