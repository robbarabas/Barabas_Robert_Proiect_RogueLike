package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

/**
 * Represents a gun-type weapon that fires projectiles in bursts.
 *
 * The GunWeapon fires multiple projectiles in rapid succession (a burst),
 * controlled by a cooldown and burst interval timer.
 *
 * It uses the protagonist's projectile multiplier to determine the number of shots per burst.
 */
public class GunWeapon extends Weapon {
    /** The texture used for the projectiles fired by this weapon. */
    Texture projectileTexture;

    /** The protagonist who owns and uses this weapon. */
    Protagonist Owner;

    /** Number of shots fired in each burst, based on the owner's projectile multiplier. */
    int burstCount;

    /** Remaining shots to be fired in the current burst. */
    int shotsRemaining = 0;

    /** Time interval (in seconds) between consecutive shots in a burst. */
    float burstInterval = 0.1f;

    /** Timer tracking time elapsed since the last shot in the current burst. */
    float timeSinceLastBurstShot = 0;

    /** Flag indicating whether the weapon is currently firing a burst. */
    boolean isBursting = false;


    /**
     * Constructs a new GunWeapon with a specified projectile texture and owner.
     * Initializes the weapon with a burst cooldown time.
     *
     * @param projectileTexture The texture used for projectiles fired by this weapon.
     * @param owner The protagonist who owns this weapon.
     */
    public GunWeapon(Texture projectileTexture, Protagonist owner, Camera viewport) {
        super("Burst Gun", 0.6f, viewport); // Sets weapon name and total cooldown
        Owner = owner;
        burstCount = owner.projectile_multiplier;
        this.projectileTexture = projectileTexture;
        this.weaponIcon = new Texture("burst_weapon.png");
    }

    /**
     * Updates the weapon state each frame.
     * If currently bursting, fires shots at defined intervals until burst is complete.
     *
     * @param delta The time elapsed since the last frame (in seconds).
     */
    @Override
    public void update(float delta) {
        super.update(delta);

        if (isBursting) {
            timeSinceLastBurstShot += delta;
            if (timeSinceLastBurstShot >= burstInterval && shotsRemaining > 0) {
                fireProjectile(Owner);  // Fire one shot
                shotsRemaining--;
                timeSinceLastBurstShot = 0;

                if (shotsRemaining == 0) {
                    isBursting = false; // Burst complete
                }
            }
        }
    }

    /**
     * Activates the weapon usage.
     * Begins a burst sequence if the weapon is ready (cooldown complete).
     *
     * @param owner The protagonist using the weapon.
     */
    @Override
    public void use(Protagonist owner) {
        if (!canUse()) return;

        burstCount = Owner.projectile_multiplier;
        this.shotsRemaining = burstCount;
        this.isBursting = true;
        this.timeSinceLastBurstShot = 0;
        this.timeSinceLastUse = 0; // Reset cooldown timer
    }

    /**
     * Fires a single projectile in the direction of the mouse cursor.
     * Converts screen coordinates to world coordinates for aiming.
     *
     * @param owner The protagonist firing the projectile.
     */
    private void fireProjectile(Protagonist owner) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        // Convert screen coordinates to world coordinates (assuming fixed world size)
        Vector3 screenCoords = new Vector3(mouseX, mouseY, 0);
        Vector3 worldCoords = super.viewport.unproject(screenCoords);

        float worldMouseX = worldCoords.x;
        float worldMouseY = worldCoords.y;

        // Calculate normalized direction vector from owner to mouse cursor
        float dirX = worldMouseX - owner.getX();
        float dirY = worldMouseY -  owner.getY();
        float len = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= len;
        dirY /= len;

        // Create and add new projectile to the owner's projectile list
        owner.getProjectiles().add(new Projectile(owner.getX(), owner.getY(), dirX, dirY, projectileTexture, 7, owner.WorldHeight,owner.WorldHeight));
    }
}
