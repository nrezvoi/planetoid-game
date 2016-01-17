package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import utils.Assets;

/**
 * Created by Rezvoi on 20.12.2015.
 */
public class Creature {

    private float x;
    private float y;

    private float angle = -270;

    public enum MovingState {UP, RIGHT, DOWN, LEFT, NONE}

    public enum CreatureState {PUSH_LEFT, PUSH_RIGHT, PUSH_UP, PUSH_DOWN, IDLE_LEFT, IDLE_RIGHT}

    private MovingState movingState, previousState;
    private CreatureState creatureState, previousCreatureState;

    private float stateTime;
    private float movingSpeed = 3;

    private boolean isMoving = false;

    private TextureRegion hero_push_left, hero_push_right, hero_push_up, hero_push_down;
    private TextureRegion hero_idle_left, hero_idle_right;

    private Sprite arrow;

    public Creature(float x, float y) {
        this.x = x;
        this.y = y;

        movingState = MovingState.UP;
        creatureState = CreatureState.IDLE_RIGHT;
        previousCreatureState = creatureState;

        hero_idle_left = Assets.hero_idle_left;
        hero_idle_right = Assets.hero_idle_right;
        hero_push_left = Assets.hero_push_left;
        hero_push_right = Assets.hero_push_right;
        hero_push_up = Assets.hero_push_up;
        hero_push_down = Assets.hero_push_down;

        arrow = new Sprite(Assets.arrow_right);
        arrow.setOrigin(arrow.getWidth() / 2, arrow.getHeight() / 2);
    }

    public void update(float dt) {

        stateTime += dt;

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !isMoving && previousState != MovingState.UP) {
            Assets.jump.play(0.2f);
            movingState = MovingState.UP;
            stateTime = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !isMoving && previousState != MovingState.RIGHT) {
            Assets.jump.play(0.2f);
            movingState = MovingState.RIGHT;
            creatureState = CreatureState.IDLE_LEFT;
            previousCreatureState = creatureState;
            stateTime = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !isMoving && previousState != MovingState.DOWN) {
            Assets.jump.play(0.2f);
            movingState = MovingState.DOWN;
            stateTime = 0;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !isMoving && previousState != MovingState.LEFT) {
            Assets.jump.play(0.2f);
            stateTime = 0;
            creatureState = CreatureState.IDLE_RIGHT;
            previousCreatureState = creatureState;
            movingState = MovingState.LEFT;
        }

        switch (movingState) {
            case UP:
                isMoving = true;
                angle = moveCreature(angle, -270, movingSpeed);
                break;
            case RIGHT:
                isMoving = true;
                angle = moveCreature(angle, 0, movingSpeed);
                break;
            case DOWN:
                isMoving = true;
                angle = moveCreature(angle, -90, movingSpeed);
                break;
            case LEFT:
                isMoving = true;
                angle = moveCreature(angle, -180, movingSpeed);
                break;
        }

    }

    public void draw(SpriteBatch batch) {

        switch (creatureState) {
            case PUSH_LEFT:
                //Assets.orbit.play(0.2f);
                batch.draw(hero_push_left, x - hero_push_left.getRegionWidth() / 2, y - hero_push_left.getRegionHeight() / 2);
                break;
            case PUSH_RIGHT:
                //Assets.orbit.play(0.2f);
                batch.draw(hero_push_right, x - hero_push_right.getRegionWidth() / 2, y - hero_push_right.getRegionHeight() / 2);
                break;
            case PUSH_UP:
                //Assets.orbit.play(0.2f);
                batch.draw(hero_push_up, x - hero_push_up.getRegionWidth() / 2, y - hero_push_up.getRegionHeight() / 2);
                break;
            case PUSH_DOWN:
                //Assets.orbit.play(0.2f);
                batch.draw(hero_push_down, x - hero_push_down.getRegionWidth() / 2, y - hero_push_down.getRegionHeight() / 2);
                break;
            case IDLE_LEFT:
                batch.draw(hero_idle_left, x - hero_idle_left.getRegionWidth() / 2, y - hero_idle_left.getRegionHeight() / 2);
                break;
            case IDLE_RIGHT:
                batch.draw(hero_idle_right, x - hero_idle_right.getRegionWidth() / 2, y - hero_idle_right.getRegionHeight() / 2);
                break;
        }

        arrow.draw(batch);
    }

    private float moveCreature(float moveFrom, float moveTo, float duration) {

        float progress = MathUtils.clamp(stateTime / duration, 0.0f, 1.0f);

        if (progress > 0.20f) {
            isMoving = false;
            previousState = movingState;
            movingState = MovingState.NONE;
        }

        return MathUtils.lerpAngleDeg(moveFrom, moveTo, progress);

    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public MovingState getPreviousState() {
        return previousState;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setCreatureState(CreatureState creatureState) {
        this.creatureState = creatureState;
    }

    public CreatureState getPreviousCreatureState() {
        return previousCreatureState;
    }

    public Sprite getArrow() {
        return arrow;
    }
}
