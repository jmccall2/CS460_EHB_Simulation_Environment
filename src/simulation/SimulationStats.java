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
    private LineChart<String,Number> _lineChart =
            new LineChart<String,Number>(_xAxis,_yAxis);
    private XYChart.Series _data = new XYChart.Series();

    private int _nCharts = 3;
    private int _currentChart = 0;

    public void init()
    {
        initializeButtons();
        _title.setTextFill(Color.web("#FFFFFF"));
        _lineChart.getStylesheets().add(
                getClass().getResource("/resources/css/graphCSS.css").toExternalForm());
        _lineChart.applyCss();
        _anchorPane.getChildren().add(_lineChart);
        _lineChart.setLayoutX(20.0);
        _lineChart.setLayoutY(30.0);
        _lineChart.setLegendVisible(false);
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

    public void updateButtonVisibility()
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
        switch(_currentChart)
        {
            case 0:
                _buildFakeSpeedData();
                _xAxis.setLabel("Time (Minutes)");
                _yAxis.setLabel("Speed (MPH)");
                _title.setText("Speed vs Time");
                break;
            case 1:
                _buildFakePressureData();
                _xAxis.setLabel("Time (Minutes)");
                _yAxis.setLabel("Pressure %");
                _title.setText("Pressure vs Time");
                break;
            case 2:
                _buildFakeSpeedVsPressureData();
                _xAxis.setLabel("Speed");
                _yAxis.setLabel("Pressure %");
                _title.setText("Speed vs Pressure");
                break;
        }
        _lineChart.getData().setAll(_data);

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


    // This is for testing purposes.. real data will be used later.
    // Note so I don't forget: choose time interval to check /add new graph nodes, like one minute.
    // Interval must match x axis incrementation.
    private void _buildFakeSpeedData()
    {
        _data = new XYChart.Series(); // _data.getData().clear(); does not work, new instance must be created.
        _data.getData().add(new XYChart.Data("0", 23));
        _data.getData().add(new XYChart.Data("1", 14));
        _data.getData().add(new XYChart.Data("2", 15));
        _data.getData().add(new XYChart.Data("3", 24));
        _data.getData().add(new XYChart.Data("4", 34));
        _data.getData().add(new XYChart.Data("5", 36));
        _data.getData().add(new XYChart.Data("6", 22));
        _data.getData().add(new XYChart.Data("7", 45));
        _data.getData().add(new XYChart.Data("8", 43));
        _data.getData().add(new XYChart.Data("9", 17));
    }
    private void _buildFakePressureData()
    {
        _data = new XYChart.Series();
        _data.getData().add(new XYChart.Data("0", 23));
        _data.getData().add(new XYChart.Data("1", 74));
        _data.getData().add(new XYChart.Data("2", 45));
        _data.getData().add(new XYChart.Data("3", 24));
        _data.getData().add(new XYChart.Data("4", 64));
        _data.getData().add(new XYChart.Data("5", 36));
        _data.getData().add(new XYChart.Data("6", 22));
        _data.getData().add(new XYChart.Data("7", 45));
        _data.getData().add(new XYChart.Data("8", 13));
        _data.getData().add(new XYChart.Data("9", 17));
    }
    private void _buildFakeSpeedVsPressureData()
    {
        _data = new XYChart.Series();
        _data.getData().add(new XYChart.Data("0", 23));
        _data.getData().add(new XYChart.Data("1", 14));
        _data.getData().add(new XYChart.Data("2", 15));
        _data.getData().add(new XYChart.Data("3", 24));
        _data.getData().add(new XYChart.Data("4", 64));
        _data.getData().add(new XYChart.Data("5", 26));
        _data.getData().add(new XYChart.Data("6", 72));
        _data.getData().add(new XYChart.Data("7", 25));
        _data.getData().add(new XYChart.Data("8", 83));
        _data.getData().add(new XYChart.Data("9", 17));
    }



}
