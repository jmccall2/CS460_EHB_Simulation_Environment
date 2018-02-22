package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;

/**
 * The gear interface provides a very simple means to check which gear
 * the car is currently in. It exposes one getter method, getGear(), which
 * returns a value from the GearTypes enum.
 */
public class GearInterface
{
    private static GearTypes _currentGear;

    {
        Engine.getMessagePump().signalInterest(SimGlobals.GEAR_CHANGE,
                (message) -> _currentGear = (GearTypes)message.getMessageData());
    }

    /**
     * Returns the current gear that the car is in. You can call this as many
     * times during an update as needed as it does not modify any internal state.
     *
     * @return current gear from the GearTypes enum
     */
    public static GearTypes getGear()
    {
        return _currentGear;
    }
}
