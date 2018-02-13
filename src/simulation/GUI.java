package simulation;

import simulation.engine.*;
import javafx.scene.layout.*;

public class GUI
{
    private ButtonPanel _buttonPanel;
    private GearControlPanel _gearControlPanel;
    private DataPanel _dataPanel;
    private InitialParametersPanel _initParamPanel;
    private GridPane _gPane;

    public GUI()
    {
        _gPane = new GridPane();
        _buttonPanel = new ButtonPanel();
        _gearControlPanel = new GearControlPanel();
        _dataPanel = new DataPanel();
        _initParamPanel = new InitialParametersPanel();
        _gPane.add(_buttonPanel.getPanel(),0, 2, 1, 1);
        _setConstraints(225,250);
        _gPane.add(_gearControlPanel.getPanel(), 1,2,1,1);
        _setConstraints(225,250);
        _gPane.add(_dataPanel.getPanel(),2,2,1,1);
        _setConstraints(225,250);
        _gPane.add(_initParamPanel.getPanel(),3,2,1,1);
        _setConstraints(225,250);
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
