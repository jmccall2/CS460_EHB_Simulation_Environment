package simulation;

import simulation.engine.RenderEntity;

/**
 * Background panel in the world.
 */
public class BackgroundPanel extends RenderEntity
{
    /**
     *
     * @param texture image path
     * @param x coordinate in the world.
     * @param y coordinate in the world.
     * @param depth in the world
     * @param w width
     * @param h height
     */
    BackgroundPanel(String texture, int x, int y, int depth, int w, int h)
    {
        setTexture(texture);
        setLocationXYDepth(x,y,depth);
        setWidthHeight(w,h);
    }

    @Override
    public void pulse(double deltaSeconds) {

    }
}
