package interfaces;

import simulation.SimGlobals;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;


public class Speed
{

  Helper helper = new Helper();

  private static double speed = 0;

  public Speed()
  {
    Singleton.engine.getMessagePump().signalInterest(SimGlobals.SET_SPEED, helper);
  }

  public static double getSpeed()
  {
    return speed;
  }

  private static void setSpeed(double speed1)
  {
    speed = speed1;
    Singleton.engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_SPEED, speed));
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

