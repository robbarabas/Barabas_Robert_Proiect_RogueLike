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

public class Deathscreen implements Screen {
    private final Main game;
    private Stage stage;
    private int stageNumber;
    private int coinsEarned;
    private int enemiesKilled;

    public Deathscreen(Main game, int stageNumber, int coinsEarned, int enemiesKilled) {
        this.game = game;
        this.stageNumber = stageNumber;
        this.coinsEarned = coinsEarned;
        this.enemiesKilled = enemiesKilled;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json")); // Or your custom skin

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

        TextButton mainMenuButton = new TextButton("Return to Main Menu", skin);
        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {


                game.setScreen(new MainMenuScreen(game)); // Replace with your actual MainMenuScreen
            }
        });

        table.add(title).pad(10).row();
        table.add(stats).pad(10).row();
        table.add(mainMenuButton).pad(10);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
}
