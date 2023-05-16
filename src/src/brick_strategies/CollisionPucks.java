package src.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import static javax.swing.JLayeredPane.DEFAULT_LAYER;

public class CollisionPucks extends CollisionStrategy{
    private static GameObjectCollection gameObjects;
    private final Vector2 position;
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    /**Pucks Properties**/
    private final static String PUCK_IMAGE_PATH = "assets/mockBall.png";
    private static final String PUCK_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final Vector2 PUCK_DIMENSIONS = new Vector2(30, 30);
    private static final float PUCK_SPEED = 200;
    private static final int PUCKS_FROM_BRICK = 1;

    /**
     * Constructor
     * @param gameObjects all the current game objects
     * @param position the position of the bricks that has this behaviour
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     */
    public CollisionPucks(GameObjectCollection gameObjects, Vector2 position, ImageReader imageReader,
                          SoundReader soundReader) {
        super(gameObjects);
        CollisionPucks.gameObjects = gameObjects;
        this.position = position;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }

    /**
     * A function to determine behaviour when the ball collides with a brick
     * @param collidedObj the object that was collided (the brick)
     * @param colliderObj the object that collided with the brick (the ball)
     * @param bricksCounter the counter for the number of bricks left in the game
     */
    @Override
    public void onCollisionEnter(GameObject collidedObj, GameObject colliderObj, Counter bricksCounter) {
        super.onCollisionEnter(collidedObj, colliderObj, bricksCounter);
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE_PATH,true);
        Sound sound = soundReader.readSound(PUCK_SOUND_PATH);

        for (int i = 0; i < PUCKS_FROM_BRICK; i++) {
            gameObjects.addGameObject(new Ball(position.add(new Vector2(i,0)), PUCK_DIMENSIONS, PUCK_SPEED,
                    puckImage, sound, true), DEFAULT_LAYER);
        }
    }
}
