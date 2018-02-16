package ehb;

import interfaces.BrakeInterface;
import interfaces.ButtonColor;
import interfaces.EHBButtonInterface;
import interfaces.Gear;
import interfaces.GearInterface;
import interfaces.SpeedInterface;


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
        EHBButtonInterface.setActiveColor(ButtonColor.PURPLE);
        EHBButtonInterface.setUnActiveColor(ButtonColor.GREEN);
        EHBButtonInterface.setEngagedSound("/resources/sounds/engaged.wav");
        EHBButtonInterface.setDisengagedSound("/resources/sounds/disengaged.wav");
        _isActive = false;
    }

    
    public void update() {
        if(EHBButtonInterface.wasPressed()) _isActive = !_isActive;
        // Use the Button interface to see if the button is active or not.
        if(_isActive)
        {
            _speed = SpeedInterface.getSpeed(); // Get the speed from the speed interface.
            // Get the current gear from the Gear interface.
            _gear = GearInterface.getGear();
            System.out.println(_gear);
            // PUT CALCULATIONS FOR HOW TO DETERMINE PRESSURE HERE BASED OFF KNOWN
            // INFORMATION.

            // For immediate testing purposes just set the pressure to 100%.
            BrakeInterface.setPressure(100.0); // Set the pressure using the brake interface.

        }
        else
        {
          //  System.out.println("EHB IS NOT ACTIVE.");
        }
    }
}
