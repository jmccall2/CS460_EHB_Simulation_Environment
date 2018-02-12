package GUI.DataDisplayPanel;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import GUI.*;
import javafx.scene.paint.Color;

public class DataPanel implements GUIPanel
{
    private BorderPane _dataPanel;
    public DataPanel()
    {
        _dataPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.PEACHPUFF, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _dataPanel.setBackground(new Background(myBF));
    }

    public BorderPane getPanel()
    {
        return _dataPanel;
    }


}
