package simulation;


import javafx.scene.paint.Color;
import simulation.engine.RenderEntity;

public class BarEntity extends RenderEntity
{

    private int _iterations = 0;
    private int _red = 0;
    private int _green = 255;
    private int _delay = 0;
    private int _xLoc;
    private int _yLoc;
    private int _depth;

    public BarEntity(Color initColor, int x, int y, int d, int xs, int ys, int w, int h)
    {
        _yLoc=y;_xLoc=x;_depth=d;
        setColor(initColor);
        setLocationXYDepth(x, y,d);
        setSpeedXY(xs, ys);
        setWidthHeight(w, h);
    }


    @Override
    public void pulse(double deltaSeconds)
    {
        _delay++;
        if(_delay ==20) {
            if(_yLoc > 475 && _green > 0)
            {
                setLocationXYDepth(_xLoc,_yLoc-=1,_depth);
                if(_iterations < 48) setColor(Color.rgb(_red,_green,0)); // Stay green for all lower values.
                else setColor(Color.rgb(_red < 255 ? _red+=5 : _red, _red == 255 ? _green-=5 : _green, 0));
            }
            _iterations++;
            _delay = 0;
        }
    }
}
