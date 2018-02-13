package interfaces;

import simulation.engine.Message;
import simulation.engine.MessageHandler;


public class Speed
{

  Helper helper;

  private static double speed = 0;

  public static double getSpeed()
  {

    return speed;
  }

  private static void setSpeed(double speed1)
  {
    speed = speed1;
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

