package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.InputMultiplexer;

public class MainMenuScreen extends ScreenAdapter {
    final Main game;
    Stage stage;
    Skin skin;
    InputMultiplexer multiplexer = new InputMultiplexer();
    FileHandle savesDir;

    public MainMenuScreen(Main game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        savesDir = Gdx.files.local("saves/");
        if (!savesDir.exists()) {
            savesDir.mkdirs();
        }

        // === UI Components ===
        TextField saveNameInput = new TextField("", skin);
        saveNameInput.setMessageText("Enter save name");

        SelectBox<String> saveSelect = new SelectBox<>(skin);
        saveSelect.setItems(getSaveNames());

        // === Start Game Button ===
        TextButton startButton = new TextButton("Start Game (No Load)", skin);
        startButton.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {
                game.totalCoins = 0;
                game.health = 10;
                game.max_health=10;
                game.power=1;
                game.projectile_multiplier=2;
                game.stage=1;
                game.enemiesKilled=0;
                game.totalCoinsEarned=0;
                game.currentSaveName = "autosave"; // default autosave name
                game.autoSave(); // save initial state
                game.setScreen(new FirstScreen(game));
            }
        });

        // === Create Save Button ===
        TextButton createSaveButton = new TextButton("Create New Save", skin);
        createSaveButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String name = saveNameInput.getText().trim();
                if (!name.isEmpty()) {
                    FileHandle file = savesDir.child(name + ".save");
                    file.writeString(game.health + "," +
                        game.totalCoins + "," +
                        game.max_health + "," +
                        game.power + "," +
                        game.projectile_multiplier + "," +
                        game.stage + "," +
                        game.totalCoinsEarned + "," +
                        game.enemiesKilled, false);
                    saveSelect.setItems(getSaveNames());
                    System.out.println("Saved game as: " + name);
                    game.currentSaveName=name;
                }
            }
        });

        // === Load Save Button ===
        TextButton loadSaveButton = new TextButton("Load Save", skin);
        loadSaveButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                String selected = saveSelect.getSelected();
                if (selected != null && !selected.isEmpty()) {
                    int[] values = loadGame(selected);
                    if (values != null) {
                        game.health = values[0];
                        game.totalCoins = values[1];
                        game.max_health=values[2];
                        game.power=values[3];
                        game.projectile_multiplier=values[4];
                        game.stage=values[5];
                        game.totalCoinsEarned=values[6];
                      game.enemiesKilled=values[7];
                        game.currentSaveName = selected; // set active save
                        game.autoSave(); // optional initial save
                        game.setScreen(new FirstScreen(game));
                        System.out.println("Loaded: " + selected);
                    } else {
                        System.out.println("Failed to load selected save.");
                    }
                }
            }
        });


        // === Delete Save Button ===
        TextButton deleteSaveButton = new TextButton("Delete Save", skin);
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

        table.add(startButton).width(220).height(50).padBottom(15).row();
        table.add(new Label("Save Name:", skin)).padBottom(5).row();
        table.add(saveNameInput).width(220).padBottom(10).row();
        table.add(createSaveButton).width(220).height(40).padBottom(10).row();
        table.add(new Label("Select Save:", skin)).padBottom(5).row();
        table.add(saveSelect).width(220).padBottom(10).row();
        table.add(loadSaveButton).width(220).height(40).padBottom(10).row();
        table.add(deleteSaveButton).width(220).height(40).padBottom(20).row();
        table.add(resolutionSelect).width(220).height(40).row();

        stage.addActor(table);
    }

    private String[] getSaveNames() {
        FileHandle[] files = savesDir.list();
        return java.util.Arrays.stream(files)
            .filter(f -> f.extension().equals("save"))
            .map(FileHandle::nameWithoutExtension)
            .toArray(String[]::new);
    }

    private int[] loadGame(String name) {
        try {
            FileHandle file = savesDir.child(name + ".save");
            if (!file.exists()) return null;

            String[] parts = file.readString().trim().split(",");

            int []values=new int[8];
            for(int i=0;i<=7;i++)
            {
               values[i] =Integer.parseInt(parts[i]);
            }

            return values;
        } catch (Exception e) {
            System.err.println("Failed to load save: " + e.getMessage());
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
