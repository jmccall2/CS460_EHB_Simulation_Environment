package ehb;

import interfaces.*;
import simulation.EHBButton;


public class EHB
{

    private double _speed;
    private Gear _gear;
    private boolean _isActive;

    public EHB()
    {
        init();
    }

    public void init()
    {
         EHBButtonInterface.setColor(ButtonColor.BLUE);
        _isActive = false;
    }

    
    public void update() {
        if(EHBButtonInterface.wasPressed())
        {
            _isActive = !_isActive;
            if(_isActive)
            {
                EHBButtonInterface.setColor(ButtonColor.RED);
                EHBButtonInterface.play(ButtonSound.ENGAGED);
            }
            else
            {
                EHBButtonInterface.setColor(ButtonColor.BLUE);
                EHBButtonInterface.play(ButtonSound.DISENGAGED);
            }
        }

         //Use the Button interface to see if the button is active or not.
        if(_isActive)
        {
            _speed = SpeedInterface.getSpeed(); // Get the speed from the speed interface.
            // Get the current gear from the Gear interface.
            _gear = GearInterface.getGear();
          //   PUT CALCULATIONS FOR HOW TO DETERMINE PRESSURE HERE BASED OFF KNOWN
          //   INFORMATION.

        //     For immediate testing purposes just set the pressure to 100%.
            BrakeInterface.setPressure(100.0); // Set the pressure using the brake interface.

        }
        else
        {
        }
    }
}
