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

/**
 * Represents the main gameplay screen of the rogue-like game.
 *
 * This screen manages rendering and updating the protagonist, enemies, projectiles,
 * walls, coins, and shop items. It handles player input via the UI, collision detection,
 * enemy spawning, level progression, and game state transitions such as death and
 * moving to the next stage.
 *
 * It uses a FitViewport with a fixed world size of 16x10 units.
 */
public class FirstScreen implements Screen {

    /** Reference to the main game class for accessing shared state and switching screens. */
    final Main game;

    /** The user interface manager for HUD and input handling. */
    public GameUI ui;

    /** The player-controlled protagonist character. */
    Protagonist Tony;

    /** SpriteBatch used for drawing all sprites on the screen. */
    SpriteBatch spriteBatch;

    /** Viewport defining the visible area and scaling. */
    FitViewport viewport;

    // Textures used throughout the screen for various game objects.
    Texture backgroundTexture, enemyTexture, enemyFace, coinTexture, wallTexture, enemy_proj_0,
        enemyTexture_ranged, enemyFace_ranged;

    /** List of all active enemies on the screen. */
    List<Enemy> enemies;

    /** List of all collectible coins on the screen. */
    List<Coin> coins;

    /** List of walls forming the level boundaries and obstacles. */
    List<Wall> walls;

    /** List of projectiles fired by enemies. */
    List<Projectile> enemyProjectiles = new ArrayList<>();

    /** List of available shop items for the player to purchase. */
    private List<ShopItem> shopItems = new ArrayList<>();

    // Textures for shop icons
    Texture healIcon, powerIcon, healthUpIcon, multIcon;

    /** List of coordinates representing occupied tiles to avoid spawning overlaps. */
    List<Vector2> ocupied_tiles = new ArrayList<>();

    /** BitmapFont used for drawing text if needed. */
    BitmapFont font;

    /** Number of coins currently held by the player. */
    int coinCount = 0;

    /** Flag indicating whether breakable walls have been removed. */
    boolean wallsBroken = false;

    /**
     * Creates a new gameplay screen with the given game instance.
     *
     * @param game The main game instance to which this screen belongs.
     */
    public FirstScreen(Main game) {
        this.game = game;
        initializeGameObjects();
        generateBorderWalls();
        initializeEnemies();
    }

    /**
     * Initializes all game objects, textures, the protagonist, UI, and input processor.
     */
    private void initializeGameObjects() {
        ui = new GameUI();
        Tony = new Protagonist(game.health, game.max_health, game.power, game.projectile_multiplier, ui);
        ui.set_protag(Tony);

        GameUI.setCrosshairCursor("crosshair.png", 16, 16);
        Gdx.input.setInputProcessor(new InputMultiplexer(ui.getStage()));

        coinCount = game.totalCoins;
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        // Load all textures
        healIcon = new Texture("shop_heal.png");
        powerIcon = new Texture("shop_power.png");
        healthUpIcon = new Texture("shop_maxhp.png");
        multIcon = new Texture("shop_mult.png");
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
    }

    /**
     * Initializes enemies or shop depending on the current stage.
     * On every 4th stage, a shop is spawned instead of enemies.
     */
    private void initializeEnemies() {
        if (game.stage % 4 == 0) {
            initializeShop();
        } else {
            int meleeCount = 2 + game.stage;
            int rangedCount = 1 + game.stage / 2;
            spawnEnemies(meleeCount, 0); // Spawn melee enemies
            spawnEnemies(rangedCount, 1); // Spawn ranged enemies
        }
    }

    /**
     * Creates and positions shop items at predefined locations.
     */
    private void initializeShop() {
        float[][] positions = {{8, 6}, {10, 6}, {8, 4}, {10, 4}};
        Texture[] icons = {healIcon, powerIcon, healthUpIcon, multIcon};

        for (int i = 0; i < 4; i++) {
            ShopItem item = new ShopItem(positions[i][0], positions[i][1], icons[i], i);
            shopItems.add(item);
        }
    }

