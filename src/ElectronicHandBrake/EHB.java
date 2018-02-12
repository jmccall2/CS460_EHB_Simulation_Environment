package ElectronicHandBrake;

import PublicInterfaces.*;

/* I am not sure what the best way to invoke the main procedure of the EHB software is so I'm temporarily
* doing it this way for now so I can test things. */

public class EHB
{
    public EHB()
    {
        init();
    }

    public void init()
    {
        ButtonInterface.setActiveColor(ButtonColor.RED);
        ButtonInterface.setUnActiveColor(ButtonColor.BLUE);
    }


}
