package simulation;

import simulation.engine.RenderEntity;

public class SingleFrameEntity extends RenderEntity
{
    SingleFrameEntity(String texture, int xLoc, int yLoc, int xSpeed, int ySpeed, int width, int height)
    {
        setTexture(texture);
        //setLocationXYDepth(xLoc, yLoc);
     //   setSpeed(xSpeed, ySpeed);
        setWidthHeight(width, height);
    }


    @Override
    public void pulse(double deltaSeconds) {

    }
}
