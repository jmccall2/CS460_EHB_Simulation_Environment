package simulation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

/**
 * FXML Controller class for the statistics popup.
 */
public class StatsPopupController
{
    @FXML
    private AnchorPane _anchorPane;
    @FXML
    private Button _exitButton;
    @FXML
    private Button _leftButton;
    @FXML
    private Button _rightButton;
    @FXML
    private Label _title;
    private CategoryAxis _xAxis = new CategoryAxis();
    private NumberAxis _yAxis = new NumberAxis();
    // Same instance of chart is used for each graph and data is cycled in and out.
    private LineChart<String, Number> _lineChart =
            new LineChart<String, Number>(_xAxis, _yAxis);
    private Map<Double, String> _timeIntervalLabels = new HashMap<>();
    // Total number of charts/graphs.
    private int _nCharts = 5;
    private int _currentChart = 0;
    private StatCollector _stats;

    // Get resources.
    private ImageView _left = new ImageView(
            new Image(getClass().getResourceAsStream("/resources/img/left.png")));
    private ImageView _right = new ImageView(
            new Image(getClass().getResourceAsStream("/resources/img/right.png")));

    // Whether the stats page is up or not.
    static boolean isUp = false;

    /**
     * Init method to setup the chart.
     * @param stats StatsCollector instance.
     */
    void init(StatCollector stats) {
        isUp = true;
        initializeButtons();
        _title.setTextFill(Color.web("#FFFFFF"));
        _lineChart.getStylesheets().add(
                getClass().getResource("/resources/css/graphCSS.css").toExternalForm());
        _lineChart.applyCss();
        _anchorPane.getChildren().add(_lineChart);
        _lineChart.setLayoutX(30.0);
        _lineChart.setLayoutY(30.0);
        _lineChart.setLegendVisible(false);
        _stats = stats;
        for (double deltaX : _stats.getDeltaXValues()) _timeIntervalLabels.put(deltaX, "(" + deltaX + " Second Intervals)");
        displayNewGraph();
    }

    /**
     * On click event for the panels exit button.
     * @param event
     */
    @FXML
    public void exit(ActionEvent event) {
        isUp = false;
        Stage stage = (Stage) _exitButton.getScene().getWindow();
        stage.close();
    }

    // Update the left and right buttons visible as applicable.
    private void updateButtonVisibility()
    {
        if (_title.getText().equals("Speed vs Time (1.0 Second Intervals)"))
        {
            _leftButton.setVisible(false);
        } else
        {
            _leftButton.setVisible(true);
        }
        if (_title.getText().equals("Jerk Avg vs Time (1.0 Second Intervals)"))
        {
            _rightButton.setVisible(false);
        } else
        {
            _rightButton.setVisible(true);
        }
    }

    /**
     * Movement to chart on the left event.
     * @param event
     */
    public void masterEventLeft(ActionEvent event) { _currentChart--; _update(); }

    /**
     * Movement to chart on the right event.
     * @param event
     */
    public void masterEventRight(ActionEvent event) { _currentChart++; _update(); }

    // Update the displayed chart.
    private void _update()
    {
        displayNewGraph();
        updateButtonVisibility();
    }

    // Gather the data for the graph to be displayed from the
    // StatCollector and update other appropriate fields.
    private void displayNewGraph()
    {
        _lineChart.getData().clear();
        GraphTypes gt;
        if(_currentChart <= 1) gt = GraphTypes.SPEED_VS_TIME;
        else if(_currentChart == _nCharts-1) gt = GraphTypes.JERK_AVG_VS_TIME;
        else gt = GraphTypes.PRESSURE_VS_TIME;
        String yTitle = "";
        String graphTitle = "";
        XYChart.Series graphData = new XYChart.Series();
        double deltaX;
        //  Graph data depends on the type.
        switch(gt)
        {
            case SPEED_VS_TIME:
                deltaX = _stats.getDeltaXValues().get(_currentChart);
                yTitle = "Speed (MPH)";
                graphTitle = "Speed vs Time " + _timeIntervalLabels.get(deltaX);
                graphData = _stats.mapToSeries(deltaX, GraphTypes.SPEED_VS_TIME);
                break;
            case PRESSURE_VS_TIME:
                deltaX = _stats.getDeltaXValues().get(_currentChart-2);
                yTitle = "Pressure %";
                graphTitle = "Pressure vs Time " + _timeIntervalLabels.get(deltaX);
                graphData = _stats.mapToSeries(deltaX, GraphTypes.PRESSURE_VS_TIME);
                break;
            case JERK_AVG_VS_TIME:
                deltaX = _stats.getDeltaXValues().get(0);
                yTitle = "Jerk Average (m/s^3)";
                graphTitle = "Jerk Avg vs Time " + _timeIntervalLabels.get(deltaX);
                graphData = _stats.mapToSeries(deltaX, GraphTypes.JERK_AVG_VS_TIME);
                break;
        }
        _xAxis.setTickLabelsVisible(false); // The ticks on the X-Axis do not resize to real time updates well.. so disable their visibility.
        _xAxis.setOpacity(0);
        _yAxis.setLabel(yTitle);
        _title.setText(graphTitle);
        _lineChart.getData().setAll(graphData);

    }


    // Initialize left/right buttons.
    private void initializeButtons()
    {
        // Makes it round.
        _leftButton.setStyle("-fx-background-radius: 5em; " + "-fx-min-width: 3px; "
                + "-fx-min-height: 3px; " + "-fx-max-width: 3px; "
                + "-fx-max-height: 3px;");

        _leftButton.setGraphic(_left);
        // Makes it round.
        _rightButton.setStyle("-fx-background-radius: 5em; " + "-fx-min-width: 3px; "
                + "-fx-min-height: 3px; " + "-fx-max-width: 3px; "
                + "-fx-max-height: 3px;");

        _rightButton.setGraphic(_right);
        _leftButton.setVisible(false); // Can't go left to start.
    }



}
