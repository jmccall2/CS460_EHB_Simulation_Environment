package interfaces;

import simulation.SimGlobals;
import simulation.engine.Engine;
import simulation.engine.Message;

/**
 * The brake interface represents the only means by which
 * the EHB can modulate the brake pressure.
 *
 * It exposes one static method:
 *        setPressure()
 */
public class BrakeInterface
{
  /**
   * Sets the pressure being applied to the drum brake as a percentage
   * on the range of [0.0, 100.0]. These are hard limits, so going above
   * or below these values will force the system to constrain the value
   * to something it can deal with.
   *
   * @param pressure pressure applied to the brake on the range [0.0, 100.0]
   */
  public static void setPressure(double pressure)
  {
    // Constrain the pressure if needed
    if (pressure < 0.0) pressure = 0.0;
    else if (pressure > 100.0) pressure = 100.0;
    Engine.getMessagePump().sendMessage(new Message(SimGlobals.SET_PRESSURE, pressure));
  }
}
