package interfaces;

import simulation.GUI;

public class EHBButton
{
    private static GUI _gui;
    public EHBButton(GUI gui)
    {
        _gui=gui;
    }

    static public void setActiveColor(ButtonColor c)
    {
        _gui.getEHBReference().setActivatedColor(c);
    }

    static public void setUnActiveColor(ButtonColor c)
    {
        _gui.getEHBReference().setUnactivatedColor(c);
    }
}
