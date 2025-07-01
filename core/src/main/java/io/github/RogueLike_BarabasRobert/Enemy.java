package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

/**
 * Represents an enemy character in the game that moves towards the protagonist,
 * can take damage, applies knockback, and interacts with walls and the player.
 */
public class Enemy {
    /** Sprite representing the enemy's body. */
    Sprite sprite;

    /** Sprite representing the enemy's face overlay. */
    Sprite face;

    /** The protagonist that this enemy targets. */
    Protagonist target;

    /** Movement speed of the enemy. */
    float speed = 2.0f;

    /** Cooldown duration between damaging the protagonist. */
    float damageCooldown = 1f;

    /** Time elapsed since last damaging hit on the protagonist. */
    float timeSinceLastHit = 0f;

    /** Current health points of the enemy. */
    float health = 4;

    /** Timer for wobble animation effect while moving. */
    float wobbleTime = 0f;

    /** Knockback velocity components on X and Y axes. */
    float knockbackX = 0;
    float knockbackY = 0;

    /** Remaining time of knockback effect. */
    float knockbackTime = 0f;

    /** Duration for knockback effect after being hit. */
    float knockbackDuration = 0.2f;

    /** Timer for hit visual effect duration. */
    float hitEffectTime = 0f;

    /** Duration for hit effect visual. */
    float hitEffectDuration = 0.15f;

    /**
     * Constructs an Enemy at a specified position with given textures for body and face,
     * targeting a specific protagonist.
     *
     * @param x Initial X position.
     * @param y Initial Y position.
     * @param texture Texture for the enemy's body sprite.
     * @param face_texture Texture for the enemy's face sprite.
     * @param target The protagonist that this enemy will pursue.
     */
    public Enemy(float x, float y, Texture texture, Texture face_texture, Protagonist target) {
        this.target = target;

        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setPosition(x, y);

        face = new Sprite(face_texture);
        face.setSize(1, 1);
        face.setPosition(x, y);
    }

    /**
     * Updates the enemy's state every frame:
     * - Applies knockback movement if active.
     * - Moves towards the protagonist avoiding walls.
     * - Applies wobble animation while moving.
     * - Handles damage effect visuals.
     * - Damages the protagonist if in contact and cooldown elapsed.
     *
     * @param delta Time elapsed since last frame.
     * @param walls List of walls to check for collision.
     */
    public void update(float delta, List<Wall> walls) {
        timeSinceLastHit += delta;

        if (knockbackTime > 0) {
            // Knockback movement logic
            float newX = sprite.getX() + knockbackX * delta;
            float newY = sprite.getY() + knockbackY * delta;
            Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());

            boolean collides = false;
            for (Wall wall : walls) {
                if (futureBounds.overlaps(wall.getBounds())) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                sprite.setPosition(newX, newY);
            } else {
                pushOutFromWalls(walls);
            }
            knockbackTime -= delta;

        } else {
            // Normal movement towards protagonist with wall avoidance
            float targetX = target.getX();
            float targetY = target.getY();
            float dx = targetX - sprite.getX();
            float dy = targetY - sprite.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist > 0.1f) {
                dx /= dist;
                dy /= dist;

                float newX = sprite.getX() + dx * speed * delta;
                float newY = sprite.getY() + dy * speed * delta;
                Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());

                boolean collides = false;
                for (Wall wall : walls) {
                    if (futureBounds.overlaps(wall.getBounds())) {
                        collides = true;
                        break;
                    }
                }

