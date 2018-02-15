package ehb;

import interfaces.*;
import simulation.engine.RenderEntity;


// I am only extending render entity so I can test things. Specifically, to get the get a method the engine constantly runs (pulse).
// We have to provide the the EHB software package a method that gets called engine continuously.
public class EHB extends RenderEntity
{

    private double _speed;
    private Gear _gear;

    public EHB()
    {
        init();
    }

    public void init()
    {
        EHBButtonInterface.setActiveColor(ButtonColor.RED);
        EHBButtonInterface.setUnActiveColor(ButtonColor.BLUE);
        addToWorld();
    }


    @Override
    public void pulse(double deltaSeconds) {
        // Use the Button interface to see if the button is active or not.
        if(EHBButtonInterface.isActive())
        {
            _speed = SpeedInterface.getSpeed(); // Get the speed from the speed interface.
            // Get the current gear from the Gear interface.
            _gear = GearInterface.getGear();
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
