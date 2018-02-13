package GUI.DataDisplayPanel;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
//Controller for FXML pane.
public class MyController implements Initializable
{
  @FXML private TextField speedField;
  @FXML private TextField accelField;
  @FXML private TextField gearField;
  @FXML private TextField tractionField;
  @FXML private TextField pressureField;
  
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    speedField.setText("");
    accelField.setText("");
    gearField.setText("");
    tractionField.setText("");
    pressureField.setText("");
  }
  
  public void setSpeed(double speed)
  {
    String speedStr = Double.toString(speed);
    speedField.setText(speedStr);
  }

  public void setAcceleration(double acceleration)
  {
    String accelStr = Double.toString(acceleration);
    accelField.setText(accelStr);
  }
  
  public void setGear(String gear)
  {
    gearField.setText(gear);
  }
  
  public void setTraction(double traction)
  {
    String tractionStr = Double.toString(traction);
    tractionField.setText(tractionStr);
  }
  
  public void setPressure(double pressure)
  {
    String pressureStr = Double.toString(pressure);
    pressureField.setText(pressureStr);
  }
}
