package simulation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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

    public void init()
    {
        initializeButtons();
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
    }

    public void masterEventLeft(ActionEvent event)
    {
        // Check if any of the buttons visibility should change.
        updateButtonVisibility();
    }

    public void masterEventRight(ActionEvent event)
    {
        // Check if any of the buttons visibility should change.
        updateButtonVisibility();
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
