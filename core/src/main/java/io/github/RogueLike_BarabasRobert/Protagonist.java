package io.github.RogueLike_BarabasRobert;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Protagonist {
    Texture texture;
    Sprite sprite;

    public Protagonist() {
        texture = new Texture("Apple_body.png");
        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
    }

    public void update() {
        // Handle movement or animation here
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }
}
