package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import static danogl.collisions.Layer.DEFAULT;
import static danogl.collisions.Layer.UI;

public class ExtraLife extends GameObject {
    private final Vector2 windowDimensions;
    private static final Vector2 HEART_DIMENSIONS = new Vector2(40,40);
    private final GameObjectCollection gameObjects;
    private final Counter livesCounter;
    private final NumericLifeCounter numericLifeCounter;
    private final GraphicLifeCounter graphicLifeCounter;
    private final int maxLives;

    /**
     * Constructor
     * @param topLeftCorner top left corner of the heart object
     * @param renderable the heart image
     * @param windowDimensions dimensions of the current game
     * @param gameObjects all the game objects in the game
     * @param livesCounter a counter objects represnting the current life of the player
     * @param numericLifeCounter an object representing the currents lives as a number
     * @param graphicLifeCounter an object representing the currents lives as an amount of hearts
     * @param maxLives the maximum lives that allowed by the game manager
     */
    public ExtraLife(Vector2 topLeftCorner, Renderable renderable, Vector2 windowDimensions,
                     GameObjectCollection gameObjects, Counter livesCounter,
                     NumericLifeCounter numericLifeCounter, GraphicLifeCounter graphicLifeCounter,
                     int maxLives) {
        super(topLeftCorner, HEART_DIMENSIONS, renderable);
        this.windowDimensions = windowDimensions;
        this.gameObjects = gameObjects;
        this.livesCounter = livesCounter;
        this.numericLifeCounter = numericLifeCounter;
        this.graphicLifeCounter = graphicLifeCounter;
        this.maxLives = maxLives;
    }

    /**
     * Defines the extra heart to collide only with the original paddle
     * @param other The other GameObject.
     * @return true if the collided object is the original paddle
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle && !(other instanceof ExtraPaddle);
    }

    /**
     * The update method that is called every frame, to render the game in the window.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.setCenter(new Vector2(this.getCenter().x(), this.getCenter().y()+1));
        if(this.getCenter().y() > windowDimensions.y()) gameObjects.removeGameObject(this,UI);
    }

    /**
     * Defines the behaviour of the heart object when colliding with an object
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        gameObjects.removeGameObject(this, DEFAULT);

        if(livesCounter.value() == this.maxLives) return;

        graphicLifeCounter.createHeart(livesCounter.value());
        livesCounter.increment();
        numericLifeCounter.updateText();
    }
}
