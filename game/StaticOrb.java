package game;

import city.cs.engine.*;
import city.cs.engine.Shape;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.jbox2d.common.MathUtils.*;

/**
 * StaticBody with a CircleShape
 */
public class StaticOrb {
    private float incrementX = 0.1f;
    private float incrementY = 0.1f;
    private float radius = 0;
    private float radiusX = 1f;
    private float radiusY = 1f;

    private float amplitudeOfOscillationX = 0;
    private float amplitudeOfOscillationY = 0;
    private float currentDisplacementX = 0;
    private float currentDisplacementY = 0;

    private float gravity = 0;


    private float localX;
    private float localY;

    private float thetaX = 0;
    private float thetaY = 0;

    private boolean isAscending;

    private float[] topDeadCentreCoords = new float[2];

    private static World localWorld;
    private static Level localLevel;
    private Shape localShape;
    private StaticBody localStaticOrb;
    private static PlayerController localRefPlayerControllerStaticOrb;

    public StaticOrb(World givenWorld, float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY, PlayerController givenPlayerController) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        Shape localShape = new CircleShape(givenRadius);

        // object coming into creation
        localStaticOrb = new StaticBody(givenWorld, localShape);
        localStaticOrb.setPosition(new Vec2(givenX, givenY));
        // end of section

