package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.*;

import static com.badlogic.gdx.math.MathUtils.random;

public class FirstScreen implements Screen {

    final Main game;
    GameUI ui;
    Protagonist Tony;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Texture backgroundTexture, enemyTexture, enemyFace, coinTexture, wallTexture, enemy_proj_0, enemyTexture_ranged, enemyFace_ranged  ;
    List<Enemy> enemies;
    List<Coin> coins;
    List<Wall> walls;
    List<Projectile> enemyProjectiles = new ArrayList<>();
    BitmapFont font;
    int coinCount=0;
    boolean wallsBroken = false;


    public FirstScreen(Main game) {
        this.game = game;
        initializeGameObjects();
        initializeEnemies();
        generateBorderWalls();
    }
    private void saveGame(int health, int coins) {
        FileHandle file = Gdx.files.local("save.txt");
        file.writeString(health + "," + coins, false);
    }

    private void initializeGameObjects() {
        ui = new GameUI();
        GameUI.setCrosshairCursor("crosshair.png", 16, 16);
        Gdx.input.setInputProcessor(new InputMultiplexer(ui.getStage())); // For UI input
        Tony = new Protagonist(game.health);
        coinCount=game.totalCoins;
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        backgroundTexture = new Texture("background_0.png");
        enemyTexture = new Texture("enemy_0_body.png");
        enemyFace = new Texture("enemy_0_face.png");
        enemyTexture_ranged = new Texture("enemy_1_body.png");
        enemyFace_ranged = new Texture("enemy_1_face.png");
        coinTexture = new Texture("Coin.png");
        wallTexture = new Texture("wall_0.png");
        enemy_proj_0 = new Texture("projectile_0.png");

        walls = new ArrayList<>();
        coins = new ArrayList<>();
        enemies = new ArrayList<>();

        // Random non-border walls
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            float x = (int) (random.nextFloat() * 15f);
            float y = (int) (random.nextFloat() * 9f);
            if (x == 2 && y == 2) continue;
            walls.add(new Wall(x, y, 1, 1));
        }
    }

    private void initializeEnemies() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            float x = random.nextFloat() * 8f;
            float y = random.nextFloat() * 7f;
            enemies.add(new Enemy(x+6, y+1, enemyTexture, enemyFace, Tony));
        }
        for (int i = 0; i < 3; i++) {
            float x = random.nextFloat() * 8f;
            float y = random.nextFloat() * 7f;
            enemies.add(new EnemyRanged(x+6, y+1, enemyTexture_ranged, enemyFace_ranged,Tony, enemy_proj_0, enemyProjectiles));
        }
    }

    private void generateBorderWalls() {
        int worldWidth = 16;
        int worldHeight = 10;

        for (int x = 0; x < worldWidth; x++) {
            walls.add(new Wall(x, 0, 1, 1)); // bottom
            walls.add(new Wall(x, worldHeight - 1, 1, 1)); // top
        }

        for (int y = 1; y < worldHeight - 1; y++) {
            walls.add(new Wall(0, y, 1, 1)); // left
            if (y >3&&y<7) {
                walls.add(new Wall(worldWidth - 1, y, 1, 1, true)); // breakable exit
            } else {
                walls.add(new Wall(worldWidth - 1, y, 1, 1,false));
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        Tony.update(delta, walls);

        updateProjectiles(delta);
        handleCollisions();
        updateEnemies(delta);

        // Break walls & transition
        if (enemies.isEmpty() && !wallsBroken) {
            walls.removeIf(Wall::isBreakable);
            wallsBroken = true;
        }

        // Go to next level
        if (wallsBroken && Tony.getBounds().overlaps(new Rectangle(15, 5, 1, 3))) {
            game.totalCoins = coinCount;
            game.health = Tony.health;
            saveGame(game.health, game.totalCoins);
            game.setScreen(new FirstScreen(game)); // Or repeat FirstScreen with variations
        }

        drawWorld();
        drawUI();
    }

    private void updateProjectiles(float delta) {
        Iterator<Projectile> projIter = Tony.getProjectiles().iterator();
        while (projIter.hasNext()) {
            Projectile p = projIter.next();
            boolean remove = false;

            for (Enemy enemy : enemies) {
                if (p.getBounds().overlaps(enemy.getBounds())) {
                    enemy.takeDamage(Tony.power);
                    remove = true;
                    if (enemy.health < 0) {
                        enemies.remove(enemy);
                        if (random() < 0.33f) {
                            coins.add(new Coin(enemy.sprite.getX(), enemy.sprite.getY(), coinTexture));
                        }
                    }
                    break;
                }
            }

            for (Wall wall : walls) {
                if (p.getBounds().overlaps(wall.getBounds())) {
                    remove = true;
                    break;
                }
            }

            if (remove) projIter.remove();
        }

        // Enemy projectiles
        Iterator<Projectile> enemyIter = enemyProjectiles.iterator();
        while (enemyIter.hasNext()) {
            Projectile p = enemyIter.next();
            p.update(delta);

            if (p.getBounds().overlaps(Tony.getBounds())) {
                Tony.health -= 1;
                enemyIter.remove();
                continue;
            }

            for (Wall wall : walls) {
                if (p.getBounds().overlaps(wall.getBounds())) {
                    enemyIter.remove();
                    break;
                }
            }

            if (p.shouldRemove()) {
                enemyIter.remove();
            }
        }
    }

    private void handleCollisions() {
        coins.removeIf(coin -> {
            if (coin.getBounds().overlaps(Tony.getBounds())) {
                coinCount++;
                return true;
            }
            return false;
        });
    }

    private void updateEnemies(float delta) {
        for (Enemy e : enemies) {
            e.update(delta, walls);
        }
    }

    private void drawWorld() {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        Tony.render(spriteBatch);

        for (Wall wall : walls)
            wall.render(spriteBatch, wallTexture);

        for (Enemy e : enemies)
            e.render(spriteBatch);

        for (Coin coin : coins)
            coin.render(spriteBatch);

        for (Projectile p : enemyProjectiles)
            p.render(spriteBatch);

        spriteBatch.end();
    }

    private void drawUI() {
        ui.update(coinCount, Tony.health);
        ui.setCooldown(Tony.getCooldownPercent());
        ui.setCooldownBarPosition(new Vector2(Tony.getX() + 0.5f, Tony.getY() + 1.5f), viewport);

        ui.render();
    }

    @Override public void resize(int width, int height) {
        viewport.update(width, height, true);
        ui.resize(width, height);
    }
    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        ui.dispose();
        spriteBatch.dispose();
        Tony.dispose();
        enemyTexture.dispose();
        wallTexture.dispose();
        backgroundTexture.dispose();
    }
}
