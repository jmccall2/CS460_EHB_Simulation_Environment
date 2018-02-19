package simulation;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class PopupController implements Initializable
{
  @FXML private TextField maxSpeedField;
  @FXML private ChoiceBox<String> gearchangeList;
  @FXML private ChoiceBox<String> gearChanges;
  ObservableList<String> newList;
  ArrayList<String> newList2;

  public PopupController()
  {
    // TODO Auto-generated constructor stub
  }

  @Override
  public void initialize(URL location, ResourceBundle resources)
  {
    addStates();
    gearChanges.getItems().addAll(newList2);
    gearChanges.getSelectionModel().selectedItemProperty().addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
      System.out.println(newValue) ;
      });
  }
  
  private void addStates()
  {
    ArrayList<String> gearStates = new ArrayList<>();
    gearStates.add("Drive -> Park");
    gearStates.add("Drive -> Reverse");
    gearStates.add("Drive -> Neutral");
    gearStates.add("Park -> Reverse");
    gearStates.add("Park -> Neutral");
    gearStates.add("Park -> Drive");
    gearStates.add("Reverse -> Park");
    gearStates.add("Reverse -> Neutral");
    gearStates.add("Reverse -> Drive");
    gearStates.add("Neutral -> Park");
    gearStates.add("Neutral -> Reverse");
    gearStates.add("Neutral -> Drive");
    newList2 = gearStates;
    //newList = FXCollections.observableArrayList(gearStates);
  }
  
  private void addNewStates(String state)
  {
    ArrayList<String> gearStates = new ArrayList<>();
    gearStates.add(state);
    gearchangeList.getItems().addAll(gearStates);
  }

}
