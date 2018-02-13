package simulation;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class DataPanel implements GUIPanel
{
    private BorderPane _dataPanel;
    double speed = 0;
    double acceleration = 0;
    String gear = "";
    double traction = 0;
    double pressure = 0;
    MyController controller = null;
    
    public DataPanel()
    {
        _dataPanel = new BorderPane();
        BackgroundFill myBF = new BackgroundFill(Color.PEACHPUFF, new CornerRadii(1),
                new Insets(0.0,0.0,0.0,0.0));
        _dataPanel.setBackground(new Background(myBF));
        _addFXMLCode();
    }

    public BorderPane getPanel()
    {
        return _dataPanel;
    }
    
    //Add FXML pane with text boxes.
    private void _addFXMLCode()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(DataPanel.class.getResource("displayPanel.fxml"));
        page = loader.load();
        controller = (MyController)loader.getController();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      _dataPanel.getChildren().setAll(page);
    }
    
    public void setSpeed(double speed)
    {
      this.speed = speed;
      controller.setSpeed(speed);
    }
    
    public double getSpeed()
    {
      return speed;
    }

    public void setAcceleration(double acceleration)
    {
      this.acceleration = acceleration;
      controller.setAcceleration(acceleration);
    }
    
    public double getAcceleration()
    {
      return acceleration;
    }
    
    public void setGear(String gear)
    {
      this.gear = gear;
      controller.setGear(gear);
    }
    
    public String getGear()
    {
      return gear;
    }
    
    public void setTraction(double traction)
    {
      this.traction = traction;
      controller.setTraction(traction);
    }
    
    public double getTraction()
    {
      return traction;
    }
    
    public void setPressure(double pressure)
    {
      this.pressure = pressure;
      controller.setPressure(pressure);
    }
    
    public double getPressure()
    {
      return pressure;
    }
}
