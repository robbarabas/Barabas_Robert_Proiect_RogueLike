package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Represents the game over screen displayed when the player dies.
 * Shows statistics such as stage reached, coins earned, and enemies killed,
 * and provides an option to return to the main menu.
 */
public class Deathscreen implements Screen {
    /** Reference to the main game class to switch screens. */
    private final Main game;

    /** Stage used for scene2d UI elements. */
    private Stage stage;

    /** The current stage/level number reached by the player. */
    private int stageNumber;

    /** Number of coins earned by the player in the run. */
    private int coinsEarned;

    /** Number of enemies killed by the player in the run. */
    private int enemiesKilled;

    /**
     * Constructs the Deathscreen with stats to display.
     *
     * @param game The main game instance for screen management.
     * @param stageNumber The stage number the player reached.
     * @param coinsEarned The number of coins collected by the player.
     * @param enemiesKilled The number of enemies killed by the player.
     */
    public Deathscreen(Main game, int stageNumber, int coinsEarned, int enemiesKilled) {
        this.game = game;
        this.stageNumber = stageNumber;
        this.coinsEarned = coinsEarned;
        this.enemiesKilled = enemiesKilled;
    }

    /**
     * Called when this screen becomes the current screen.
     * Sets up the UI elements and input handling.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // Use your skin file

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Game Over", skin);
        Label stats = new Label(
            "Stage: " + stageNumber + "\n" +
                "Coins Earned: " + coinsEarned + "\n" +
                "Enemies Killed: " + enemiesKilled,
            skin
        );
        stats.setFontScale(2f);

        TextButton mainMenuButton = new TextButton("Return to Main Menu", skin);
        mainMenuButton.sizeBy(2f);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game)); // Navigate to main menu screen
            }
        });

        table.add(title).pad(10).row();
        table.add(stats).pad(10).row();
        mainMenuButton.sizeBy(2f);
        table.add(mainMenuButton).pad(10);
    }

    /**
     * Renders the screen, clearing it and drawing the stage UI.
     *
     * @param delta Time elapsed since the last frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    /**
     * Adjusts viewport on window resize.
     *
     * @param width New width of the window.
     * @param height New height of the window.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Dispose of resources when no longer needed.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
