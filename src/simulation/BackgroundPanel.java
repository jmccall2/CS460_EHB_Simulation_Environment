package simulation;

import simulation.engine.RenderEntity;

public class BackgroundPanel extends RenderEntity
{
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
