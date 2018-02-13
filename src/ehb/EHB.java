package ehb;

import interfaces.ButtonColor;
import interfaces.EHBButton;

/* I am not sure what the best way to invoke the main procedure of the ehb software is so I'm temporarily
* doing it this way for now so I can test things. */

public class EHB
{
    public EHB()
    {
        init();
    }

    public void init()
    {
        EHBButton.setActiveColor(ButtonColor.RED);
        EHBButton.setUnActiveColor(ButtonColor.BLUE);
    }


}
