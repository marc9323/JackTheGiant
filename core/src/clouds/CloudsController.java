package clouds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Random;

import helpers.GameInfo;

public class CloudsController {

    private World world;
    // resizable dynamic array
    private Array<Cloud> clouds = new Array<Cloud>();
    private float minX, maxX;
    private float lastCloudPositionY; // last cloud position y from postiton cloud method
    private float cameraY;

    private Random random = new Random();

    private final float DISTANCE_BETWEEN_CLOUDS = 250;

    public CloudsController(World world) {
        this.world = world;

        minX = GameInfo.WIDTH / 2f - 110;
        maxX = GameInfo.WIDTH / 2f + 110;

        createClouds();
        positionClouds(true);
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

    public void positionClouds(boolean firstTimeArranging) {
        while(clouds.get(0).getCloudName() == "Dark Cloud") {
            clouds.shuffle();
        }

        float positionY = 0;

        if(firstTimeArranging) {
            positionY = GameInfo.HEIGHT / 2;
        } else {
            positionY = lastCloudPositionY;
        }

        int controlX = 0;

        for(Cloud c : clouds) {

            if(c.getX() == 0 && c.getY() == 0) {
                float tempX = 0;

                // alternate right/left positioning
                if(controlX == 0) {
                    // position cloud on the right side
                    tempX = randomBetweenNumbers(maxX - 40, maxX);
                    controlX = 1;
                } else if(controlX == 1) {
                    // position left
                    tempX = randomBetweenNumbers(minX + 40, minX);
                    controlX = 0;
                }

                c.setSpritePosition(tempX, positionY);

                positionY -= DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;
            }


        }
    }

    public void drawClouds(SpriteBatch batch) {
        for(Cloud c : clouds) {
            batch.draw(c, c.getX() - c.getWidth() / 2f,
                    c.getY() - c .getHeight() / 2f);
        }
    }

    public void createAndArrangeNewClouds() {
        for(int i = 0; i < clouds.size; i++) {
            // cloud out of bounds?
            if((clouds.get(i).getY() - GameInfo.HEIGHT / 2 - 15) > cameraY) {
                // remove it
                clouds.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }

        if(clouds.size == 4) {
            createClouds();
            positionClouds(false);
        }
    }

    public void setCameraY(float cameraY) {
        this.cameraY = cameraY;
    }

    private float randomBetweenNumbers(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

}  // end CloudsController