        radius = givenRadius;
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
        gravity = givenGravity;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;
        localRefPlayerControllerStaticOrb = givenPlayerController;
    }

    public StaticOrb(World givenWorld, float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        localWorld = givenWorld;
        localShape = new CircleShape(givenRadius);

        // object coming into creation
        localStaticOrb = new StaticBody(givenWorld, localShape);
        localStaticOrb.setPosition(new Vec2(givenX, givenY));
        // end of section

        radius = givenRadius;
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
        gravity = givenGravity;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;
    }

    /**
     * Currently active Constructor; others can be used and are used where it was not changed. It has been left for code compatibility with older code that has not had all constructors changed, either too many bodies in level or there was no need/non-issue on priority list
     * @param givenRadius float
     * @param givenX float
     * @param givenY float
     * @param givenAmplitudeOfOscillationX float
     * @param givenAmplitudeOfOscillationY float
     * @param givenGravity float
     * @param givenIncrementX float
     * @param givenIncrementY float
     * @param givenRadiusX float
     * @param givenRadiusY float
     */
    public StaticOrb(float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        localShape = new CircleShape(givenRadius);

        // object coming into creation
        localStaticOrb = new StaticBody(localWorld, localShape);
        localStaticOrb.setPosition(new Vec2(givenX, givenY));
        // end of section

        radius = givenRadius;
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
        gravity = givenGravity;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;
    }


    //setters
    public void setX(float givenX) {
        localX = givenX;
        topDeadCentreCoords[0] = givenX;
    }

    public void setY(float givenY) {
        localY = givenY;
        topDeadCentreCoords[1] = givenY;
    }

    public void setCoords(float givenX, float givenY) {
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
    }

    public void setAmplitudeOfOscillationX(float givenAmplitudeOfOscillationX) {
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
    }

    public void setAmplitudeOfOscillationY(float givenAmplitudeOfOscillationY) {
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
    }

    public void setGravity(float givenGravity) {
        gravity = givenGravity;
    }

    public void setIncrementX(float givenIncrementX) {
        incrementX = givenIncrementX;
    }

    public void setIncrementY(float givenIncrementY) {
        incrementY = givenIncrementY;
    }

    public void setRadiusX(float givenRadiusX) {
        radiusX = givenRadiusX;
    }

    public void setRadiusY(float givenRadiusY) {
        radiusY = givenRadiusY;
    }

    public static void setPlayerController(PlayerController givenPlayerController) {
        localRefPlayerControllerStaticOrb = givenPlayerController;
    }

    public static void setWorld(World givenWorld) {
        localWorld = givenWorld;
    }

    public static void setLevel(Level givenLevel) {
        localLevel = givenLevel;
    }
    //end of setters

    //slider code
    public void staticSlider() {
        localX += incrementX;
        currentDisplacementX += incrementX;

        if (abs(currentDisplacementX) >= amplitudeOfOscillationX) {
            incrementX =- incrementX;
        }
        localStaticOrb.setPosition(new Vec2(localX, localY));
    }
    //end of slider code. If it switches between sliding and orbit you may encounter inconsistencies with movement (not reproducible behaviour)

    //riser code
    public void staticRiser() {
        localY += incrementY;
        currentDisplacementY += incrementY;

        if (abs(currentDisplacementY) >= amplitudeOfOscillationY) {
            incrementY =- incrementY;
        }
        localStaticOrb.setPosition(new Vec2(localX, localY));
    }
    //end of riser code read the comments for slider code, same issue here if switching to circular

    public void staticGravity() {
        localY -= gravity;
        localStaticOrb.setPosition(new Vec2(localX, localY));
    }

    public void staticGravity(float givenGravity) {
        localY -= givenGravity;
        //DO NOT USE, just set new gravity value and then do normal one, you don't need to dynamically change gravity (too complex, for no marks as well. Could be future cushioning effect)
    }

    public void staticDiagonal() {
        //more immediate than manual use of position updating
        localX += incrementX;
        currentDisplacementX += incrementX;
        localY += incrementY;
        currentDisplacementY += incrementY;

        if (abs(currentDisplacementX) >= amplitudeOfOscillationX) {
            incrementX =- incrementX;
        }
        if (abs(currentDisplacementY) >= amplitudeOfOscillationY) {
            incrementY =- incrementY;
        }
        localStaticOrb.setPosition(new Vec2(localX, localY));
    }
    
    public void staticOrbit() {
        localX = topDeadCentreCoords[0] + radiusX * sin(thetaX * ((2*PI)/360));
        /* using increments here is assumption that the platform will only be used as an orbiting body, not reused from a previous type. Either way, set the value again using setter if you
        are reusing the object from slider to orbiting (or other type to orbiting) NOT RECOMMENDED THOUGH
         */
        localY = topDeadCentreCoords[1] + radiusY * cos(thetaY * ((2*PI)/360));

        thetaX += incrementX;
        thetaY += incrementY;

        if (thetaX >= 360) {
            thetaX -= 360;
        }
        if (thetaY >= 360) {
            thetaY -= 360;
        }

        localStaticOrb.setPosition(new Vec2(localX, localY));

        isAscending = thetaY >= 180;
        //System.out.println(isAscending);
    }

    //getters
    public boolean getIsAscending() {
        return isAscending;
    }
    //end of getters

    public void isSpeedPotionSensor() {
        sensorPrep().addSensorListener(new SensorListener() {
            SoundClip sc;{
                try {
                    sc = new SoundClip("data/drink.wav");
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    localStaticOrb.destroy();
                    localRefPlayerControllerStaticOrb.superSpeedPickup();
                    sc.play();
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {

            }
        });
        localStaticOrb.addImage(new BodyImage("data/speedpotionOutline.png", radius*2));
    }

    public Sensor sensorPrep() {
        localStaticOrb.destroy();
        localShape = new CircleShape(radius*0.1f);
        localStaticOrb = new StaticBody(localWorld, localShape);
        localStaticOrb.setPosition(new Vec2(localX, localY));
        return new Sensor(localStaticOrb, new CircleShape(radius));
    }

    public void isCoin() {
        localStaticOrb.removeAllImages(); //remove previous images
        localStaticOrb.addImage(new BodyImage("data/coinCircle.png", radius*2));
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener coinCollect = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerStaticOrb.scoreIncrease();
                    reportingBody.destroy();
                } //destroy the coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticOrb.addCollisionListener(coinCollect);
    }//creates collision listener that has coin logic: active version in DynamicOrb as it functions better and does not collide with/interfere as much with
    //player movement as much as a DYNAMICBODY with COLLISIONLISTENER attachted to it

    public void isSpike() {
        localStaticOrb.removeAllImages(); //remove all prev. images in preparation for next/new image
        localStaticOrb.addImage(new BodyImage("data/spikeball.png", radius*2)); //new image added
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener spikeKiller = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerStaticOrb.respawn();
                } //destroy a coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticOrb.addCollisionListener(spikeKiller);
    }//creates collision listener that has spike logic

    public void isHeart() {
        localStaticOrb.removeAllImages(); //remove previous images
        localStaticOrb.addImage(new BodyImage("data/heartsquare.png", radius*2));
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener coinCollect = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerStaticOrb.healthIncrease();
                    reportingBody.destroy();
                } //destroy the coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticOrb.addCollisionListener(coinCollect);
    }//creates collision listener that has heart logic

    public void isSpeedPotion() {
        localStaticOrb.removeAllImages(); //remove previous images
        localStaticOrb.addImage(new BodyImage("data/speedpotionOutline.png", radius*2));
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener coinCollect = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerStaticOrb.superSpeedPickup();
                    reportingBody.destroy();
                } //destroy the coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticOrb.addCollisionListener(coinCollect);
    }//creates collision listener that has CrystalSpeed logic

    public void isInertBall() {
        localStaticOrb.removeAllImages();
        localStaticOrb.addImage(new BodyImage("data/inertball.png", radius*2));
    }

    public void isWarningSign() {
        localStaticOrb.addImage(new BodyImage("data/warning.png", radius*2));
    }//adds the warning image

    //image missing
    public void imageMissing() {
        try {
            localStaticOrb.removeAllImages();
            localStaticOrb.addImage(new BodyImage("data/missingimage.png", radius*2));
        }
        catch (Exception e) {
            localStaticOrb.removeAllImages();
            System.err.println("Cannot Add New Image - Cannot Add missingimage.png Image: MAJOR ERROR" + e.getMessage());
        }
    }

    //destroyer
    public void destroy() {
        localStaticOrb.destroy();
    }
}
