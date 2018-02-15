package simulation;

import java.io.IOException;

import interfaces.ButtonColor;
import interfaces.EHBButtonInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.Singleton;

public class GUI
{
    GridPane _gPane;
    ButtonColor activeColor = ButtonColor.RED;
    ButtonColor inactiveColor = ButtonColor.BLUE;
    MyController controller = null;
    
    public GUI(ButtonColor activeColor, ButtonColor inactiveColor)
    {
      setActiveColor(activeColor);
      setUnActiveColor(inactiveColor);
      _gPane = new GridPane();
      EHBButtonInterface.setActiveColor(activeColor);
      EHBButtonInterface.setUnActiveColor(inactiveColor);
      controller = new MyController();
      controller.setGUI(this);
      _addFXMLCode();
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(0);
      newPane.setLayoutY(460);
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
    }

    public GUI()
    {
      _gPane = new GridPane();
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      controller = new MyController();
      controller.setGUI(this);
      _addFXMLCode();
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(0);
      newPane.setLayoutY(460);
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
    }
    
    public void setInitColor()
    {
      controller.setInitButtonColor();
    }
    
    public void setActiveColor(ButtonColor col)
    {
      activeColor = col;
    }
    
    public void setUnActiveColor(ButtonColor col)
    {
      inactiveColor = col;
    }
    
    public ButtonColor getActiveColor()
    {
      return activeColor;
    }
    
    public ButtonColor getUnActiveColor()
    {
      return inactiveColor;
    }
    
    private void _addFXMLCode()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("controlPanel.fxml"));
        loader.setController(controller);
        page = loader.load();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      _gPane.getChildren().setAll(page);
    }

    public GUI getReference() {return this;}

    public EHBButton getEHBReference() {return controller.getEHB();}

}
