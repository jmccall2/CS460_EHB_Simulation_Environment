package simulation;


import javafx.scene.paint.Color;
import java.util.ArrayList;

import simulation.engine.RenderEntity;

/**
 * Class that handles all logic related
 * to the speed or pressure bars.
 */
public class BarEntity extends RenderEntity
{

    private final double SPEED_TO_Y_CONST = 2.3650793650793;
    private final double PRESSURE_TO_Y_CONST = 1.587301587;
    private final double INDEXER_CONST = 63;
    private final int SPEED_BAR_MAX = 140;
    private final int PRESSURE_BAR_MAX = 100;
    private final int SPEED_BAR_GREEN_THRESH = 38;
    private final int PRESSURE_BAR_GREEN_TRESH = 26;

    private int _xLoc;
    private int _yLoc;
    private int _yInit;
    private int _depth;
    private ArrayList<Color> _colors = new ArrayList<>();
    private Color _currentColor;
    private double _speedToColorIndexMult;
    private BarEntityModes _Mode;

    /**
     *
     * @param initColor Start color of the bar.
     * @param x coordinate in the world
     * @param y coordinate in the world
     * @param d depth in the world
     * @param xs x speed
     * @param ys y speed
     * @param w width
     * @param h height
     * @param mode Bar type; Speed or Pressure.
     */
    BarEntity(Color initColor, int x, int y, int d, int xs, int ys, int w, int h, BarEntityModes mode)
    {
        _Mode = mode;
        _yInit=y;_yLoc=y;_xLoc=x;_depth=d;
        _currentColor = initColor;
        setColor(initColor);
        setLocationXYDepth(x, y,d);
        setSpeedXY(xs, ys);
        setWidthHeight(w, h);
        _setColors();
    }

    // Update the color of the bar.
    private void _setColors()
    {
        int nColors = (_Mode == BarEntityModes.SPEED) ? SPEED_BAR_MAX : PRESSURE_BAR_MAX;
        int nPureGreen = (_Mode == BarEntityModes.SPEED) ? SPEED_BAR_GREEN_THRESH : PRESSURE_BAR_GREEN_TRESH;
        int nColorInc = (nColors - nPureGreen)/2;
        double multiplier = (255.0/nColorInc);
        _speedToColorIndexMult = ((double)nColors)/INDEXER_CONST;
        for(int i = 0; i < nPureGreen; i++) _colors.add(Color.rgb(0,255,0)); // Keep a green color for most of the lower speeds.
        for(int i = 1; i <= nColorInc; i++) _colors.add(Color.rgb((int)(i*multiplier),255,0));
        for(int i = 1; i <= nColorInc; i++) _colors.add(Color.rgb(255,255 - (int)(i*multiplier), 0));
    }

    // Update the state of the bar, this includes y location and color.
    void updateState(double mapVal)
    {
        if(mapVal < 0) mapVal = -mapVal;
        _yLoc =_yInit - (int)(mapVal*((_Mode == BarEntityModes.SPEED) ? SPEED_TO_Y_CONST : PRESSURE_TO_Y_CONST));
        int index = (int)(mapVal * _speedToColorIndexMult);
        if(index >= _colors.size()) index = _colors.size() - 1;
        _currentColor = _colors.get(index);

    }

    /**
     * Based off updated state change the bar in the world.
     * @param deltaSeconds Change in seconds since the last update.
     *                     If the simulation.engine is running at 60 frames per second,
     */
    @Override
    public void pulse(double deltaSeconds)
    {
        setLocationXYDepth(_xLoc,_yLoc,_depth);
        setColor(_currentColor);
    }
}
