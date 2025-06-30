package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {
    float x, y;
    float dx, dy;
    float speed = 10f;
    float lifeTime = 0f;
    float maxLifeTime = 3f; // Optional: remove after 3 seconds
    boolean hasHit = false;

    Texture texture;
    Sprite sprite;

    public Projectile(float x, float y, float dirX, float dirY, Texture texture) {
        this.x = x;
        this.y = y;
        float length = (float)Math.sqrt(dirX * dirX + dirY * dirY);
        this.dx = (dirX / length) * speed;
        this.dy = (dirY / length) * speed;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(0.5f, 0.5f);

        float angle = (float) Math.toDegrees(Math.atan2(dirY, dirX));
        sprite.setRotation(angle);
        sprite.setOriginCenter();
        sprite.setPosition(x, y);
    }

    public Projectile(float x, float y, float dirX, float dirY, Texture texture,float size_x,float size_y)
    {
        this(x,y,dirX,dirY,texture);
        this.sprite.setSize(size_x,size_y);
    }

    public void update(float delta) {
        x += dx * delta;
        y += dy * delta;
        sprite.setPosition(x, y);
        lifeTime += delta;
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public boolean isOffScreen() {

        return x < -1 || y < -1 || x > 17 || y > 11;
    }

    public boolean shouldRemove() {
        return isOffScreen() || hasHit || lifeTime > maxLifeTime;
    }

    public void onHit() {
        hasHit = true;
    }

}
