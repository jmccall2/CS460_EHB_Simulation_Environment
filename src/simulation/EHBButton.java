package simulation;

import interfaces.ButtonColor;
import interfaces.ButtonSound;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

import java.net.URL;


public class EHBButton
{
    private Button _ehbButton;
    private boolean _activated = false;
    private String _engagedSound = "";
    private String _disengagedSound = "";
    private ButtonColor _buttonColor = null;




    private String _buildCSSString(String hexColor)
    {
        return "-fx-background-color: rgba(0,0,0,0.75), rgba(255,255,255,0.75), "
                +    "linear-gradient(to bottom," + hexColor + " 0%,#cccccc 100%);"
                + "    -fx-background-radius: 5em; " + "-fx-min-width: 70px; "
                + "-fx-min-height: 70px; " + "-fx-max-width: 70px; "
                + "-fx-max-height: 70px;";
    }


//    public Button getEHBButton()
//    {
//        return _ehbButton;
//    }

}
