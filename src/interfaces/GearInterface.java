package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class GearInterface
{
    Helper helper = new Helper();

    private static Gear _currentGear;

    public GearInterface()
    {
       Engine.getMessagePump().signalInterest(SimGlobals.GEAR_CHANGE, helper);
    }

    public static Gear getGear()
    {
        return _currentGear;
    }


    class Helper implements MessageHandler
    {
        @Override
        public void handleMessage(Message message) { _currentGear = (Gear)message.getMessageData(); }
    }
}
