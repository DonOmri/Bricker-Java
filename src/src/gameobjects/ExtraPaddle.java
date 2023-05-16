package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.CollisionExtraPaddle;
import static danogl.collisions.Layer.DEFAULT;

public class ExtraPaddle extends Paddle{
    private final GameObjectCollection gameObjects;
    private int hitCounter;
    private static final int INITIAL_HIT_COUNTER = 0;
    private static final int HITS_TO_DESTROY = 3;

    /**
     * Constructor
     * @param topLeftCorner    the top left corner of the position of the text object
     * @param dimensions       the size of the text object
     * @param renderable       the image file of the paddle
     * @param inputListener    The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge  Minimum distance allowed for the paddle from the edge of the walls
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge,
                       GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, minDistFromEdge);
        this.hitCounter = INITIAL_HIT_COUNTER;
        this.gameObjects = gameObjects;
    }

    /**
     * A function to determine what happens when a ball collides with extra paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        if(!(other instanceof Ball)) return;

        if(++hitCounter == HITS_TO_DESTROY) {
            hitCounter = INITIAL_HIT_COUNTER;
            gameObjects.removeGameObject(this,DEFAULT);
            CollisionExtraPaddle.canSpawnAgain();
        }
    }
}
