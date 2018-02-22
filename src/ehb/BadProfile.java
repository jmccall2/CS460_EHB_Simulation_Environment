package ehb;

import interfaces.*;

import java.util.Map;
import java.util.TreeMap;

public class BadProfile {

    private double _speed;
    private GearTypes _gear;
    private boolean wasEngaged = false;
    private int alertPlayed = 0;

    //the car should crash on high speeds with the bad pressure profiler
    static TreeMap<Long, Double> badPressureProfile;

    static
    {
        badPressureProfile = new TreeMap<Long, Double>();
        badPressureProfile.put(Long.valueOf(20), 6.0);
        badPressureProfile.put(Long.valueOf(30), 6.0);
        badPressureProfile.put(Long.valueOf(40), 6.0);
        badPressureProfile.put(Long.valueOf(50), 6.0);
        badPressureProfile.put(Long.valueOf(60), 6.0);
        badPressureProfile.put(Long.valueOf(70), 6.0);
        badPressureProfile.put(Long.valueOf(80), 6.0);
        badPressureProfile.put(Long.valueOf(90), 6.0);
        badPressureProfile.put(Long.valueOf(100), 6.0);
    }

    // TODO Remove
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
                Map.Entry<Long, Double> floor = badPressureProfile.floorEntry(key);
                Map.Entry<Long, Double> ceiling = badPressureProfile.ceilingEntry(key);


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
                //BrakeInterface.setPressure(100);
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
