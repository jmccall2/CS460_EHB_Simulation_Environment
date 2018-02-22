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
    private GuiController _controller = null;
    private UITextField _currSpeedField;
    private UITextField _pressureField;
    private HBox _popupBox;
    private Stage _popUpStage;
    private ErrorPopupController _errorPopupController;

    /**
     * This represents the GUI for the simulation.
     */
    GUI()
    {
      _gPane = new GridPane();
      _popUpStage = new Stage();
      _popupBox = new HBox();
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
      Engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
      _controller = new GuiController();
      _controller.setGui(this);
      _errorPopupController = new ErrorPopupController();
      _errorPopupController.setGUI(this);
      //Load FXML files.
      _addGUIFXMLCode();
      _addErrorPopupFXMLCode();
      _popUpStage.setScene(new Scene(_popupBox));
      _popUpStage.setOnCloseRequest(event ->
      {
        _popUpStage.close();
      });
      _popUpStage.setTitle("Error");
      Pane newPane = new Pane();
      newPane.getChildren().add(_gPane);
      newPane.setLayoutX(110);
      newPane.setLayoutY(460);
      _currSpeedField = new UITextField("", 10, 625);
      _currSpeedField.setWidthHeight(100, 10);
      _currSpeedField.addToWindow();
      _currSpeedField.setEditable(false);
        //Create text field to display current speed.
      UILabel speedLabel = new UILabel("Current Speed", 11, 655);
      speedLabel.addToWindow();
      speedLabel.setColor(Color.WHITE);
      _pressureField = new UITextField("", 890, 625);
      _pressureField.setWidthHeight(100, 10);
      _pressureField.addToWindow();
      _pressureField.setEditable(false);
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
      _currSpeedField.setText(speedStr);
    }

    /**
     * Update pressure text field with current pressure.
     * @param pressure
     */
    public void setPressure(double pressure)
    {
      String pressureStr = String.format("%.1f", pressure);
      _pressureField.setText(pressureStr);
    }

    /**
     * Set initial color of the hand brake.
     */
    void setInitColor()
    {
        _controller.setInitButtonColor();
    }

    /**
     * Close error message pop up box.
     */
    void closePopup()
    {
        _controller.errorPopupClosed();
      _popUpStage.close();
    }

    /**
     * Display error message.
     * @param error
     */
    void showPopup(String error)
    {
        _errorPopupController.setErrorMessage(error);
      _popUpStage.show();
    }

    // Add GUI FXML code.
    private void _addGUIFXMLCode()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/resources/fxml/controlPanel.fxml"));
        loader.setController(_controller);
        page = loader.load();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      _gPane.getChildren().setAll(page);
    }

    // Add Error popup FXML code.
    private void _addErrorPopupFXMLCode()
    {
      Parent page = null;
      try
      {
        FXMLLoader loader = new FXMLLoader(GUI.class.getResource("/resources/fxml/errorPopup.fxml"));
        loader.setController(_errorPopupController);
        page = loader.load();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
      _popupBox.getChildren().setAll(page);
    }
}
