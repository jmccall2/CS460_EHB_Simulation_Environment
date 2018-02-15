package simulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import simulation.engine.Message;
import simulation.engine.Singleton;
import simulation.engine.Engine;
//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField setSpeedField;
  @FXML private TextField currSpeedField;
  @FXML private TextField pressureField;
  @FXML private Button start_stop_sim;
  @FXML private Button handBrake;
  @FXML private Button enterSpeed;
  @FXML private RadioButton parkButton;
  @FXML private RadioButton neutralButton;
  @FXML private RadioButton driveButton;
  @FXML private RadioButton firstGear;
  @FXML private RadioButton secondGear;
  ToggleGroup group = new ToggleGroup();
  private boolean stopped = true;
  private boolean brakeOff = true;
  EHBButton ehb = null;
  GUI guiRef = null;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    currSpeedField.setText("");
    pressureField.setText("");
    parkButton.setToggleGroup(group);
    neutralButton.setToggleGroup(group);
    driveButton.setToggleGroup(group);
    firstGear.setToggleGroup(group);
    secondGear.setToggleGroup(group);
    start_stop_sim.setOnAction((event) -> {
      if(stopped)
      {
        stopped = false;
        start_stop_sim.setText("Stop simulation");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.START_SIM));
      }
      else if(!stopped)
      {
        stopped = true;
        start_stop_sim.setText("Start simulation");
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.STOP_SIM));
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
    });
    parkButton.setOnAction((event) ->{
      Engine.getMessagePump().sendMessage(new Message(SimGlobals.PARK));
    });
    neutralButton.setOnAction((event)->{
      Engine.getMessagePump().sendMessage(new Message(SimGlobals.NEUTRAL));
    });
    driveButton.setOnAction((event)->{
      Engine.getMessagePump().sendMessage(new Message(SimGlobals.DRIVE));
    });
    firstGear.setOnAction((event)->{
      Engine.getMessagePump().sendMessage(new Message(SimGlobals.FIRST));
    });
    secondGear.setOnAction((event)->{
      Engine.getMessagePump().sendMessage(new Message(SimGlobals.SECOND));
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
}
