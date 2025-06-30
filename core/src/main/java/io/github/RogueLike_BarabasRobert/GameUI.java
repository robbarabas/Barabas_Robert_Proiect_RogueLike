package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameUI {
    private Stage stage;
    private Skin skin;

    private Label coinLabel;
    private Label weaponNameLabel;
    private ProgressBar healthBar;
    private ProgressBar cooldownBar;
    private Image coinIcon, heartIcon, weaponIconImage;

    public GameUI() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // === ICONS ===
        coinIcon = new Image(new Texture("Coin.png"));
        heartIcon = new Image(new Texture("Health.png"));
        weaponIconImage = new Image(); // Placeholder

        // === LABELS ===
        coinLabel = new Label("0", skin);
        coinLabel.setFontScale(2.5f);
        weaponNameLabel = new Label("Weapon: ???", skin);
        weaponNameLabel.setFontScale(1.5f);

        // === HEALTH BAR ===
        Drawable bg = skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 1);
        Drawable fg = skin.newDrawable("white", 1, 0, 0, 1);
        bg.setMinHeight(40);
        fg.setMinHeight(40);

        ProgressBar.ProgressBarStyle healthStyle = new ProgressBar.ProgressBarStyle();
        healthStyle.background = bg;
        healthStyle.knobBefore = fg;
        healthStyle.knob = new Image().getDrawable();

        healthBar = new ProgressBar(0, 100, 1, false, healthStyle);
        healthBar.setAnimateDuration(0.2f);
        healthBar.setValue(100);
        healthBar.setWidth(150);

        // === COOLDOWN BAR ===
        ProgressBar.ProgressBarStyle cdStyle = new ProgressBar.ProgressBarStyle();
        cdStyle.background = skin.newDrawable("white", 0.2f, 0.2f, 0.2f, 1);
        cdStyle.knobBefore = skin.newDrawable("white", 0.3f, 0.8f, 1f, 1);
        cdStyle.knob = new Image().getDrawable();
        cdStyle.background.setMinHeight(30);
        cdStyle.knobBefore.setMinHeight(30);

        cooldownBar = new ProgressBar(0f, 1f, 0.01f, false, cdStyle);
        cooldownBar.setValue(0f);
        cooldownBar.setAnimateDuration(0.01f);
        cooldownBar.setSize(50, 30);
        stage.addActor(cooldownBar);

        // === PANEL ===
        Table panel = new Table();
        panel.setBackground(skin.newDrawable("white", 0, 0, 0, 0.5f));
        panel.pad(10).top().left();
        panel.setFillParent(false);
        panel.setPosition(10, stage.getViewport().getWorldHeight() - 10);

        panel.add(heartIcon).size(48, 48).padRight(5);
        panel.add(healthBar).width(150).padRight(20);

        panel.row().padTop(10);
        panel.add(coinIcon).size(48, 48).padRight(5);
        panel.add(coinLabel).left();

        panel.row().padTop(5);
        panel.add(weaponIconImage).size(80, 80).padRight(5);


        stage.addActor(panel);
    }

    // === Public Methods ===
    public void update(int coins, int health) {
        coinLabel.setText(String.valueOf(coins));
        healthBar.setValue(health);
    }

    public void setCooldown(float percent) {
        cooldownBar.setValue(percent);
    }

    public void setCooldownBarPosition(Vector2 worldPos, FitViewport viewport) {
        Vector2 screenPos = viewport.project(worldPos.cpy());
        cooldownBar.setPosition(screenPos.x - cooldownBar.getWidth() / 2, screenPos.y);
    }

    public void setWeaponIcon(Texture texture) {
        if (texture != null) {
            weaponIconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        }
    }

    public void setWeaponName(String name) {
        if (name != null) {
            weaponNameLabel.setText("Weapon: " + name);
        }
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

    // === Cursor Utility ===
    public static void setCrosshairCursor(String path, int hotspotX, int hotspotY) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(path));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, hotspotX, hotspotY);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }
}
