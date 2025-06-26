package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FirstScreen implements Screen {
    Protagonist Tony;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    Texture enemyTexture;
    List<Enemy> enemies;

    public FirstScreen() {
        Tony = new Protagonist();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);

        // Load enemy texture
        enemyTexture = new Texture("Apple_body.png");

        // Create one or more enemies
        enemies = new ArrayList<>();
        enemies.add(new Enemy(10, 1, enemyTexture)); // Example enemy
        enemies.add(new Enemy(12, 2, enemyTexture)); // Optional second enemy
    }

    @Override
    public void show() {
        // You could move enemy setup here if preferred
    }

    @Override
    public void render(float delta) {
        Tony.update(delta);

        // Handle projectile collision with enemies
        Iterator<Enemy> enemyIter = enemies.iterator();
        while (enemyIter.hasNext()) {
            Enemy enemy = enemyIter.next();
            for (Projectile p : Tony.getProjectiles()) {
                if (p.getBounds().overlaps(enemy.getBounds())) {
                    enemyIter.remove();
                    break;
                }
            }
        }

        // Draw everything
        ScreenUtils.clear(Color.CLEAR_WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        Tony.render(spriteBatch);
        for (Enemy e : enemies) {
            e.render(spriteBatch);
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        Tony.dispose();
        enemyTexture.dispose();
    }
}
