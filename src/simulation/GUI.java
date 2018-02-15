package simulation;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.Singleton;

public class GUI
{
    private ButtonPanel _buttonPanel;
    private GearControlPanel _gearControlPanel;
    private DataPanel _dataPanel;
    private InitialParametersPanel _initParamPanel;
    private GridPane _gPane;
    MyController controller = null;

    public GUI()
    {
        _gPane = new GridPane();
//        _buttonPanel = new ButtonPanel();
//        _gearControlPanel = new GearControlPanel();
//        _dataPanel = new DataPanel();
//        _initParamPanel = new InitialParametersPanel();
//        _gPane.add(_buttonPanel.getPanel(),0, 2, 1, 1);
//        _setConstraints(225,250);
//        _gPane.add(_gearControlPanel.getPanel(), 1,2,1,1);
//        _setConstraints(225,250);
//        _gPane.add(_dataPanel.getPanel(),2,2,1,1);
//        _setConstraints(225,250);
//        _gPane.add(_initParamPanel.getPanel(),3,2,1,1);
//        _setConstraints(225,250);
        _addFXMLCode();
        Pane newPane = new Pane();
        newPane.getChildren().add(_gPane);
        newPane.setLayoutX(0);
        newPane.setLayoutY(460);
        Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
    }
    
    private void _addFXMLCode()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("controlPanel.fxml"));
        page = loader.load();
        controller = (MyController)loader.getController();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      _gPane.getChildren().setAll(page);
    }

    private void _setConstraints(int height, int width)
    {
        _gPane.getColumnConstraints().add(new ColumnConstraints(width));
        _gPane.getRowConstraints().add(new RowConstraints(height));
    }

    public GUI getReference() {return this;}

    public EHBButton getEHBReference() {return _buttonPanel.getEHBButton();}

}
