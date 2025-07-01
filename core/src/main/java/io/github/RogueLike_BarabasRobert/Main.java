package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends Game {
    public int totalCoins = 0;
    public int health = 10;
    public int max_health=10;
    public int power=1;
    public int projectile_multiplier=2;
    public int stage=1;
    public String currentSaveName;
    public int enemiesKilled=0;
    public int totalCoinsEarned=0;


    SpriteBatch spriteBatch;
    FitViewport viewport;

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1024, 720);
            } else {
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode();
                Gdx.graphics.setFullscreenMode(displayMode);
            }

            // Force screen resize (important for Stage input)
            if (getScreen() != null) {
                getScreen().resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10); // Game world viewport


        // Start with menu screen
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (getScreen() != null) {
            getScreen().resize(width, height);
        }
    }

    @Override
    public void render() {
        handleInput();    // Fullscreen toggle
        logic();          // Game updates (only if needed here)
        super.render();   // Renders the active screen (MainMenuScreen or others)
    }

    private void logic() {

    }

    @Override
    public void dispose() {
        if (spriteBatch != null) spriteBatch.dispose();
        FileHandle file = Gdx.files.local("save.txt");
        file.writeString(health + "," + totalCoins, false);
    }


    public void autoSave() {
        if (currentSaveName == null || currentSaveName.isEmpty()) return;

        FileHandle file = Gdx.files.local("saves/" + currentSaveName + ".save");
        file.writeString(health + "," + totalCoins+ "," +max_health+","+power+ "," + projectile_multiplier+","+stage+","+totalCoins+","+enemiesKilled, false);
        System.out.println("Auto-saved to: " + currentSaveName);
    }

}
