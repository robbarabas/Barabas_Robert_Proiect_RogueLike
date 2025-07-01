package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class EnemyRanged extends Enemy {
    float shootCooldown = 1.5f;
    float timeSinceLastShot = 0f;

    List<Projectile> enemyProjectiles;
    Texture projectileTexture;

    public EnemyRanged(float x, float y, Texture texture, Texture faceTexture, Protagonist target,
                       Texture projectileTexture, List<Projectile> enemyProjectiles) {
        super(x, y, texture, faceTexture, target);
        this.projectileTexture = projectileTexture;
        this.enemyProjectiles = enemyProjectiles;
        this.health=6;
        setSpeed(1);
    }

    @Override
    public void update(float delta, List<Wall> walls) {
        super.update(delta, walls);

        timeSinceLastShot += delta;

        float dx = target.getX() - getX();
        float dy = target.getY() - getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        // Fire projectile if within range and cooldown passed
        if (dist < 6f && timeSinceLastShot >= shootCooldown) {
            dx /= dist;
            dy /= dist;

            enemyProjectiles.add(new Projectile(getX(), getY(), dx, dy, projectileTexture,5f));
            timeSinceLastShot = 0f;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

    }
}