    /**
     * Spawns a specified number of enemies of a given type at random, unoccupied locations.
     *
     * @param count The number of enemies to spawn.
     * @param Type  The type of enemy to spawn (0 = melee, 1 = ranged).
     */
    private void spawnEnemies(int count, int Type) {
        Random random = new Random();
        float x, y;
        boolean occupied;

        for (int i = 0; i < count; i++) {
            x = random.nextFloat() * 8f;
            y = random.nextFloat() * 7f;

            // Ensure enemy doesn't spawn on an occupied tile
            occupied = true;
            while (occupied) {
                occupied = false;
                for (Vector2 tile : ocupied_tiles) {
                    if (tile.x == x + 6 && tile.y == y + 1) {
                        occupied = true;
                        x = random.nextFloat() * 8f;
                        y = random.nextFloat() * 7f;
                        break;
                    }
                }
            }

            // Instantiate appropriate enemy type
            switch (Type) {
                case 0:
                    enemies.add(new Enemy(x + 6, y + 1, enemyTexture, enemyFace, Tony));
                    break;
                case 1:
                    enemies.add(new EnemyRanged(x + 6, y + 1, enemyTexture_ranged, enemyFace_ranged, Tony, enemy_proj_0, enemyProjectiles));
                    break;
            }
        }
    }

