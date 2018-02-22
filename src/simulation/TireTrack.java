package simulation;

import simulation.engine.RenderEntity;

/**
 * Tiretrack animations.
 */
public class TireTrack extends RenderEntity
{

    // Toggles images for each anonymous class instance added to the world.
    private static int _swerveToggle = 0;

    /**
     * Initialize tire track in the world.
     *
     * @param x world coordinate.
     * @param y world coordinate.
     * @param tractionLossLevel
     */
    TireTrack(double x, double y, int tractionLossLevel)
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

    /**
     * Remove tire track once it is not visible on the screen.
     * @param deltaSeconds Change in seconds since the last update.
     *                     If the simulation.engine is running at 60 frames per second,
     */
    @Override
    public void pulse(double deltaSeconds)
    {
        if(!isVisibleOnScreen()) removeFromWorld();
    }

}
