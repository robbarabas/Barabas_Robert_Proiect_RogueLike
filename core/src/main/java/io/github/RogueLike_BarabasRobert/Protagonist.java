package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Protagonist {
    Texture Texture_Tony;
    Texture Tony_face;
    Sprite sprite;
    Sprite sprite_face;
    Sprite Weapon;
    Texture projectileTexture;
    List<Projectile> projectiles;
    List<Weapon> inventory = new ArrayList<>();
    int currentWeaponIndex = 0;



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
        Weapon gun = new GunWeapon(new Texture("arrow.png"),this);
        Weapon shotgun = new ShotGunWeapon(new Texture("projectile_0.png"),this);
        Weapon=new Sprite(gun.getWeaponIcon());
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
        Weapon=new Sprite(inventory.get(currentWeaponIndex).weaponIcon);
        Weapon.setSize(1f,1f);
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        rotate_around_cursor(Weapon,mouseX,mouseY);
    }


    private void rotate_around_cursor(Sprite spr, float mouseX, float mouseY) {
        // Convert mouse screen coords to world coords
        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        // Get protagonist center
        float centerX = sprite.getX() + sprite.getWidth() / 2f;
        float centerY = sprite.getY() + sprite.getHeight() / 2f;

        // Calculate angle to mouse
        float angle = (float)Math.toDegrees(Math.atan2(worldMouseY - centerY, worldMouseX - centerX))-45.2f;

        // Position weapon near player, e.g., slightly in front of them (optional tweak)
        float weaponOffset = 0.4f;
        float offsetX = (float)Math.cos(Math.toRadians(angle)) * weaponOffset;
        float offsetY = (float)Math.sin(Math.toRadians(angle)) * weaponOffset;
        spr.setPosition(centerX + offsetX - spr.getWidth() / 2f, centerY + offsetY - spr.getHeight() / 2f);

        // Rotate and center
        spr.setRotation(angle);
        spr.setOriginCenter();
    }

    public Vector2 getWeaponTipPosition() {
        float angleDeg = Weapon.getRotation()-45f;
        float angleRad = (float) Math.toRadians(angleDeg);

        // Distance from weapon center to tip (adjust if needed)
        float tipOffset = Weapon.getWidth() / 2f;

        // Calculate tip position using rotation and origin
        float tipX = Weapon.getX() + Weapon.getOriginX() + (float)Math.cos(angleRad) * tipOffset;
        float tipY = Weapon.getY() + Weapon.getOriginY() + (float)Math.sin(angleRad) * tipOffset;

        return new Vector2(tipX, tipY);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        sprite_face.draw(batch);
        Weapon.draw(batch);
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



    public Weapon getCurrentWeapon() {
        return inventory.get(currentWeaponIndex);
    }
}
