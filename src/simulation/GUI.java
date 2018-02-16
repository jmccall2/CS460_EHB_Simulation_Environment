package simulation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import simulation.engine.*;

import java.io.IOException;

public class GUI
{
    private GridPane _gPane;
    private MyController controller = null;
    private UITextField currSpeedField;
    private UITextField pressureField;

    public GUI()
    {
      _gPane = new GridPane();
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      controller = new MyController();
      _addFXMLCode();
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(110);
      newPane.setLayoutY(460);
      currSpeedField = new UITextField("", 10, 625);
      currSpeedField.setWidthHeight(100, 10);
      currSpeedField.addToWindow();
      currSpeedField.setEditable(false);
      UILabel speedLabel = new UILabel("Current Speed", 21, 655);
      speedLabel.addToWindow();
      speedLabel.setColor(Color.WHITE);
      pressureField = new UITextField("", 890, 625);
      pressureField.setWidthHeight(100, 10);
      pressureField.addToWindow();
      pressureField.setEditable(false);
      UILabel pressureLabel = new UILabel("Current Pressure", 894, 655);
      pressureLabel.addToWindow();
      pressureLabel.setColor(Color.WHITE);
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
    }
    
    public void setSpeed(double speed)
    {
      String speedStr = Double.toString(speed);
      currSpeedField.setText(speedStr);
    }

    public void setInitColor()
    {
      controller.setInitButtonColor();
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


}
