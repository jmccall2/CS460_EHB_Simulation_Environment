package simulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

/**
 * Controller for one of the pop up error messages.
 *
 */
public class PopupController2 implements Initializable
{
  @FXML private Button okButton;
  private GUI guiRef;
  
  /**
   * Initialize the button.
   */
  @Override
  public void initialize(URL arg0, ResourceBundle arg1)
  {
    okButton.setOnAction((event)->{
     guiRef.closePopup(); 
    });
    
  }
  
  /**
   * Add reference to the GUI.
   * @param gui
   */
  public void setGUI(GUI gui)
  {
    this.guiRef = gui;
  }
}
