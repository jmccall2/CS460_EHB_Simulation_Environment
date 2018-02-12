package GUI.SimulationInitPanel;

import GUI.*;
import GUI.SimulationScreen.ExternalView.SimulationPanel;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class InitialParametersPanel implements GUIPanel
{
    private BorderPane _initParamsPanel;
    public InitialParametersPanel()
    {
        _initParamsPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.ORANGE, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _initParamsPanel.setBackground(new Background(myBF));
        SimulationStateButton _simStateButton = new SimulationStateButton();
        _initParamsPanel.setCenter(_simStateButton.getButton());
    }

    public BorderPane getPanel()
    {
        return _initParamsPanel;
    }


}
