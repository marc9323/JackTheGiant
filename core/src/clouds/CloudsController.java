package clouds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import helpers.GameInfo;

public class CloudsController {

    private World world;
    // resizable dynamic array
    private Array<Cloud> clouds = new Array<Cloud>();

    private final float DISTANCE_BETWEEN_CLOUDS = 250;

    public CloudsController(World world) {
        this.world = world;
        createClouds();
        positionClouds();
    }

    void createClouds() {
        for(int i = 0; i < 2; i++) {
            clouds.add(new Cloud(world, "Dark Cloud"));
        }

        int index = 1;

        for(int i = 0; i < 6; i++) {
            clouds.add(new Cloud(world, "Cloud " + index));
            index++;

            if(index == 4) {
                index = 1;
            }
        }

        clouds.shuffle();
    }

    public void positionClouds() {
        float positionY = GameInfo.HEIGHT / 2;
        float tempX = GameInfo.WIDTH / 2;

        for(Cloud c : clouds) {
            c.setSpritePosition(tempX, positionY);

            positionY -= DISTANCE_BETWEEN_CLOUDS;
        }
    }

    public void drawClouds(SpriteBatch batch) {
        for(Cloud c : clouds) {
            batch.draw(c, c.getX() - c.getWidth() / 2f,
                    c.getY() - c .getHeight() / 2f);
        }
    }

}  // end CloudsController
