package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Wall {
    Rectangle bounds;

    public Wall(float x, float y, float width, float height) {
        bounds = new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }
}

