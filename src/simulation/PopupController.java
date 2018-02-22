package simulation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

//Not currently used.
public class PopupController implements Initializable
{
  @FXML private TextField maxSpeedField;
  @FXML private ChoiceBox<String> gearchangeList;
  @FXML private ChoiceBox<String> gearChanges;
  @FXML private Button deleteButton;
  ObservableList<String> newList;
  ArrayList<String> newList2;
  GUI guiRef;
  String itemToDelete = "";
  
  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    deleteButton.setDisable(true);
    addStates();
    gearChanges.getItems().addAll(newList2);
    gearChanges.getSelectionModel().selectedItemProperty().addListener( 
        (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          addNewStates(newValue);
          guiRef.setGearState(newValue);
    });
    gearchangeList.getSelectionModel().selectedItemProperty().addListener( 
        (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
          itemToDelete = newValue;
          deleteButton.setDisable(false);
    });
    deleteButton.setOnAction((event)-> {
      gearchangeList.getItems().remove(itemToDelete);
      guiRef.removeGearState(itemToDelete);
      itemToDelete = "";
      deleteButton.setDisable(true);
    });
  }
  
  private void addStates()
  {
    ArrayList<String> gearStates = new ArrayList<>();
    gearStates.add("Drive->Park");
    gearStates.add("Drive->Reverse");
    gearStates.add("Drive->Neutral");
    gearStates.add("Park->Reverse");
    gearStates.add("Park->Neutral");
    gearStates.add("Park->Drive");
    gearStates.add("Reverse->Park");
    gearStates.add("Reverse->Neutral");
    gearStates.add("Reverse->Drive");
    gearStates.add("Neutral->Park");
    gearStates.add("Neutral->Reverse");
    gearStates.add("Neutral->Drive");
    newList2 = gearStates;
  }
  
  private void addNewStates(String state)
  {
    if(!gearchangeList.getItems().contains(state))
    {
      gearchangeList.getItems().add(state);
    }
  }
  
  public void setGui(GUI gui)
  {
    this.guiRef = gui;
  }
  
}
