package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;

public class EHBButton
{
    Helper helper = new Helper();
    private static int numTimesPressed;

    public EHBButton()
    {
        Engine.getMessagePump().signalInterest(SimGlobals.SET_ENGAGED_SOUND, helper);
        Engine.getMessagePump().signalInterest(SimGlobals.SET_DISENGAGED_SOUND, helper);
    }

    static public void setActiveColor(ButtonColor c)
    {
        //Singleton.engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_ACTIVATED_COLOR, c));
    }

    static public void setUnActiveColor(ButtonColor c)
    {
        //Singleton.engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_UNACTIVATED_COLOR, c));
    }

    public static void setEngagedSound(String path)
    {

    }

    public static void setDisenagedSound(String path)
    {

    }

    public static int getNumTimesPressed()
    {
        return numTimesPressed;
    }

    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {

        }
    }

}
