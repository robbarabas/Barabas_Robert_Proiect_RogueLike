package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a projectile fired by the player or enemies.
 *
 * Handles movement, rendering, bounding box for collision,
 * and lifespan (automatic removal after a timeout or on hit).
 */
public class Projectile {
    /** Current X position of the projectile in game world units. */
    float x, y;

    /** Velocity components for X and Y directions, computed from direction and speed. */
    float dx, dy;

    /** Movement speed of the projectile in units per second. */
    float speed = 10f;

    /** Time elapsed since the projectile was created, in seconds. */
    float lifeTime = 0f;

    /** Maximum allowed lifetime before the projectile is removed automatically, in seconds. */
    float maxLifeTime = 3f;

    /** Flag indicating whether the projectile has hit a target and should be removed. */
    boolean hasHit = false;

    /** Texture used to display the projectile. */
    Texture texture;

    /** Sprite representing the projectile with position, rotation, and size. */
    Sprite sprite;
    private float width;
    private float height;
    /**
     * Creates a new projectile with a given position, direction, texture, and speed.
     * The direction vector is normalized internally.
     *
     * @param x        Initial X position of the projectile.
     * @param y        Initial Y position of the projectile.
     * @param dirX     X component of the direction vector.
     * @param dirY     Y component of the direction vector.
     * @param texture  Texture used to draw the projectile.
     * @param speed    Movement speed in units per second.
     */
    public Projectile(float x, float y, float dirX, float dirY, Texture texture, float speed,float WorldHeight ,float WorldWidth ) {
        this.x = x;
        this.y = y;
        float length = (float)Math.sqrt(dirX * dirX + dirY * dirY);
        this.dx = (dirX / length) * speed;
        this.dy = (dirY / length) * speed;
        this.texture = texture;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(0.5f, 0.5f);  // Default size
        this.speed = speed;
        this.width=WorldWidth;
        this.height=WorldHeight;
        // Calculate rotation angle based on direction vector
        float angle = (float) Math.toDegrees(Math.atan2(dirY, dirX));
        sprite.setRotation(angle);
        sprite.setOriginCenter();
        sprite.setPosition(x, y);
    }

    /**
     * Overloaded constructor allowing custom sprite size.
     *
     * @param x        Initial X position of the projectile.
     * @param y        Initial Y position of the projectile.
     * @param dirX     X component of the direction vector.
     * @param dirY     Y component of the direction vector.
     * @param texture  Texture used to draw the projectile.
     * @param speed    Movement speed in units per second.
     * @param size_x   Width of the projectile sprite.
     * @param size_y   Height of the projectile sprite.
     */
    public Projectile(float x, float y, float dirX, float dirY, Texture texture, float speed,float WorldHeight ,float WorldWidth , float size_x, float size_y) {
        this(x, y, dirX, dirY, texture, speed,WorldHeight,WorldWidth);
        this.sprite.setSize(size_x, size_y);
    }

    /**
     * Updates the projectile's position based on velocity and the elapsed time.
     * Also increments the lifeTime counter.
     *
     * @param delta Time elapsed since the last frame, in seconds.
     */
    public void update(float delta) {
        x += dx * delta;
        y += dy * delta;
        sprite.setPosition(x, y);
        lifeTime += delta;
    }

    /**
     * Draws the projectile sprite to the screen using the provided SpriteBatch.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Returns the bounding rectangle of the projectile used for collision detection.
     * The bounding box is adjusted to a fixed size (0.5 x 0.3) regardless of sprite size.
     *
     * @return A Rectangle representing the projectile's collision bounds.
     */
    public Rectangle getBounds() {
        Rectangle bounds = sprite.getBoundingRectangle();
        return bounds.setSize(0.5f, 0.3f);
    }

    /**
     * Checks whether the projectile is outside the visible game area.
     * Coordinates assume the game world boundaries are roughly from (0,0) to (16,10).
     *
     * @return true if the projectile is off-screen, false otherwise.
     */
    public boolean isOffScreen(float width,float height) {
        return x < -1 || y < -1 || x > width+1 || y > height+1;
    }

    /**
     * Determines whether this projectile should be removed from the game,
     * either because it went off screen, hit a target, or expired.
     *
     * @return true if the projectile should be removed, false otherwise.
     */
    public boolean shouldRemove() {
        return isOffScreen(width,height) || hasHit || lifeTime > maxLifeTime;
    }

    /**
     * Marks the projectile as having hit something,
     * so it will be removed on the next update cycle.
     */
    public void onHit() {
        hasHit = true;
    }
}
