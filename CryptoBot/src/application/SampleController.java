package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.marketdata.Ticker;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class SampleController implements Initializable {
	
    @FXML
    void buttonClose(ActionEvent event) {
    	System.exit(0);
    }
    
    @FXML
    void onDrag(MouseEvent event) {
    	Main.onDrag(event);
    }

    @FXML
    void onDragged(MouseEvent event) {
    	Main.onDragged(event);
    }
    
    @FXML
    ListView<Profile> exchangeListView;
    
    
    
    @Override // EQUIVALENT OF MAIN METHOD
	public void initialize(URL location, ResourceBundle resources) {
    	/**
    	 * Initializes the program and its components.
    	 * Currently hardcoded to display a pre-generated list of test profiles,
    	 * and creates an Ethereum/Bitcoin chart fetched from Cryptopia.co.nz
    	 */
    	
    	
    	ObservableList<Profile> exchangeList = FXCollections.observableArrayList();
        
    	exchangeList.addAll(
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Default, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Default, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Default, null, null, Currency.BTC, Currency.ETH),
    			new Profile(Exchange.Cryptopia, null, null, Currency.BTC, Currency.ETH)
    			);
    	
        exchangeListView.setItems(exchangeList);
	    exchangeListView.setCellFactory(param -> new ExchangeListCell());
	    
	    
	    createAndSetSwingDrawingPanel(chartSwingNode); // create Cryptopia ETH/BTC test chart
	    
	    /*ChangeListener<Number> chartPaneSizeListener = (observable, oldValue, newValue) ->
	    	chartSwingNode.resize(chartPane.getWidth(), chartPane.getHeight());
	    	chartSwingNode.setPreferredSize(chartPane.getWidth(), chartPane.getHeight());
	    chartPane.widthProperty().addListener(chartPaneSizeListener);
	    chartPane.heightProperty().addListener(chartPaneSizeListener);*/
	    
	    ChangeListener<Number> chartPaneSizeListener = ((observable, oldValue, newValue) -> {
	    	chartSwingNode.resize(chartPane.getWidth(), chartPane.getHeight());
	    	//chartSwingNode.prefWidth(chartPane.getWidth()); chartSwingNode.prefHeight(chartPane.getHeight());
	    	chartSwingNode.getContent().setPreferredSize(new Dimension((int) chartPane.getWidth(), (int) chartPane.getHeight()));
	    	mainChartPanel.setPreferredSize(new Dimension((int) chartPane.getWidth(), (int) chartPane.getHeight()));
    		mainChartPanel.setSize((int) chartPane.getWidth(), (int) chartPane.getHeight());
	    });
    	chartPane.widthProperty().addListener(chartPaneSizeListener);
    	chartPane.heightProperty().addListener(chartPaneSizeListener);
	    //timer that updates charts with ticker data should be here
	}
    
    
    @FXML
	Pane chartPane;
	
	@FXML
	SwingNode chartSwingNode = new SwingNode();
	
	ChartPanel mainChartPanel; // panel for the main chart, not to be confused with chartPane
	
	public void createAndSetSwingDrawingPanel(final SwingNode swingNode) {
		/**
		 * Creates the chart displayed in the center right of the program.
		 * Defines its dimensions, and schedules a TimerTask to update the chart
		 * every 60 seconds with new ticker data.
		 */
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	mainChartPanel = new ChartPanel(createChart(createDataset()));
            	System.out.println("done creating");
            	mainChartPanel.setPreferredSize(new Dimension(960, 490)); //960 495
                chartSwingNode.setContent(mainChartPanel);
                //mainChartPanel.getChart().fireChartChanged();
                //dataset.getSeries().add((TimeSeries) MovingAverage.createMovingAverage(market, "testMAV", 864000000, 0));
                
                Timer t = new Timer();
        	    t.schedule(new TimerTask() {
        	        @Override
        	        public void run() {
        	        	Ticker ticker; BigDecimal close; Date timestamp;
        				try {
        					ticker = actor.marketService.getTicker(actor.currencyPair);
        					close = ticker.getLast();
        					timestamp = ticker.getTimestamp();
        					market.addOrUpdate(new Millisecond(timestamp), close);
        					//mainChartPanel.getChart().fireChartChanged();
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        	        }
        	    }, 0, 60000);
            }
        });
    }
    
    /**
     * Creates a sample dataset from fetched Ethereum/Bitcoin data from Cryptopia.
     * In addition, it also generates two sample moving averages for the chart.
     * 
     * @return a sample dataset.
     */
	CryptopiaActor actor = new CryptopiaActor("REDACTED API KEY", "REDACTED API SECRET", Currency.BTC, Currency.ETH);
	// To make the code on the line above work, the API Key and API Secret for the Cryptopia account must be entered
	TimeSeriesCollection dataset;
	TimeSeries market;
	private XYDataset createDataset() {
    	TimeSeriesCollection dataset = new TimeSeriesCollection();
		try {
			market = actor.marketInfo(100); //smaller better, 240 some number
			System.out.println("done with process");
			final TimeSeries mavShort = MovingAverage.createMovingAverage(
					market, "1 day moving average", 86400000, 0 // 86400000 is one day 5 day 432000000
				);
			System.out.println("5 day moving average - done");
		    final TimeSeries mav = MovingAverage.createMovingAverage(
		            market, "10 day moving average", 864000000, 0 // 86400000 is one day 20 day 1728000000
		        );
		    System.out.println("20 day moving average - done");
		    dataset.addSeries(market);
		    dataset.addSeries(mav);
		    dataset.addSeries(mavShort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataset;
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset the dataset used for the chart.
     * The current test case is ETH/BTC dataset detched from cryptopia
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
            null, // "Time Series Demo 8"
            null, // "Date" 
            null, // "Value"
            dataset, 
            true, // true 
            true, 
            false // false
        );
        System.out.println("createTimeSeriesChart - Done");
        // setting up colors of main chart
        chart.setBackgroundPaint(Color.decode("#2E3337")); // outer of chart
        XYPlot xyPlot = chart.getXYPlot();
	    xyPlot.setBackgroundPaint(Color.decode("#3b4045")); // inner of chart
	    xyPlot.setDomainTickBandPaint(Color.decode("#42474d")); // vertical band color - 28% (lighter), normal 25% hsl
	    
	    xyPlot.getDomainAxis().setTickLabelPaint(Color.decode("#bac0c4"));
	    xyPlot.getRangeAxis().setTickLabelPaint(Color.decode("#bac0c4"));
	    
	    chart.getLegend().setBackgroundPaint(Color.decode("#3b4045"));
	    chart.getLegend().setItemPaint(Color.decode("#bac0c4"));
	    chart.getLegend().setPosition(RectangleEdge.TOP);
	    chart.getLegend().setHorizontalAlignment(HorizontalAlignment.RIGHT);
	    // end of setting up colors of main chart
	    System.out.println("before render set");
        final XYItemRenderer renderer = chart.getXYPlot().getRenderer();
        BasicStroke stroke = new BasicStroke(1f);
        renderer.setSeriesStroke(1, stroke);
        renderer.setBaseOutlinePaint(Color.WHITE);
        final StandardXYToolTipGenerator g = new StandardXYToolTipGenerator(
            StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
            new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")
        );
        renderer.setBaseToolTipGenerator(g);
        System.out.println("after render set");
        return chart;
    }
	
    

    
    
    public class ExchangeListCell extends ListCell<Profile> {
    	/**
    	 * Template for exchange list cells. These cells are populated by exchange profiles.
    	 * Different images and text are set depending on the exchange and market of the profile.
    	 */
        private HBox hbox = new HBox();
        private ImageView imageView = new ImageView();
        private Button exchangeDisplay = new Button(); //display exchange name
        private Region region = new Region(); //Fill space in middle
        private Button status = new Button("Offline");
        private Button deleteButton = new Button("X");
        
        private String exchangeName = "exchangeName error";
        
        public ExchangeListCell() {
            configureGrid();        
            configureControls(); //Can be more methods, just have to add them if needed, all is currently done in this method
            addControlsToGrid();            
        }
     
        private void configureGrid() {
        	hbox.setSpacing(5);
        	hbox.setAlignment(Pos.CENTER_LEFT);
        	HBox.setHgrow(region, Priority.ALWAYS);
            //hbox.setPadding(new Insets(5, 5, 5, 5));
        }
     
        private void configureControls() {
			exchangeDisplay.setDisable(true); //disabled because it is intended to act like a label instead of a button
            //exchangeDisplay.setMnemonicParsing(false); // not working? underscore still not appearing
			
            deleteButton.setStyle("-fx-background-color: #e3e6e8; -fx-text-fill: #2E3337;");
            deleteButton.setOnAction(event -> getListView().getItems().remove(getItem()));
            
            status.getStyleClass().add("status-Offline");
            
        }
     
        private void addControlsToGrid() {
        	hbox.getChildren().add(imageView);
        	hbox.getChildren().add(exchangeDisplay);
        	hbox.getChildren().add(region);
        	hbox.getChildren().add(status);
        	hbox.getChildren().add(deleteButton);
        }
     
        @Override
        public void updateItem(Profile profile, boolean empty) {
            super.updateItem(profile, empty);
            if (empty) {
                clearContent();
            } else {
                addContent(profile);
            }
        }
     
        private void clearContent() {
            setText(null);
            setGraphic(null);
        }
     
        private void addContent(Profile profile) {
            setText(null);
            exchangeName = profile.getExchange().toString();
            
            exchangeDisplay = new Button(exchangeName + " - " + profile.getCoinTwo().toString()
            		+ " _ " + profile.getCoinOne().toString());
        	try{ // setup image
				imageView = new ImageView(new Image(getClass().getResource(exchangeName.toLowerCase(Locale.US) + "_image.png").toExternalForm(), 40, 40, true, true)); // sets exchange image
			}catch (Exception e){
				imageView = new ImageView(new Image(getClass().getResource("default_image.png").toExternalForm(), 40, 40, true, true)); // sets default image
			}
        	hbox.getChildren().clear();
        	hbox.getChildren().add(imageView);
        	hbox.getChildren().add(exchangeDisplay);
        	hbox.getChildren().add(region);
        	hbox.getChildren().add(status);
        	hbox.getChildren().add(deleteButton);
            //setStyleClassDependingOnFoundState(cache);        
            setGraphic(hbox);
        }
     
        /*private void setStyleClassDependingOnFoundState(Cache cache) {
            if (CacheUtils.hasUserFoundCache(cache, new Long(3906456))) {
                addClasses(this, CACHE_LIST_FOUND_CLASS);
                removeClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
            } else {
                addClasses(this, CACHE_LIST_NOT_FOUND_CLASS);
                removeClasses(this, CACHE_LIST_FOUND_CLASS);
            }
        }*/
    }
    
    
     
}

