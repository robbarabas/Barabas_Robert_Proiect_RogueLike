package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents an item available in the in-game shop.
 *
 * Each item has a type defining its effect, a cost,
 * and a visual representation (sprite) for rendering.
 */
public class ShopItem {
    /** Bounding rectangle used for collision or interaction detection */
    private Rectangle bounds;

    /** Sprite used to render the item */
    private Sprite sprite;

    /** Type of the item:
     *  0 = heal,
     *  1 = power,
     *  2 = max_health,
     *  3 = projectile multiplier
     */
    private int type;

    /** Cost in in-game currency to purchase this item */
    private int cost = 3;

    /**
     * Constructs a new ShopItem at the specified position, with a given texture and type.
     *
     * @param x X coordinate of the item's position.
     * @param y Y coordinate of the item's position.
     * @param texture Texture to use for the item's sprite.
     * @param type Integer representing the item type.
     */
    public ShopItem(float x, float y, Texture texture, int type) {
        this.type = type;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(1, 1);
        this.sprite.setPosition(x, y);
        this.bounds = new Rectangle(x, y, 1, 1);
    }

    /**
     * Renders the ShopItem's sprite using the provided SpriteBatch.
     *
     * @param batch SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Gets the bounding rectangle of the ShopItem for detecting interactions.
     *
     * @return The bounding Rectangle.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Returns the type of this ShopItem.
     *
     * @return Item type as an integer.
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the cost to purchase this ShopItem.
     *
     * @return The item cost.
     */
    public int getCost() {
        return cost;
    }
}
