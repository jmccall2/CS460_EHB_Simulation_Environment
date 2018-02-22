package simulation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class for the error popup.
 */
public class ErrorPopupController implements Initializable
{
  @FXML private Button _okButton;
  @FXML private Label _errorMessage;
  private GUI _guiRef;


  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    _okButton.setOnAction((event)->{_guiRef.closePopup();});
    
  }
  void setErrorMessage(String error) {_errorMessage.setText(error);}

  void setGUI(GUI gui)
  {
    this._guiRef = gui;
  }
}
