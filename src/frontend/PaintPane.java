package frontend;

import backend.CanvasState;
import backend.model.Figure;
import backend.model.Point;
import backend.model.Rectangle;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.*;
import java.util.stream.Collectors;

public class PaintPane extends BorderPane {

	// BackEnd
	private final CanvasState canvasState;

	//Colores por defecto
	private final Color edgeColor = Color.BLACK;
	private final Color fillColor = Color.YELLOW;

	//Labels
	private final Label edgeLabel = new Label("Borde");
	private final Label fillLabel = new Label("Relleno");

	// Canvas y relacionados
	private final Canvas canvas = new Canvas(800, 600);
	private final GraphicsContext gc = canvas.getGraphicsContext2D();

	// Botones Barra Izquierda
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private final ToggleButton deleteButton = new ToggleButton("Borrar");
	private final ToggleButton sendFrontButton = new ToggleButton("Al Fondo");
	private final ToggleButton sendBackButton = new ToggleButton("Al Frente");

	//Border slider
	private final Slider edgeSlider = new Slider(1,50,0);

	//Color Picker
	private final ColorPicker edgeColorPicker = new ColorPicker(edgeColor);
	private final ColorPicker fillColorPicker = new ColorPicker(fillColor);

	// Punto inicial para dibujar una figura
	private Point startPoint;

	// Coleccion con las figuras seleccionadas
	private final Collection<Figure> selectedFigures = new LinkedList<>();

	// StatusBar.
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
		edgeSlider.setShowTickMarks(true);
		edgeSlider.setShowTickLabels(true);

		//Agrego los labels, slider y colorPick
		buttonsBox.getChildren().add(edgeLabel);
		buttonsBox.getChildren().add(edgeSlider);
		buttonsBox.getChildren().add(edgeColorPicker);
		buttonsBox.getChildren().add(fillLabel);
		buttonsBox.getChildren().add(fillColorPicker);

		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		canvas.setOnMousePressed(event -> startPoint = new Point(event.getX(), event.getY()));

		canvas.setOnMouseReleased(event -> {
			Point endPoint = new Point(event.getX(), event.getY());
			try {
				selectedFigures.clear();
				Figure newFigure = FigureButton.findButtons(startPoint, endPoint);
				// Cuando se crea una figura por primera vez se la setea con lo valores por default:
				// Ancho de borde: 1
				// Color de borde: Negro
				// Color de relleno: Amarillo
				// Y se agrega la figura a la lista de figuras
				if (newFigure != null) {
					newFigure.setEdgeWidth(edgeSlider.getValue());
					newFigure.setFillColor(fillColorPicker.getValue());
					newFigure.setEdgeColor(edgeColorPicker.getValue());
					canvasState.add(newFigure);
				}
			} catch (Exception e){
				statusPane.updateStatus(e.getMessage());
			}
			redrawCanvas();
		});

		// Evento de movimiento de mouse
		canvas.setOnMouseMoved(event -> {
			Point eventPoint = new Point(event.getX(), event.getY());
			boolean found = false;
			StringBuilder label = new StringBuilder();
			for(Figure figure : canvasState) {
				// Recorremos las figuras y nos fijamos si el punto donde se encuentra el mouse
				// pertenece a alguna figura
				if(figure.pointBelongs(eventPoint)) {
					found = true;
					label.append(figure.toString());
				}
			}
			//Si estamos con el mouse sobre una figura retornamos sus datos
			if(found) {
				statusPane.updateStatus(label.toString());
			}
			//Sino, retornamos los datos del punto en el que nos hallamos
			else {
				statusPane.updateStatus(eventPoint.toString());
			}
		});

		// Evento de seleccion
		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				selectedFigures.clear();
				//En caso de ser seleccion multiple
				if( !startPoint.equals(eventPoint) ) {
					try {
						//Nos fijamos que figuras se encuentran dentro del rectangulo imaginario
						Rectangle container = new Rectangle(startPoint, eventPoint);
						for(Figure figure : canvasState) {
							if(figure.isEnclosedBy(container)) {
								selectedFigures.add(figure);
							}
						}
					} catch(Exception e) {
						statusPane.updateStatus(e.getMessage());
					}
				}
				//Si estamos aca es que fue seleccion simple, solamente se hizo click
				else {
					Iterator<Figure> it = canvasState.descendingIterator();
					while(it.hasNext() && selectedFigures.isEmpty()) {
						Figure fig = it.next();
						if(fig.pointBelongs(eventPoint)) {
							selectedFigures.add(fig);
						}
					}
				}

				StringBuilder label = new StringBuilder("Se seleccionó: ");
				if ( !selectedFigures.isEmpty() ) {
					for(Figure figure : selectedFigures){
						label.append(figure);
					}
					statusPane.updateStatus(label.toString());
				}
				else {
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
		});

		// Evento de mover las figuras que estan seleccionadas
		canvas.setOnMouseDragged(event -> {
			if(selectionButton.isSelected() && !selectedFigures.isEmpty()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				double deltaX = eventPoint.getX() - startPoint.getX();
				double deltaY = eventPoint.getY() - startPoint.getY();
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
			sendFrontButton.setSelected(false);
			redrawCanvas();
		});

		//LLeva "Al Fondo" todas las figuras selecciondas
		sendBackButton.setOnAction(event -> {
			canvasState.moveBackwards(selectedFigures);
			sendBackButton.setSelected(false);
			redrawCanvas();
		});


		//Se cambia el borde de las figuras
		edgeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
			for (Figure figure : selectedFigures) {
				figure.setEdgeWidth(edgeSlider.getValue());
				redrawCanvas();
			}
		});

		//Cambia el color del borde
		edgeColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			for(Figure figure: selectedFigures){
				figure.setEdgeColor(newValue);
				redrawCanvas();
			}
		});

		//Cambia el color del relleno
		fillColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			for(Figure figure : selectedFigures){
				figure.setFillColor(newValue);
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState) {
			if(selectedFigures.contains(figure)) {
				gc.setStroke(Color.RED);
			} else {
				gc.setStroke(figure.getEdgeColor());
			}
			gc.setLineWidth(figure.getEdgeWidth());
			gc.setFill(figure.getFillColor());
			figure.draw(gc);
		}
	}
}