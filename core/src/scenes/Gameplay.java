package scenes;

import com.awesometuts.jackthegiant.GameMain;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.management.MBeanServerDelegateMBean;

import clouds.Cloud;
import clouds.CloudsController;
import helpers.GameInfo;
import player.Player;

public class Gameplay implements Screen {

    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport; // takes above camera and displays what we see

    // for clouds
    private OrthographicCamera box2dCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private Sprite[] bgs;
    private float lastYPosition; // of the backgrounds

    private CloudsController cloudsController;
    private Player player;


    public Gameplay(GameMain game) {

        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH / 2, GameInfo.HEIGHT / 2, 0);

        // pass width, height, and camera (mainCamera)
        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        // box2d camera
        box2dCamera = new OrthographicCamera();
        box2dCamera.setToOrtho(false, GameInfo.WIDTH / GameInfo.PPM,
                GameInfo.HEIGHT / GameInfo.PPM);
        box2dCamera.position.set(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f, 0);

        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, -9.8f), true); // world gravity

//        c = new Cloud(world, "Cloud 1");
//        c.setSpritePosition(GameInfo.WIDTH / 2f, GameInfo.HEIGHT / 2f);
         cloudsController = new CloudsController(world);
         player = cloudsController.positionThePlayer(player);
         createBackgrounds();
    }

    void handleInput(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            player.setWalking(true);
            player.movePlayer(-2);
        } else  if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            player.setWalking(true);
            player.movePlayer(2);
        } else {
            player.setWalking(false);
        }
    }

    void update(float dt) {
        handleInput(dt);
        moveCamera();
        checkBackgroundsOutOfBounds();
        cloudsController.setCameraY(mainCamera.position.y);
        cloudsController.createAndArrangeNewClouds();
    }

    private void moveCamera() {
        mainCamera.position.y -= 1.5f;

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
//        game.getBatch().draw(c, c.getX() - c.getWidth() / 2f,
//                c.getY() - c.getHeight() / 2f);

        cloudsController.drawClouds(game.getBatch());

        player.drawPlayerIdle(game.getBatch());
        player.drawPlayerAnimation(game.getBatch());

        game.getBatch().end();

        debugRenderer.render(world, box2dCamera.combined);  // world, projection matrix
        game.getBatch().setProjectionMatrix(mainCamera.combined);


        mainCamera.update();

        player.updatePlayer();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2); // higher the more precise box2d - performance hit

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
