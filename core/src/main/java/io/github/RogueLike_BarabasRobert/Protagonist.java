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

/**
 * Represents the player character in the game.
 *
 * Manages movement, health, weapons, projectiles, rendering,
 * and interaction with the game world such as collision detection.
 */
public class Protagonist {
    /** Texture for the protagonist's body sprite */
    Texture Texture_Tony;

    /** Texture for the protagonist's face sprite */
    Texture Tony_face;

    /** Sprite for the protagonist's body */
    Sprite sprite;

    /** Sprite for the protagonist's face */
    Sprite sprite_face;

    /** Sprite representing the current weapon */
    Sprite Weapon;

    /** Texture used for projectiles (not currently initialized explicitly) */
    Texture projectileTexture;

    /** List of active projectiles fired by the protagonist */
    List<Projectile> projectiles;

    /** Inventory holding available weapons */
    List<Weapon> inventory = new ArrayList<>();

    /** Index of the currently selected weapon */
    int currentWeaponIndex = 0;

    /** Timer to control wobble animation when moving */
    float wobbleTime = 0f;

    /** Timer since last shot fired */
    float timeSinceLastShot = 0;

    /** Movement speed of the protagonist in units per second */
    public float speed = 5.5f;

    /** Current health points */
    public int health;

    /** Maximum health points */
    public int max_health;

    /** Power multiplier affecting damage or effects */
    public float power = 1;

    /** Number of projectiles fired per shot */
    public int projectile_multiplier = 2;

    /** Reference to the UI to update weapon icon */
    private GameUI gameUI;

    /**
     * Reduces the protagonist's health by the specified amount.
     * Prints debug info and triggers death logic if health drops to zero or below.
     *
     * @param amount Amount of damage to take.
     */
    public void takeDamage(int amount) {
        health -= amount;
        System.out.println("Player hit! Health: " + health);
        if (health <= 0) {
            System.out.println("Player is dead.");
            // TODO: trigger game over or respawn logic here
        }
    }

    /**
     * Gets the current X coordinate of the protagonist's position.
     *
     * @return Current X position.
     */
    public float getX() {
        return sprite.getX();
    }

    /**
     * Gets the current Y coordinate of the protagonist's position.
     *
     * @return Current Y position.
     */
    public float getY() {
        return sprite.getY();
    }

    /**
     * Returns the bounding rectangle of the protagonist for collision detection.
     * The bounding box is scaled to 70% of the sprite size.
     *
     * @return A Rectangle representing the collision bounds.
     */
    public Rectangle getBounds() {
        Rectangle bounds = sprite.getBoundingRectangle();
        bounds.setSize(0.7f);
        return bounds;
    }

    /**
     * Constructs a new Protagonist with specified stats and UI reference.
     * Initializes textures, sprites, weapons, and starting position.
     *
     * @param health Initial health points.
     * @param max_health Maximum health points.
     * @param power Power multiplier.
     * @param projectile_multiplier Number of projectiles fired per shot.
     * @param ui Reference to the GameUI for updating weapon icon.
     */
    public Protagonist(int health, int max_health, float power, int projectile_multiplier, GameUI ui) {
        Texture_Tony = new Texture("Apple_body.png");
        Tony_face = new Texture("Apple_face_0.png");
        sprite = new Sprite(Texture_Tony);
        sprite.setSize(1, 1);
        sprite.setPosition(2, 2);

        sprite_face = new Sprite(Tony_face);
        sprite_face.setSize(1, 1);
        sprite_face.setPosition(2, 2);

        gameUI = ui;
        this.health = health;
        this.max_health = max_health;
        this.power = power;
        this.projectile_multiplier = projectile_multiplier;

        projectiles = new ArrayList<>();

        // Initialize weapons and add to inventory
        Weapon gun = new GunWeapon(new Texture("arrow.png"), this);
        Weapon shotgun = new ShotGunWeapon(new Texture("projectile_0.png"), this);
        Weapon = new Sprite(gun.getWeaponIcon());
        inventory.add(gun);
        inventory.add(shotgun);
    }

