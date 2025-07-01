package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ShotGunWeapon extends Weapon {
    Texture projectileTexture;
    Protagonist owner;
    int pelletsPerShot = 5; // Total projectiles per shot
    float spreadAngle = 50f; // Total spread angle in degrees
    Vector2 tip;

    public ShotGunWeapon(Texture projectileTexture, Protagonist owner) {

        super("Shotgun", 2f); // Cooldown time between shots
        super.base_power=1;
        this.owner = owner;
        this.projectileTexture = projectileTexture;
        this.weaponIcon=new Texture("shotgun.png");
    }

    @Override
    public void use(Protagonist owner) {
        if (!canUse()) return;
        fireProjectile(owner);
        this.timeSinceLastUse = 0;
    }

    private void fireProjectile(Protagonist owner) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();

        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        float startX = owner.getX()+0.3f ;
        float startY = owner.getY()+0.3f ;

        float dirX = worldMouseX - startX;
        float dirY = worldMouseY - startY;
        float baseAngle = (float) Math.atan2(dirY, dirX);

        int totalProjectiles = pelletsPerShot * owner.projectile_multiplier;
        for (int i = 0; i < totalProjectiles; i++) {
            float offset = ((float) i / (totalProjectiles - 1)) - 0.5f; // range from -0.5 to 0.5
            float angle = baseAngle + (float) Math.toRadians(offset * spreadAngle);

            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);


            owner.getProjectiles().add(new Projectile(startX, startY, dx, dy, projectileTexture,7f));
        }
    }
}
