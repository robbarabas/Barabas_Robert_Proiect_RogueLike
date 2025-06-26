package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FirstScreen implements Screen {
    Protagonist Tony;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Texture backgroundTexture;
    Texture enemyTexture;
    Texture enemyFace;
    List<Enemy> enemies;

    public FirstScreen() {
        Tony = new Protagonist();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);

        // Load enemy texture
        backgroundTexture=new Texture("background_0.png");
        enemyTexture = new Texture("enemy_0_body.png");
        enemyFace=new Texture("enemy_0_face.png");

        Random random = new Random();

        float x = random.nextFloat() * 15f; // Random X between 0 and 16
        float y = random.nextFloat() * 9f; // Random Y between 0 and 10
        // Create one or more enemies
        enemies = new ArrayList<>();
        int num_enemies=5;
        for (int i=1;i<=5;i++) {
            enemies.add(new Enemy(x+1,y+1, enemyTexture, enemyFace, Tony));
            x=random.nextFloat() * 15f;
            y=random.nextFloat() * 9f;
        }
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

            Iterator<Projectile> projectileIter = Tony.getProjectiles().iterator();
            while (projectileIter.hasNext()) {
                Projectile p = projectileIter.next();
                while (enemyIter.hasNext()) {
                    {
                        Enemy enemy = enemyIter.next();
                        if (p.getBounds().overlaps(enemy.getBounds())) {
                            projectileIter.remove(); // Deletes projectile
                            enemyIter.remove();
                            break;
                        }
                    }
                }
            }

        // Draw everything
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();


        ScreenUtils.clear(Color.CLEAR_WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        Tony.render(spriteBatch);
        for (Enemy e : enemies) {
            e.update(delta);
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
