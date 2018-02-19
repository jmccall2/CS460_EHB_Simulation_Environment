package simulation;

import simulation.engine.RenderEntity;

public class InefficientParticle extends RenderEntity {
    private double _lifespan;

    public InefficientParticle(double x, double y, double depth, double speedX, double speedY, double dirX, double dirY, double lifespan)
    {
        _lifespan = lifespan;
        setLocationXYDepth(x, y, depth);
        if (dirX > 0) dirX = 1;
        else if (dirX < 0) dirX = -1;
        else dirX = 0;
        if (dirY > 0) dirY = 1;
        else if (dirY < 0) dirY = -1;
        else dirY = 0;
        setSpeedXY(speedX * dirX, speedY * dirY);
        setWidthHeight(2, 2);
        addToWorld();
    }

    @Override
    public void pulse(double deltaSeconds) {
        _lifespan -= deltaSeconds;
        if (_lifespan <= 0.0) removeFromWorld();
    }
}