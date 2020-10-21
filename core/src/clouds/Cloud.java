package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

public class Cloud extends Sprite {
    private World world;
    private Body body;
    private String cloudName;

    public Cloud(World world, String cloudName) {
        super(new Texture("Clouds/" + cloudName + ".png"));
        this.world = world;
        this.cloudName = cloudName;
    }

    void createBody() {
        // define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set((getX() - 40) / GameInfo.PPM,
                getY() / GameInfo.PPM);

        // let the world create the body
        body = world.createBody(bodyDef);

        // do the shape
        PolygonShape shape = new PolygonShape();
        // size shape according to width of cloud
        shape.setAsBox((getWidth() / 2 - 20) / GameInfo.PPM,
                (getHeight() / 2) / GameInfo.PPM);

        // assign the shape to a fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        // use fixture to create the fixture
        Fixture fixture = body.createFixture(fixtureDef);

        // we no longer need the shape
        shape.dispose();
    }

    public void setSpritePosition(float x, float y) {
        setPosition(x, y);
        createBody();
    }

    public String getCloudName() {
        return this.cloudName;
    }

} // end Cloud class
