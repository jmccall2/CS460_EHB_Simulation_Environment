package GUI.GearControlPanel;

import GUI.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class GearControlPanel implements GUIPanel
{
    private BorderPane _gearControlPanel;
    private Shifter _shifter;
    public GearControlPanel()
    {
        _gearControlPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.BLACK, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _gearControlPanel.setBackground(new Background(myBF));
        _shifter = new Shifter();
        _gearControlPanel.setCenter(_shifter.getHBox());
      //  _gearControlPanel.setAlignment(_shifter.getHBox(), Pos.CENTER);
    }

    public BorderPane getPanel()
    {
        return _gearControlPanel;
    }

}
