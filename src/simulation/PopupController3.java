package simulation;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class PopupController3 implements Initializable
{
  @FXML private Button okButton;
  private GUI guiRef;

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    okButton.setOnAction((event) -> {
      guiRef.closePopup2();
    });
  }
  
  public void setGUI(GUI gui)
  {
    this.guiRef = gui;
  }

}
