package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen extends ScreenAdapter {
    final Main game; // reference to your main game class
    Stage stage;
    Skin skin;

    public MainMenuScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Use LibGDX default skin (or your own)
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Create UI elements
        TextButton startButton = new TextButton("Start Game", skin);

        // Add listener
        startButton.addListener(event -> {
            if (startButton.isPressed()) {
                game.setScreen(new FirstScreen()); // Launch game
                return true;
            }
            return false;
        });

        // Layout using a Table
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(startButton).width(200).height(60);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
