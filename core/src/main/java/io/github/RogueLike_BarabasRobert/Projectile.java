package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {
    float x, y;
    float dx, dy;
    float speed = 10f;
    Texture texture;
    Sprite sprite;

    public Projectile(float x, float y, float dirX, float dirY, Texture texture) {
        this.x = x;
        this.y = y;
        this.dx = dirX * speed;
        this.dy = dirY * speed;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(0.3f, 0.3f);

        // Set rotation based on direction
        float angle = (float)Math.toDegrees(Math.atan2(dirY, dirX));
        sprite.setRotation(angle);
        sprite.setOriginCenter();

        this.sprite.setPosition(x, y);
    }

    public void update(float delta) {
        x += dx * delta;
        y += dy * delta;
        sprite.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public boolean isOffScreen() {
        return x < 0 || y < 0 || x > 16 || y > 10;
    }

    public com.badlogic.gdx.math.Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
