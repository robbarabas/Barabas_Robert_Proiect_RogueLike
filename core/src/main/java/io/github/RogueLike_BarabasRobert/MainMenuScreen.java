package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class MainMenuScreen extends ScreenAdapter {
    final Main game;
    Stage stage;
    Skin skin;
    InputMultiplexer multiplexer = new InputMultiplexer();

    public MainMenuScreen(Main game) {
        this.game = game;

        // Create stage with ScreenViewport (correct for UI)
        stage = new Stage(new ScreenViewport());

        // InputMultiplexer to allow future extensibility
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        // Debug UI layout
        stage.setDebugAll(true);

        // Load skin (ensure "ui/uiskin.json" exists)
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create Start Game button
        TextButton startButton = new TextButton("Start Game", skin);

        // Correct click detection
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen()); // Swap to game screen
            }
        });

        // Layout button using table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(startButton).width(200).height(60);

        stage.addActor(table);
    }
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
