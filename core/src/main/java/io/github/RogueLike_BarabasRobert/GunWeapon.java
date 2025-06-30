package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GunWeapon extends Weapon {
    Texture projectileTexture;
    Protagonist Owner;
    int burstCount ;
    int shotsRemaining = 0;
    float burstInterval = 0.1f; // seconds between burst shots
    float timeSinceLastBurstShot = 0;
    boolean isBursting = false;

    public GunWeapon(Texture projectileTexture, Protagonist owner) {


        super("Burst Gun", 0.5f); // Total cooldown before next burst
        Owner = owner;
        burstCount=owner.projectile_multiplier;
        this.projectileTexture = projectileTexture;
        this.weaponIcon=new Texture("projectile_0.png");
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (isBursting) {
            timeSinceLastBurstShot += delta;
            if (timeSinceLastBurstShot >= burstInterval && shotsRemaining > 0) {
                fireProjectile(Owner); // Fire one shot in the burst
                shotsRemaining--;
                timeSinceLastBurstShot = 0;

                if (shotsRemaining == 0) {
                    isBursting = false;
                }
            }
        }
    }



    @Override
    public void use(Protagonist owner) {
        if (!canUse()) return;
        burstCount =Owner.projectile_multiplier;

        this.shotsRemaining = burstCount;
        this.isBursting = true;
        this.timeSinceLastBurstShot = 0;
        this.timeSinceLastUse = 0;
    }

    private void fireProjectile(Protagonist owner) {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        float startX = owner.getX() + 0.5f;
        float startY = owner.getY() + 0.5f;

        float dirX = worldMouseX - startX;
        float dirY = worldMouseY - startY;
        float len = (float) Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= len;
        dirY /= len;

        owner.getProjectiles().add(new Projectile(startX, startY, dirX, dirY, projectileTexture));
    }
}
