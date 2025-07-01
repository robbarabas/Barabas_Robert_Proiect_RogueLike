package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

/**
 * Represents a ranged enemy that inherits from Enemy and can shoot projectiles
 * towards the protagonist when within range and cooldown allows.
 */
public class EnemyRanged extends Enemy {
    /** Cooldown time between shots (in seconds). */
    float shootCooldown = 1.5f;

    /** Time elapsed since the last projectile was fired. */
    float timeSinceLastShot = 0f;

    /** List that holds all enemy projectiles currently active. */
    List<Projectile> enemyProjectiles;

    /** Texture used for the projectiles fired by this enemy. */
    Texture projectileTexture;

    /**
     * Constructs a ranged enemy at the specified position with given textures and target.
     *
     * @param x Initial X position.
     * @param y Initial Y position.
     * @param texture Texture for the enemy body.
     * @param faceTexture Texture for the enemy face overlay.
     * @param target The protagonist this enemy targets.
     * @param projectileTexture Texture for the enemy's projectiles.
     * @param enemyProjectiles Reference to the list managing active enemy projectiles.
     */
    public EnemyRanged(float x, float y, Texture texture, Texture faceTexture, Protagonist target,
                       Texture projectileTexture, List<Projectile> enemyProjectiles) {
        super(x, y, texture, faceTexture, target);
        this.projectileTexture = projectileTexture;
        this.enemyProjectiles = enemyProjectiles;
        this.health = 6;  // Ranged enemies have more health
        setSpeed(1);     // Slower movement speed than base Enemy
    }

    /**
     * Updates the ranged enemy behavior including movement and shooting projectiles.
     *
     * @param delta Time elapsed since last frame.
     * @param walls List of walls to consider for collision.
     */
    @Override
    public void update(float delta, List<Wall> walls) {
        super.update(delta, walls);

        timeSinceLastShot += delta;

        float dx = target.getX() - getX();
        float dy = target.getY() - getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        // Shoot projectile towards the protagonist if within range and cooldown elapsed
        if (dist < 6f && timeSinceLastShot >= shootCooldown) {
            dx /= dist;
            dy /= dist;

            enemyProjectiles.add(new Projectile(getX(), getY(), dx, dy, projectileTexture, 5f));
            timeSinceLastShot = 0f;
        }
    }

    /**
     * Renders the ranged enemy by drawing it using the given SpriteBatch.
     *
     * @param batch SpriteBatch used for rendering.
     */
    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
