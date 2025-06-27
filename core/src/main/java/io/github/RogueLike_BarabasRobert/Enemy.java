package io.github.RogueLike_BarabasRobert;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.List;

public class Enemy {
    Sprite sprite;
    Sprite face;
    Protagonist target;
    float speed = 2.0f;
    float damageCooldown = 1f;
    float timeSinceLastHit = 0f;
    float health = 3;

    // Knockback and hit effect
    float knockbackX = 0;
    float knockbackY = 0;
    float knockbackTime = 0f;
    float knockbackDuration = 0.2f;

    float hitEffectTime = 0f;
    float hitEffectDuration = 0.15f;

    public Enemy(float x, float y, Texture texture, Texture face_texture, Protagonist target) {
        this.target = target;

        sprite = new Sprite(texture);
        sprite.setSize(1, 1);
        sprite.setPosition(x, y);

        face = new Sprite(face_texture);
        face.setSize(1, 1);
        face.setPosition(x, y);
    }

    public void update(float delta, List<Wall> walls) {
        timeSinceLastHit += delta;

        if (knockbackTime > 0) {
            // Apply knockback
            float newX = sprite.getX() + knockbackX * delta;
            float newY = sprite.getY() + knockbackY * delta;
            Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());

            boolean collides = false;
            for (Wall wall : walls) {
                if (futureBounds.overlaps(wall.getBounds())) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                sprite.setPosition(newX, newY);
            }
            knockbackTime -= delta;

        } else {
            // Move toward the protagonist
            float targetX = target.getX();
            float targetY = target.getY();
            float dx = targetX - sprite.getX();
            float dy = targetY - sprite.getY();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist > 0.1f) {
                dx /= dist;
                dy /= dist;

                // Try moving directly
                float newX = sprite.getX() + dx * speed * delta;
                float newY = sprite.getY() + dy * speed * delta;
                Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());

                boolean collides = false;
                for (Wall wall : walls) {
                    if (futureBounds.overlaps(wall.getBounds())) {
                        collides = true;
                        break;
                    }
                }

                if (!collides) {
                    sprite.setPosition(newX, newY);
                } else {
                    // Try alternate movement (axis-aligned)
                    float[] offsetsX = { dx, 0, dx, -dx, 0 };
                    float[] offsetsY = { 0, dy, -dy, dy, -dy };

                    for (int i = 0; i < offsetsX.length; i++) {
                        newX = sprite.getX() + offsetsX[i] * speed * 2*delta;
                        newY = sprite.getY() + offsetsY[i] * speed * 2*delta;
                        futureBounds.setPosition(newX, newY);

                        boolean altCollides = false;
                        for (Wall wall : walls) {
                            if (futureBounds.overlaps(wall.getBounds())) {
                                altCollides = true;
                                break;
                            }
                        }

                        if (!altCollides) {
                            sprite.setPosition(newX, newY);
                            break;
                        }
                    }
                }
            }

            if (dist > 0.3) {
                dx /= dist;
                dy /= dist;
                float newX = sprite.getX() + dx * speed * delta;
                float newY = sprite.getY() + dy * speed * delta;
                Rectangle futureBounds = new Rectangle(newX, newY, sprite.getWidth(), sprite.getHeight());

                boolean collides = false;
                for (Wall wall : walls) {
                    if (futureBounds.overlaps(wall.getBounds())) {
                        collides = true;
                        break;
                    }
                }

                if (!collides) {
                    sprite.setPosition(newX, newY);
                }
            }
        }

        face.setPosition(sprite.getX(), sprite.getY());

        // Reset size after hit effect
        if (hitEffectTime > 0) {
            hitEffectTime -= delta;
            if (hitEffectTime <= 0) {
                sprite.setSize(1f, 1f);
                face.setSize(1f, 1f);
            }
        }

        // Damage the player if overlapping
        if (sprite.getBoundingRectangle().overlaps(target.getBounds())) {
            if (timeSinceLastHit >= damageCooldown) {
                target.takeDamage(1);
                timeSinceLastHit = 0f;
            }
        }
    }


    public void takeDamage(float amount) {
        health -= amount;
        System.out.println("Enemy hit! Health: " + health);

        // Enlarge on hit
        sprite.setSize(1.2f, 1.2f);
        face.setSize(1.2f, 1.2f);
        hitEffectTime = hitEffectDuration;

        // Calculate knockback direction
        float dx = sprite.getX() - target.getX();
        float dy = sprite.getY() - target.getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist != 0) {
            dx /= dist;
            dy /= dist;
            float knockbackForce = 5f;
            knockbackX = dx * knockbackForce;
            knockbackY = dy * knockbackForce;
            knockbackTime = knockbackDuration;
        }


    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
        face.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
    public void moveBy(float dx, float dy) {
        sprite.translate(dx, dy);
        face.setPosition(sprite.getX(), sprite.getY());
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
    public void pushOutFromWalls(List<Wall> walls) {
        Rectangle bounds = getBounds();

        for (Wall wall : walls) {
            Rectangle wallBounds = wall.getBounds();

            if (bounds.overlaps(wallBounds)) {
                float overlapLeft = bounds.x + bounds.width - wallBounds.x;
                float overlapRight = wallBounds.x + wallBounds.width - bounds.x;
                float overlapTop = wallBounds.y + wallBounds.height - bounds.y;
                float overlapBottom = bounds.y + bounds.height - wallBounds.y;

                // Find the minimal overlap distance
                float minOverlap = Math.min(
                    Math.min(overlapLeft, overlapRight),
                    Math.min(overlapTop, overlapBottom)
                );

                // Push out along minimal overlap axis
                if (minOverlap == overlapLeft) {
                    moveBy(-overlapLeft, 0);
                } else if (minOverlap == overlapRight) {
                    moveBy(overlapRight, 0);
                } else if (minOverlap == overlapTop) {
                    moveBy(0, overlapTop);
                } else if (minOverlap == overlapBottom) {
                    moveBy(0, -overlapBottom);
                }

                // Update bounds after pushing out
                bounds = getBounds();
            }
        }
    }

}
