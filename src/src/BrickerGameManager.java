package src;

import danogl.components.CoordinateSpace;
import src.brick_strategies.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import static danogl.collisions.Layer.*;

public class BrickerGameManager extends GameManager {
    private UserInputListener inputListener;
    private SoundReader soundReader;
    private ImageReader imageReader;
    private static final int BORDER_SIZE = 20;

    /**Window Properties**/
    private WindowController windowController;
    private Vector2 windowDimensions;
    private static final Vector2 WINDOW_DIMENSIONS = new Vector2(700, 500);
    private static final String WINDOW_TITLE = "Bricker";
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";

    /**Ball Properties**/
    private Ball ball;
    private static final Vector2 BALL_DIMENSIONS = new Vector2(20, 20);
    private static final int BALL_SPEED = 200;
    private static final String BALL_IMAGE_PATH = "assets/ball.png";
    private static final String BALL_SOUND_PATH = "assets/blop_cut_silenced.wav";

    /**Paddle Properties**/
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100,10);
    private static final int MIN_DIST_FROM_EDGE = 45;
    private static final String PADDLE_IMAGE_PATH = "assets/paddle.png";
    private static final String EXTRA_PADDLE_IMAGE_PATH = "assets/botGood.png";

    /**Prompt Message Properties**/
    private static final String WIN = "You Win!";
    private static final String LOSE = "You Lose!";
    private static final String PLAY_AGAIN = " Play Again?";

    /**Life Properties**/
    private static final String HEART_IMAGE_PATH = "assets/heart.png";
    private static final Counter LIVES_COUNTER = new Counter();
    private static final int INITIAL_LIFE_COUNT = 3;
    private static final int MAX_LIVES = 4;
    private static final Vector2 NUMBER_DIMENSIONS = new Vector2(20,20);
    private GraphicLifeCounter graphicLifeCounter;
    private NumericLifeCounter numericLifeCounter;

    /**Brick Properties**/
    private static final int BRICK_ROWS = 8;
    private static final int BRICK_COLS = 7;
    private static final String BRICK_IMAGE_PATH = "assets/brick.png";
    private static final Counter BRICKS_COUNTER = new Counter();
    private static final Vector2 BRICK_DIMENSIONS = new Vector2(80, 20);
    private static final Vector2 UPPER_LEFT_BRICK_LOCATION = new Vector2(65, 30);
    private static final int BRICKS_SPACING = 3;

    /**Behaviours Constants**/
    private final static int STANDARD = 0;
    private final static int PUCKS = 1;
    private final static int EXTRA_PADDLE = 2;
    private final static int CHANGE_CAMERA = 3;
    private final static int EXTRA_LIFE = 4;
    private final static int DOUBLE_BEHAVIOUR = 5;
    private final static int BEHAVIOURS = 6;
    private final static int MAX_BEHAVIOURS = 3;

    /**
     * The main function to run a game
     * @param args given arguments by user
     */
    public static void main(String[] args) {
        new BrickerGameManager(WINDOW_TITLE, WINDOW_DIMENSIONS).run();
    }

    /**
     * Constructor of the game manager
     * @param windowTitle name of the game (displayed in the actual window)
     * @param windowDimensions dimensions of the window
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        super(windowTitle, windowDimensions);
    }

    /**
     * The update method that is called every frame, to render the game in the window.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
    }

    /**
     * The function to initialize a new game.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();

        LIVES_COUNTER.reset();
        LIVES_COUNTER.increaseBy(INITIAL_LIFE_COUNT);

        createBackground(imageReader);
        createBorders();
        createGraphicLifeCounter(imageReader);
        createNumericLifeCounter();
        createPaddle(imageReader, inputListener);
        createBall();
        createBricks(imageReader);
    }

    /**
     * Checks whether a game has finished. if it does, close the game window.
     */
    private void checkForGameEnd() {
        double ballHeight = ball.getCenter().y();
        String prompt = "";

        if(inputListener.isKeyPressed(KeyEvent.VK_W) || BRICKS_COUNTER.value() == 0) prompt = WIN;

        if(ballHeight > windowDimensions.y()) {
            LIVES_COUNTER.decrement();
            ball.setBallVelocity();
            ball.setCenter(windowDimensions.mult(0.5f));
        }

        if(LIVES_COUNTER.value() == 0) prompt = LOSE;

        if(!prompt.isEmpty()){
            prompt += PLAY_AGAIN;
            if(windowController.openYesNoDialog(prompt)) windowController.resetGame();
            else windowController.closeWindow();
        }
    }

    /**
     * Creates a ball object
     */
    private void createBall() {
        Renderable ballImage = imageReader.readImage(BALL_IMAGE_PATH,true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND_PATH);
        ball = new Ball(Vector2.ZERO,BALL_DIMENSIONS, BALL_SPEED, ballImage,collisionSound,false);
        ball.setCenter(windowDimensions.mult(0.5f));
        gameObjects().addGameObject(ball,DEFAULT);
    }

    /**
     * Creates the borders of the game
     */
    private void createBorders() {
        //left border
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(BORDER_SIZE, windowDimensions.y()), null),STATIC_OBJECTS);

        //right border
        gameObjects().addGameObject(new GameObject(
                new Vector2(windowDimensions.x()-BORDER_SIZE, 0),
                new Vector2(BORDER_SIZE, windowDimensions.y()), null),STATIC_OBJECTS);

        //upper border
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), BORDER_SIZE), null),STATIC_OBJECTS);
    }

    /**
     * Creates the paddle controlled by the user
     * @param imageReader an object for the visual asset
     * @param inputListener an object for detecting key pressing by the user
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE_PATH,false);
        GameObject paddle = new Paddle(Vector2.ZERO, PADDLE_DIMENSIONS,paddleImage,inputListener,
                windowDimensions,MIN_DIST_FROM_EDGE);
        paddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y() - 10));
        gameObjects().addGameObject(paddle,DEFAULT);
    }

    /**
     * Creates the background of the game
     * @param imageReader an object for the visual asset
     */
    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage = imageReader.readImage(BACKGROUND_IMAGE_PATH,false);
        GameObject background = new GameObject(Vector2.ZERO,windowDimensions,backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background,BACKGROUND);
    }

    /**
     * Creates the hearts representation of the life counter
     * @param imageReader an object for the visual asset
     */
    private void createGraphicLifeCounter(ImageReader imageReader) {
        Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH,true);
        Vector2 leftHeartLocation = new Vector2(20, windowDimensions.y()-50);

        graphicLifeCounter = new GraphicLifeCounter(leftHeartLocation,Vector2.ZERO,
                LIVES_COUNTER,heartImage,gameObjects(),INITIAL_LIFE_COUNT);
        gameObjects().addGameObject(graphicLifeCounter,UI);
    }

    /**
     * Creates the numeric representation of the life counter
     */
    private void createNumericLifeCounter() {
        Vector2 numberLocation = new Vector2(25, windowDimensions.y() - 85);

        numericLifeCounter = new NumericLifeCounter(LIVES_COUNTER,numberLocation,
                NUMBER_DIMENSIONS,gameObjects());
        gameObjects().addGameObject(numericLifeCounter,UI);
    }

    /**
     * Creates the bricks in the game, by demands in pdf.
     * @param imageReader an object for the visual asset
     */
    private void createBricks(ImageReader imageReader) {
        BRICKS_COUNTER.reset();
        BRICKS_COUNTER.increaseBy(BRICK_ROWS*BRICK_COLS);

        Renderable brickImage = imageReader.readImage(BRICK_IMAGE_PATH,false);

        for (int row = 0; row < BRICK_ROWS; ++row) {
            for (int col = 0; col < BRICK_COLS; ++col) {
                Vector2 location = new Vector2(UPPER_LEFT_BRICK_LOCATION.x() +
                        (BRICK_DIMENSIONS.x()+BRICKS_SPACING)*col,UPPER_LEFT_BRICK_LOCATION.y() +
                        (BRICK_DIMENSIONS.y()+BRICKS_SPACING)*row);
                CollisionStrategy[] collisionStrategy = createStrategy(location);
                Brick newBrick = new Brick(location, BRICK_DIMENSIONS, brickImage, collisionStrategy,
                        BRICKS_COUNTER);

                gameObjects().addGameObject(newBrick,STATIC_OBJECTS);
            }
        }
    }

    /**
     * A helper function to create the collision strategies for a brick
     * @param location the location of the bricks to which apply the collision strategy
     * @return a collision strategy array, containing 1-3 strategies
     */
    private CollisionStrategy[] createStrategy(Vector2 location) {
        CollisionStrategy[] collisionStrategy = new CollisionStrategy[MAX_BEHAVIOURS];
        getRandomStrategy(location, collisionStrategy, BEHAVIOURS,0, 0);
        return collisionStrategy;
    }

    /**
     * A function to generate the actual collision strategies
     * @param location location of the brick to which apply the strategy
     * @param collisionStrategies the current applied strategies
     * @param pool max int for the random to choose from (3rd roll should not include the double behaviour)
     * @param index current index to insert a strategy into
     * @param timesRolled how much double behaviour rolls already occured
     */
    private void getRandomStrategy(Vector2 location, CollisionStrategy[] collisionStrategies,
                                   int pool, int index, int timesRolled) {
        Random rand = new Random();
        switch(rand.nextInt(pool)) {
            case STANDARD:
                collisionStrategies[index] = new CollisionStrategy(gameObjects());
                break;
            case PUCKS:
                collisionStrategies[index] = new CollisionPucks(gameObjects(), location, imageReader,
                        soundReader);
                break;
            case EXTRA_PADDLE:
                Renderable paddleImage = imageReader.readImage(EXTRA_PADDLE_IMAGE_PATH,false);
                collisionStrategies[index] = new CollisionExtraPaddle(gameObjects(),
                        windowDimensions.mult(0.5f), PADDLE_DIMENSIONS, paddleImage, inputListener,
                        windowDimensions, MIN_DIST_FROM_EDGE);
                break;
            case CHANGE_CAMERA:
                collisionStrategies[index] = new CollisionChangeCamera(gameObjects(),this, ball,
                        windowController);
                break;
            case EXTRA_LIFE:
                Renderable heartImage = imageReader.readImage(HEART_IMAGE_PATH,true);
                collisionStrategies[index] = new CollisionExtraLife(gameObjects(), location, heartImage,
                        windowDimensions, LIVES_COUNTER, numericLifeCounter, graphicLifeCounter, MAX_LIVES);
                break;
            case DOUBLE_BEHAVIOUR:
                if(timesRolled == 0) {
                    getRandomStrategy(location, collisionStrategies, pool, index, ++timesRolled);
                    getRandomStrategy(location, collisionStrategies, pool, ++index, timesRolled);
                }
                else {
                    getRandomStrategy(location, collisionStrategies, pool - 1, index, timesRolled);
                    getRandomStrategy(location, collisionStrategies, pool - 1, MAX_BEHAVIOURS-1,
                            timesRolled);
                }
                break;
        }
    }
}
