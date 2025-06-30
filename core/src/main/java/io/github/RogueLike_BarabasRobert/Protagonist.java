package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Protagonist {
    Texture Texture_Tony;
    Texture Tony_face;
    Sprite sprite;
    Sprite sprite_face;
    Texture projectileTexture;
    List<Projectile> projectiles;
    List<Weapon> inventory = new ArrayList<>();
    int currentWeaponIndex = 0;


    float shootCooldown = 0.2f;
    float timeSinceLastShot = 0;
    public float speed=5.5f;
    public int health ;
    public float power=1;
    public int projectile_multiplier=2;
    private GameUI gameUI;

    public void takeDamage(int amount) {
        health -= amount;
        System.out.println("Player hit! Health: " + health);
        if (health <= 0) {
            System.out.println("Player is dead.");
            // Trigger game over logic here
        }
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }


    public Protagonist(int health,GameUI ui) {
        Texture_Tony = new Texture("Apple_body.png");
        projectileTexture = new Texture("projectile_0.png"); // Make sure this exists
        Tony_face=new Texture("Apple_face_0.png");
        sprite = new Sprite(Texture_Tony);
        sprite.setSize(1, 1);
        sprite.setPosition(2, 2);
        sprite_face = new Sprite(Tony_face);
        sprite_face.setSize(1, 1);
        sprite_face.setPosition(2, 2);
        gameUI=ui;
        this.health=health;
        projectiles = new ArrayList<>();
        Weapon gun = new GunWeapon(projectileTexture,this);
        Weapon shotgun = new ShotGunWeapon(projectileTexture,this);
        inventory.add(gun);
        inventory.add(shotgun);


    }

    public void update(float delta, List<Wall> walls) {
        float moveX = 0;
        float moveY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveX -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveX += 1;

        float length = (float) Math.sqrt(moveX * moveX + moveY * moveY);
        if (length != 0) {
            moveX /= length;
            moveY /= length;
        }

        float newX = sprite.getX() + moveX * speed * delta;
        float newY = sprite.getY() + moveY * speed * delta;
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
            sprite_face.setPosition(newX,newY);
        }

        timeSinceLastShot += delta;


        // Update projectiles
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);
            if (p.isOffScreen()) {
                iter.remove();
            }
        }
        // use weapon
        Weapon current = inventory.get(currentWeaponIndex);
        current.update(delta);

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && current.canUse()) {
            current.use(this);
        }

// Switch weapons with Q/E
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            currentWeaponIndex = (currentWeaponIndex - 1 + inventory.size()) % inventory.size();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            currentWeaponIndex = (currentWeaponIndex + 1) % inventory.size();
        }
        gameUI.setWeaponIcon(inventory.get(currentWeaponIndex).weaponIcon);
    }

    private void rotate_around_cursor(Sprite spr,float newX,float newY)
    {
        // Offset face 0.1 units up (optional)
        spr.setPosition(newX, newY + 0.1f);

// Point face toward cursor (optional)
        float centerX = newX + sprite.getWidth() / 2f;
        float centerY = newY + sprite.getHeight() / 2f;

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        float angle = (float)Math.toDegrees(Math.atan2(worldMouseY - centerY, worldMouseX - centerX));
        spr.setRotation(angle);
        spr.setOriginCenter();
    }



    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        sprite_face.draw(batch);
        for (Projectile p : projectiles) {
            p.render(batch);
        }
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void dispose() {
        projectileTexture.dispose();
        Texture_Tony.dispose();
        Tony_face.dispose();
    }

    public float getCooldownPercent() {
        if (timeSinceLastShot > shootCooldown)
            return 1f;
        else {
            return timeSinceLastShot / shootCooldown;
        }
    }

    public Weapon getCurrentWeapon() {
        return inventory.get(currentWeaponIndex);
    }
}
