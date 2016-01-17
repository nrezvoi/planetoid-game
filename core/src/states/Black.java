package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rezzo.libgdxjam.GameStateManager;
import com.rezzo.libgdxjam.LifeIsSpace;
import com.rezzo.libgdxjam.State;
import utils.Assets;

/**
 * Created by Rezvoi on 16.01.2016.
 */
public class Black extends State {


    private Stage blackStage;

    public Black(final GameStateManager gms) {
        super(gms);

        blackStage = new Stage(new FitViewport(LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT));

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Assets.normalFont;

        Label info = new Label("Congratulations!\n\nNow you have everything to bring some life on this little planet!", style);
        info.setPosition(LifeIsSpace.WIDTH / 2 - info.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - info.getPrefHeight() / 2);
        info.setAlignment(Align.center);

        blackStage.addActor(info);

        blackStage.addAction(Actions.fadeOut(0));
        blackStage.addAction(Actions.sequence(Actions.delay(3), Actions.fadeIn(2), Actions.run(new Runnable() {
            @Override
            public void run() {
                Assets.stageTwoMusic.play();
                Assets.stageTwoMusic.setLooping(true);
                Assets.stageTwoMusic.setVolume(0.3f);
            }
        }), Actions.delay(5), Actions.fadeOut(3), Actions.run(new Runnable() {
            @Override
            public void run() {
                gms.SetState(new StageTwo(gms));
            }
        })));
    }

    @Override
    protected void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch batch) {
        blackStage.act();
        blackStage.draw();
    }
}
