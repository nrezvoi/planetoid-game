package com.rezzo.libgdxjam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import states.Menu;
import utils.Assets;

public class LifeIsSpace extends ApplicationAdapter {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private SpriteBatch batch;
    private GameStateManager manager;

    @Override
    public void create() {

        Assets.loadAssets();

        batch = new SpriteBatch();
        manager = new GameStateManager();

        //manager.AddState(new StageOne(manager));
        //manager.AddState(new StageTwo(manager));
        manager.AddState(new Menu(manager));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        manager.update(Gdx.graphics.getDeltaTime());
        manager.render(batch);
    }
}
