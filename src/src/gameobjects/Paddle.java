package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
public class Paddle extends GameObject{
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;
    private final int minDistFromEdge;
    public static final int MOVE_SPEED = 350;

    /**
     * Constructor
     * @param topLeftCorner the top left corner of the position of the text object
     * @param dimensions the size of the text object
     * @param renderable the image file of the paddle
     * @param inputListener The input listener which waits for user inputs and acts on them.
     * @param windowDimensions The dimensions of the screen, to know the limits for paddle movements.
     * @param minDistFromEdge Minimum distance allowed for the paddle from the edge of the walls
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, Vector2 windowDimensions, int minDistFromEdge){
        super(topLeftCorner, dimensions, renderable);

        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.minDistFromEdge = minDistFromEdge;
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
        Vector2 movementDir = CalcMovementDir();
        setVelocity(movementDir.mult(MOVE_SPEED));
    }

    /**
     * Calculates the position of the paddle, based on user input;
     * @return current location of the paddle
     */
    private Vector2 CalcMovementDir() {
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && getCenter().x() - getDimensions().x() >=
                -minDistFromEdge){
            movementDir =  movementDir.add(Vector2.LEFT);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && getCenter().x() + getDimensions().x()
                < windowDimensions.x()+minDistFromEdge){
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        return movementDir;
    }

    /**
     * A function to determine what happens when a ball collides with paddle
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
    }
}
