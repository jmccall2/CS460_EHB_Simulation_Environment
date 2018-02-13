package simulation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SimulationStateButton
{
    private Button _simStateButton;
    private boolean _simOn;
    SimulationStateButton()
    {
        _simOn = false;
        _simStateButton = new Button("Start Simulation");
        _simStateButton.setId("simStateButton");
        _simStateButton.getStylesheets().add(getClass().getResource("/resources/css/simStartButton.css").toExternalForm());
        _simStateButton.applyCss();
        EventHandler<ActionEvent> _buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                _toggleButtonState();
                // other stuff.
            }
        };
        _simStateButton.setOnAction(_buttonHandler);

    }

    public Button getButton()
    {
        return _simStateButton;
    }

    private void _toggleButtonState()
    {
        System.out.println("Toggle simulator state.");
        _simOn = !_simOn;
        _simStateButton.getStylesheets().clear();
        String css = _simOn ? "/resources/css/simStopButton.css" : "/resources/css/simStartButton.css";
        String buttonText = _simOn ? "Stop Simulation" : "Start Simulation";
        _simStateButton.setText(buttonText);
        _simStateButton.getStylesheets().add(getClass().getResource(css).toExternalForm());
    }



}
