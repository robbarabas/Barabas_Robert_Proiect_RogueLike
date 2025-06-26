package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends Game {
    Protagonist Tony;
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
        Tony = new Protagonist();

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
    }
}
