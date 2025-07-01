package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;

/**
 * Abstract base class for all weapons.
 *
 * Weapons have a name, cooldown period between uses, an icon texture,
 * and a base power value. Subclasses must implement the use behavior.
 */
public abstract class Weapon {
    /** Base damage or power of the weapon */
    public float base_power = 4;

    /** Cooldown duration (in seconds) between consecutive uses */
    protected float cooldown;

    /** Time elapsed since the weapon was last used */
    protected float timeSinceLastUse = 10;

    /** The weapon's icon texture (for UI display) */
    protected Texture weaponIcon;

    /** The weapon's name */
    protected String name;

    /**
     * Constructs a weapon with the specified name and cooldown.
     *
     * @param name Weapon's name
     * @param cooldown Cooldown duration in seconds
     */
    public Weapon(String name, float cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    /**
     * Constructs a weapon with the specified name, cooldown, and icon texture.
     *
     * @param name Weapon's name
     * @param cooldown Cooldown duration in seconds
     * @param weaponIcon Texture representing the weapon's icon
     */
    public Weapon(String name, float cooldown, Texture weaponIcon) {
        this.name = name;
        this.cooldown = cooldown;
        this.weaponIcon = weaponIcon;
        this.timeSinceLastUse = cooldown; // Ready to use immediately
    }

    /**
     * Returns the weapon's icon texture used for UI representation.
     *
     * @return The weapon icon texture
     */
    public Texture getWeaponIcon() {
        return weaponIcon;
    }

    /**
     * Updates the weapon's cooldown timer.
     *
     * Should be called once per frame with the time elapsed since the last frame.
     *
     * @param delta Time elapsed since last frame, in seconds
     */
    public void update(float delta) {
        timeSinceLastUse += delta;
    }

    /**
     * Checks whether the weapon is ready to be used (cooldown elapsed).
     *
     * @return true if the weapon can be used; false if still cooling down
     */
    public boolean canUse() {
        return timeSinceLastUse >= cooldown;
    }

    /**
     * Gets the cooldown progress as a percentage between 0 and 1.
     *
     * Useful for UI cooldown indicators.
     *
     * @return Cooldown progress percentage (1 means ready)
     */
    public float getCooldownPercent() {
        return Math.min(1f, timeSinceLastUse / cooldown);
    }

    /**
     * Executes the weapon's use action.
     *
     * Must be implemented by subclasses to define specific weapon behavior.
     *
     * @param owner The protagonist using the weapon
     */
    public abstract void use(Protagonist owner);

    /**
     * Returns the weapon's name.
     *
     * @return The weapon's name
     */
    public String getName() {
        return name;
    }
}
