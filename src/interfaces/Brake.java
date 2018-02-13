package interfaces;

import simulation.engine.Message;
import simulation.engine.MessageHandler;

public class Brake
{

  private static double pressure;

  public static double getPressure()
  {
    return pressure;
  }

  public static void setPressure(double press)
  {
    pressure = press;
  }

  class Helper implements MessageHandler
  {
    @Override
    public void handleMessage(Message message)
    {
      pressure = (Double)message.getMessageData();
    }
  }
}
