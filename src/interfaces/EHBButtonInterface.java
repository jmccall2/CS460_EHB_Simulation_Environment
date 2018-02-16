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
    private static int numTimesPressed;
    private static boolean _active;
    private static boolean _wasPressed;
    private static boolean init = true;
    private static boolean init2 = true;
    static String path1 = "";
    static String path2 = "";

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
        if(init)Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_ENGAGED_SOUND, path));
        else Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_ENGAGED_SOUND, path1));
    }

    public static void setDisengagedSound(String path)
    {
        if(init2)Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_DISENGAGED_SOUND, path));
        else Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_DISENGAGED_SOUND, path2));
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
                case SimGlobals.SET_ENGAGED_SOUND:
                    path1 = (String)message.getMessageData();
                    if(!init)
                    {
                      URL url = getClass().getResource(path1);
                      AudioClip sound = new AudioClip(url.toExternalForm());
                      sound.play(volume, balance, 1, 0, 1);
                    }
                    init = false;
                    break;
                case SimGlobals.SET_DISENGAGED_SOUND:
                    path2 = (String)message.getMessageData();
                    if(!init2)
                    {
                      URL url2 = getClass().getResource(path2);
                      AudioClip sound2 = new AudioClip(url2.toExternalForm());
                      sound2.play(volume, balance, 1, 0, 1);
                    }
                    init2 = false;
                    break;
                case SimGlobals.ACTIVATE_BRAKE:
                case SimGlobals.DEACTIVATE_BRAKE:
                    _wasPressed  = !_wasPressed;
                    break;
            }
        }
    }

}
