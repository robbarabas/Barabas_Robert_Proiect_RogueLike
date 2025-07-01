package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ShopItem {
    private Rectangle bounds;
    private Sprite sprite;
    private int type; // 0 = heal, 1 = power, 2 = max_health, 3 = multiplier
    private int cost = 3;

    public ShopItem(float x, float y, Texture texture, int type) {
        this.type = type;
        this.sprite = new Sprite(texture);
        this.sprite.setSize(1, 1);
        this.sprite.setPosition(x, y);
        this.bounds = new Rectangle(x, y, 1, 1);
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getType() {
        return type;
    }

    public int getCost() {
        return cost;
    }
}

