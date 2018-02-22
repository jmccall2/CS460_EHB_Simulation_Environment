package interfaces;

import java.net.URL;

import javafx.scene.media.AudioClip;
import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

/**
 * NOTE :: Almost no button state is maintained by this class. That burden
 *         is left to the designers of the EHB software. For example, if
 *         you receive a value of 'true' from the function isDown(), the EHB
 *         software must update itself to reflect that state change.
 *
 * The button interface gives selective control over certain
 * properties of the "activate hand brake" button. Such properties
 * include setting its color and playing a sound to signify a change
 * in state.
 *
 * In addition to this, it exposes a single getter method, isDown(),
 * which reports 'true' if the button has been pressed down and 'false'
 * otherwise.
 */
public class ButtonInterface
{
    private static boolean _isDown;

    {
        Helper helper = new Helper();
        Engine.getMessagePump().signalInterest(SimGlobals.ACTIVATE_BRAKE, helper);
        Engine.getMessagePump().signalInterest(SimGlobals.DEACTIVATE_BRAKE,helper);
    }

    /**
     * Sets the color of the hand brake button. This change will take effect immediately
     * and become visible to the user.
     * @param c a button type from the enum ButtonColorTypes
     */
    static public void setColor(ButtonColorTypes c)
    {
        Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_BUTTON_COLOR, c));
    }

    /**
     * Plays a sound to signify some change in state of the EHB system. This sound
     * will be played immediately and exactly one time.
     * @param s sound type from the enum ButtonSoundTypes
     */
    static public void play(ButtonSoundTypes s)
    {
        URL url = ButtonInterface.class.getResource(s.toString());
        AudioClip sound = new AudioClip(url.toExternalForm());
        sound.play(1, 0, 1, 0, 1);
    }

    /**
     * NOTE :: If you call this and receive a value of 'true' and then immediately
     *         try to call it again, you will receive a value of 'false'. It is recommended
     *         to only call this method once per EHB update sequence.
     *
     * Checks if the button is currently pressed down. This function modifies what minimal
     * internal state that this interface maintains, so calling it twice during the same
     * update will result in two different values so long as the first value was 'true'.
     *
     * @return true if the button is currently pressed down and false otherwise
     */
    public static boolean isDown()
    {
        return _isDown;
    }

    private class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message)
        {
            switch (message.getMessageName())
            {
                case SimGlobals.ACTIVATE_BRAKE:
                    _isDown = true;
                    break;
                case SimGlobals.DEACTIVATE_BRAKE:
                    _isDown = false;
                    break;
            }
        }
    }

}
