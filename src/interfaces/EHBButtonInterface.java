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

    // After looking at the diagram he sent us I believe the boolean getter in this
    // interface he was referencing was one that returns true if activated, false if otherwise.
    // (We should probably check).

    public static boolean isActive() {return _active;}

    public static int getNumTimesPressed()
    {
        return numTimesPressed;
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
                    _active = true;
                    break;
                case SimGlobals.DEACTIVATE_BRAKE:
                    _active = false;
                    break;
            }
        }
    }

}
