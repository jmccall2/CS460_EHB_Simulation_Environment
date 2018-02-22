package simulation;


import javafx.scene.paint.Color;
import java.util.ArrayList;

import simulation.engine.RenderEntity;


public class BarEntity extends RenderEntity
{

    private int _xLoc;
    private int _yLoc;
    private int _yInit;
    private int _depth;
    private ArrayList<Color> _colors = new ArrayList<>();
    private Color _currentColor;
    private double SPEED_TO_Y_CONST = 2.3650793650793;
    private double PRESSURE_TO_Y_CONST = 1.587301587;
    private double MAX_SPEED = 63;
    private double _speedToColorIndexMult;
    private BarEntityModes _Mode;

    public BarEntity(Color initColor, int x, int y, int d, int xs, int ys, int w, int h, BarEntityModes mode)
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

    private void _setColors()
    {
        int nColors = (_Mode == BarEntityModes.SPEED) ? 140 : 100;
        int nPureGreen = (_Mode == BarEntityModes.SPEED) ? 38 : 26;
        int nColorInc = (nColors - nPureGreen)/2;
        double multiplier = (255.0/nColorInc);
        _speedToColorIndexMult = ((double)nColors)/MAX_SPEED;
        for(int i = 0; i < nPureGreen; i++) _colors.add(Color.rgb(0,255,0)); // Keep a green color for most of the lower speeds.
        for(int i = 1; i <= nColorInc; i++) _colors.add(Color.rgb((int)(i*multiplier),255,0));
        for(int i = 1; i <= nColorInc; i++) _colors.add(Color.rgb(255,255 - (int)(i*multiplier), 0));
    }


    public void updateState(double mapVal)
    {
        if(mapVal < 0) mapVal = -mapVal;
        _yLoc =_yInit - (int)(mapVal*((_Mode == BarEntityModes.SPEED) ? SPEED_TO_Y_CONST : PRESSURE_TO_Y_CONST));
        int index = (int)(mapVal * _speedToColorIndexMult);
        if(index >= _colors.size()) index = _colors.size() - 1;
        _currentColor = _colors.get(index);

    }


    @Override
    public void pulse(double deltaSeconds)
    {
        setLocationXYDepth(_xLoc,_yLoc,_depth);
        setColor(_currentColor);
    }
}
