package application;
	
import java.awt.BasicStroke;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
    
	static Stage primaryStage = new Stage();
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		/**
		 * Generates the primary stage the program displays on. Uses Sample.fxml for layout.
		 * This method also makes sure that the stage is undecorated (without default border and buttons)
		 */
		
		this.primaryStage = primaryStage; //makes primaryStage global = primaryStage local of method
		AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
		Scene scene = new Scene(root,1280,720);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setMinHeight(720);
		primaryStage.setMinWidth(1280);
	    primaryStage.setScene(scene);
	    primaryStage.show();
    } /* general note about consoles, in theory shouldn't be allowed to type in them,
    but there should be input bar and button on bottom of console in case it's needed */
	
	public static void main(String[] args) {
		launch(args);
	}
    
	//methods that SampleController calls
	
	static final Delta dragDelta = new Delta(); //dragging top node around section
	
	static void onDrag(MouseEvent mouseEvent){
		dragDelta.x = primaryStage.getX() - mouseEvent.getScreenX();
		dragDelta.y = primaryStage.getY() - mouseEvent.getScreenY();
	}
	
	static void onDragged(MouseEvent mouseEvent){
		primaryStage.setX(mouseEvent.getScreenX() + dragDelta.x);
	    primaryStage.setY(mouseEvent.getScreenY() + dragDelta.y);
	}
	
}

class Delta { double x, y; } //used for coordinates of mouse and dragging
