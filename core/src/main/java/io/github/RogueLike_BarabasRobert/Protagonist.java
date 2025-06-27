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
import java.util.TooManyListenersException;

public class Protagonist {
    Texture Texture_Tony;
    Texture Tony_face;
    Sprite sprite;
    Sprite sprite_face;
    Texture projectileTexture;
    List<Projectile> projectiles;

    float shootCooldown = 0.3f;
    float timeSinceLastShot = 0;
    public float speed=5.5f;
    public int health = 5;
    public float power=2;

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


    public Protagonist() {
        Texture_Tony = new Texture("Apple_body.png");
        projectileTexture = new Texture("projectile_0.png"); // Make sure this exists
        Tony_face=new Texture("Apple_face_0.png");
        sprite = new Sprite(Texture_Tony);
        sprite.setSize(1, 1);
        sprite.setPosition(1, 1);
        sprite_face = new Sprite(Tony_face);
        sprite_face.setSize(1, 1);
        sprite_face.setPosition(1, 1);

        projectiles = new ArrayList<>();
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
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && timeSinceLastShot >= shootCooldown) {
            shoot();
            timeSinceLastShot = 0;
        }

        // Update projectiles
        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.update(delta);
            if (p.isOffScreen()) {
                iter.remove();
            }
        }

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

    private void shoot() {
        // Convert mouse position to world coordinates
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.input.getY();
        // Invert Y because LibGDX screen origin is bottom-left, but input is top-left
        float worldMouseX = mouseX * (16f / Gdx.graphics.getWidth());
        float worldMouseY = (Gdx.graphics.getHeight() - mouseY) * (10f / Gdx.graphics.getHeight());

        // Get protagonist center
        float startX = sprite.getX() + sprite.getWidth() / 2f;
        float startY = sprite.getY() + sprite.getHeight() / 2f;

        // Calculate direction
        float dirX = worldMouseX - startX;
        float dirY = worldMouseY - startY;
        float length = (float)Math.sqrt(dirX * dirX + dirY * dirY);
        dirX /= length;
        dirY /= length;

        projectiles.add(new Projectile(startX, startY, dirX, dirY, projectileTexture));
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
}
