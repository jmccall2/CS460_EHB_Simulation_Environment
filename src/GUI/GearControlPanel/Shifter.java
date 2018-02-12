package GUI.GearControlPanel;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import PublicInterfaces.Gear;

public class Shifter
{
    private VBox _shifter;
    private HBox _container;
    private ToggleGroup _gears;
    private Gear _currentGear;

    Shifter()
    {
        _gears = new ToggleGroup();

        RadioButton park = new RadioButton("P");
        park.setToggleGroup(_gears);
        park.setUserData("P");

        RadioButton reverse = new RadioButton("R");
        reverse.setToggleGroup(_gears);
        reverse.setUserData("R");

        RadioButton neutral = new RadioButton("N");
        neutral.setToggleGroup(_gears);
        neutral.setUserData("N");

        RadioButton drive = new RadioButton("D");
        drive.setToggleGroup(_gears);
        drive.setUserData("D");

        RadioButton firstGear = new RadioButton("1");
        firstGear.setToggleGroup(_gears);
        firstGear.setUserData("1");

        RadioButton secondGear = new RadioButton("2");
        secondGear.setToggleGroup(_gears);
        secondGear.setUserData("2");

        _gears.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (_gears.getSelectedToggle() != null) {
                    _updateCurrentGear(_gears.getSelectedToggle().getUserData().toString());
                    // Do something based on gear selected.
                }
            }
        });

        _shifter = new VBox();

        _shifter.getChildren().add(park);
        _shifter.getChildren().add(reverse);
        _shifter.getChildren().add(drive);
        _shifter.getChildren().add(neutral);
        _shifter.getChildren().add(firstGear);
        _shifter.getChildren().add(secondGear);
        _shifter.setSpacing(10);
        _shifter.setPadding(new Insets(8,2,2,5));
        _shifter.setStyle( "-fx-background-color:#D4D0D7;"
                + "    -fx-background-radius: 5em; " + "-fx-min-width: 40px; "
                + "-fx-min-height: 175px; " + "-fx-max-width: 40px; "
                + "-fx-max-height: 175px;");

        _container = new HBox();

        _container.getChildren().add(_shifter);
        _container.setSpacing(50);
        _container.setStyle("-fx-background-color:#000000;");
        _container.setPadding(new Insets(0,75,0,100));
    }

    public HBox getHBox()
    {
        return _container;
    }

    private void _updateCurrentGear(String g)
    {
        switch(g)
        {
            case "P":
                _currentGear = Gear.PARK;
                break;
            case "R":
                _currentGear = Gear.REVERSE;
                break;
            case "D":
                _currentGear = Gear.DRIVE;
                break;
            case "N":
                _currentGear = Gear.NEUTRAL;
                break;
            case "1":
                _currentGear = Gear.FIRST;
                break;
            case "2":
                _currentGear = Gear.SECOND;
                break;
            default:
                 _currentGear = Gear.PARK;
                 break;
        }
        System.out.println("Current Gear: " + _currentGear);
    }



}
