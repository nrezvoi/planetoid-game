package com.rezzo.libgdxjam;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Rezvoi on 19.12.2015.
 */
public abstract class State {

    protected OrthographicCamera cam;
    protected GameStateManager gms;

    public State(GameStateManager gms) {
        this.gms = gms;
        cam = new OrthographicCamera();
    }

    protected abstract void handleInput(float dt);
    public abstract void update(float dt);
    public abstract void render(SpriteBatch batch);

}
