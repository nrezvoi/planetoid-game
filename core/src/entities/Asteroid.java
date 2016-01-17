package entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.rezzo.libgdxjam.LifeIsSpace;
import states.StageOne;
import utils.Assets;

/**
 * Created by Rezvoi on 23.12.2015.
 */
public class Asteroid {

    private final StageOne stageOne;

    private float x, y, rotationSpeed;
    private int rotationDir;

    private Sprite asteroidSprite;

    private Circle hitbox;

    private float targetX, targetY;
    private float angleToTarget = 0, angle = 0;
    private float speed;

    public Asteroid(final StageOne stageOne, float x, float y, float rotationSpeed, int rotationDir, int speed) {

        this.stageOne = stageOne;
        this.x = x;
        this.y = y;
        this.rotationSpeed = rotationSpeed;
        this.rotationDir = rotationDir;
        this.speed = speed;

        if (x == 1)
            x = -5;
        else if (x == 2)
            x = LifeIsSpace.WIDTH + 5;

        targetX = stageOne.getSun().getX() + stageOne.getSun().getWidth() / 2;
        targetY = stageOne.getSun().getY() + stageOne.getSun().getHeight() / 2;

        asteroidSprite = new Sprite(Assets.asteroid);
        asteroidSprite.setPosition(x - asteroidSprite.getWidth() / 2, y - asteroidSprite.getHeight() / 2);
        asteroidSprite.setOriginCenter();

        hitbox = new Circle(asteroidSprite.getX() + asteroidSprite.getWidth() / 2, asteroidSprite.getY() + asteroidSprite.getHeight() / 2, 6);

        calculateAngleToTarget();

    }

    private void calculateAngleToTarget() {
        float dX = hitbox.x - targetX;
        float dY = hitbox.y - targetY;

        angleToTarget = MathUtils.atan2(dY, dX);
    }

    public void update(float dt) {

        float dX = MathUtils.cos(angleToTarget);
        float dY = MathUtils.sin(angleToTarget);

        hitbox.x -= dX * dt * speed;
        hitbox.y -= dY * dt * speed;


        if (rotationDir == 1)
            angle += rotationSpeed * dt;
        else
            angle -= rotationSpeed * dt;

        asteroidSprite.setRotation(angle);
        asteroidSprite.setPosition(hitbox.x - asteroidSprite.getWidth() / 2, hitbox.y - asteroidSprite.getHeight() / 2);

    }

    public void draw(SpriteBatch batch) {
        asteroidSprite.draw(batch);
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public boolean collisionDetectionSun() {
        if (hitbox.overlaps(stageOne.getSunHitbox()))
            return true;
        return false;
    }

    public boolean collisionDetectionPlanet() {
        if (hitbox.overlaps(stageOne.getPlanet().getHitbox()))
            return true;
        return false;
    }

    public float getAngleToTarget() {
        return angleToTarget;
    }
}