    /**
     * Updates the protagonist state: movement, collision with walls,
     * weapon firing, weapon switching, projectile updates, and weapon rotation.
     *
     * @param delta Time elapsed since last frame.
     * @param walls List of walls for collision detection.
     */
    public void update(float delta, List<Wall> walls) {
        float moveX = 0;
        float moveY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) moveY += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) moveY -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) moveX -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) moveX += 1;

        boolean isMoving = moveX != 0 || moveY != 0;

        // Normalize movement vector
        float length = (float) Math.sqrt(moveX * moveX + moveY * moveY);
        if (length != 0) {
            moveX /= length;
            moveY /= length;
        }

        float newX = sprite.getX() + moveX * speed * delta;
        float newY = sprite.getY() + moveY * speed * delta;

        // Animate wobble when moving
        if (isMoving) {
            wobbleTime += delta * 10f;
            float scaleY = 1f + (float) Math.sin(wobbleTime) * 0.2f;
            float scaleX = 1f - (float) Math.sin(wobbleTime) * 0.05f;
            sprite.setOriginCenter();
            sprite.setScale(scaleX, scaleY);
        } else {
            sprite.setScale(1f, 1f);
        }

        // Collision detection with walls
        Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth() * 0.7f, sprite.getHeight() * 0.7f);
        boolean collides = false;
        for (Wall wall : walls) {
            if (futureBounds.overlaps(wall.getBounds())) {
                collides = true;
                break;
            }
        }

        if (!collides) {
            sprite.setPosition(newX, newY);
            sprite_face.setPosition(newX, newY);
        }

        timeSinceLastShot += delta;

        // Update projectiles and remove expired ones
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);
            if (p.shouldRemove()) {
                iter.remove();
            }
        }

        // Update current weapon
        Weapon current = inventory.get(currentWeaponIndex);
        current.update(delta);

        // Fire weapon if left mouse button is pressed and weapon ready
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && current.canUse()) {
            current.use(this);
        }

        // Switch weapons with Q and E keys
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            currentWeaponIndex = (currentWeaponIndex - 1 + inventory.size()) % inventory.size();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            currentWeaponIndex = (currentWeaponIndex + 1) % inventory.size();
        }

        gameUI.setWeaponIcon(inventory.get(currentWeaponIndex).weaponIcon);

        Weapon = new Sprite(inventory.get(currentWeaponIndex).weaponIcon);
        Weapon.setSize(1f, 1f);

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        rotate_around_cursor(Weapon, mouseX, mouseY);
    }

    /**
     * Rotates the given sprite (usually the weapon) to face the mouse cursor.
     * Also positions the sprite near the protagonist accordingly.
     *
     * @param spr The sprite to rotate and position.
     * @param mouseX Mouse X coordinate in screen pixels.
     * @param mouseY Mouse Y coordinate in screen pixels.
     */
    private void rotate_around_cursor(Sprite spr, float mouseX, float mouseY) {
        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        float centerX = sprite.getX() + sprite.getWidth() / 2f;
        float centerY = sprite.getY() + sprite.getHeight() / 2f;

        float angle = (float) Math.toDegrees(Math.atan2(worldMouseY - centerY, worldMouseX - centerX)) - 45.2f;

        float weaponOffset = 0.4f;
        float offsetX = (float) Math.cos(Math.toRadians(angle)) * weaponOffset;
        float offsetY = (float) Math.sin(Math.toRadians(angle)) * weaponOffset;
        spr.setPosition(centerX + offsetX - spr.getWidth() / 2f, centerY + offsetY - spr.getHeight() / 2f);

        spr.setRotation(angle);
        spr.setOriginCenter();
    }

    /**
     * Returns the world position of the tip of the currently held weapon.
     * Useful for spawning projectiles at the correct location.
     *
     * @return Vector2 containing X and Y coordinates of the weapon tip.
     */
    public Vector2 getWeaponTipPosition() {
        float angleDeg = Weapon.getRotation() - 45f;
        float angleRad = (float) Math.toRadians(angleDeg);

        float tipOffset = Weapon.getWidth() / 2f;

        float tipX = Weapon.getX() + Weapon.getOriginX() + (float) Math.cos(angleRad) * tipOffset;
        float tipY = Weapon.getY() + Weapon.getOriginY() + (float) Math.sin(angleRad) * tipOffset;

        return new Vector2(tipX, tipY);
    }

    /**
     * Renders the protagonist and all related sprites (face, weapon, projectiles).
     *
     * @param batch The SpriteBatch used for drawing.
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        sprite_face.draw(batch);
        Weapon.draw(batch);
        for (Projectile p : projectiles) {
            p.render(batch);
        }
    }

    /**
     * Returns the list of active projectiles fired by the protagonist.
     *
     * @return List of projectiles.
     */
    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    /**
     * Disposes all textures to free up GPU resources.
     * Should be called when the protagonist is no longer needed.
     */
    public void dispose() {
        projectileTexture.dispose();
        Texture_Tony.dispose();
        Tony_face.dispose();
    }

    /**
     * Returns the currently equipped weapon.
     *
     * @return Current Weapon object.
     */
    public Weapon getCurrentWeapon() {
        return inventory.get(currentWeaponIndex);
    }
}
