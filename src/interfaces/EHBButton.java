package interfaces;

import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class EHBButton
{
    private static int numTimesPressed;

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
