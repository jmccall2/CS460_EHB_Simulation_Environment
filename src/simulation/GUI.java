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

/**
 * This represents the GUI for the simulation.
 *
 */
public class GUI
{
    private GridPane _gPane;
    private MyController controller = null;
    private UITextField currSpeedField;
    private UITextField pressureField;
    HBox popupBox;
    Stage popUpStage;
    PopupController2 controller2;
    HBox popupBox2;
    Stage popUpStage2;
    PopupController3 controller3;
    
    /**
     * Constructor.
     */
    public GUI()
    {
      _gPane = new GridPane();
      popUpStage = new Stage();
      popupBox = new HBox();
      popupBox2 = new HBox();
      popUpStage2 = new Stage();
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      controller = new MyController();
      controller.setGui(this);
      controller2 = new PopupController2();
      controller2.setGUI(this);
      controller3 = new PopupController3();
      controller3.setGUI(this);
      //Load FXML files.
      _addFXMLCode1();
      _addFXMLCode2();
      _addFXMLCode3();
      popUpStage.setScene(new Scene(popupBox));
      popUpStage.setOnCloseRequest(event ->
      {
        popUpStage.close();
      });
      popUpStage.setTitle("Error");
      popUpStage2.setScene(new Scene(popupBox2));
      popUpStage2.setOnCloseRequest(event ->
      {
        popUpStage2.close();
      });
      popUpStage2.setTitle("Error");
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(110);
      newPane.setLayoutY(460);
      currSpeedField = new UITextField("", 10, 625);
      currSpeedField.setWidthHeight(100, 10);
      currSpeedField.addToWindow();
      currSpeedField.setEditable(false);
      //Create text field to display current speed.
      UILabel speedLabel = new UILabel("Current Speed", 11, 655);
      speedLabel.addToWindow();
      speedLabel.setColor(Color.WHITE);
      pressureField = new UITextField("", 890, 625);
      pressureField.setWidthHeight(100, 10);
      pressureField.addToWindow();
      pressureField.setEditable(false);
      //Create text field to display current pressure.
      UILabel pressureLabel = new UILabel("Current Pressure", 889, 655);
      pressureLabel.addToWindow();
      pressureLabel.setColor(Color.WHITE);
      //Add pane with GUI to simulation.
      Engine.getMessagePump().sendMessage(new Message(Singleton.ADD_UI_ELEMENT, newPane));
    }
    
    /**
     * Update speed text field with current speed.
     * @param speed
     */
    public void setSpeed(double speed)
    {
      String speedStr = String.format("%.1f", Math.abs(speed));
      currSpeedField.setText(speedStr);
    }
    
    /**
     * Update pressure text field with current pressure.
     * @param pressure
     */
    public void setPressure(double pressure)
    {
      String pressureStr = String.format("%.1f", pressure);
      pressureField.setText(pressureStr);
    }

    /**
     * Set initial color of the handbrake button.
     */
    public void setInitColor()
    {
      controller.setInitButtonColor();
    }
    
    /**
     * Close error message pop up box.
     */
    public void closePopup()
    {
      popUpStage.close();
    }
    
    /**
     * Display error message pop up box.
     */
    public void showPopup()
    {
      popUpStage.show();
    }
    
    /**
     * Close error message pop up box.
     */
    public void closePopup2()
    {
      popUpStage2.close();
    }
    
    /**
     * Display error message pop up box.
     */
    public void showPopup2()
    {
      popUpStage2.show();
    }
    
    //Add FXML code.
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
    
    //Add FXML code.
    private void _addFXMLCode2()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("errorPopup.fxml"));
        loader.setController(controller2);
        page = loader.load();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      popupBox.getChildren().setAll(page);
    }
    
    //Add FXML code.
    private void _addFXMLCode3()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("errorPopup2.fxml"));
        loader.setController(controller3);
        page = loader.load();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      popupBox2.getChildren().setAll(page);
    }
    
    //Not currently used.
    public void setGearState(String state)
    {
      controller.setRestrictedGear(state);
    }
    
    //Not currently used.
    public void removeGearState(String state)
    {
      controller.removeRestrictedGear(state);
    }
}
