package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    Sprite sprite;

    public Enemy(float x, float y, Texture texture) {
        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
