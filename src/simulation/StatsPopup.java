package simulation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StatsPopup
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
    private LineChart<String, Number> _lineChart =
            new LineChart<String, Number>(_xAxis, _yAxis);
    private Map<Double, String> _timeIntervalLabels = new HashMap<>();
    private int _nCharts = 4;
    private int _currentChart = 0;
    private StatCollector _stats;

    public void init(StatCollector stats) {
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

    // Get resources.
    private ImageView _left = new ImageView(
            new Image(getClass().getResourceAsStream("/resources/img/left.png")));
    private ImageView _right = new ImageView(
            new Image(getClass().getResourceAsStream("/resources/img/right.png")));

    @FXML
    public void exit(ActionEvent event) {
        Stage stage = (Stage) _exitButton.getScene().getWindow();
        stage.close();
    }

    private void updateButtonVisibility()
    {
        if (_title.getText().equals("Speed vs Time (1.0 Second Intervals)"))
        {
            _leftButton.setVisible(false);
        } else
        {
            _leftButton.setVisible(true);
        }
        if (_title.getText().equals("Pressure vs Time (10.0 Second Intervals)"))
        {
            _rightButton.setVisible(false);
        } else
        {
            _rightButton.setVisible(true);
        }
    }

    public void masterEventLeft(ActionEvent event) { _currentChart--; _update(); }

    public void masterEventRight(ActionEvent event) { _currentChart++; _update(); }

    private void _update()
    {
        displayNewGraph();
        updateButtonVisibility();
    }

    private void displayNewGraph()
    {
        _lineChart.getData().clear();
        int index = _currentChart <= 1 ? _currentChart : _currentChart - 2;
        double deltaX = _stats.getDeltaXValues().get(index);
        String timeInterval =  _timeIntervalLabels.get(deltaX);
        _xAxis.setTickLabelsVisible(false); // The ticks on the X-Axis do not resize to real time updates well.. so disable their visibility.
        _xAxis.setOpacity(0);
        _yAxis.setLabel(_currentChart <= 1 ? "Speed (MPH)" : "Pressure %");
        _title.setText((_currentChart <= 1 ? "Speed vs " : "Pressure vs") + " Time " + timeInterval);
        _lineChart.getData().setAll(_currentChart <= 1 ?
                _stats.mapToSeries(deltaX, GraphTypes.SPEED_VS_TIME) :
                _stats.mapToSeries(deltaX, GraphTypes.PRESSURE_VS_TIME));
    }

    /**
     * Initialize the buttons.
     */
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
