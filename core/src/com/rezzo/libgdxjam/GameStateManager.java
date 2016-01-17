package com.rezzo.libgdxjam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by Rezvoi on 19.12.2015.
 */
public class GameStateManager {

    private Stack<State> states;

    public GameStateManager() {
        states = new Stack<State>();
    }

    public void AddState(State state) {
        states.push(state);
    }

    public void RemoveState(State state) {
        states.pop();
    }

    public void SetState(State state) {
        states.pop();
        states.push(state);
    }

    public void update(float dt) {
        states.peek().update(dt);
    }

    public void render(SpriteBatch batch) {
        states.peek().render(batch);
    }

}
