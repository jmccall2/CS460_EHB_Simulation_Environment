package GUI.SimulationScreen.InternalBrakeView;

import GUI.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class BrakeDisplayPanel implements GUIPanel
{
    private BorderPane _brakeDisplayPanel;
    public BrakeDisplayPanel()
    {
        _brakeDisplayPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.BLUEVIOLET, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _brakeDisplayPanel.setBackground(new Background(myBF));

    }

    public BorderPane getPanel()
    {
        return _brakeDisplayPanel;
    }
}
