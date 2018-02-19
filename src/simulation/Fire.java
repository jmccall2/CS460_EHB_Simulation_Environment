package simulation;

import javafx.scene.paint.Color;
import simulation.engine.RenderEntity;

import java.util.Random;

/**
 * This class is so inefficient and I'm sorry :(
 */
public class Fire extends RenderEntity {
    private int _particlesPerSecond;
    private double _elapsedSeconds = 0.0;
    private double _secondsPerParticle;
    private double _size;
    private double _dirX, _dirY;
    private Random _rand = new Random();

    public Fire(int particlesPerSecond, double x, double y, double depth, double size, int directionX, int directionY)
    {
        _particlesPerSecond = particlesPerSecond;
        _secondsPerParticle = 1.0 / particlesPerSecond;
        setLocationXYDepth(x, y, depth);
        setWidthHeight(size, size);
        if (directionX > 0) directionX = 1;
        else if (directionX < 0) directionX = -1;
        else directionX = 0;
        if (directionY > 0) directionY = 1;
        else if (directionY < 0) directionY = -1;
        else directionY = 0;
        _dirX = directionX;
        _dirY = directionY;
        setSpeedXY(100 * _dirX, 100 * _dirY);
    }

    public void setParticleRate(int particlesPerSecond)
    {
        _particlesPerSecond = particlesPerSecond;
    }

    @Override
    public void setWidthHeight(double width, double height)
    {
        super.setWidthHeight(0, 0);
        _size = width;
    }

    @Override
    public void pulse(double deltaSeconds) {
        _elapsedSeconds += deltaSeconds;
        if (_elapsedSeconds > _secondsPerParticle)
        {
            _elapsedSeconds = 0.0;
            double halfSize = _size / 2.0;
            double x = (getLocationX() - halfSize) + _rand.nextDouble() * _size;
            double y = (getLocationY() - halfSize) + _rand.nextDouble() * _size;
            InefficientParticle particle = new InefficientParticle(x, y, getDepth(), 100, 100, _dirX, _dirY, 1);
            int randColor = _rand.nextInt(100);
            if (randColor < 33)
            {
                particle.setColor(Color.RED);
            }
            else if (randColor < 66)
            {
                particle.setColor(Color.ORANGE);
            }
            else
            {
                particle.setColor(Color.WHITE);
            }
        }
    }
}
