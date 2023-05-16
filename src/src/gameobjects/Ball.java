package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import src.brick_strategies.CollisionChangeCamera;
import java.util.Random;
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final float ballSpeed;
    private final boolean puck;

    /**
     * Constructor
     * @param topLeftCorner position of the top left corner of the ball in the window.
     * @param dimensions the dimensions of the ball
     * @param renderable the image object of the ball
     * @param sound the sound file object of the ball's collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, float ballSpeed, Renderable renderable,
                Sound sound, boolean puck){
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = sound;
        this.puck = puck;
        this.ballSpeed = ballSpeed;
        setBallVelocity();
    }

    /**
     * A function to define what happens when the ball collides with other object
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    public void onCollisionEnter(GameObject other, Collision collision){
        super.onCollisionEnter(other, collision);
        Vector2 velocity = getVelocity().flipped(collision.getNormal());
        setVelocity(velocity);
        if(!puck) CollisionChangeCamera.manageCollisionCounter();
        collisionSound.play();
    }

    /**
     * Creates the movement vector of the ball
     */
    public void setBallVelocity(){
        float ballVelX = ballSpeed;
        float ballVelY = ballSpeed;

        //randomize the initial ball direction
        Random rand = new Random();
        if(rand.nextBoolean()) ballVelX *= (-1);
        if(rand.nextBoolean()) ballVelY *= (-1);

        this.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * A quick check if a certain ball is a puck or original
     * @return true if the ball is puck, false otherwise
     */
    public boolean isPuck() {
        return puck;
    }
}
