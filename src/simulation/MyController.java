package simulation;

import interfaces.ButtonColor;
import interfaces.Gear;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;

import java.net.URL;
import java.util.ResourceBundle;

//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField setSpeedField;
  @FXML private Button start_stop_sim;
  @FXML private Button handBrake;
  @FXML private Button enterSpeed;
  @FXML private Button statsButton;
  @FXML private RadioButton parkButton;
  @FXML private RadioButton reverseButton;
  @FXML private RadioButton neutralButton;
  @FXML private RadioButton driveButton;

  private ButtonMessageHelper _buttonMessageHelper = new ButtonMessageHelper();

  private boolean stopped = true;
  private ToggleGroup group = new ToggleGroup();
  private boolean brakeOff = true;
  private ButtonColor _buttonColor = null;
  //True when car is in drive gear. This is the default gear.
  private boolean carDriving = true;
  private boolean inReverse = false;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    Engine.getMessagePump().signalInterest(SimGlobals.SET_BUTTON_COLOR, _buttonMessageHelper);
    parkButton.setToggleGroup(group);
    parkButton.setUserData("P");
    neutralButton.setToggleGroup(group);
    neutralButton.setUserData("N");
    driveButton.setToggleGroup(group);
    driveButton.setUserData("D");
    reverseButton.setToggleGroup(group);
    reverseButton.setUserData("R");
    // We need to start in some gear.
    driveButton.setSelected(true);
    Engine.getMessagePump().sendMessage(new Message(SimGlobals.GEAR_CHANGE, Gear.DRIVE));
    statsButton.setOnAction((event) ->{
      _InvokeOtherStage();
    });
    start_stop_sim.setOnAction((event) -> {
      if(stopped)
      {
        if(carDriving)reverseButton.setDisable(true);
        stopped = false;
        start_stop_sim.setText("Stop simulation");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.START_SIM));
        // Stop simulating movement
        Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).setValue("true");
        parkButton.setDisable(true);
        enterSpeed.setDisable(true);
      }
      else if(!stopped)
      {
        stopped = true;
        start_stop_sim.setText("Start simulation");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.STOP_SIM));
        // Start simulating movement
        Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).setValue("false");
        parkButton.setDisable(false);
        enterSpeed.setDisable(false);
        reverseButton.setDisable(false);
      } 
    });
    handBrake.setOnAction((event) -> {
      if(brakeOff)
      {
        brakeOff = false;
        handBrake.setText("Deactivate");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.ACTIVATE_BRAKE));
      }
      else if(!brakeOff)
      {
        brakeOff = true;
        handBrake.setText("Activate Brake");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.DEACTIVATE_BRAKE));
      }
    });
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov,
                          Toggle old_toggle, Toggle new_toggle) {
        if (group.getSelectedToggle() != null)
        {
          String gearString = group.getSelectedToggle().getUserData().toString();
          Engine.getMessagePump().sendMessage(new Message(SimGlobals.GEAR_CHANGE, _getGear(gearString)));
          if(gearString == "D")
          {
            carDriving = true;
            if(!stopped)reverseButton.setDisable(true);
          }
          else
          {
            carDriving = false;
          }
          if(gearString == "R")
          {
            inReverse = true;
          }
          else if(!(gearString == "R"))
          {
            inReverse = false;
          }
        }
      }
    });

    enterSpeed.setOnAction((event)->{
      if(setSpeedField.getText() != null && !setSpeedField.getText().isEmpty())
      {
        double speed = -1;
        try
        {
        
          speed = Double.parseDouble(setSpeedField.getText());
        }
        catch(NumberFormatException ex)
        {
          
        }
        if(speed >= 0 && speed <=140)
        {
          if(inReverse)speed *=-1;
          Engine.getMessagePump().sendMessage(new Message(SimGlobals.SPEED, speed));
        }
      }
    });
  }

  
  public void setInitButtonColor()
  {
    handBrake.setStyle(_buildCSSString());
  }

  private Gear _getGear(String s)
  {
    switch(s)
    {
      case "P": return Gear.PARK;
      case "R": return Gear.REVERSE;
      case "N": return Gear.NEUTRAL;
      case "D": return Gear.DRIVE;
      default:
        System.err.println("UNSUPPORTED GEAR, returning drive.");
        return Gear.DRIVE;
    }
  }

  private void _InvokeOtherStage()
  {
    try
    {
      Stage newStage = new Stage();
      // Make the stage transparent.
      newStage.initStyle(StageStyle.TRANSPARENT);

      FXMLLoader fxmlLoader = new FXMLLoader(
              getClass().getResource("/simulation/simData.fxml"));
      Parent root = fxmlLoader.load();
      SimulationStats statController = (SimulationStats) fxmlLoader.getController();
      Scene scene = new Scene(root);
      // Again needed for making the window
      // transparent.
      statController.init();
      scene.setFill(Color.TRANSPARENT);
      newStage.setScene(scene);
      newStage.show();
    } catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  private String _buildCSSString()
  {
    return "-fx-background-color:" + ((_buttonColor == null) ? "grey" : _buttonColor.toString())+";"
            + "-fx-background-radius:100;"
            + "-fx-border-width:0;"
            + "-fx-border-style:none;";
  }

  class ButtonMessageHelper implements MessageHandler
  {
    @Override
    public void handleMessage(Message message)
    {
      switch(message.getMessageName())
      {
        case SimGlobals.SET_BUTTON_COLOR:
          _buttonColor = (ButtonColor) message.getMessageData();
          handBrake.setStyle(_buildCSSString());
          break;

      }
    }
  }


}
