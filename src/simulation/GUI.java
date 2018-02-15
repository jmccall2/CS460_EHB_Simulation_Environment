package simulation;

import java.io.IOException;

import interfaces.ButtonColor;
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
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      interfaces.EHBButton.setActiveColor(activeColor);
      interfaces.EHBButton.setUnActiveColor(inactiveColor);
      controller = new MyController();
      controller.setGUI(this);
      _addFXMLCode();
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(0);
      newPane.setLayoutY(460);
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
      Engine.getMessagePump().registerMessage(new Message(Singleton.START_SIM));
      Engine.getMessagePump().registerMessage(new Message(Singleton.STOP_SIM));
      Engine.getMessagePump().registerMessage(new Message(Singleton.ACTIVATE_BRAKE));
      Engine.getMessagePump().registerMessage(new Message(Singleton.DEACTIVATE_BRAKE));
      Engine.getMessagePump().registerMessage(new Message(Singleton.PARK));
      Engine.getMessagePump().registerMessage(new Message(Singleton.NEUTRAL));
      Engine.getMessagePump().registerMessage(new Message(Singleton.DRIVE));
      Engine.getMessagePump().registerMessage(new Message(Singleton.FIRST));
      Engine.getMessagePump().registerMessage(new Message(Singleton.SECOND));
      Engine.getMessagePump().registerMessage(new Message(Singleton.SPEED));
    }

    public GUI()
    {
      _gPane = new GridPane();
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      interfaces.EHBButton.setActiveColor(activeColor);
      interfaces.EHBButton.setUnActiveColor(inactiveColor);
      controller = new MyController();
      controller.setGUI(this);
      _addFXMLCode();
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(0);
      newPane.setLayoutY(460);
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
      Engine.getMessagePump().registerMessage(new Message(Singleton.START_SIM));
      Engine.getMessagePump().registerMessage(new Message(Singleton.STOP_SIM));
      Engine.getMessagePump().registerMessage(new Message(Singleton.ACTIVATE_BRAKE));
      Engine.getMessagePump().registerMessage(new Message(Singleton.DEACTIVATE_BRAKE));
      Engine.getMessagePump().registerMessage(new Message(Singleton.PARK));
      Engine.getMessagePump().registerMessage(new Message(Singleton.NEUTRAL));
      Engine.getMessagePump().registerMessage(new Message(Singleton.DRIVE));
      Engine.getMessagePump().registerMessage(new Message(Singleton.FIRST));
      Engine.getMessagePump().registerMessage(new Message(Singleton.SECOND));
      Engine.getMessagePump().registerMessage(new Message(Singleton.SPEED));
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
