package simulation;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.Singleton;
import simulation.engine.UILabel;
import simulation.engine.UITextField;

public class GUI
{
    private GridPane _gPane;
    private MyController controller = null;
    private UITextField currSpeedField;
    private UITextField pressureField;
    HBox popupBox;
    Stage popUpStage;
    PopupController controller2;
    
    public GUI()
    {
      _gPane = new GridPane();
      popUpStage = new Stage();
      popupBox = new HBox();
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      controller = new MyController();
      controller.setGui(this);
      controller2 = new PopupController();
      controller2.setGui(this);
      _addFXMLCode1();
      _addFXMLCode2();
      popUpStage.setScene(new Scene(popupBox));
      popUpStage.setOnCloseRequest(event ->
      {
        controller.setMaxSpeed(controller2.getMaxSpeed());
        popUpStage.close();
      });
      popUpStage.setTitle("Set Gear States");
      popUpStage.show();
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(110);
      newPane.setLayoutY(460);
      currSpeedField = new UITextField("", 10, 625);
      currSpeedField.setWidthHeight(100, 10);
      currSpeedField.addToWindow();
      currSpeedField.setEditable(false);
      UILabel speedLabel = new UILabel("Current Speed", 11, 655);
      speedLabel.addToWindow();
      speedLabel.setColor(Color.WHITE);
      pressureField = new UITextField("", 890, 625);
      pressureField.setWidthHeight(100, 10);
      pressureField.addToWindow();
      pressureField.setEditable(false);
      UILabel pressureLabel = new UILabel("Current Pressure", 889, 655);
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

    private void _addFXMLCode1()
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
    
    private void _addFXMLCode2()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("initPopup.fxml"));
        loader.setController(controller2);
        page = loader.load();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      popupBox.getChildren().setAll(page);
    }
    
    public void activatePopup()
    {
      popUpStage.show();
    }
    
    public void setGearState(String state)
    {
      controller.setRestrictedGear(state);
    }
    
    public void removeGearState(String state)
    {
      controller.removeRestrictedGear(state);
    }
    
    public void setMaxSpeed(double speed)
    {
      controller.setMaxSpeed(speed);
    }

}
