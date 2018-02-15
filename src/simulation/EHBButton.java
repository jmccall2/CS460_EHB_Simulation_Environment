package simulation;

import interfaces.ButtonColor;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;


public class EHBButton
{
    private Button _ehbButton;
    private final String _DEFAULT_COLOR = "#0032FF";
    private boolean _activated = false;
    private ButtonColor _activatedColor = null;
    private ButtonColor _unactivatedColor = null;
    private ColorMessageHelper _colorMessageHelper;
  //  private UnactiveColorMessageHelper _unactiveColorMessageHelper;


    EHBButton()
    {
        _colorMessageHelper = new ColorMessageHelper();
        Engine.getMessagePump().signalInterest(SimGlobals.SET_ACTIVATED_COLOR,_colorMessageHelper);
        Engine.getMessagePump().signalInterest(SimGlobals.SET_UNACTIVATED_COLOR, _colorMessageHelper);
    }

    public Color getColor()
    {
        _activated = !_activated;
        ButtonColor bc = _activated ? _activatedColor : _unactivatedColor;
        Color c = _mapToFXColor(bc);
        return c;
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
