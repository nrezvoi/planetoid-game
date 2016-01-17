package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.rezzo.libgdxjam.GameStateManager;
import com.rezzo.libgdxjam.LifeIsSpace;
import entities.Planet;
import states.Black;
import states.StageOne;

/**
 * Created by Rezvoi on 22.12.2015.
 */
public class StageOneUI {

    private Stage uiStage;


    private final StageOne stageOne;

    private Image habitableArrow;

    float arrowMin;
    float arrowMax;

    boolean stageOver = false;

    float idealOrbit = MathUtils.random(200, 400);

    public static float orbitProgress;

    float dX = 0;
    float dY = 0;

    OrthographicCamera zoomCam;

    public static int currentCold = 0, currentGrav = 0, currentHeat = 0, currentRock = 0;

    public static Color orbitColor;

    private Label distanceInfo, coldTinyLabel, heatTinyLabel, gravTinyLabel, rockTinyLabel, stageCompleteLabel;

    public StageOneUI(final StageOne stageOne) {

        this.stageOne = stageOne;

        zoomCam = new OrthographicCamera();
        zoomCam.setToOrtho(false, LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT);

        uiStage = new Stage(new FitViewport(LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT));

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = Assets.normalFont;

        Label infoText = new Label("Use ARROW KEYS to move around the planet", style);
        infoText.setPosition(LifeIsSpace.WIDTH / 2 - infoText.getPrefWidth() / 2, 24);
        Label infoText2 = new Label("Press X to move orbit", style);
        infoText2.setPosition(LifeIsSpace.WIDTH / 2 - infoText2.getPrefWidth() / 2, 13);

        //Habitable zone

        /*Label habitableZoneText = new Label("Habitable zone", style);
        habitableZoneText.setPosition(12, LifeIsSpace.HEIGHT - 18);

        Image successBarImg = new Image(Assets.successBar);
        successBarImg.setPosition(12, LifeIsSpace.HEIGHT - 31);

        arrowMin = successBarImg.getX() + 2f;
        arrowMax = successBarImg.getX() + successBarImg.getPrefWidth() - 2f;

        habitableArrow = new Image(Assets.hudArrow);
        habitableArrow.setPosition(arrowMin - habitableArrow.getPrefWidth() / 2, successBarImg.getY() - habitableArrow.getHeight());*/

        //-----

        //Components

        Label componentsLabel = new Label("Components", style);
        componentsLabel.setPosition(12, LifeIsSpace.HEIGHT - 18);

        Image coldTiny = new Image(Assets.e_cold_tiny);
        coldTiny.setPosition(12, componentsLabel.getY() - 14.5f);
        coldTinyLabel = new Label(String.format("%d/7", currentCold), style);
        coldTinyLabel.setPosition(21, coldTiny.getY() - 2f);

        Image heatTiny = new Image(Assets.e_heat_tiny);
        heatTiny.setPosition(12, coldTiny.getY() - 14.5f);
        heatTinyLabel = new Label(String.format("%d/7", currentHeat), style);
        heatTinyLabel.setPosition(21, heatTiny.getY() - 2f);

        Image gravTiny = new Image(Assets.e_grav_tiny);
        gravTiny.setPosition(12, heatTiny.getY() - 14.5f);
        gravTinyLabel = new Label(String.format("%d/7", currentGrav), style);
        gravTinyLabel.setPosition(21, gravTiny.getY() - 2f);

        Image rockTiny = new Image(Assets.e_rock_tiny);
        rockTiny.setPosition(12, gravTiny.getY() - 14.5f);
        rockTinyLabel = new Label(String.format("%d/7", currentRock), style);
        rockTinyLabel.setPosition(21, rockTiny.getY() - 2f);

        //-----

        stageCompleteLabel = new Label("Great work!\n\nIt's time to land now!", style);
        stageCompleteLabel.setPosition(LifeIsSpace.WIDTH / 2 - stageCompleteLabel.getPrefWidth() / 2, LifeIsSpace.HEIGHT / 2 - stageCompleteLabel.getPrefHeight() / 2 + 125);
        stageCompleteLabel.setAlignment(Align.center);
        stageCompleteLabel.setVisible(false);

        distanceInfo = new Label("", style);

        //uiStage.addActor(infoText);
        //uiStage.addActor(infoText2);
        //uiStage.addActor(habitableZoneText);
        //uiStage.addActor(successBarImg);
        //uiStage.addActor(habitableArrow);
        uiStage.addActor(distanceInfo);
        uiStage.addActor(componentsLabel);
        uiStage.addActor(coldTiny);
        uiStage.addActor(coldTinyLabel);
        uiStage.addActor(heatTiny);
        uiStage.addActor(heatTinyLabel);
        uiStage.addActor(gravTiny);
        uiStage.addActor(gravTinyLabel);
        uiStage.addActor(rockTiny);
        uiStage.addActor(rockTinyLabel);
        uiStage.addActor(stageCompleteLabel);

        orbitColor = Color.YELLOW;
    }

