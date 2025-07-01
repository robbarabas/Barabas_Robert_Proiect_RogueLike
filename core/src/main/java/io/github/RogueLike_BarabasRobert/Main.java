package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The main game class that extends LibGDX's Game class.
 *
 * This class manages the core game state such as player stats,
 * screen transitions, input handling (like fullscreen toggle),
 * and saving/loading of game data.
 */
public class Main extends Game {
    /** Total coins collected by the player during the current session. */
    public int totalCoins = 0;

    /** The current health of the player. */
    public int health = 10;

    /** The maximum health the player can have. */
    public int max_health = 10;

    /** The player's power level, affecting damage or abilities. */
    public int power = 1;

    /** Multiplier for projectiles fired by the player. */
    public int projectile_multiplier = 2;

    /** Current game stage or level number. */
    public int stage = 1;

    /** The filename of the current save file. */
    public String currentSaveName;

    /** Total number of enemies killed by the player in this session. */
    public int enemiesKilled = 0;

    /** Total coins earned across all stages or sessions. */
    public int totalCoinsEarned = 0;

    /** SpriteBatch used for rendering 2D graphics. */
    SpriteBatch spriteBatch;

    /** Viewport for handling screen resizing and aspect ratio. */
    FitViewport viewport;

    /**
     * Handles input each frame for global controls like fullscreen toggling.
     * Toggles fullscreen mode when the F11 key is pressed.
     */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1024, 720);
            } else {
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
                Gdx.graphics.setFullscreenMode(displayMode);
            }

            // Force current screen to resize and adjust input handling
            if (getScreen() != null) {
                getScreen().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
    }

    /**
     * Initializes game resources and sets the initial screen.
     * Called once when the application is created.
     */
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10); // World viewport with 16:10 ratio

        // Start with the main menu screen
        setScreen(new MainMenuScreen(this));
    }

    /**
     * Updates the viewport and propagates resize events to the active screen.
     *
     * @param width The new screen width in pixels
     * @param height The new screen height in pixels
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }

    /**
     * Called each frame to update game logic and render the current screen.
     * Also handles global input such as fullscreen toggling.
     */
    @Override
    public void render() {
        handleInput();  // Check for fullscreen toggle key
        logic();        // Update any game-specific logic if needed
        super.render(); // Render the active screen (e.g., MainMenuScreen)
    }

    /**
     * Placeholder for game update logic.
     * Currently empty, but can be expanded for global updates if needed.
     */
    private void logic() {
        // Implement any game-wide logic here
    }

    /**
     * Cleans up resources when the game is closed.
     * Also saves minimal game data (health and total coins) to a local file.
     */
    @Override
    public void dispose() {
        if (spriteBatch != null) spriteBatch.dispose();

        FileHandle file = Gdx.files.local("save.txt");
        file.writeString(health + "," + totalCoins, false);
    }

    /**
     * Automatically saves the current game state to a save file.
     * The save file includes health, total coins, max health, power,
     * projectile multiplier, stage, total coins, and enemies killed.
     *
     * The save file is stored in the "saves/" directory with the
     * currentSaveName as filename.
     */
    public void autoSave() {
        if (currentSaveName == null || currentSaveName.isEmpty()) return;

        FileHandle file = Gdx.files.local("saves/" + currentSaveName + ".save");
        file.writeString(
            health + "," +
                totalCoins + "," +
                max_health + "," +
                power + "," +
                projectile_multiplier + "," +
                stage + "," +
                totalCoins + "," +
                enemiesKilled,
            false
        );

        System.out.println("Auto-saved to: " + currentSaveName);
    }
}
