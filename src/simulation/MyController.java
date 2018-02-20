package simulation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import interfaces.ButtonColorTypes;
import interfaces.GearTypes;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;

//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField setSpeedField;
  @FXML private Button start_stop_sim;
  @FXML private Button handBrake;
  @FXML private Button statsButton;
  @FXML private Button gearButton;
  @FXML private RadioButton parkButton;
  @FXML private RadioButton reverseButton;
  @FXML private RadioButton neutralButton;
  @FXML private RadioButton driveButton;
  private ButtonMessageHelper _buttonMessageHelper = new ButtonMessageHelper();


  private double MPH_TO_MS = 0.448;
  private boolean stopped = true;
  private ToggleGroup group = new ToggleGroup();
  private boolean brakeOff = true;
  private ButtonColorTypes _buttonColor = null;
  //True when car is in drive gear. This is the default gear.
  private boolean carDriving = true;
  private boolean inReverse = false;
  GUI guiRef;
  ArrayList<String> invalidTransitions = new ArrayList<>();
  String currGear = "D";
  double max_speed = 140;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  { 
    Engine.getConsoleVariables().loadConfigFile("src/resources/gearStates.cfg");
    setDefaultStates();
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
    Engine.getMessagePump().sendMessage(new Message(SimGlobals.GEAR_CHANGE, GearTypes.DRIVE));
    statsButton.setOnAction((event) ->{
      _InvokeOtherStage();
    });
    gearButton.setOnAction((event)->{
      guiRef.activatePopup();
    });
    start_stop_sim.setOnAction((event) -> {
      if(stopped)
      {
        gearButton.setDisable(true);
        setGearTransitions();
        stopped = false;
        start_stop_sim.setText("Stop simulation");
        _setInitSpeed();
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.START_SIM));
        // Stop simulating movement
        Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).setValue("true");
        setSpeedField.setDisable(true);
      }
      else if(!stopped)
      {
        stopped = true;
        start_stop_sim.setText("Start simulation");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.STOP_SIM));
        // Start simulating movement
        Engine.getConsoleVariables().find(Singleton.CALCULATE_MOVEMENT).setValue("false");
        setSpeedField.setDisable(false);
        parkButton.setDisable(false);
        reverseButton.setDisable(false);
        neutralButton.setDisable(false);
        driveButton.setDisable(false);
        gearButton.setDisable(false);
      } 
    });
    handBrake.setOnAction((event) -> {
      if(!stopped)
      {
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
      }
    });
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> ov,
                          Toggle old_toggle, Toggle new_toggle) {
        if (group.getSelectedToggle() != null)
        {
          String gearString = group.getSelectedToggle().getUserData().toString();
          currGear = gearString;
          Engine.getMessagePump().sendMessage(new Message(SimGlobals.GEAR_CHANGE, _getGear(gearString)));
          setGearTransitions();
          if(gearString == "D")
          {
            carDriving = true;
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
  }
  
  private void setDefaultStates()
  {
    if(Engine.getConsoleVariables().contains("default1"))
    {
      setRestrictedGear(Engine.getConsoleVariables().find("default1").getcvarValue());
    }
    if(Engine.getConsoleVariables().contains("default2"))
    {
      setRestrictedGear(Engine.getConsoleVariables().find("default2").getcvarValue());
    }
    if(Engine.getConsoleVariables().contains("default3"))
    {
      setRestrictedGear(Engine.getConsoleVariables().find("default3").getcvarValue());
    }
    if(Engine.getConsoleVariables().contains("default4"))
    {
      setRestrictedGear(Engine.getConsoleVariables().find("default4").getcvarValue());
    }
  }
  
  private void setGearTransitions()
  {
    for(int i = 0; i < invalidTransitions.size(); i ++)
    {
      String transition = invalidTransitions.get(i);
      if(currGear.charAt(0) == transition.charAt(0))
      {
        char invalidGear = transition.charAt(1);
        if(invalidGear == 'D')
        {
          driveButton.setDisable(true);
        }
        else if(invalidGear == 'R')
        {
          reverseButton.setDisable(true);
        }
        else if(invalidGear == 'P')
        {
          parkButton.setDisable(true);
        }
        else if(invalidGear == 'N')
        {
          neutralButton.setDisable(true);
        }
      }
    }
  }
  
  public void setRestrictedGear(String gearString)
  {
    switch(gearString)
    {
      case "Drive->Park":
        invalidTransitions.add("DP");
        break;
      case "Drive->Reverse":
        invalidTransitions.add("DR");
        break;
      case "Drive->Neutral":
        invalidTransitions.add("DN");
        break;
      case "Park->Reverse":
        invalidTransitions.add("PR");
        break;
      case "Park->Neutral":
        invalidTransitions.add("PN");
        break;
      case "Park->Drive":
        invalidTransitions.add("PD");
        break;
      case "Reverse->Park":
        invalidTransitions.add("RP");
        break;
      case "Reverse->Neutral":
        invalidTransitions.add("RN");
        break;
      case "Reverse->Drive":
        invalidTransitions.add("RD");
        break;
      case "Neutral->Park":
        invalidTransitions.add("NP");
        break;
      case "Neutral->Reverse":
        invalidTransitions.add("NR");
        break;
      case "Neutral->Drive":
        invalidTransitions.add("ND");
        break;
      default:
        System.err.println("Invalid gear transition.");
    }
  }
  
  public void removeRestrictedGear(String gearString)
  {
    String stringToRemove = "";
    switch(gearString)
    {
      case "Drive->Park":
        stringToRemove = "DP";
        break;
      case "Drive->Reverse":
        stringToRemove = "DR";
        break;
      case "Drive->Neutral":
        stringToRemove = "DN";
        break;
      case "Park->Reverse":
        stringToRemove = "PR";
        break;
      case "Park->Neutral":
        stringToRemove = "PN";
        break;
      case "Park->Drive":
        stringToRemove = "PD";
        break;
      case "Reverse->Park":
        stringToRemove = "RP";
        break;
      case "Reverse->Neutral":
        stringToRemove = "RN";
        break;
      case "Reverse->Drive":
        stringToRemove = "RD";
        break;
      case "Neutral->Park":
        stringToRemove = "NP";
        break;
      case "Neutral->Reverse":
        stringToRemove = "NR";
        break;
      case "Neutral->Drive":
        stringToRemove = "ND";
        break;
      default:
        System.err.println("Invalid gear transition.");
    }
    invalidTransitions.remove(stringToRemove);
  }
  
  public void setGui(GUI gui)
  {
    this.guiRef = gui;
  }
  
  private void _setInitSpeed()
  {
    if(setSpeedField.getText() != null && !setSpeedField.getText().isEmpty())
    {
      double speed = -1;
      try
      {

        speed = Double.parseDouble(setSpeedField.getText());
        if (speed < 0)
        {
          speed *= -1;
          String speedStr = String.format("%.1f", speed);
          setSpeedField.setText(speedStr);
        }
      }
      catch(NumberFormatException ex)
      {
        System.out.println(ex);
      }
      if(speed >= 0 && speed <= max_speed)
      {
        if(inReverse)speed *=-1;
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SPEED, speed*MPH_TO_MS));
        System.out.println("SENDING SPEED " + speed);
      }
    }
  }
  
  public void setInitButtonColor()
  {
    handBrake.setStyle(_buildCSSString());
  }

  private GearTypes _getGear(String s)
  {
    switch(s)
    {
      case "P": return GearTypes.PARK;
      case "R": return GearTypes.REVERSE;
      case "N": return GearTypes.NEUTRAL;
      case "D": return GearTypes.DRIVE;
      default:
        System.err.println("UNSUPPORTED GEAR, returning drive.");
        return GearTypes.DRIVE;
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
          _buttonColor = (ButtonColorTypes) message.getMessageData();
          handBrake.setStyle(_buildCSSString());
          break;

      }
    }
  }


}
