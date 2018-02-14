package simulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField setSpeedField;
  @FXML private TextField currSpeedField;
  @FXML private TextField pressureField;
  @FXML private ToggleButton start_stop_sim;
  @FXML private ToggleButton handBrake;
  @FXML private RadioButton parkButton;
  @FXML private RadioButton neutralButton;
  @FXML private RadioButton driveButton;
  @FXML private RadioButton firstGear;
  @FXML private RadioButton secondGear;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
 
  }
  
  public void setSpeed(double speed)
  {
    String speedStr = Double.toString(speed);
    currSpeedField.setText(speedStr);
  }
//
//  public void setAcceleration(double acceleration)
//  {
//    String accelStr = Double.toString(acceleration);
//    accelField.setText(accelStr);
//  }
//  
//  public void setGear(String gear)
//  {
//    gearField.setText(gear);
//  }
//  
//  public void setTraction(double traction)
//  {
//    String tractionStr = Double.toString(traction);
//    tractionField.setText(tractionStr);
//  }
//  
//  public void setPressure(double pressure)
//  {
//    String pressureStr = Double.toString(pressure);
//    pressureField.setText(pressureStr);
//  }
}
