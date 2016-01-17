package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.rezzo.libgdxjam.LifeIsSpace;
import states.StageOne;
import utils.Assets;
import utils.StageOneUI;

/**
 * Created by Rezvoi on 25.12.2015.
 */
public class Element {

    private final StageOne stageOne;

    public enum ElementType {COLD, HEAT, GRAV, ROCK}

    private float x, y;
    private ElementType type;
    private char[] chars;
    private String elementChar;

    private Sprite elementSprite, barSprite, markSprite;

    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private Rectangle uiBarRed, uiBarGreen;

    private float minMarkX, maxMarkX;

    private float dX = 0;
    private boolean right = true;
    public boolean destroy = false;

    private int correctBtn = 0;

    public Circle hitbox;

    private float targetX, targetY, speed;

    private float angleToTarget = 0;

    private float markSpeed;

    public Element(final StageOne stageOne, int el_type, float x, float y, float speed, float markSpeed) {

        this.stageOne = stageOne;
        this.speed = speed;
        this.x = x;
        this.y = y;
        this.markSpeed = markSpeed;

        if (x == 1)
            x = -5;
        else if (x == 2)
            x = LifeIsSpace.WIDTH + 5;

        switch (el_type) {
            case 1:
                this.type = ElementType.COLD;
                break;
            case 2:
                this.type = ElementType.HEAT;
                break;
            case 3:
                this.type = ElementType.GRAV;
                break;
            case 4:
                this.type = ElementType.ROCK;
                break;
        }

        targetX = stageOne.getSun().getX() + stageOne.getSun().getWidth() / 2;
        targetY = stageOne.getSun().getY() + stageOne.getSun().getHeight() / 2;

        chars = new char[]{'A', 'B', 'Q', 'W', 'E', 'R', 'S', 'D', 'F'};
        font = Assets.normalFont;
        setElement();
        barSprite = new Sprite(Assets.small_bar);
        barSprite.setPosition(elementSprite.getX() + elementSprite.getWidth() / 2 - barSprite.getWidth() / 2, elementSprite.getY() - 9);

        uiBarRed = new Rectangle(barSprite.getX(), barSprite.getY(), 21, 1f);
        uiBarRed.setPosition(barSprite.getX() + barSprite.getWidth() / 2 - uiBarRed.getWidth() / 2,
                barSprite.getY() + barSprite.getHeight() / 2 - uiBarRed.getHeight() / 2);

        uiBarGreen = new Rectangle(barSprite.getX(), barSprite.getY(), 21 / 3f, 1f);
        uiBarGreen.setPosition(barSprite.getX() + barSprite.getWidth() / 2 - uiBarGreen.getWidth() / 2,
                barSprite.getY() + barSprite.getHeight() / 2 - uiBarGreen.getHeight() / 2);

        markSprite = new Sprite(Assets.bar_mark);

        minMarkX = -10;
        maxMarkX = 10;

        markSprite.setPosition(0,
                barSprite.getY() + barSprite.getHeight() / 2 - markSprite.getHeight() / 2);

        glyphLayout = new GlyphLayout();

        glyphLayout.setText(font, elementChar);

        hitbox = new Circle(x, y, 8);

        calculateAngleToTarget();

    }


    public void update(float dt) {

        float dX = MathUtils.cos(angleToTarget);
        float dY = MathUtils.sin(angleToTarget);

        hitbox.x -= dX * dt * speed;
        hitbox.y -= dY * dt * speed;

        elementSprite.setPosition(hitbox.x - elementSprite.getWidth() / 2, hitbox.y - elementSprite.getHeight() / 2);

        barSprite.setPosition(elementSprite.getX() + elementSprite.getWidth() / 2 - barSprite.getWidth() / 2, elementSprite.getY() - 9);
        uiBarRed.setPosition(barSprite.getX() + barSprite.getWidth() / 2 - uiBarRed.getWidth() / 2,
                barSprite.getY() + barSprite.getHeight() / 2 - uiBarRed.getHeight() / 2);
        uiBarGreen.setPosition(barSprite.getX() + barSprite.getWidth() / 2 - uiBarGreen.getWidth() / 2,
                barSprite.getY() + barSprite.getHeight() / 2 - uiBarGreen.getHeight() / 2);

        if (hitbox.overlaps(stageOne.getSunHitbox())) {
            destroy = true;
        }

        updateMark();
        checkForBtnPress();


    }

