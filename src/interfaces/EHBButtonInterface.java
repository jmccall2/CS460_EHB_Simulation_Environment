package interfaces;

import java.net.URL;

import ehb.EHB;
import javafx.scene.media.AudioClip;
import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class EHBButtonInterface
{
    Helper helper = new Helper();
    private static boolean _wasPressed;

    public EHBButtonInterface()
    {
        Engine.getMessagePump().signalInterest(SimGlobals.ACTIVATE_BRAKE, helper);
        Engine.getMessagePump().signalInterest(SimGlobals.DEACTIVATE_BRAKE,helper);
    }

    static public void setActiveColor(ButtonColor c)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR, c));
    }

    static public void setUnActiveColor(ButtonColor c)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR, c));
    }

    public static void setEngagedSound(ButtonSound sound)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_ENGAGED_SOUND, sound));
    }

    public static void setDisengagedSound(ButtonSound sound)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_DISENGAGED_SOUND, sound));
    }


    public static boolean wasPressed() {
        boolean tmp = _wasPressed;
        if(_wasPressed) _wasPressed = !_wasPressed;
        return tmp;

    }

    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            double volume = 1;
            double balance = 0;
            switch (message.getMessageName())
            {
                case SimGlobals.ACTIVATE_BRAKE:
                case SimGlobals.DEACTIVATE_BRAKE:
                    _wasPressed  = !_wasPressed;
                    break;
            }
        }
    }

}
