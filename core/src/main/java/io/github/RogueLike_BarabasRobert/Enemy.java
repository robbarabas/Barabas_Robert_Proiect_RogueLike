package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    Sprite sprite;
    Sprite face;
    Protagonist target;
    float speed = 3.5f;
    float damageCooldown = 1f; // seconds
    float timeSinceLastHit = 0f;

    public Enemy(float x, float y, Texture texture, Texture face_texture, Protagonist target) {
        this.target = target;

        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setPosition(x, y);

        face = new Sprite(face_texture);
        face.setSize(1, 1);
        face.setPosition(x, y);
    }

    public void update(float delta) {
        // Move toward the protagonist
        float targetX = target.getX();
        float targetY = target.getY();
        float dx = targetX - sprite.getX();
        float dy = targetY - sprite.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist > 0.1f) { // avoid jitter
            dx /= dist;
            dy /= dist;
            sprite.setPosition(sprite.getX() + dx * speed * delta, sprite.getY() + dy * speed * delta);
            face.setPosition(sprite.getX(), sprite.getY());
        }

        // Handle collision and damage
        timeSinceLastHit += delta;
        if (sprite.getBoundingRectangle().overlaps(target.getBounds())) {
            if (timeSinceLastHit >= damageCooldown) {
                target.takeDamage(1); // reduce health
                timeSinceLastHit = 0f;
            }
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        face.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
