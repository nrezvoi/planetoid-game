package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rezzo.libgdxjam.GameStateManager;
import com.rezzo.libgdxjam.LifeIsSpace;
import com.rezzo.libgdxjam.State;
import utils.Assets;

public class Menu extends State {


    private Stage menuStage;

    private Label instructionLabel;

    int state = 0;

    public Menu(GameStateManager gms) {
        super(gms);

        cam.setToOrtho(false, LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT);

        menuStage = new Stage(new FitViewport(LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT));

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Assets.normalFont;

        instructionLabel = new Label("Welcome to Planetoid!", style);
        instructionLabel.setPosition(LifeIsSpace.WIDTH / 2 - instructionLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - instructionLabel.getPrefHeight() / 2 - 125);
        instructionLabel.setAlignment(Align.center);

        Label startLabel = new Label("Press SPACE to continue...", style);
        startLabel.setPosition(LifeIsSpace.WIDTH / 2 - startLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - startLabel.getPrefHeight() / 2 - 175);

        Image arrow = new Image(Assets.titleArrow);
        arrow.setPosition(startLabel.getX() - 8, startLabel.getY() + startLabel.getPrefHeight() / 2 - arrow.getPrefHeight() / 2 + 1);

        Label creditsLabel = new Label("© 2016 STUDIO REZZO\n\nGAME ART BY ALEXANDER REZVOI, PROGRAMMING BY NIKOLAI REZVOI", style);
        creditsLabel.setPosition(LifeIsSpace.WIDTH / 2 - creditsLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - creditsLabel.getPrefHeight() / 2 - 225);
        creditsLabel.setAlignment(Align.center);

        Image bg = new Image(Assets.title);

        Assets.menuMusic.play();
        Assets.menuMusic.setLooping(true);
        Assets.menuMusic.setVolume(0.3f);


        menuStage.addActor(bg);
        menuStage.addActor(startLabel);
        menuStage.addActor(instructionLabel);
        menuStage.addActor(creditsLabel);

    }

    @Override
    protected void handleInput(float dt) {

    }

    @Override
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            state++;

            switch (state) {
                case 1:
                    instructionLabel.setText("Please read instructions...");
                    break;
                case 2:
                    instructionLabel.setText("Collect elements by pressing the correct letter\n\non your keyboard while mark is in the green zone.");
                    break;
                case 3:
                    instructionLabel.setText("Remember to keep you orbit in the «Perfect» zone to catch elements");
                    break;
                case 4:
                    instructionLabel.setText("Press X to push your planet. With the arrow keys\n\nyou can choose which direction to push");
                    break;
                case 5:
                    instructionLabel.setText("Good luck!");
                    break;
                case 6:
                    Assets.menuMusic.stop();
                    Assets.stageOneMusic.play();
                    Assets.stageOneMusic.setLooping(true);
                    Assets.stageOneMusic.setVolume(0.3f);
                    instructionLabel.setText("GET READY...");

                    menuStage.addAction(Actions.sequence(Actions.fadeOut(4), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            menuStage.addAction(Actions.sequence(Actions.delay(1.75f, Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    gms.SetState(new StageOne(gms));
                                }
                            }))));

                        }
                    })));
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);

        menuStage.act();
        menuStage.draw();

    }
}
