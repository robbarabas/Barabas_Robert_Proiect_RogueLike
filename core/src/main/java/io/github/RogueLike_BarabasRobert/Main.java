package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
   Protagonist Tony;
    SpriteBatch spriteBatch;
    FitViewport viewport;
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1024, 720); // Back to windowed mode
            } else {
                Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(); // Get default display mode
                Gdx.graphics.setFullscreenMode(displayMode); // Switch to fullscreen
            }
        }
    }
    @Override
    public void create() {

        Tony=new Protagonist();
        viewport = new FitViewport(16, 10);

        setScreen(new MainMenuScreen(this)); // Start with menu
        spriteBatch = new SpriteBatch();

    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // true centers the camera
    }
    @Override
    public void render() {
        // organize code into three methods
        input();
        handleInput();
        logic();
        super.render();
        //draw();
    }

    private void input() {

    }

    private void logic() {
        float delta = Gdx.graphics.getDeltaTime(); // Get time since last frame
        Tony.update(delta);
    }

    private void draw() {
        ScreenUtils.clear(Color.CLEAR_WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
       // spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // draw the background
      //  spriteBatch.draw(Texture_Tony, 0, 0, 1, 1);
        Tony.render(spriteBatch);
        spriteBatch.end();
    }
}
