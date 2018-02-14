package simulation;

import simulation.engine.RenderEntity;

public class TireTrack extends RenderEntity
{
    // This will be more abstracted later.
    public TireTrack(double x, double y)
    {
        setTexture("resources/img/car/tiretrack.png");
        setLocationXYDepth(x, y, 1);
        setSpeedXY(0, 0);
        setWidthHeight(20, 50);
    }


    @Override
    public void pulse(double deltaSeconds) {

    }

}
