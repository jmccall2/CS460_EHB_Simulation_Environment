package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;


public class SpeedInterface
{

  Helper helper = new Helper();

  private static double speed = 0;

  public SpeedInterface()
  {
    Engine.getMessagePump().signalInterest(SimGlobals.SPEED, helper);
  }

  public static double getSpeed()
  {
    return speed;
  }

  class Helper implements MessageHandler
  {
    @Override
    public void handleMessage(Message message)
    {
      speed = (Double)message.getMessageData();
    }
  }
}

