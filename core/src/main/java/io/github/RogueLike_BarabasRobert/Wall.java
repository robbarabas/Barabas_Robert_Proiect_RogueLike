package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Wall {
    private static final float BOUNDING_BOX_SCALE = 0.7f; // 80% of size
    Rectangle bounds;
    float x, y, width, height;
    boolean breakable;
    public Wall(float x, float y, float width, float height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Shrink bounding box and center it inside the sprite
        float shrinkX = width * (1 - BOUNDING_BOX_SCALE) / 2;
        float shrinkY = height * (1 - BOUNDING_BOX_SCALE) / 2;
        bounds = new Rectangle(x + shrinkX, y + shrinkY, width * BOUNDING_BOX_SCALE, height * BOUNDING_BOX_SCALE);
    }
    public Wall(float x, float y, float width, float height, boolean breakable) {

       this(x,y,width,height);
       this.breakable=breakable;
    }


    public Rectangle getBounds() {
        return bounds;
    }



    public boolean isBreakable() {
        return breakable;
    }

    public void render(SpriteBatch batch, Texture texture) {
        batch.draw(texture, x, y, width, height);
    }
}
