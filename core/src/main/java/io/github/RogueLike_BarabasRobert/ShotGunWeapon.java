package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
    public ShotGunWeapon(Texture projectileTexture, Protagonist owner, Camera viewport) {
        super("Shotgun", 2f,viewport); // 2 seconds cooldown between shots
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


        // Starting position of the projectile (near the protagonist)
        float startX = owner.getX() ;
        float startY = owner.getY() ;
        // mouse cords
        Vector3 screenCoords = new Vector3(mouseX, mouseY, 0);
        Vector3 worldCoords = super.viewport.unproject(screenCoords);


        float dirX = worldCoords.x- startX;
        float dirY = worldCoords.y- startY;
        float baseAngle = (float) Math.atan2(dirY, dirX);

        int totalProjectiles = pelletsPerShot * owner.projectile_multiplier;

        for (int i = 0; i < totalProjectiles; i++) {
            // Offset pellets evenly in the spread angle
            float offset = ((float) i / (totalProjectiles - 1)) - 0.5f; // from -0.5 to 0.5
            float angle = baseAngle + (float) Math.toRadians(offset * spreadAngle);

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            owner.getProjectiles().add(new Projectile(startX, startY, dx, dy, projectileTexture, 7f,owner.WorldHeight,owner.WorldWidth));
        }
    }
}
