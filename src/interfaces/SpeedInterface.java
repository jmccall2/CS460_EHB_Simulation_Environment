package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;

/**
 * This interface provides a way to query the current speed of the car in
 * meters per second. It exposes one getter method, getSpeed().
 */
public class SpeedInterface
{
  private static double _speed = 0;

  {
    Engine.getMessagePump().signalInterest(SimGlobals.SPEED,
            (message) -> _speed = (Double)message.getMessageData());
  }

  /**
   * @return the current speed of the car in meters per second
   */
  public static double getSpeed()
  {
    return _speed;
  }
}

