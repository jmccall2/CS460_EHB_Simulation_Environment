package ehb;

import interfaces.*;

import java.util.Map;
import java.util.TreeMap;


public class EHB
{

  private double _speed;
  private GearTypes _gear;
  private boolean _isActive;

  //Max pressure is considered to be at 6 kPa.
  //first is speed. second is pressure
//  static Map<Integer, Integer> goodPressureProfile;
  static TreeMap<Long, Integer> goodPressureProfile;

  static
  {
    goodPressureProfile = new TreeMap<Long, Integer>();
    goodPressureProfile.put(Long.valueOf(20), 2);
    goodPressureProfile.put(Long.valueOf(30), 3);
    goodPressureProfile.put(Long.valueOf(40), 4);
    goodPressureProfile.put(Long.valueOf(50), 4);
    goodPressureProfile.put(Long.valueOf(60), 5);
    goodPressureProfile.put(Long.valueOf(70), 6);
    goodPressureProfile.put(Long.valueOf(80), 6);
    goodPressureProfile.put(Long.valueOf(90), 5);
    goodPressureProfile.put(Long.valueOf(100), 5);
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
    ButtonInterface.setColor(ButtonColorTypes.BLUE);
    _isActive = false;
  }

  //Add timer to class to demo how they can measure. time between button clicks.

  public void update()
  {
    if (ButtonInterface.wasPressed())
    {
      _isActive = !_isActive;
      if (_isActive)
      {
        ButtonInterface.setColor(ButtonColorTypes.RED);
        ButtonInterface.play(ButtonSoundTypes.ENGAGED);
      }
      else
      {
        ButtonInterface.setColor(ButtonColorTypes.BLUE);
        ButtonInterface.play(ButtonSoundTypes.DISENGAGED);
      }
    }

    //Use the Button interface to see if the button is active or not.
    if (_isActive)
    {
      //Why do all variables have an underscore before them?!!!

      //Start a while loop to continuously keep getting speed and gear and calculate
      //pressure accordingly/

      _speed = SpeedInterface.getSpeed(); // Get the speed from the speed interface.
      _gear = GearInterface.getGear();  // Get the current gear from the Gear interface.

      //Using MKS units, pressure [=] N/m^2 or kg m /s^2

      //     For immediate testing purposes just set the pressure to 100%.
//        BrakeInterface.setPressure(100.0); // Set the pressure using the brake interface.

//        First of all, we are going to check the "base" cases where where the "parking" break
//        or emergency break should be applied.

      //If the gear is on park and the button is pressed, then full pressure is applied.
//        if the gear is on anything other than reverse and the speed is negative then this means
//        the car is sliding back so we also apply max pressure as a safety mechanism.

      //        We are going to make two attempts at calculating the parking brake pressure. Once, using
//        a good pressure profile and the other using a bad pressure profile.
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
        Long key = Long.valueOf((int)_speed);
        Map.Entry<Long, Integer> floor = goodPressureProfile.floorEntry(key);
        Map.Entry<Long, Integer> ceiling = goodPressureProfile.ceilingEntry(key);
        Integer closestResult = 50;
        if (floor != null && ceiling != null)
        {
//          System.out.println(floor.getKey());
//          System.out.println(ceiling.getKey());
          closestResult = Math.abs(closestResult - floor.getKey()) < Math.abs(closestResult - ceiling.getKey())
            ? floor.getValue()
            : ceiling.getValue();
        }
        else if (floor != null || ceiling != null)
        {
          closestResult = floor != null ? floor.getValue() : ceiling.getValue();
        }

        System.out.println("speed is " + _speed);
        System.out.println("Pressure perc is " + (closestResult/6.0)*100);
        BrakeInterface.setPressure((closestResult/6.0)*100);
      }
    }
    else
    {
      BrakeInterface.setPressure(0.0);
    }
  }
}
