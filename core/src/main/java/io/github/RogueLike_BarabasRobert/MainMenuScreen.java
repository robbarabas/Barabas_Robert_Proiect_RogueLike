package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.files.FileHandle;

public class MainMenuScreen extends ScreenAdapter {
    final Main game;
    Stage stage;
    Skin skin;
    InputMultiplexer multiplexer = new InputMultiplexer();

    public MainMenuScreen(Main game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // === Start Button ===
        TextButton startButton = new TextButton("Start Game", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new FirstScreen(game));
            }
        });

        // === Load Game Button ===
        TextButton loadButton = new TextButton("Load Game", skin);
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int[] values = loadGame();
                if (values != null) {
                    game.health = values[0];
                    game.totalCoins = values[1];
                    game.setScreen(new FirstScreen(game));
                    System.out.println("Loaded health: " + values[0] + ", coins: " + values[1]);
                } else {
                    System.out.println("No saved game found.");
                }
            }
        });

        // === Resolution Selector ===
        SelectBox<String> resolutionSelect = new SelectBox<>(skin);
        resolutionSelect.setItems("800x600", "1280x720", "1920x1080");
        resolutionSelect.setSelected("1280x720");

        resolutionSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String[] dims = resolutionSelect.getSelected().split("x");
                int width = Integer.parseInt(dims[0]);
                int height = Integer.parseInt(dims[1]);
                Gdx.graphics.setWindowedMode(width, height);
            }
        });

        // === Layout ===
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(startButton).width(200).height(60).padBottom(20).row();
        table.add(loadButton).width(200).height(60).padBottom(20).row();

        TextButton deleteButton = new TextButton("Delete Save", skin);
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FileHandle file = Gdx.files.local("save.txt");
                if (file.exists()) {
                    file.delete();
                    System.out.println("Save deleted.");
                } else {
                    System.out.println("No save to delete.");
                }
            }
        });

        table.add(deleteButton).width(200).height(60).padBottom(20).row();
        table.add(resolutionSelect).width(200).height(50).row();
        stage.addActor(table);
    }

    private int[] loadGame() {
        try {
            FileHandle file = Gdx.files.local("save.txt");
            if (!file.exists()) return null;

            String[] parts = file.readString().trim().split(",");
            int health = Integer.parseInt(parts[0]);
            int coins = Integer.parseInt(parts[1]);
            return new int[]{health, coins};
        } catch (Exception e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
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
