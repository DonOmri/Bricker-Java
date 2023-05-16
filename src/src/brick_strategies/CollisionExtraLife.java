package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.ExtraLife;
import src.gameobjects.GraphicLifeCounter;
import src.gameobjects.NumericLifeCounter;
import static danogl.collisions.Layer.DEFAULT;

public class CollisionExtraLife extends CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final Vector2 location;
    private final Renderable heartImage;
    private final Vector2 windowDimensions;
    private final Counter livesCounter;
    private final NumericLifeCounter numericLifeCounter;
    private final GraphicLifeCounter graphicLifeCounter;
    private final int maxLives;

    /**
     * Constructor
     * @param gameObjects holds all current game objects
     * @param location location of the brick (where to create the heart)
     * @param heartImage renderable object holds the heart image
     * @param windowDimensions current dimensions of the window
     * @param livesCounter counter object holds current number of lives
     * @param numericLifeCounter the numeric counter
     * @param graphicLifeCounter the graphic counter
     * @param maxLives max lives possible
     */
    public CollisionExtraLife(GameObjectCollection gameObjects, Vector2 location, Renderable heartImage,
                              Vector2 windowDimensions, Counter livesCounter,
                              NumericLifeCounter numericLifeCounter, GraphicLifeCounter graphicLifeCounter,
                              int maxLives) {
        super(gameObjects);
        this.gameObjects = gameObjects;
        this.location = location;
        this.heartImage = heartImage;
        this.windowDimensions = windowDimensions;
        this.livesCounter = livesCounter;
        this.numericLifeCounter = numericLifeCounter;
        this.graphicLifeCounter = graphicLifeCounter;
        this.maxLives = maxLives;
    }

    /**
     * A function to define what happens on collision with ball
     * @param collidedObj the object that was collided (the heart)
     * @param colliderObj the object that collided with the heart (the paddle)
     * @param bricksCounter the counter for the number of bricks left in the game
     */
    @Override
    public void onCollisionEnter(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollisionEnter(collidedObj, colliderObj, bricksCounter);
        ExtraLife extraLife = new ExtraLife(location,heartImage,windowDimensions,gameObjects, livesCounter,
                numericLifeCounter, graphicLifeCounter, maxLives);
        gameObjects.addGameObject(extraLife, DEFAULT);
    }
}
