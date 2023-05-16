package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.ExtraPaddle;

import static danogl.collisions.Layer.DEFAULT;

public class CollisionExtraPaddle extends CollisionStrategy{
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;
    private final GameObjectCollection gameObjects;
    private static boolean isSpawned = false;

    /**
     * Constructor
     * @param gameObjects contains all game objects curently in game
     * @param topLeftCorner the top left corner of the paddle to create
     * @param dimensions the dimensions of the paddle to create
     * @param renderable the extra paddle image
     * @param inputListener inputlistener to detect user input
     * @param windowDimensions the size of the game window
     * @param minDistFromEdge the distance from the edge of the window, for preventing extra paddle from
     *                        going outside the window.
     */
    public CollisionExtraPaddle(GameObjectCollection gameObjects, Vector2 topLeftCorner, Vector2 dimensions,
                                Renderable renderable, UserInputListener inputListener,
                                Vector2 windowDimensions, int minDistFromEdge) {
        super(gameObjects);
        this.gameObjects = gameObjects;
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen.
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball)
     * @param bricksCounter the counter for the number of bricks left in the game
     */
    @Override
    public void onCollisionEnter(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollisionEnter(collidedObj, colliderObj, bricksCounter);
        if(isSpawned) return;

        ExtraPaddle extraPaddle = new ExtraPaddle(topLeftCorner, dimensions, renderable, inputListener,
                windowDimensions, minDistFromEdge,gameObjects);
        gameObjects.addGameObject(extraPaddle,DEFAULT);
        isSpawned = true;
    }

    public static void canSpawnAgain(){
        isSpawned = false;
    }
}