    /**
     * Generates the walls along the borders of the level and some interior obstacles.
     * Also marks some walls as breakable exits on the right side.
     */
    private void generateBorderWalls() {
        int worldWidth = 16;
        int worldHeight = 10;

        for (int x = 0; x < worldWidth; x++) {
            walls.add(new Wall(x, 0, 1, 1)); // bottom
            walls.add(new Wall(x, worldHeight - 1, 1, 1)); // top
        }

        for (int y = 1; y < worldHeight - 1; y++) {
            walls.add(new Wall(0, y, 1, 1)); // left
            if (y > 3 && y < 7) {
                walls.add(new Wall(worldWidth - 1, y, 1, 1, true)); // breakable exit
            } else {
                walls.add(new Wall(worldWidth - 1, y, 1, 1, false));
            }
        }

        if (game.stage % 4 != 0) { // Add random non-border walls for normal stages
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                float x = (int) (random.nextFloat() * 15f);
                float y = (int) (random.nextFloat() * 9f);
                if (x == 2 && y == 2) continue;
                walls.add(new Wall(x, y, 1, 1));
                ocupied_tiles.add(new Vector2(x, y));
            }
        }
    }

    /**
     * Main render loop called every frame.
     * Updates game logic, processes input, handles collisions, and renders all objects.
     *
     * @param delta Time elapsed since last frame.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        Tony.update(delta, walls);
        updateProjectiles(delta);
        handleCollisions();
        updateEnemies(delta);

        // Remove breakable walls when all enemies are defeated
        if (enemies.isEmpty() && !wallsBroken) {
            walls.removeIf(Wall::isBreakable);
            wallsBroken = true;
        }

        // Transition to next stage when player reaches exit area
        if (wallsBroken && Tony.getBounds().overlaps(new Rectangle(15, 5, 1, 3))) {
            game.totalCoins = coinCount;
            game.health = Tony.health;
            game.autoSave();
            game.stage++;
            game.setScreen(new FirstScreen(game));
        }

        drawWorld();
        drawUI();
    }

    /**
     * Updates all projectiles in the game, including player and enemy projectiles.
     * Handles collisions with enemies, walls, and the player.
     *
     * @param delta Time elapsed since last frame.
     */
    private void updateProjectiles(float delta) {
        if (Tony.health <= 0) {
            game.setScreen(new Deathscreen(game, game.stage, game.totalCoinsEarned, game.enemiesKilled));
        }

        // Player projectiles update and collision detection
        Iterator<Projectile> projIter = Tony.getProjectiles().iterator();
        while (projIter.hasNext()) {
            Projectile p = projIter.next();
            boolean remove = false;

            for (Enemy enemy : enemies) {
                if (p.getBounds().overlaps(enemy.getBounds())) {
                    enemy.takeDamage(Tony.power * Tony.getCurrentWeapon().base_power);
                    remove = true;
                    if (enemy.health <= 0) {
                        enemies.remove(enemy);
                        game.enemiesKilled++;
                        if (random() < 0.50f) {
                            coins.add(new Coin(enemy.sprite.getX(), enemy.sprite.getY(), coinTexture, 1));
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

        // Enemy projectiles update and collision detection
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

    /**
     * Handles collisions for coins pickup and shop item purchases.
     */
    private void handleCollisions() {
        // Collect coins when overlapping the player
        coins.removeIf(coin -> {
            if (coin.getBounds().overlaps(Tony.getBounds())) {
                coinCount++;
                game.totalCoinsEarned++;
                return true;
            }
            return false;
        });

        // Purchase shop items if player has enough coins and overlaps the item
        Iterator<ShopItem> iter = shopItems.iterator();
        while (iter.hasNext()) {
            ShopItem item = iter.next();
            if (item.getBounds().overlaps(Tony.getBounds()) && coinCount >= item.getCost()) {
                coinCount -= item.getCost();
                applyShopEffect(item.getType());
                iter.remove();
            }
        }
    }

    /**
     * Applies the effect of a purchased shop item to the protagonist and game state.
     *
     * @param type The shop item type to apply.
     */
    private void applyShopEffect(int type) {
        switch (type) {
            case 0: // Heal
                Tony.health = Math.min(Tony.health + 10, Tony.max_health);
                game.health = Tony.health;
                break;
            case 1: // Power increase
                Tony.power += 1;
                game.power += 1;
                break;
            case 2: // Max health increase
                Tony.max_health += 5;
                Tony.health += 5;
                game.health += 5;
                game.max_health += 5;
                break;
            case 3: // Projectile multiplier increase
                Tony.projectile_multiplier += 1;
                game.projectile_multiplier += 1;
                break;
        }
    }

    /**
     * Updates all enemies each frame.
     *
     * @param delta Time elapsed since last frame.
     */
    private void updateEnemies(float delta) {
        for (Enemy e : enemies) {
            e.update(delta, walls);
            e.pushOutFromWalls(walls);
        }
    }

    /**
     * Draws all game world objects including background, player, enemies, coins, walls, projectiles, and shop items.
     */
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

        for (ShopItem item : shopItems)
            item.render(spriteBatch);

        for (Projectile p : enemyProjectiles)
            p.render(spriteBatch);

        spriteBatch.end();
    }

    /**
     * Draws the user interface including weapon info, cooldown, and coin count.
     */
    private void drawUI() {
        Weapon currentWeapon = Tony.getCurrentWeapon();
        ui.setWeaponIcon(currentWeapon.getWeaponIcon());
        ui.setWeaponName(currentWeapon.getName());
        ui.setCooldown(currentWeapon.getCooldownPercent());
        ui.setCooldownBarPosition(new Vector2(Tony.getX() + 0.5f, Tony.getY() + 1.5f), viewport);
        ui.update(coinCount, Tony.health);
        ui.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        ui.resize(width, height);
    }

    @Override public void show() {}

    @Override public void pause() {}

    @Override public void resume() {}

    @Override public void hide() {}

    /**
     * Dispose of textures and resources when this screen is no longer needed.
     */
    @Override
    public void dispose() {
        ui.dispose();
        spriteBatch.dispose();
        Tony.dispose();
        enemyTexture.dispose();
        wallTexture.dispose();
        backgroundTexture.dispose();
        enemyFace.dispose();
        enemyTexture_ranged.dispose();
        enemyFace_ranged.dispose();
        coinTexture.dispose();
        enemy_proj_0.dispose();
        healIcon.dispose();
        powerIcon.dispose();
        healthUpIcon.dispose();
        multIcon.dispose();
        font.dispose();
    }
}
