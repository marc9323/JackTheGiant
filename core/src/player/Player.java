package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import helpers.GameInfo;

public class Player extends Sprite {

    private World world;
    private Body body;

    private TextureAtlas playerAtlas;
    private Animation animation;
    private float elapsedTime;

    private boolean isWalking;

    public Player(World world, float x, float y) {
        super(new Texture("Player/Player 1.png"));
        this.world = world;
        setPosition(x, y);

        createBody();
        playerAtlas = new TextureAtlas("Player Animation/player animation.atlas");
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() / GameInfo.PPM, getY() / GameInfo.PPM); // 1 by 100 ratio 1 meter = 100 pixels

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth() / 2f - 20) / GameInfo.PPM,
                (getHeight() / 2f) / GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 4;
        fixtureDef.friction = 2f;
        fixtureDef.shape = shape;

        Fixture fixture = body.createFixture(fixtureDef);

        // no longer need shape
        shape.dispose();
    }

    public void movePlayer(float x) {
        // as like below where we used linear velocity x to test
        if(x < 0 && !this.isFlipX()) {  // moving to the left, flip to face left
            this.flip(true, false);
        } else if(x > 0 && this.isFlipX()) {  // moving right, flip to the right
            this.flip(true, false);
        }

        isWalking = true;
        body.setLinearVelocity(x, body.getLinearVelocity().y);
    }

    public void drawPlayerIdle(SpriteBatch batch) {
        if(!isWalking) {
            batch.draw(this, getX() + getWidth()  / 2f,
                    getY() - getHeight() / 2f);
        }
    }
    public void drawPlayerAnimation(SpriteBatch batch) {
        if(isWalking) {
            elapsedTime += Gdx.graphics.getDeltaTime();

            Array<TextureAtlas.AtlasRegion> frames = playerAtlas.getRegions();

            for(TextureRegion frame : frames) {
                if(body.getLinearVelocity().x < 0
                        && !frame.isFlipX()) {
                    frame.flip(true, false);
                } else if(body.getLinearVelocity().x > 0
                        && frame.isFlipX()){
                    frame.flip(true, false);
                }
            }

            animation = new Animation(1f/10f, playerAtlas.getRegions());

            batch.draw((TextureRegion) animation.getKeyFrame(elapsedTime, true), getX() + getWidth() / 2f - 20f, getY() - getHeight() / 2f);

        }
    }

    public void updatePlayer() {
        this.setPosition(body.getPosition().x * GameInfo.PPM,
                body.getPosition().y * GameInfo.PPM);
    }

    public void setWalking(boolean isWalking) {
        this.isWalking = isWalking;
    }

}  // end player.Player class
