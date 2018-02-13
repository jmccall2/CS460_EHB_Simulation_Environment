package simulation;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

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
        _addHeader();
    }

    public BorderPane getPanel()
    {
        return _gearControlPanel;
    }

    private void _addHeader()
    {
        HBox container = new HBox();
        Text t = new Text (10, 20, "Gear Control Panel");
        t.setFont(Font.font ("Verdana", 20));
        t.setFill(Color.RED);
        container.getChildren().add(t);
        container.setPadding(new Insets(10,10,10,25));
        _gearControlPanel.setTop(container);
    }

}
