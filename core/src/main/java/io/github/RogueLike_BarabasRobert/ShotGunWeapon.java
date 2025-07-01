package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Represents a shotgun-style weapon that fires multiple projectiles in a spread.
 *
 * The shotgun shoots several pellets per shot, each with a spread angle around the aim direction.
 * The number of pellets fired scales with the owner's projectile multiplier.
 */
public class ShotGunWeapon extends Weapon {
    /** Texture used for each projectile fired by the shotgun */
    Texture projectileTexture;

    /** The protagonist who owns and uses this weapon */
    Protagonist owner;

    /** Number of pellets fired per shot */
    int pelletsPerShot = 5;

    /** Total spread angle in degrees over which pellets are distributed */
    float spreadAngle = 50f;

    /**
     * Creates a new ShotGunWeapon.
     *
     * @param projectileTexture Texture for the pellets/projectiles.
     * @param owner The protagonist who owns this weapon.
     */
    public ShotGunWeapon(Texture projectileTexture, Protagonist owner) {
        super("Shotgun", 2f); // 2 seconds cooldown between shots
        super.base_power = 1;
        this.owner = owner;
        this.projectileTexture = projectileTexture;
        this.weaponIcon = new Texture("shotgun.png");
    }

    /**
     * Fires the shotgun if cooldown allows.
     *
     * @param owner The protagonist using the weapon.
     */
    @Override
    public void use(Protagonist owner) {
        if (!canUse()) return;
        fireProjectile(owner);
        this.timeSinceLastUse = 0;
    }

    /**
     * Fires multiple projectiles in a spread pattern centered on the cursor's position.
     * The number of pellets is multiplied by the protagonist's projectile multiplier.
     *
     * @param owner The protagonist firing the shotgun.
     */
    private void fireProjectile(Protagonist owner) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        // Convert screen coordinates to world coordinates
        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        // Starting position of the projectile (near the protagonist)
        float startX = owner.getX() + 0.3f;
        float startY = owner.getY() + 0.3f;

        float dirX = worldMouseX - startX;
        float dirY = worldMouseY - startY;
        float baseAngle = (float) Math.atan2(dirY, dirX);

        int totalProjectiles = pelletsPerShot * owner.projectile_multiplier;

        for (int i = 0; i < totalProjectiles; i++) {
            // Offset pellets evenly in the spread angle
            float offset = ((float) i / (totalProjectiles - 1)) - 0.5f; // from -0.5 to 0.5
            float angle = baseAngle + (float) Math.toRadians(offset * spreadAngle);

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            owner.getProjectiles().add(new Projectile(startX, startY, dx, dy, projectileTexture, 7f));
        }
    }
}
