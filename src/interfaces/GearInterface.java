package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class GearInterface
{
    Helper helper = new Helper();

    private static GearTypes _currentGear;

    public GearInterface()
    {
       Engine.getMessagePump().signalInterest(SimGlobals.GEAR_CHANGE, helper);
    }

    public static GearTypes getGear()
    {
        return _currentGear;
    }


    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message) { _currentGear = (GearTypes)message.getMessageData(); }
    }
}
