package simulation;

import java.net.URL;
import java.util.ResourceBundle;

import interfaces.ButtonSound;
import interfaces.EHBButtonInterface;
import interfaces.Gear;
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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.Singleton;

//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField setSpeedField;
  @FXML private Button start_stop_sim;
  @FXML private Button handBrake;
  @FXML private Button enterSpeed;
  @FXML private Button statsButton;
  @FXML private RadioButton parkButton;
  @FXML private RadioButton neutralButton;
  @FXML private RadioButton driveButton;
  @FXML private RadioButton firstGear;
  @FXML private RadioButton secondGear;

  private boolean stopped = true;
  ToggleGroup group = new ToggleGroup();
  private boolean brakeOff = true;
  EHBButton ehb = null;
  GUI guiRef = null;
  //True when car is in drive gear. This is the default gear.
  private boolean carDriving = true;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    parkButton.setToggleGroup(group);
    parkButton.setUserData("P");
    neutralButton.setToggleGroup(group);
    neutralButton.setUserData("N");
    driveButton.setToggleGroup(group);
    driveButton.setUserData("D");
    firstGear.setToggleGroup(group);
    firstGear.setUserData("1");
    secondGear.setToggleGroup(group);
    secondGear.setUserData("2");
    // We need to start in some gear.
    driveButton.setSelected(true);
    Engine.getMessagePump().sendMessage(new Message(SimGlobals.GEAR_CHANGE, Gear.DRIVE));
    statsButton.setOnAction((event) ->{
      invokeOtherStage();
    });
    start_stop_sim.setOnAction((event) -> {
      if(stopped)
      {
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
      } 
    });
    ehb = new EHBButton();
    String color = guiRef.getUnActiveColor().toString();
    String colorStr = "-fx-background-color:" + color+";";
    String radStr = "-fx-background-radius:100;";
    String backgroundStr = "-fx-border-width:0;";
    String backgroundStr2 = "-fx-border-style:none;";
    String cssStr = colorStr + radStr + backgroundStr + backgroundStr2;
    handBrake.setStyle(cssStr);
    handBrake.setOnAction((event) -> {
      String color1 = getColor(ehb.getColor());
      String colorStr1 = "-fx-background-color:" + color1+";";
      String radStr1 = "-fx-background-radius:100;";
      String backgroundStr1 = "-fx-border-width:0;";
      String backgroundStr22 = "-fx-border-style:none;";
      String cssStr1 = colorStr1 + radStr1 + backgroundStr1 + backgroundStr22;
      handBrake.setStyle(cssStr1);
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
      URL url = getClass().getResource(ehb.getSoundFile());
      AudioClip sound = new AudioClip(url.toExternalForm());
      sound.play(1, 0, 1, 0, 1);
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
          }
          else
          {
            carDriving = false;
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
          Engine.getMessagePump().sendMessage(new Message(SimGlobals.SPEED, speed));
        }
      }
    });
  }
  
  public EHBButton getEHB()
  {
    return ehb;
  }
  
  public void setGUI(GUI mainGUI)
  {
    guiRef = mainGUI;
  }
  
  public void setInitButtonColor()
  {
    Color col = ehb.getInitUnactiveColor();
    String color1 = getColor(col);
    String colorStr1 = "-fx-background-color:" + color1+";";
    String radStr1 = "-fx-background-radius:100;";
    String backgroundStr1 = "-fx-border-width:0;";
    String backgroundStr22 = "-fx-border-style:none;";
    String cssStr1 = colorStr1 + radStr1 + backgroundStr1 + backgroundStr22;
    handBrake.setStyle(cssStr1);
  }

  private Gear _getGear(String s)
  {
    switch(s)
    {
      case "P": return Gear.PARK;
      case "N": return Gear.NEUTRAL;
      case "D": return Gear.DRIVE;
      case "1": return Gear.FIRST;
      case "2": return Gear.SECOND;
      default:
        System.err.println("UNSUPPORTED GEAR, returning drive.");
        return Gear.DRIVE;
    }
  }

  private String getColor(Color col)
  {
    if(col.equals(Color.CYAN))
    {
      return "cyan";
    }
    else if(col.equals(Color.RED))
    {
      return "red";
    }
    else if(col.equals(Color.GREEN))
    {
      return "green";
    }
    else if(col.equals(Color.PURPLE))
    {
      return "purple";
    }
    else if(col.equals(Color.BLUE))
    {
      return "blue";
    }
    return null;
  }

  public void invokeOtherStage()
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
      scene.setFill(Color.TRANSPARENT);
      newStage.setScene(scene);
      newStage.show();
    } catch (Exception e)
    {
      e.printStackTrace();
    }

  }


}
