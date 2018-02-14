package simulation;

import interfaces.ButtonColor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;


public class EHBButton
{
    private Button _ehbButton;
    private final String _DEFAULT_COLOR = "#0032FF";
    private boolean _activated = false;
    private ButtonColor _activatedColor;
    private ButtonColor _unactivatedColor;
    private ColorMessageHelper _colorMessageHelper;
  //  private UnactiveColorMessageHelper _unactiveColorMessageHelper;


    EHBButton()
    {
         Singleton.engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR));
         Singleton.engine.getMessagePump().registerMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR));
        _colorMessageHelper = new ColorMessageHelper();
        Singleton.engine.getMessagePump().signalInterest(SimGlobals.SET_ACTIVATED_COLOR,_colorMessageHelper);
        Singleton.engine.getMessagePump().signalInterest(SimGlobals.SET_UNACTIVATED_COLOR, _colorMessageHelper);
        _ehbButton = new Button();
        _ehbButton.getStyleClass().add("ehbButton");
        _ehbButton.setStyle(_buildCSSString(_DEFAULT_COLOR));


        /* There needs to be some way to add additional methods to invoke in the onclick listener,
           in addition to the default. Possibly a list of Functions/Callables that are iterated and invoked
           in the listener?
         */

        EventHandler<ActionEvent> _buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleVisualState();
                // event.consume(); Needed?
            }
        };
        _ehbButton.setOnAction(_buttonHandler);
    }

    public void toggleVisualState()
    {
        _activated = !_activated;
        ButtonColor bc = _activated ? _activatedColor : _unactivatedColor;
        Color c = _mapToFXColor(bc);
        _ehbButton.setStyle(_buildCSSString(bc.convertToHex()));
        if(_activated) _ehbButton.setEffect(new DropShadow(50, c));
        else _ehbButton.setEffect(null);
    }

    public void setUnactivatedColor(ButtonColor bc)
    {
        _unactivatedColor = bc;
    }

    public void setActivatedColor(ButtonColor bc)
    {
        _activatedColor = bc;
    }

    private Color _mapToFXColor(ButtonColor bc)
    {
        switch (bc) {
            case CYAN:
                return Color.CYAN;
            case BLUE:
                return Color.BLUE;
            case RED:
                return Color.RED;
            case GREEN:
                return Color.GREEN;
            case PURPLE:
                return Color.PURPLE;
            default:
                System.err.println("WARNING: Invalid color provided. Used default: Grey.");
                return Color.web(_DEFAULT_COLOR);
        }
    }

    private String _buildCSSString(String hexColor)
    {
        return "-fx-background-color: rgba(0,0,0,0.75), rgba(255,255,255,0.75), "
                +    "linear-gradient(to bottom," + hexColor + " 0%,#cccccc 100%);"
                + "    -fx-background-radius: 5em; " + "-fx-min-width: 70px; "
                + "-fx-min-height: 70px; " + "-fx-max-width: 70px; "
                + "-fx-max-height: 70px;";
    }

    class ColorMessageHelper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            switch(message.getMessageName())
            {
                case SimGlobals.SET_ACTIVATED_COLOR:
                    _activatedColor = (ButtonColor) message.getMessageData();
                    break;
                case SimGlobals.SET_UNACTIVATED_COLOR:
                    _unactivatedColor = (ButtonColor) message.getMessageData();
                    break;
            }
        }
    }

    public Button getEHBButton()
    {
        return _ehbButton;
    }

}
