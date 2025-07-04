package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.InputMultiplexer;

import static com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable.draw;

/**
 * Main menu screen for the RogueLike game.
 *
 * This screen provides the player with options to start a new game,
 * create a new save, load an existing save, delete saves, and change
 * the game resolution. It uses LibGDX's Scene2D UI framework for
 * the interface.
 */
public class MainMenuScreen extends ScreenAdapter {

    /** Reference to the main game class, for changing screens and accessing game state. */
    final Main game;

    /** The stage that holds the UI actors and handles input/events. */
    Stage stage;

    /** UI skin for styling widgets. */
    Skin skin;

    /** Handles multiple input processors simultaneously, including the stage input. */
    InputMultiplexer multiplexer = new InputMultiplexer();

    /** Directory where save files are stored. */
    FileHandle savesDir;
    /** Texture for background. */
    Texture backgroundTexture;
    /** Batch to render background. */
    SpriteBatch backgroundBatch;

    /**
     * Constructs the main menu screen, initializes UI components,
     * loads existing saves, and sets up event listeners.
     *
     * @param game The main game instance
     */
    public MainMenuScreen(Main game) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal("background_menu.png"));
        backgroundBatch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        savesDir = Gdx.files.local("saves/");
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }

        // UI Components
        TextField saveNameInput = new TextField("", skin);
        saveNameInput.setMessageText("Enter save name");
        saveNameInput.setTextFieldListener((textField, c) -> {});
        saveNameInput.sizeBy(1.5f);

        SelectBox<String> saveSelect = new SelectBox<>(skin);
        saveSelect.setItems(getSaveNames());
        saveSelect.getList().sizeBy(1.5f);
        saveSelect.getStyle().font.getData().setScale(1.5f);

        // Labels
        Label saveLabel = new Label("Save Name:", skin);
        saveLabel.setFontScale(1.5f);

        Label selectSaveLabel = new Label("Select Save:", skin);
        selectSaveLabel.setFontScale(1.5f);

        // Buttons
        TextButton startButton = new TextButton("Start Game (No Load)", skin);
        startButton.getLabel().setFontScale(1.6f);
        startButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.totalCoins = 0;
                game.health = 10;
                game.max_health = 10;
                game.power = 1;
                game.projectile_multiplier = 2;
                game.stage = 1;
                game.enemiesKilled = 0;
                game.totalCoinsEarned = 0;
                game.currentSaveName = "autosave";
                game.autoSave();
                game.setScreen(new FirstScreen(game));
            }
        });

        TextButton createSaveButton = new TextButton("Create New Save", skin);
        createSaveButton.getLabel().setFontScale(1.6f);
        createSaveButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String name = saveNameInput.getText().trim();
                if (!name.isEmpty()) {
                    FileHandle file = savesDir.child(name + ".save");
                    file.writeString(
                        game.health + "," +
                            game.totalCoins + "," +
                            game.max_health + "," +
                            game.power + "," +
                            game.projectile_multiplier + "," +
                            game.stage + "," +
                            game.totalCoinsEarned + "," +
                            game.enemiesKilled,
                        false
                    );
                    saveSelect.setItems(getSaveNames());
                    game.currentSaveName = name;
                    System.out.println("Saved game as: " + name);
                }
            }
        });

        TextButton loadSaveButton = new TextButton("Load Save", skin);
        loadSaveButton.getLabel().setFontScale(1.6f);
        loadSaveButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String selected = saveSelect.getSelected();
                if (selected != null && !selected.isEmpty()) {
                    int[] values = loadGame(selected);
                    if (values != null) {
                        game.health = values[0];
                        game.totalCoins = values[1];
                        game.max_health = values[2];
                        game.power = values[3];
                        game.projectile_multiplier = values[4];
                        game.stage = values[5];
                        game.totalCoinsEarned = values[6];
                        game.enemiesKilled = values[7];
                        game.currentSaveName = selected;
                        game.autoSave();
                        game.setScreen(new FirstScreen(game));
                        System.out.println("Loaded: " + selected);
                    } else {
                        System.out.println("Failed to load selected save.");
                    }
                }
            }
        });

        TextButton deleteSaveButton = new TextButton("Delete Save", skin);
        deleteSaveButton.getLabel().setFontScale(1.6f);
        deleteSaveButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String selected = saveSelect.getSelected();
                if (selected != null && !selected.isEmpty()) {
                    FileHandle file = savesDir.child(selected + ".save");
                    if (file.exists()) {
                        file.delete();
                        saveSelect.setItems(getSaveNames());
                        System.out.println("Deleted save: " + selected);
                    }
                }
            }
        });

        SelectBox<String> resolutionSelect = new SelectBox<>(skin);
        resolutionSelect.setItems("800x600", "1280x720", "1920x1080");
        resolutionSelect.setSelected("1280x720");
        resolutionSelect.getList().sizeBy(1.5f);
        resolutionSelect.getStyle().font.getData().setScale(1.4f);
        resolutionSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String[] dims = resolutionSelect.getSelected().split("x");
                int width = Integer.parseInt(dims[0]);
                int height = Integer.parseInt(dims[1]);
                Gdx.graphics.setWindowedMode(width, height);
            }
        });

        // Layout
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(startButton).width(240).height(55).padBottom(15).row();
        table.add(saveLabel).padBottom(5).row();
        table.add(saveNameInput).width(240).padBottom(10).row();
        table.add(createSaveButton).width(240).height(45).padBottom(10).row();
        table.add(selectSaveLabel).padBottom(5).row();
        table.add(saveSelect).width(240).padBottom(10).row();
        table.add(loadSaveButton).width(240).height(45).padBottom(10).row();
        table.add(deleteSaveButton).width(240).height(45).padBottom(20).row();
        table.add(resolutionSelect).width(240).height(45).row();

        stage.addActor(table);
    }

    /**
     * Returns the list of save names (without file extensions) found
     * in the saves directory.
     *
     * @return Array of save names as Strings
     */
    private String[] getSaveNames() {
        FileHandle[] files = savesDir.list();
        return java.util.Arrays.stream(files)
            .filter(f -> f.extension().equals("save"))
            .map(FileHandle::nameWithoutExtension)
            .toArray(String[]::new);
    }

    /**
     * Loads a saved game state from the file with the given name.
     *
     * The save file should contain comma-separated integers representing:
     * health, totalCoins, max_health, power, projectile_multiplier,
     * stage, totalCoinsEarned, enemiesKilled (in that order).
     *
     * @param name The save file name (without extension)
     * @return An int array containing the loaded values, or null if loading fails
     */
    private int[] loadGame(String name) {
        try {
            FileHandle file = savesDir.child(name + ".save");
            if (!file.exists()) return null;

            String[] parts = file.readString().trim().split(",");

            int[] values = new int[8];
            for (int i = 0; i <= 7; i++) {
                values[i] = Integer.parseInt(parts[i]);
            }

            return values;
        } catch (Exception e) {
            System.err.println("Failed to load save: " + e.getMessage());
            return null;
        }
    }

    /** Called when this screen is no longer the current screen. */
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Called every frame to clear the screen and draw the stage actors.
     *
     * @param delta Time in seconds since the last frame
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundBatch.setProjectionMatrix(stage.getCamera().combined);
        backgroundBatch.begin();
        backgroundBatch.draw(backgroundTexture, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundBatch.end();

        stage.act(delta);
        stage.draw();
    }

    /**
     * Handles screen resizing events and updates the viewport accordingly.
     *
     * @param width  New screen width in pixels
     * @param height New screen height in pixels
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /** Releases all resources managed by this screen. */

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        backgroundBatch.dispose();
    }

}