    private void updateArrow() {

        float currentOrbitWidth = stageOne.getOrbitWidth();
        float currentOrbitHeight = stageOne.getOrbitHeight();

        float differenceWidth = idealOrbit - (Math.abs(idealOrbit - currentOrbitWidth));
        float differenceHeight = idealOrbit - (Math.abs(idealOrbit - currentOrbitHeight));

        /*float currentOrbitExX = stageOne.getOrbit().x;
        float currentOrbitExY = stageOne.getOrbit().y;

        float differenceExX = exX - (Math.abs(exX - currentOrbitExX));
        float differenceExY = exY - (Math.abs(exY - currentOrbitExY));*/

        float orbitSizeProgress = ((differenceWidth + differenceHeight) / 2) / idealOrbit;

        /*float exXprogress = differenceExX / exX;
        float exYprogress = differenceExY / exY;*/

        float progress = orbitSizeProgress;
        orbitProgress = progress;

        if (progress > 0.95f) {
            orbitColor = Color.GREEN;
            distanceInfo.setText("Perfect!");
            distanceInfo.setPosition(dX - distanceInfo.getPrefWidth() / 2, dY - distanceInfo.getPrefHeight() / 2);
        }

        /*
        habitableArrow.setPosition((arrowMax * progress) - habitableArrow.getPrefWidth() / 2, habitableArrow.getY());
        if (habitableArrow.getX() - habitableArrow.getPrefWidth() / 2 < arrowMin)
            habitableArrow.setPosition(arrowMin - habitableArrow.getPrefWidth() / 2, habitableArrow.getY());
        if (habitableArrow.getX() - habitableArrow.getPrefWidth() / 2 > arrowMax)
            habitableArrow.setPosition(arrowMax, habitableArrow.getY());*/

    }

    private void updateDistanceInfo() {

        // LABEL POSITION
        float angle = -180;

        float x = stageOne.getPlanet().getPlanetSprite().getX() + stageOne.getPlanet().getPlanetSprite().getWidth() / 2;
        float y = stageOne.getPlanet().getPlanetSprite().getY() + stageOne.getPlanet().getPlanetSprite().getHeight() / 2;
        dX = x + MathUtils.cosDeg(angle);
        dY = y + MathUtils.sinDeg(angle) - 27;

        //----

        float sunCenterX = stageOne.getSun().getX() + stageOne.getSun().getWidth() / 2;
        float sunCenterY = stageOne.getSun().getY() + stageOne.getSun().getHeight() / 2;

        float diffX = x - sunCenterX;
        float diffY = y - sunCenterY;

        float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);

        if (distance < 55) {
            orbitColor = Color.RED;
            distanceInfo.setText("Too hot!");
        } else if (distance > 55 && distance < 200) {
            orbitColor = Color.YELLOW;
            distanceInfo.setText("");
        } else if (distance > 300 && distance < 500) {
            orbitColor = Color.BLUE;
            distanceInfo.setText("Too Far!");
        }

        distanceInfo.setPosition(dX - distanceInfo.getPrefWidth() / 2, dY - distanceInfo.getPrefHeight() / 2);

    }

    public void endStage(OrthographicCamera camera, Planet planet, GameStateManager gms) {
        if (stageOver) {
            if (camera.zoom > 0.3f) {
                stageCompleteLabel.setVisible(true);
                camera.position.x += (planet.getHitbox().x - camera.position.x) * Gdx.graphics.getDeltaTime();
                camera.position.y += (planet.getHitbox().y - camera.position.y) * Gdx.graphics.getDeltaTime();
                camera.zoom -= 0.25f * Gdx.graphics.getDeltaTime();
                if (Assets.stageOneMusic.getVolume() > 0.0001f)
                    Assets.stageOneMusic.setVolume(Assets.stageOneMusic.getVolume() - 0.08f * Gdx.graphics.getDeltaTime());
                else
                    Assets.stageOneMusic.setVolume(0);
                camera.update();
            } else {
                Assets.stageOneMusic.stop();
                gms.SetState(new Black(gms));
            }


        }
    }

    public void draw(SpriteBatch batch) {

        updateDistanceInfo();
        updateArrow();
        updateData();

        uiStage.act();
        uiStage.draw();
    }

    private void updateData() {

        if (currentHeat > 7) currentHeat = 7;
        if (currentGrav > 7) currentGrav = 7;
        if (currentCold > 7) currentCold = 7;
        if (currentRock > 7) currentRock = 7;

        coldTinyLabel.setText(String.format("%d/7", currentCold));
        heatTinyLabel.setText(String.format("%d/7", currentHeat));
        gravTinyLabel.setText(String.format("%d/7", currentGrav));
        rockTinyLabel.setText(String.format("%d/7", currentRock));

        if (currentHeat == 7 && currentRock == 7 && currentGrav == 7 && currentCold == 7)
            stageOver = true;
    }

}
