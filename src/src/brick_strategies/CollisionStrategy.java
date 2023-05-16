package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;
import static danogl.collisions.Layer.STATIC_OBJECTS;

public class CollisionStrategy {
    private final GameObjectCollection gameObjects;

    /**
     * Constructor
     * @param gameObjects An object which holds all game objects of the game running.
     */
    public CollisionStrategy(GameObjectCollection gameObjects){
        this.gameObjects = gameObjects;
    }

    /**
     * When a brick detects a collision, this method is called and activates the current strategy.
     * It decrements the number of active bricks on the screen and removes the current brick from the screen.
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball)
     * @param bricksCounter the counter for the number of bricks left in the game
     */
    public void onCollisionEnter(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter){
        if(gameObjects.removeGameObject(collidedObj,STATIC_OBJECTS)){
            bricksCounter.decrement();
        }
    }
}
