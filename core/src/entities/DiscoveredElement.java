package entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Rezvoi on 08.01.2016.
 */
public class DiscoveredElement {

    private Sprite sprite;
    private String name;

    public DiscoveredElement(Sprite sprite, String name) {
        this.sprite = sprite;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setPosition(Vector2 pos) {
        sprite.setPosition(pos.x - sprite.getWidth() / 2, pos.y - sprite.getHeight() / 2);
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

}
