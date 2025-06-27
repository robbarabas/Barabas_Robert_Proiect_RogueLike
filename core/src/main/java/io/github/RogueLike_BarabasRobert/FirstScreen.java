package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

public class FirstScreen implements Screen {
    Protagonist Tony;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Texture backgroundTexture;
    Texture enemyTexture;
    Texture enemyFace;
    List<Enemy> enemies;
    Texture coinTexture;
    List<Coin> coins;
    BitmapFont font;
    int coinCount = 0;
    float rng;
    List<Wall> walls;

    Texture wallTexture;

    public FirstScreen() {
        Tony = new Protagonist();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);

        // Use default font
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        backgroundTexture = new Texture("background_0.png");
        enemyTexture = new Texture("enemy_0_body.png");
        enemyFace = new Texture("enemy_0_face.png");
        coinTexture = new Texture("Coin.png");

       // Load an image for wall

        walls = new ArrayList<>();
        wallTexture = new Texture("coin.png"); // Make sure this exists

// Example walls

        walls.add(new Wall(4, 4, 2, 0.5f));
        walls.add(new Wall(8, 2, 0.5f, 3));


        Random random = new Random();
        float x = random.nextFloat() * 15f;
        float y = random.nextFloat() * 9f;

        enemies = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy(x + 1, y + 1, enemyTexture, enemyFace, Tony));
            x = random.nextFloat() * 15f;
            y = random.nextFloat() * 9f;
        }

        coins = new ArrayList<>();
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Clear screen to avoid flickering / ghosting
        ScreenUtils.clear(Color.BLACK);

        Tony.update(delta,walls);

        // Projectile-enemy collision
        Iterator<Projectile> projectileIter = Tony.getProjectiles().iterator();
        while (projectileIter.hasNext()) {
            Projectile p = projectileIter.next();
            boolean removeProjectile = false;

            Iterator<Enemy> enemyIter = enemies.iterator();
            while (enemyIter.hasNext()) {
                Enemy enemy = enemyIter.next();
                if (p.getBounds().overlaps(enemy.getBounds())) {
                    removeProjectile = true;
                    enemy.takeDamage(Tony.power);

                    if (enemy.health < 0) {
                        enemyIter.remove();
                        rng = random.nextFloat() * 100f;
                        if (rng < 33)
                            coins.add(new Coin(enemy.sprite.getX(), enemy.sprite.getY(), coinTexture));
                        break;
                    }
                }
            }

            for (Wall wall : walls) {
                if (p.getBounds().overlaps(wall.getBounds())) {
                    removeProjectile = true;
                    break;
                }
            }

            if (removeProjectile) {
                projectileIter.remove();
            }
        }

        //ememies-enemies coliosion
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e1 = enemies.get(i);
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy e2 = enemies.get(j);
                if (e1.getBounds().overlaps(e2.getBounds())) {
                    float dx = e2.getX() - e1.getX();
                    float dy = e2.getY() - e1.getY();
                    float distance = (float)Math.sqrt(dx * dx + dy * dy);
                    if (distance == 0) distance = 0.05f;

                    float overlap = 0.05f;
                    dx /= distance;
                    dy /= distance;

                    e1.moveBy(-dx * overlap, -dy * overlap);
                    e2.moveBy(dx * overlap, dy * overlap);

                    // After moving, push enemies out of walls if they got stuck
                    e1.pushOutFromWalls(walls);
                    e2.pushOutFromWalls(walls);
                }
            }
        }

        // Coin pickup
        Iterator<Coin> coinIter = coins.iterator();
        while (coinIter.hasNext()) {
            Coin coin = coinIter.next();
            if (coin.getBounds().overlaps(Tony.getBounds())) {
                coinCount++;
                coinIter.remove();
            }
        }

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);


        //  Draw game world
        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        Tony.render(spriteBatch);
        // draw walls
        for (Wall wall : walls) {
            wall.render(spriteBatch, wallTexture);
        }
        //draw enemies
        for (Enemy e : enemies) {
            e.update(delta,walls);
            e.render(spriteBatch);
        }
        //draw coins
        for (Coin coin : coins) {
            coin.render(spriteBatch);
        }
        spriteBatch.end();


        //  Draw UI in screen space
        spriteBatch.setProjectionMatrix(spriteBatch.getProjectionMatrix().idt().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch.begin();
        font.getData().setScale(2.0f);
        font.draw(spriteBatch, "Coins: " + coinCount, 10, Gdx.graphics.getHeight() - 10);
        font.draw(spriteBatch, "Health: " + Tony.health, 10, Gdx.graphics.getHeight() - 40);
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
