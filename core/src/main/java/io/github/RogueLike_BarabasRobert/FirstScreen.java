package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {
    Protagonist Tony;
    SpriteBatch spriteBatch;
    FitViewport viewport;

    public FirstScreen() {
        Tony = new Protagonist();
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(16, 10);
    }

    @Override
    public void show() {
        // Prepare your screen here.
    }


    @Override
    public void render(float delta) {
        Tony.update(delta);
        ScreenUtils.clear(Color.CLEAR_WHITE);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        Tony.render(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }
    @Override
    public void dispose() {
        spriteBatch.dispose();
        Tony.dispose();
    }
}
