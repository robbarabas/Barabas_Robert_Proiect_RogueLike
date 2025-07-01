package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Coin {
    Sprite sprite;
    int type;
    public Coin(float x, float y, Texture texture ,int type) {
        sprite = new Sprite(texture);
        this.type=type;
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
