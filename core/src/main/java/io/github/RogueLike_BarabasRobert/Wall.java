package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 * Represents a wall in the game world.
 *
 * Walls have a position and size, a bounding box for collision detection,
 * and can optionally be breakable.
 */
public class Wall {
    /** Scale factor for the bounding box size relative to the wall's size (60%) */
    private static final float BOUNDING_BOX_SCALE = 0.6f;

    /** The bounding rectangle used for collision detection */
    Rectangle bounds;

    /** Position and size */
    float x, y, width, height;

    /** Whether the wall can be broken or destroyed */
    boolean breakable;

    /**
     * Creates an unbreakable wall with specified position and size.
     *
     * The bounding box is shrunk and centered inside the wall's sprite for better collision accuracy.
     *
     * @param x The X coordinate of the wall
     * @param y The Y coordinate of the wall
     * @param width The width of the wall
     * @param height The height of the wall
     */
    public Wall(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Calculate a smaller bounding box centered inside the wall sprite
        float shrinkX = width * (1 - BOUNDING_BOX_SCALE) / 2;
        float shrinkY = height * (1 - BOUNDING_BOX_SCALE) / 2;
        bounds = new Rectangle(x + shrinkX, y + shrinkY, width * BOUNDING_BOX_SCALE, height * BOUNDING_BOX_SCALE);
    }

    /**
     * Creates a wall with specified position, size, and breakable property.
     *
     * @param x The X coordinate of the wall
     * @param y The Y coordinate of the wall
     * @param width The width of the wall
     * @param height The height of the wall
     * @param breakable Whether the wall can be broken/destroyed
     */
    public Wall(float x, float y, float width, float height, boolean breakable) {
        this(x, y, width, height);
        this.breakable = breakable;
    }

    /**
     * Gets the bounding box of the wall used for collision detection.
     *
     * @return The bounding Rectangle of the wall
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Returns whether the wall is breakable.
     *
     * @return true if the wall is breakable, false otherwise
     */
    public boolean isBreakable() {
        return breakable;
    }

    /**
     * Renders the wall using the given SpriteBatch and texture.
     *
     * @param batch The SpriteBatch used for rendering
     * @param texture The texture to draw for the wall
     */
    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, x, y, width, height);
    }
}
