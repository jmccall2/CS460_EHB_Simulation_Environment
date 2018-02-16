package simulation;

import java.io.IOException;

import interfaces.ButtonColor;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.Singleton;
import simulation.engine.UILabel;
import simulation.engine.UITextField;

public class GUI
{
    GridPane _gPane;
    ButtonColor activeColor = ButtonColor.RED;
    ButtonColor inactiveColor = ButtonColor.BLUE;
    MyController controller = null;
    private UITextField currSpeedField;
    private UITextField pressureField;

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
      newPane.setLayoutX(123);
      newPane.setLayoutY(460);
      currSpeedField = new UITextField("", 10, 640);
      currSpeedField.setWidthHeight(100, 10);
      currSpeedField.addToWindow();
      currSpeedField.setEditable(false);
      UILabel speedLabel = new UILabel("Current Speed", 21, 666);
      speedLabel.addToWindow();
      speedLabel.setColor(Color.WHITE);
      pressureField = new UITextField("", 901, 640);
      pressureField.setWidthHeight(100, 10);
      pressureField.addToWindow();
      pressureField.setEditable(false);
      UILabel pressureLabel = new UILabel("Current Pressure", 906, 666);
      pressureLabel.addToWindow();
      pressureLabel.setColor(Color.WHITE);
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
    }
    
    public void setSpeed(double speed)
    {
      String speedStr = Double.toString(speed);
      currSpeedField.setText(speedStr);
    }
    
    public void setPressure(double pressure)
    {
      String pressureStr = Double.toString(pressure);
      pressureField.setText(pressureStr);
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
