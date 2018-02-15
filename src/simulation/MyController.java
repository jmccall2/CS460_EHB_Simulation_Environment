package simulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField setSpeedField;
  @FXML private TextField currSpeedField;
  @FXML private TextField pressureField;
  @FXML private Button start_stop_sim;
  @FXML private Button handBrake;
  @FXML private RadioButton parkButton;
  @FXML private RadioButton neutralButton;
  @FXML private RadioButton driveButton;
  @FXML private RadioButton firstGear;
  @FXML private RadioButton secondGear;
  private boolean stopped = true;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    start_stop_sim.setOnAction((event) -> {
      if(stopped)
      {
        stopped = false;
        start_stop_sim.setText("Stop simulation.");
      }
      else if(!stopped)
      {
        stopped = true;
        start_stop_sim.setText("Start simulation.");
      } 
    });
  }
  
  
  public void setSpeed(double speed)
  {
    String speedStr = Double.toString(speed);
    currSpeedField.setText(speedStr);
  }
  
//  @FXML
//  private void handleButtonAction(ActionEvent event)
//  {
//    if(stopped)
//    {
//      stopped = false;
//      start_stop_sim.setText("Stop simulation.");
//    }
//    else if(!stopped)
//    {
//      stopped = true;
//      start_stop_sim.setText("Start simulation.");
//    }
//  }
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
