package src.gameobjects;

import src.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class Brick extends GameObject {
    private final Counter counter;
    private final CollisionStrategy[] strategy;

    /**
     * Constructor
     * @param topLeftCorner the position in the window the top left corner of the object will be placed.
     * @param dimensions the 2d dimensions of the object on the screen.
     * @param renderable the image object to display on the screen.
     * @param strategy the strategy that will be used when the brick breaks.
     * @param counter Bricks Counter
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy[] strategy, Counter counter){
        super(topLeftCorner, dimensions, renderable);

        this.strategy = strategy;
        this.counter = counter;
    }

    /**
     * This is an override method for GameObject's onCollisionEnter. When the game detects a collision
     * between the two objects, it activates the strategy of the brick.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    public void onCollisionEnter(GameObject other, Collision collision){
        for (CollisionStrategy collisionStrategy : strategy) {
            if (collisionStrategy == null) return;
            collisionStrategy.onCollisionEnter(this, other, counter);
        }
    }
}
