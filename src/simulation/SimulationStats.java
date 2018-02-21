package simulation;

import interfaces.SpeedInterface;
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
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Pulsar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SimulationStats {
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
    private LinkedList<Double> _deltaXValues = new LinkedList<>();
    private Map<Double, LinkedList<Double>> _deltaXSpeedData = new HashMap<>();
    private Map<Double, LinkedList<Double>> _deltaXPressureData = new HashMap<>();
    private Map<Double, String> _timeAxisLabels = new HashMap<>();
    private XYChart.Series _data = new XYChart.Series();
    private double _pressure = 0.0;
    private int _nCharts = 3;
    private int _currentChart = 0;
    private Helper _helper = new Helper();


    public void init() {
        initializeButtons();
        _title.setTextFill(Color.web("#FFFFFF"));
        _lineChart.getStylesheets().add(
                getClass().getResource("/resources/css/graphCSS.css").toExternalForm());
        _lineChart.applyCss();
        _anchorPane.getChildren().add(_lineChart);
        _lineChart.setLayoutX(20.0);
        _lineChart.setLayoutY(30.0);
        _lineChart.setLegendVisible(false);

        Engine.getMessagePump().signalInterest(SimGlobals.SET_PRESSURE, _helper);

        // Initialize data containers.
        for (double deltaX : _deltaXValues) {
            _deltaXPressureData.put(deltaX, new LinkedList<>());
            _deltaXSpeedData.put(deltaX, new LinkedList<>());
            _timeAxisLabels.put(deltaX, "(" + deltaX + " Second Intervals)");
        }
        // Generate Pulsars to get data from the engine.
        new Pulsar(1.0, () -> _updateData(1.0)).start();
        new Pulsar(10.0, () -> _updateData(10.0)).start();
        new Pulsar(30.0, () -> _updateData(30.0)).start();
        new Pulsar(60.0, () -> _updateData(60.0)).start();

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


    private void _updateData(double deltaX)
    {
        if(_deltaXSpeedData.get(deltaX).size() >= 20) _deltaXSpeedData.get(deltaX).pop();
        if(_deltaXPressureData.get(deltaX).size() >= 20) _deltaXPressureData.get(deltaX).pop();
        _deltaXSpeedData.get(deltaX).add(SpeedInterface.getSpeed());
        _deltaXPressureData.get(deltaX).add(_pressure); // Not available through the interfaces.
    }

    private void updateButtonVisibility()
    {
        if (_title.getText().equals("Speed vs Time"))
        {
            _leftButton.setVisible(false);
        } else
        {
            _leftButton.setVisible(true);
        }
        if (_title.getText().equals("Speed vs Pressure"))
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
        double deltaX = _deltaXValues.get(_currentChart);
        String timeInterval =  _timeAxisLabels.get(deltaX);
        _xAxis.setLabel(timeInterval);
        _yAxis.setLabel(_currentChart < 3 ? "Speed (MPH)" : "Pressure %");
        _title.setText((_currentChart < 3 ? "Speed vs " : "Pressure vs ") + " Time " + timeInterval);
        _lineChart.getData().setAll(_currentChart < 3 ? _mapToSeries(_deltaXSpeedData) : _mapToSeries(_deltaXPressureData));
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

    private XYChart.Series _mapToSeries(Map<Double, LinkedList<Double>> rawData)
    {
        XYChart.Series series = new XYChart.Series();
        for(Map.Entry<Double,LinkedList<Double>> entry : rawData.entrySet()) series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        return series;
    }


    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.getMessageName())
            {
                case SimGlobals.SET_PRESSURE:
                    _pressure = (Double) message.getMessageData();
                    break;

            }
        }
    }

}
