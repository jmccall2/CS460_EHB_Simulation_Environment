package interfaces;

import simulation.SimGlobals;
import simulation.engine.Message;
import simulation.engine.MessageHandler;
import simulation.engine.Singleton;

import java.util.HashSet;

public class Brake
{

  Helper helper = new Helper();

  private static double pressure;

  public static double getPressure()
  {
    return pressure;
  }

  public Brake()
  {
    Singleton.engine.getMessagePump().signalInterest(SimGlobals.SET_PRESSURE, helper);
  }

  public static void setPressure(double press)
  {
    pressure = press;
    Singleton.engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_PRESSURE, pressure));
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
