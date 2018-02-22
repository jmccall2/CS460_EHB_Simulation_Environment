package ehb;

import interfaces.BrakeInterface;
import interfaces.ButtonColorTypes;
import interfaces.ButtonInterface;
import interfaces.ButtonSoundTypes;

public class BadProfile {
    private boolean _isActive = false;

    //Add timer to class to demo how they can measure. time between button clicks.
    public void update()
    {
        boolean active = ButtonInterface.isDown();
        if (active && !_isActive)
        {
            //ButtonInterface.play(ButtonSoundTypes.SHORT_BEEP_A);
            ButtonInterface.play(ButtonSoundTypes.ENGAGED);
            _isActive = true;
        }
        else if (!active && _isActive)
        {
            //ButtonInterface.play(ButtonSoundTypes.SHORT_BEEP_B);
            ButtonInterface.play(ButtonSoundTypes.DISENGAGED);
            _isActive = false;
        }

        if (_isActive)
        {
            BrakeInterface.setPressure(100);
            ButtonInterface.setColor(ButtonColorTypes.RED);
        }
        else
        {
            BrakeInterface.setPressure(0);
            ButtonInterface.setColor(ButtonColorTypes.GREEN);
        }
    }
}