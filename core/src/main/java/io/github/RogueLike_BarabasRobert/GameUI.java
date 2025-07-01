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

/**
 * Handles the game's user interface elements such as health bar, coin count, weapon info, and cooldown bar.
 *
 * This class manages UI rendering and updates through a Stage and Scene2D UI widgets.
 * It displays dynamic information related to the protagonist's status and game resources.
 */
public class GameUI {
    /** The stage that manages UI actors and handles input events for the UI. */
    private Stage stage;

    /** The skin providing UI widget styles and assets. */
    private Skin skin;

    /** Label displaying the current coin count. */
    private Label coinLabel;

    /** Label displaying the current weapon's name. */
    private Label weaponNameLabel;

    /** ProgressBar representing the protagonist's current health. */
    private ProgressBar healthBar;

    /** ProgressBar representing the cooldown status of the current weapon. */
    private ProgressBar cooldownBar;

    /** Image icon representing coins in the UI. */
    private Image coinIcon;

    /** Image icon representing health (heart) in the UI. */
    private Image heartIcon;

    /** Image showing the current weapon's icon. */
    private Image weaponIconImage;

    /** Reference to the protagonist whose stats are shown in the UI. */
    Protagonist Protag;

    /** Maximum health value used to configure the health bar range. */
    private float max_health = 10;

    /** Table layout panel that holds the UI components for easy layout and dynamic updates. */
    private Table panel;

    /**
     * Constructs the GameUI, initializing UI components and layout.
     * Sets up the stage, skin, icons, labels, progress bars, and main panel.
     */
    public GameUI() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Initialize icons
        coinIcon = new Image(new Texture("Coin.png"));
        heartIcon = new Image(new Texture("Health.png"));
        weaponIconImage = new Image(); // Placeholder, updated dynamically

        // Initialize labels
        coinLabel = new Label("0", skin);
        coinLabel.setFontScale(2.5f);
        weaponNameLabel = new Label("Weapon: ???", skin);
        weaponNameLabel.setFontScale(1.5f);

        // Initialize cooldown bar with custom style
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

        // Initialize main UI panel for layout
        panel = new Table();
        panel.setBackground(skin.newDrawable("white", 0, 0, 0, 0.5f));
        panel.pad(10).top().left();
        panel.setFillParent(false);
        panel.setPosition(10, stage.getViewport().getWorldHeight() - 10);
        stage.addActor(panel);
    }

    /**
     * Rebuilds the health bar UI element to match the protagonist's current max health.
     * Updates the panel layout to include the health bar along with icons and labels.
     * Called after the protagonist is set.
     */
    private void rebuildHealthBar() {
        max_health = Protag.max_health;

        Drawable bg = skin.newDrawable("white", 0.1f, 0.1f, 0.1f, 1);
        Drawable fg = skin.newDrawable("white", 1, 0, 0, 1);
        bg.setMinHeight(40);
        fg.setMinHeight(40);

        ProgressBar.ProgressBarStyle healthStyle = new ProgressBar.ProgressBarStyle();
        healthStyle.background = bg;
        healthStyle.knobBefore = fg;
        healthStyle.knob = new Image().getDrawable();

        healthBar = new ProgressBar(0, max_health, 1, false, healthStyle);
        healthBar.setAnimateDuration(0.2f);
        healthBar.setValue(Protag.health);
        healthBar.setWidth(150);

        panel.clearChildren();

        panel.add(heartIcon).size(48, 48).padRight(5);
        panel.add(healthBar).width(150).padRight(20);

        panel.row().padTop(10);
        panel.add(coinIcon).size(48, 48).padRight(5);
        panel.add(coinLabel).left();

        panel.row().padTop(5);
        panel.add(weaponIconImage).size(80, 80).padRight(5);
    }

    /**
     * Updates the UI elements with current coin count and protagonist health.
     *
     * @param coins  The current number of coins the player has.
     * @param health The current health value of the protagonist.
     */
    public void update(int coins, int health) {
        coinLabel.setText(String.valueOf(coins));
        if (healthBar != null) {
            healthBar.setValue(health);
        }
    }

    /**
     * Sets the value of the cooldown progress bar as a percentage (0 to 1).
     *
     * @param percent The cooldown progress percentage.
     */
    public void setCooldown(float percent) {
        cooldownBar.setValue(percent);
    }

    /**
     * Positions the cooldown bar on screen based on a world position.
     *
     * @param worldPos The world coordinates to position the cooldown bar near.
     * @param viewport The viewport used to convert world coordinates to screen coordinates.
     */
    public void setCooldownBarPosition(Vector2 worldPos, FitViewport viewport) {
        Vector2 screenPos = viewport.project(worldPos.cpy());
        cooldownBar.setPosition(screenPos.x - cooldownBar.getWidth() / 2, screenPos.y);
    }

    /**
     * Sets the weapon icon image shown in the UI.
     *
     * @param texture The texture to display as the weapon icon.
     */
    public void setWeaponIcon(Texture texture) {
        if (texture != null) {
            weaponIconImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        }
    }

    /**
     * Sets the weapon name label text.
     *
     * @param name The weapon's name to display.
     */
    public void setWeaponName(String name) {
        if (name != null) {
            weaponNameLabel.setText("Weapon: " + name);
        }
    }

    /**
     * Assigns the protagonist whose stats are displayed in the UI.
     * This also triggers a rebuild of the health bar to match the protagonist's max health.
     *
     * @param Protag The protagonist instance.
     */
    public void set_protag(Protagonist Protag) {
        this.Protag = Protag;
        rebuildHealthBar();
    }

    /**
     * Updates and draws the UI elements each frame.
     * Should be called from the game's render loop.
     */
    public void render() {
        stage.act();
        stage.draw();
    }

    /**
     * Handles resizing of the UI viewport.
     *
     * @param width  The new screen width.
     * @param height The new screen height.
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Releases resources used by the UI stage and skin.
     * Should be called when the UI is no longer needed.
     */
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    /**
     * Returns the Stage managed by this UI for input processing or additional UI actors.
     *
     * @return The Scene2D stage instance.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Utility method to set a custom crosshair mouse cursor from a given image file.
     *
     * @param path     The file path of the cursor image.
     * @param hotspotX The x-coordinate of the cursor hotspot.
     * @param hotspotY The y-coordinate of the cursor hotspot.
     */
    public static void setCrosshairCursor(String path, int hotspotX, int hotspotY) {
        Pixmap pixmap = new Pixmap(Gdx.files.internal(path));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, hotspotX, hotspotY);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }
}
