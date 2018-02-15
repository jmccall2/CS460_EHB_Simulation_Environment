package simulation;

import simulation.engine.RenderEntity;

public class TireTrack extends RenderEntity
{

    private static int _swerveToggle = 0;


    public TireTrack(double x, double y, int tractionLossLevel)
    {
        if(tractionLossLevel==1) setTexture("resources/img/car/tiretrack.png");
        if(tractionLossLevel==2)
        {
            if(_swerveToggle%2==0) setTexture("resources/img/car/swerveLeft.png");
            else setTexture("resources/img/car/swerveRight.png");
            _swerveToggle++;
        }

        setLocationXYDepth(x, y, 1);
        setSpeedXY(0, 0);
        setWidthHeight(50, 50);
    }


    @Override
    public void pulse(double deltaSeconds)
    {
        if(getLocationX() < - 1000) removeFromWorld();
    }

}
