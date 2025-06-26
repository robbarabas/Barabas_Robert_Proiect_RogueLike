package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Protagonist {
    Texture Texture_Tony;
    Sprite sprite;

    Texture projectileTexture;
    List<Projectile> projectiles;

    float shootCooldown = 0.3f;
    float timeSinceLastShot = 0;
    private float speed=5f;

    public Protagonist() {
        Texture_Tony = new Texture("Apple_body.png");
        projectileTexture = new Texture("Apple_body.png"); // Make sure this exists
        sprite = new Sprite(Texture_Tony);
        sprite.setSize(1, 1);
        sprite.setPosition(1, 1);

        projectiles = new ArrayList<>();
    }

    public void update(float delta) {
        // Shooting
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += speed * delta;

        sprite.setPosition(sprite.getX() + dx, sprite.getY() + dy);
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
    }
}
