package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.rezzo.libgdxjam.GameStateManager;
import com.rezzo.libgdxjam.LifeIsSpace;
import com.rezzo.libgdxjam.State;
import utils.Assets;
import utils.StageTwoUI;

/**
 * Created by Rezvoi on 07.01.2016.
 */
public class StageTwo extends State {


    StageTwoUI stageTwoUI;

    public StageTwo(GameStateManager gms) {
        super(gms);

        cam.setToOrtho(false, LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT);
        cam.update();

        stageTwoUI = new StageTwoUI(this);
    }

    @Override
    protected void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(Assets.spaceBg, 0, 0);
        batch.end();

        stageTwoUI.draw(batch);

    }
}
