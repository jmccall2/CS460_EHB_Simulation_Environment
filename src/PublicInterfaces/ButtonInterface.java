package PublicInterfaces;

import GUI.GUI;

public class ButtonInterface
{
    private static GUI _gui;
    public ButtonInterface(GUI gui)
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
