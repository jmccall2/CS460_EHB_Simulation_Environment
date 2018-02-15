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
    Engine.getMessagePump().signalInterest(SimGlobals.SET_SPEED, helper);
  }

  public static double getSpeed()
  {
    return speed;
  }

  //since the GUI will set the speed and the car will have the speed there is no need for this method setSpeed
  private static void setSpeed(double speed1)
  {
    speed = speed1;
    Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_SPEED, speed));
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

