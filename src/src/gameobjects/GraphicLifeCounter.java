package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import static danogl.collisions.Layer.UI;

public class GraphicLifeCounter extends GameObject {
    private int numOfLives; //presented number of life
    private static final int MAX_LIVES = 4;
    private final Counter livesCounter; //actual number of life
    private final GameObjectCollection gameObjects;
    private final GameObject[] hearts = new GameObject[MAX_LIVES];
    private static final Vector2 HEART_DIMENSIONS = new Vector2(40,40);
    private static final int HEARTS_SPACING = 45;


    /**
     * Constructor
     * @param widgetTopLeftCorner the top left corner of the left most heart
     * @param widgetDimensions the dimension of each heart
     * @param livesCounter the counter which holds current lives count
     * @param widgetRenderable the image renderable of the hearts
     * @param gameObjects the collection of all game objects currently in the game
     * @param numOfLives number of current lives
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner, Vector2 widgetDimensions, Counter livesCounter,
                       Renderable widgetRenderable, GameObjectCollection gameObjects, int numOfLives){

        super(widgetTopLeftCorner, widgetDimensions, widgetRenderable);

        this.numOfLives = numOfLives;
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
        createHearts();
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
        updateHearts();
    }

    /**
     * Creates the hearts display
     */
    private void createHearts(){
        for (int i = 0; i < livesCounter.value(); i++) {
            Vector2 topLeftCorner = new Vector2(getTopLeftCorner().x()+ i*HEARTS_SPACING,
                    getTopLeftCorner().y());
            hearts[i] = new GameObject(topLeftCorner, HEART_DIMENSIONS, renderer().getRenderable());
            gameObjects.addGameObject(hearts[i],UI);
        }
    }

    /**
     * Updates the hearts display
     */
    private void updateHearts(){
        numOfLives = livesCounter.value();
        if(numOfLives <MAX_LIVES){
            if(hearts[numOfLives] != null){
                gameObjects.removeGameObject(hearts[numOfLives],UI);
            }
        }
    }

    /**
     * creates a graphic heart icon in the right place
     * @param spot the place to create the heart
     */
    public void createHeart(int spot) {
        Vector2 location = new Vector2(getTopLeftCorner().x()+ spot*HEARTS_SPACING, getTopLeftCorner().y());
        hearts[spot] = new GameObject(location, HEART_DIMENSIONS, renderer().getRenderable());
        gameObjects.addGameObject(hearts[numOfLives],UI);
    }
}
