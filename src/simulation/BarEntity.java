package simulation;


import javafx.scene.paint.Color;
import simulation.engine.RenderEntity;

public class BarEntity extends RenderEntity
{

    int height;
    int width;
    int xLoc;
    int yLoc;
    int depth;
    public BarEntity(Color initColor, int x, int y, int d, int xs, int ys, int w, int h)
    {
        height= h;
        width=w;
        xLoc = x;
        yLoc =y;
        depth = d;
        setColor(initColor);
        setLocationXYDepth(x, y,d);
        setSpeedXY(xs, ys);
        setWidthHeight(w, h);
        //setAcceleration(0,-1);
    }


    class RGB
    {
        public int r = 255;
        public int b = 0;
        public int g = 0;
    }

    RGB _RGB = new RGB();
    int stepSize = 1;

    void updateRGB()
    {
        while(_RGB.g < 255)
        {
            _RGB.g += stepSize;
            if(_RGB.g > 255) { _RGB.g = 255; }
        }
        while(_RGB.r > 0)
        {
            _RGB.r -= stepSize;
            if(_RGB.r < 0) { _RGB.r = 0; }

        }

    }

    int delay = 0;

    @Override
    public void pulse(double deltaSeconds)
    {
        delay++;
        if(delay ==20) {
            updateRGB();
            //setWidthHeight(width, height += 5);
             setLocationXYDepth(xLoc,yLoc-=10,depth);
            setColor(Color.rgb(_RGB.r, _RGB.g, _RGB.b));
            delay = 0;
        }
    }
}