                if (!collides) {
                    sprite.setPosition(newX, newY);
                } else {
                    // Try alternative axis-aligned movements to bypass walls
                    float[] offsetsX = { dx, 0, dx, -dx, 0 };
                    float[] offsetsY = { 0, dy, -dy, dy, -dy };

                    for (int i = 0; i < offsetsX.length; i++) {
                        newX = sprite.getX() + offsetsX[i] * speed * 2 * delta;
                        newY = sprite.getY() + offsetsY[i] * speed * 2 * delta;
                        futureBounds.setPosition(newX, newY);

                        boolean altCollides = false;
                        for (Wall wall : walls) {
                            if (futureBounds.overlaps(wall.getBounds())) {
                                altCollides = true;
                                break;
                            }
                        }

                        if (!altCollides) {
                            sprite.setPosition(newX, newY);
                            break;
                        }
                    }
                }
            }

            wobbleTime += delta * 10f;
            float scaleY = 1f + (float) Math.sin(wobbleTime) * 0.1f;
            float scaleX = 1f - (float) Math.sin(wobbleTime) * 0.2f;
            sprite.setOriginCenter();
            sprite.setScale(scaleX, scaleY);

            if (dist > 0.3) {
                dx /= dist;
                dy /= dist;
                float newX = sprite.getX() + dx * speed * delta;
                float newY = sprite.getY() + dy * speed * delta;
                Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());

                boolean collides = false;
                for (Wall wall : walls) {
                    if (futureBounds.overlaps(wall.getBounds())) {
                        collides = true;
                        break;
                    }
                }

                if (!collides) {
                    sprite.setPosition(newX, newY);
                }
            }
        }

        face.setPosition(sprite.getX(), sprite.getY());

        if (hitEffectTime > 0) {
            hitEffectTime -= delta;
            if (hitEffectTime <= 0) {
                sprite.setSize(1f, 1f);
                face.setSize(1f, 1f);
            }
        }

        // Damage the protagonist if overlapping and cooldown elapsed
        if (sprite.getBoundingRectangle().overlaps(target.getBounds())) {
            if (timeSinceLastHit >= damageCooldown) {
                target.takeDamage(1);
                timeSinceLastHit = 0f;
            }
        }
    }

    /**
     * Applies damage to the enemy, triggers knockback and visual effects.
     *
     * @param amount The amount of damage to inflict.
     */
    public void takeDamage(float amount) {
        health -= amount;
        System.out.println("Enemy hit! Health: " + health);

        // Enlarge sprite for hit effect
        sprite.setSize(1.2f, 1.2f);
        face.setSize(1.2f, 1.2f);
        hitEffectTime = hitEffectDuration;

        // Calculate knockback vector away from protagonist
        float dx = sprite.getX() - target.getX();
        float dy = sprite.getY() - target.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist != 0) {
            dx /= dist;
            dy /= dist;
            float knockbackForce = 5f;
            knockbackX = dx * knockbackForce;
            knockbackY = dy * knockbackForce;
            knockbackTime = knockbackDuration;
        }
    }

    /**
     * Draws the enemy and its face sprite using the provided batch.
     *
     * @param batch SpriteBatch used for drawing.
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        face.draw(batch);
    }

    /**
     * Returns the bounding rectangle of the enemy sprite.
     *
     * @return Rectangle representing the enemy's bounds.
     */
    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    /**
     * Moves the enemy and face by a specified offset.
     *
     * @param dx Movement offset on the X axis.
     * @param dy Movement offset on the Y axis.
     */
    public void moveBy(float dx, float dy) {
        sprite.translate(dx, dy);
        face.setPosition(sprite.getX(), sprite.getY());
    }

    /**
     * Returns the current X position of the enemy.
     *
     * @return The X coordinate.
     */
    public float getX() {
        return sprite.getX();
    }

    /**
     * Returns the current Y position of the enemy.
     *
     * @return The Y coordinate.
     */
    public float getY() {
        return sprite.getY();
    }

    /**
     * Pushes the enemy out from overlapping walls by the minimal overlap distance.
     *
     * @param walls List of walls to resolve collision against.
     */
    public void pushOutFromWalls(List<Wall> walls) {
        Rectangle bounds = getBounds();

        for (Wall wall : walls) {
            Rectangle wallBounds = wall.getBounds();

            if (bounds.overlaps(wallBounds)) {
                float overlapLeft = bounds.x + bounds.width - wallBounds.x;
                float overlapRight = wallBounds.x + wallBounds.width - bounds.x;
                float overlapTop = wallBounds.y + wallBounds.height - bounds.y;
                float overlapBottom = bounds.y + bounds.height - wallBounds.y;

                float minOverlap = Math.min(
                    Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom)
                );

                if (minOverlap == overlapLeft) {
                    moveBy(-overlapLeft - 0.5f, 0);
                } else if (minOverlap == overlapRight) {
                    moveBy(overlapRight, 0);
                } else if (minOverlap == overlapTop) {
                    moveBy(0, overlapTop + 0.5f);
                } else if (minOverlap == overlapBottom) {
                    moveBy(0, -overlapBottom - 0.5f);
                }

                bounds = getBounds();
            }
        }
    }

    /**
     * Gets the enemy's movement speed.
     *
     * @return The speed value.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the enemy's movement speed.
     *
     * @param speed New speed value.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
