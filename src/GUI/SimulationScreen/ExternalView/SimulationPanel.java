package GUI.SimulationScreen.ExternalView;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import GUI.*;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;

public class SimulationPanel implements GUIPanel
{

    private BorderPane _simulationPanel;
    public SimulationPanel()
    {
        _simulationPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.GREEN, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _simulationPanel.setBackground(new Background(myBF));
    }

    public BorderPane getPanel()
    {
        return _simulationPanel;
    }

}
