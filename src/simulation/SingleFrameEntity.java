package simulation;

import simulation.engine.RenderEntity;

/**
 * Generic entity to add to the world containing a single frame (image).
 */
public class SingleFrameEntity extends RenderEntity
{
    /**
     *
     * @param texture Image path
     * @param x coordinate in the world
     * @param y coordinate in the world
     * @param d depth in the world
     * @param xs x speed in the world
     * @param ys y speed in the world
     * @param w entity width
     * @param h entity height
     */
    SingleFrameEntity(String texture, int x, int y, int d, int xs, int ys, int w, int h)
    {
        setTexture(texture);
        setLocationXYDepth(x, y,d);
        setSpeedXY(xs, ys);
        setWidthHeight(w, h);
    }


    @Override
    public void pulse(double deltaSeconds) {

    }
}
