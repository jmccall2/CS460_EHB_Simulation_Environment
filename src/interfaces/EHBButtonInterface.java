package interfaces;

import java.lang.invoke.MethodHandle;
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

    static public void setColor(ButtonColor c)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_BUTTON_COLOR, c));
    }
    static public void play(ButtonSound s)
    {
        URL url = EHBButtonInterface.class.getResource(_mapToSoundFile(s));
        AudioClip sound = new AudioClip(url.toExternalForm());
        sound.play(1, 0, 1, 0, 1);
    }

    public static boolean wasPressed() {
        boolean tmp = _wasPressed;
        if(_wasPressed) _wasPressed = !_wasPressed;
        return tmp;

    }

    private static String _mapToSoundFile(ButtonSound sound)
    {
        switch(sound)
        {
            case ENGAGED:
                return "/resources/sounds/engaged.wav";
            case DISENGAGED:
                return "/resources/sounds/disengaged.wav";
            default:
                System.err.println("WARNING: Invalid color provided");
                return "";
        }
    }

    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
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
