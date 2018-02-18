package ehb;

import interfaces.*;


public class EHB
{

    private double _speed;
    private GearTypes _gear;
    private boolean _isActive;

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
    
    public void update() {
        if(ButtonInterface.wasPressed())
        {
            _isActive = !_isActive;
            if(_isActive)
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
        if(_isActive)
        {
            _speed = SpeedInterface.getSpeed(); // Get the speed from the speed interface.
            // Get the current gear from the GearTypes interface.
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
