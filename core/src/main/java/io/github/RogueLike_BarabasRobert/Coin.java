package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a collectible coin in the game.
 *
 * Each coin has a position, a type (which can represent different coin values or effects NOT YET IMPLEMENTED),
 * and a visual representation using a sprite.
 */
public class Coin {
    /** The sprite used to draw the coin. */
    Sprite sprite;

    /** The type of the coin (e.g., different values or categories). */
    int type;

    /**
     * Constructs a Coin at the specified position with the given texture and type.
     *
     * @param x The x-coordinate of the coin's position.
     * @param y The y-coordinate of the coin's position.
     * @param texture The texture used for the coin's sprite.
     * @param type The type/category of the coin.
     */
    public Coin(float x, float y, Texture texture, int type) {
        sprite = new Sprite(texture);
        this.type = type;
        sprite.setSize(0.5f, 0.5f);
        sprite.setPosition(x, y);
    }

    /**
     * Draws the coin using the provided SpriteBatch.
     *
     * @param batch The SpriteBatch used for rendering.
     */
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * Returns the bounding rectangle of the coin's sprite.
     * Useful for collision detection.
     *
     * @return The bounding Rectangle of the coin.
     */
    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
