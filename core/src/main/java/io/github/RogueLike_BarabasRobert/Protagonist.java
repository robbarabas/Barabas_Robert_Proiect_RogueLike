package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Protagonist {
    Texture texture;
    public Sprite s_body,s_eyes;
    float speed = 5f; // units per second

    public Protagonist() {
        texture = new Texture("Apple_body.png");
        s_body = new Sprite(texture);
        s_body.setSize(1, 1);
    }

    public void update(float delta) {
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += speed * delta;

        s_body.setPosition(s_body.getX() + dx, s_body.getY() + dy);
    }

    public void render(SpriteBatch batch) {
        s_body.draw(batch);
    }

    public void dispose() {
        texture.dispose();
    }
}
