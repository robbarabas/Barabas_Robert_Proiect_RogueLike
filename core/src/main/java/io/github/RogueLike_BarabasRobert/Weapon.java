package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;

public abstract class Weapon {
    protected float cooldown;
    protected float timeSinceLastUse = 0;
    protected Texture icon;
    protected String name;
    Texture weaponIcon;

    public Weapon(String name, float cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }
    public Weapon(String name, float cooldown, Texture weaponIcon) {
        this.name = name;
        this.cooldown = cooldown;
        this.weaponIcon = weaponIcon;
        this.timeSinceLastUse = cooldown;
    }

    public Texture getWeaponIcon() {
        return weaponIcon;
    }

    public void update(float delta) {
        timeSinceLastUse += delta;
    }

    public boolean canUse() {
        return timeSinceLastUse >= cooldown;
    }

    public float getCooldownPercent() {
        return Math.min(1f, timeSinceLastUse / cooldown);
    }
    public abstract void use(Protagonist owner);


    public String getName() {
        return name;
    }
}
