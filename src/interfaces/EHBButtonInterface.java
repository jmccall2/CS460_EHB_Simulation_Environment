package interfaces;

import ehb.EHB;
import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class EHBButtonInterface
{
    Helper helper = new Helper();
    private static int numTimesPressed;
    private static boolean _active;
    private static boolean _wasPressed;

    public EHBButtonInterface()
    {
        Engine.getMessagePump().signalInterest(SimGlobals.SET_ENGAGED_SOUND, helper);
        Engine.getMessagePump().signalInterest(SimGlobals.SET_DISENGAGED_SOUND, helper);
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

    public static void setEngagedSound(String path)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_ENGAGED_SOUND, path));
    }

    public static void setDisenagedSound(String path)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_DISENGAGED_SOUND, path));
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
            switch (message.getMessageName())
            {
                case SimGlobals.SET_ENGAGED_SOUND:
                    // do something
                    break;
                case SimGlobals.SET_DISENGAGED_SOUND:
                    // do something
                    break;
                case SimGlobals.ACTIVATE_BRAKE:
                case SimGlobals.DEACTIVATE_BRAKE:
                    _wasPressed  = !_wasPressed;
                    break;
            }
        }
    }

}
