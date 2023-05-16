package src.brick_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;

public class CollisionChangeCamera extends CollisionStrategy {
    private static GameManager gameManager;
    private final Ball ball;
    private final WindowController windowController;
    private static int collidedWith = 0;
    private static final int COLLISIONS_TO_RESTORE_CAMERA = 4;
    private static final float ZOOM_WHEN_HIT = 1.2f;

    /**
     * Constructor
     * @param gameObjects holds all current gameobjects
     * @param gameManager the current game manager
     * @param ball the original ball object
     * @param windowController the game's window controller
     */
    public CollisionChangeCamera(GameObjectCollection gameObjects, GameManager gameManager, Ball ball,
                                 WindowController windowController) {
        super(gameObjects);
        CollisionChangeCamera.gameManager = gameManager;
        this.ball = ball;
        this.windowController = windowController;
    }

    /**
     * A function to define what happens when a ball collides with a brick
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball)
     * @param bricksCounter the counter for the number of bricks left in the game
     */
    @Override
    public void onCollisionEnter(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollisionEnter(collidedObj, colliderObj, bricksCounter);

        if (gameManager.getCamera() != null || colliderObj instanceof Ball && ((Ball) colliderObj).isPuck())
            return;
        Vector2 windowDimensions = windowController.getWindowDimensions();
        gameManager.setCamera(new Camera(ball, Vector2.ZERO, windowDimensions.mult(ZOOM_WHEN_HIT),
                windowDimensions));
    }

    /**
     * A counter for number of collisions the ball did since the effect was activated
     */
    public static void manageCollisionCounter() {
        if(gameManager.getCamera() == null) return;
        if (collidedWith++ == COLLISIONS_TO_RESTORE_CAMERA) {
            gameManager.setCamera(null);
            collidedWith = 0;
        }
    }
}
