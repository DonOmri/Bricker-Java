package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;

public class NumericLifeCounter extends GameObject {
    private final TextRenderable textRenderable = new TextRenderable("");
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private static final String LIVES_TEXT = "Lives: ";

    /**
     * Constructor
     * @param livesCounter The counter of how many lives are left right now.
     * @param topLeftCorner the top left corner of the position of the text object
     * @param dimensions the size of the text object
     * @param gameObjectCollection the collection of all game objects currently in the game
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection){
        super(topLeftCorner, dimensions,null);

        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.renderer().setRenderable(textRenderable);
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
        updateText();
    }

    /**
     * Updates the text with the relevant number of lives, and the appropriate color
     */
    public void updateText() {
        switch (livesCounter.value()) {
            case 3:
                textRenderable.setColor(Color.green);
                break;
            case 2:
                textRenderable.setColor(Color.yellow);
                break;
            case 1:
                textRenderable.setColor(Color.red);
                break;
            case 0:
                gameObjectCollection.removeGameObject(this);
            default:
                break;
        }
        textRenderable.setString(LIVES_TEXT + livesCounter.value());
    }
}
