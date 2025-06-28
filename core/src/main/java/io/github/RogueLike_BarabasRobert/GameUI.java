package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameUI {
    private Stage stage;
    private Skin skin;

    private Label coinLabel;
    private ProgressBar healthBar;
    private Image coinIcon, heartIcon;
    private ProgressBar cooldownBar;

    public GameUI() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Load icons
        coinIcon = new Image(new Texture("Coin.png"));
        heartIcon = new Image(new Texture("Health.png"));

        coinLabel = new Label("0", skin);
        coinLabel.setFontScale(2.5f);

        // === Health Bar ===
        // === Health Bar ===
        Drawable bg = skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 1);
        Drawable fg = skin.newDrawable("white", 1, 0, 0, 1);

// THIS IS IMPORTANT
        bg.setMinHeight(40);
        fg.setMinHeight(40);

        ProgressBar.ProgressBarStyle healthStyle = new ProgressBar.ProgressBarStyle();
        healthStyle.background = bg;
        healthStyle.knobBefore = fg;
        healthStyle.knob = new Image().getDrawable();

        healthBar = new ProgressBar(0, 100, 1, false, healthStyle);
        healthBar.setAnimateDuration(0.2f);
        healthBar.setValue(100);
// You can still set width here, or in the table
        healthBar.setWidth(150); // optional

        // === Cooldown Bar (starts off-screen) ===
        ProgressBar.ProgressBarStyle cdStyle = new ProgressBar.ProgressBarStyle();
        cdStyle.background = skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1);
        cdStyle.knobBefore = skin.newDrawable("white", 0.3f, 0.8f, 1f, 1);
        cdStyle.knob = new Image().getDrawable();
        cdStyle.background.setMinHeight(30);
        cdStyle.knobBefore.setMinHeight(30);
        cooldownBar = new ProgressBar(0f, 1f, 0.01f, false, cdStyle);
        cooldownBar.setValue(0f);
        cooldownBar.setAnimateDuration(0.01f);
        cooldownBar.setWidth(50);
        cooldownBar.setHeight(30);
        stage.addActor(cooldownBar);

        // === Background Panel ===
        Table panel = new Table();
        Drawable panelBg = skin.newDrawable("white", 0, 0, 0, 0.5f);
        panel.setBackground(panelBg);
        panel.pad(10);
        panel.top().left();
        panel.setFillParent(false);
        panel.setPosition(10, stage.getViewport().getWorldHeight() - 10);

        panel.add(heartIcon).size(48, 48).padRight(5);
        panel.add(healthBar).width(150).padRight(20);
        panel.row().padTop(10);
        panel.add(coinIcon).size(48, 48).padRight(5);
        panel.add(coinLabel).left();

        stage.addActor(panel);
    }

    // === Public Methods ===
    public void update(int coins, int health) {
        coinLabel.setText(String.valueOf(coins));
        healthBar.setValue(health);
    }

    public void setCooldown(float percent) {
        cooldownBar.setValue(percent); // 0 to 1
    }

    public void setCooldownBarPosition(Vector2 worldPos, FitViewport viewport) {
        Vector2 screenPos = viewport.project(worldPos.cpy());
        cooldownBar.setPosition(screenPos.x - cooldownBar.getWidth() / 2, screenPos.y);
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    // === Optional: Set Crosshair Cursor ===
    public static void setCrosshairCursor(String path, int hotspotX, int hotspotY) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(path));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, hotspotX, hotspotY);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }
}
