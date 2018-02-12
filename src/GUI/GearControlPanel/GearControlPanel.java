package GUI.GearControlPanel;

import GUI.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GearControlPanel implements GUIPanel
{
    private BorderPane _gearControlPanel;
    public GearControlPanel()
    {
        _gearControlPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.BROWN, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _gearControlPanel.setBackground(new Background(myBF));
    }

    public BorderPane getPanel()
    {
        return _gearControlPanel;
    }

}
