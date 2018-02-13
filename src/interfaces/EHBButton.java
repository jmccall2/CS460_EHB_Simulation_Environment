package interfaces;

import simulation.GUI;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class EHBButton
{
    private static int numTimesPressed;
    private static GUI _gui;
    public EHBButton(GUI gui)
    {
        _gui=gui;
    }

    static public void setActiveColor(ButtonColor c)
    {
        _gui.getEHBReference().setActivatedColor(c);
    }

    static public void setUnActiveColor(ButtonColor c)
    {
        _gui.getEHBReference().setUnactivatedColor(c);
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
