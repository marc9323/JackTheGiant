package scenes;

import com.awesometuts.jackthegiant.GameMain;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import helpers.GameInfo;

public class Gameplay implements Screen {

    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport; // takes above camera and displays what we see
    private Sprite[] bgs;
    private float lastYPosition; // of the backgrounds

    public Gameplay(GameMain game) {

        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2, 0);

        // pass width, height, and camera (mainCamera)
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        createBackgrounds();
    }

    void update(float dt) {
        moveCamera();
        checkBackgroundsOutOfBounds();
    }

    private void moveCamera() {
        mainCamera.position.y -= 3;
        System.out.println("Position: " + mainCamera.position.y);
    }

    void createBackgrounds() {
        bgs = new Sprite[3];

        for(int i = 0; i < bgs.length; i++) {
            bgs[i] = new Sprite((new Texture("Backgrounds/Game Bg.png")));
            // position one below the other, - we move camera downwards
            bgs[i].setPosition(0, -(i * bgs[i].getHeight()));
            lastYPosition = Math.abs(bgs[i].getY()); // make positive

        }
    }

    void drawBackgrounds() {
        for(int i = 0; i < bgs.length; i++) {
            game.getBatch().draw(bgs[i], bgs[i].getX(), bgs[i].getY());
        }
    }

    // are the backgrounds outside the cameras view?
    void checkBackgroundsOutOfBounds(){
        for(int i = 0; i < bgs.length; i++) {
            // do we need to reposition the background?
            if((bgs[i].getY() - bgs[i].getHeight() / 2f -5) > mainCamera.position.y) {
                float newPosition = bgs[i].getHeight() + lastYPosition;

                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // rendering code
        game.getBatch().begin();
        drawBackgrounds();
        game.getBatch().end();

        game.getBatch().setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}  // end gameplay
