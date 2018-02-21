package ehb;

import java.util.Map;
import java.util.TreeMap;

import interfaces.BrakeInterface;
import interfaces.ButtonColorTypes;
import interfaces.ButtonInterface;
import interfaces.ButtonSoundTypes;
import interfaces.GearInterface;
import interfaces.GearTypes;
import interfaces.SpeedInterface;


public class EHB
{

  private double _speed;
  private GearTypes _gear;
  private boolean wasEngaged = false;
  private int alertPlayed = 0;

  //Max pressure is considered to be at 6 kPa.
  //first is speed. second is pressure
//  static Map<Integer, Integer> goodPressureProfile;
  static TreeMap<Long, Integer> goodPressureProfile;

  //The units for the profiler is miles per hour to kPascal
  static
  {
    goodPressureProfile = new TreeMap<Long, Integer>();
    goodPressureProfile.put(Long.valueOf(20), 6);
    goodPressureProfile.put(Long.valueOf(30), 6);
    goodPressureProfile.put(Long.valueOf(40), 5);
    goodPressureProfile.put(Long.valueOf(50), 5);
    goodPressureProfile.put(Long.valueOf(60), 4);
    goodPressureProfile.put(Long.valueOf(70), 4);
    goodPressureProfile.put(Long.valueOf(80), 3);
    goodPressureProfile.put(Long.valueOf(90), 3);
    goodPressureProfile.put(Long.valueOf(100), 2);
  }

  //the car should crash on high speeds with the bad pressure profiler
  static TreeMap<Long, Integer> badPressureProfile;

  static
  {
    badPressureProfile = new TreeMap<Long, Integer>();
    badPressureProfile.put(Long.valueOf(20), 2);
    badPressureProfile.put(Long.valueOf(30), 2);
    badPressureProfile.put(Long.valueOf(40), 3);
    badPressureProfile.put(Long.valueOf(50), 3);
    badPressureProfile.put(Long.valueOf(60), 4);
    badPressureProfile.put(Long.valueOf(70), 4);
    badPressureProfile.put(Long.valueOf(80), 5);
    badPressureProfile.put(Long.valueOf(90), 5);
    badPressureProfile.put(Long.valueOf(100), 6);
  }

  //The plot is not supposed to be perfectly linear. These values only serve as
  //an approximation of what the graph should be.

  //We use the pressure profile obtained from http://www.scielo.br/scielo.php?script=sci_arttext&pid=S0100-73862001000100007
  // with supplement http://www.optimumg.com/docs/Brake_tech_tip.pdf

  public EHB()
  {
    init();
  }

  public void init()
  {
  }

  //Add timer to class to demo how they can measure. time between button clicks.

  public void update()
  {
    _speed = 0;
    if (ButtonInterface.isDown())
    {
      ButtonInterface.setColor(ButtonColorTypes.RED);
      if (alertPlayed == 0)
      {
        ButtonInterface.play(ButtonSoundTypes.ENGAGED);
        wasEngaged = true;
        alertPlayed++;
      }
      _speed = (SpeedInterface.getSpeed() / 0.44704); // Get the speed from the speed interface.
      _gear = GearInterface.getGear();  // Get the current gear from the Gear interface.

//      System.out.println("speed is " + (SpeedInterface.getSpeed() / 0.44704));

      if (_gear.toString().equals("Park"))
      {
        BrakeInterface.setPressure(100.00);
      }
      else if ((!_gear.toString().equals("Reverse")) && _speed < 0)
      {
        BrakeInterface.setPressure(100.00);
      }
      else
      {
        //This uses the max and low values of the tree map to get the closest value in the
        //pressure profile
        Long key = Long.valueOf((int) _speed);
        Map.Entry<Long, Integer> floor = goodPressureProfile.floorEntry(key);
        Map.Entry<Long, Integer> ceiling = goodPressureProfile.ceilingEntry(key);


        double closestResult;
        if (floor != null && ceiling != null)
        {
          closestResult = (floor.getValue() + ceiling.getValue()) / 2.0;
        }
        else if (floor != null)
        {
          closestResult = floor.getValue();
        }
        else
        {
          closestResult = ceiling.getValue();
        }

//        System.out.println("Pressure applied is " + (closestResult / 6.0) * 100);
        BrakeInterface.setPressure((closestResult / 6.0) * 100);
      }
    }
    else
    {
      ButtonInterface.setColor(ButtonColorTypes.BLUE);
      if (wasEngaged)
      {
        ButtonInterface.play(ButtonSoundTypes.DISENGAGED);
        wasEngaged = false;
        alertPlayed--;
      }

      BrakeInterface.setPressure(0.0);
    }


  }
}
