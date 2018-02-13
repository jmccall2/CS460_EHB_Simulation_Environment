package interfaces;

import simulation.engine.Message;
import simulation.engine.MessageHandler;


public class Speed
{

  Helper helper;

  private static double speed;

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

