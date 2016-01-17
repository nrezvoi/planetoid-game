package entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import utils.Assets;

/**
 * Created by Rezvoi on 19.12.2015.
 */
public class Planet {

    private float x, y;

    private TextureRegion planetText;

    private Sprite planetSprite;

    private Circle hitbox;

    public Planet(float x, float y) {
        this.x = x;
        this.y = y;

        planetText = Assets.planetText;
        planetSprite = new Sprite(planetText);

        hitbox = new Circle(x + planetSprite.getWidth() / 2, y + planetSprite.getHeight() / 2, 11);
    }

    public void update() {
        planetSprite.setPosition(x - planetSprite.getWidth() / 2 , y - planetSprite.getHeight() / 2);
        hitbox.setPosition(planetSprite.getX() + planetSprite.getWidth() / 2, planetSprite.getY() + planetSprite.getHeight() / 2);
    }

    public void draw(SpriteBatch batch) {
        planetSprite.draw(batch);

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Sprite getPlanetSprite() {
        return planetSprite;
    }

    public Circle getHitbox() {
        return hitbox;
    }
}
