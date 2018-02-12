package GUI;

import GUI.DataDisplayPanel.DataPanel;
import GUI.GearControlPanel.GearControlPanel;
import GUI.SimulationInitPanel.InitialParametersPanel;
import GUI.SimulationScreen.ExternalView.SimulationPanel;
import GUI.SimulationScreen.InternalBrakeView.BrakeDisplayPanel;
import engine.*;
import GUI.ButtonPanel.ButtonPanel;
import javafx.scene.layout.*;
import GUI.ButtonPanel.EHBButton;

public class GUI
{
    private ButtonPanel _buttonPanel;
    private SimulationPanel _simulationPanel;
    private BrakeDisplayPanel _brakeDisplayPanel;
    private GearControlPanel _gearControlPanel;
    private DataPanel _dataPanel;
    private InitialParametersPanel _initParamPanel;
    private GridPane _gPane;

    public GUI()
    {
        _gPane = new GridPane();
        _buttonPanel = new ButtonPanel();
        _simulationPanel = new SimulationPanel();
        _brakeDisplayPanel = new BrakeDisplayPanel();
        _gearControlPanel = new GearControlPanel();
        _dataPanel = new DataPanel();
        _initParamPanel = new InitialParametersPanel();
        _gPane.add(_brakeDisplayPanel.getPanel(),0,0,2,1);
        _setConstraints(200,250);
        _gPane.add(_dataPanel.getPanel(),0,1,1,1);
        _setConstraints(200,250);
        _gPane.add(_simulationPanel.getPanel(),1,0,3,2);
        _setConstraints(400,250);
        _gPane.add(_buttonPanel.getPanel(),0, 2, 1, 1);
        _setConstraints(200,250);
        _gPane.add(_gearControlPanel.getPanel(), 1,2,1,1);
        _setConstraints(200,250);
        _gPane.add(_initParamPanel.getPanel(),2,2,2,1);
        _setConstraints(200,250);
        //gPane.setFillHeight(bp.getPanel(), true);
      //  gPane.setFillWidth(bp.getPanel(), true);
      //  gPane.setStyle("fx-background-color:#000000;");
      //  gPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));

//        BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResource("/Resources/img/black-leather-texture.jpg").toExternalForm()),
//                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//                BackgroundSize.DEFAULT);
//
//        gPane.setBackground(new Background(myBI));

        Singleton.engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, _gPane));
    }

    private void _setConstraints(int height, int width)
    {
        _gPane.getColumnConstraints().add(new ColumnConstraints(width));
        _gPane.getRowConstraints().add(new RowConstraints(height));
    }


    public GUI getReference() {return this;}

    public EHBButton getEHBReference() {return _buttonPanel.getEHBButton();}

}