    public void draw(ShapeRenderer sr, SpriteBatch batch) {
        batch.begin();
        elementSprite.draw(batch);
        barSprite.draw(batch);
        font.draw(batch, glyphLayout, elementSprite.getX() + elementSprite.getWidth() / 2 - glyphLayout.width / 2, elementSprite.getY() + 24);
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.RED);
        sr.rect(uiBarRed.getX(), uiBarRed.getY(), uiBarRed.getWidth(), uiBarRed.getHeight());
        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.GREEN);
        sr.rect(uiBarGreen.getX(), uiBarGreen.getY(), uiBarGreen.getWidth(), uiBarGreen.getHeight());
        sr.end();

        /*sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        sr.circle(hitbox.x, hitbox.y, hitbox.radius);
        sr.end();*/

        batch.begin();
        markSprite.draw(batch);
        batch.end();
    }

    private void calculateAngleToTarget() {
        float dX = hitbox.x - targetX;
        float dY = hitbox.y - targetY;

        angleToTarget = MathUtils.atan2(dY, dX);
    }

    private void setElement() {
        switch (type) {
            case HEAT:
                elementSprite = new Sprite(Assets.e_heat);
                break;
            case COLD:
                elementSprite = new Sprite(Assets.e_cold);
                break;
            case GRAV:
                elementSprite = new Sprite(Assets.e_grav);
                break;
            case ROCK:
                elementSprite = new Sprite(Assets.e_rock);
                break;
        }

        elementChar = String.valueOf(chars[MathUtils.random(0, chars.length - 1)]);

        switch (elementChar) {
            case "A":
                correctBtn = Input.Keys.A;
                break;
            case "B":
                correctBtn = Input.Keys.B;
                break;
            case "Q":
                correctBtn = Input.Keys.Q;
                break;
            case "W":
                correctBtn = Input.Keys.W;
                break;
            case "E":
                correctBtn = Input.Keys.E;
                break;
            case "R":
                correctBtn = Input.Keys.R;
                break;
            case "S":
                correctBtn = Input.Keys.S;
                break;
            case "D":
                correctBtn = Input.Keys.D;
                break;
            case "F":
                correctBtn = Input.Keys.F;
                break;
        }

    }

    private void updateMark() {

        float posX = barSprite.getX() + barSprite.getWidth() / 2 - markSprite.getWidth() + dX;

        if (right) {
            dX += markSpeed * Gdx.graphics.getDeltaTime();
            if (dX > maxMarkX)
                right = false;
        } else {
            dX -= markSpeed * Gdx.graphics.getDeltaTime();
            if (dX < minMarkX)
                right = true;
        }

        markSprite.setPosition(posX, barSprite.getY() + barSprite.getHeight() / 2 - markSprite.getHeight() / 2);
    }

    private void checkForBtnPress() {

        boolean rightZone = false;

        if (uiBarGreen.contains(markSprite.getX() + markSprite.getWidth() / 2, markSprite.getY() + markSprite.getHeight() / 2)) {
            rightZone = true;
        } else {
            rightZone = false;
        }

        if (Gdx.input.isKeyJustPressed(correctBtn) && rightZone && StageOneUI.orbitProgress > 0.95f) {
            switch (type) {
                case COLD:
                    Assets.catched.play(0.2f);
                    StageOneUI.currentCold++;
                    break;
                case HEAT:
                    Assets.catched.play(0.2f);
                    StageOneUI.currentHeat++;
                    break;
                case GRAV:
                    Assets.catched.play(0.2f);
                    StageOneUI.currentGrav++;
                    break;
                case ROCK:
                    Assets.catched.play(0.2f);
                    StageOneUI.currentRock++;
                    break;
            }
            destroy = true;
        } else if ((Gdx.input.isKeyJustPressed(correctBtn) && !rightZone)) {
            destroy = true;
        }
    }


    public Circle getHitbox() {
        return hitbox;
    }

    public ElementType getType() {
        return type;
    }

    public void setElementChar(String elementChar) {
        this.elementChar = elementChar;
    }

    public char[] getChars() {
        return chars;
    }

}
