package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static org.jbox2d.common.MathUtils.*;

/**
 * DynamicBody with BoxShape
 */
public class DynamicPlatform {
    private float incrementX = 0;
    private float incrementY = 0;
    private float radiusX = 0;
    private float radiusY = 0;

    private float amplitudeOfOscillationX = 0;
    private float amplitudeOfOscillationY = 0;
    private float currentDisplacementX = 0;
    private float currentDisplacementY = 0;

    private float gravity = 0;
    private float gravityScale = 0;

    private float localX = 0;
    private float localY = 0;//optional: make these into localCoords[], but nah no point in doing it, but if I wanted it to be nicer, as coord pairs look better and are way easier to deal with, but if I'm bored then yeah otherwise, nothing wrong with this implementation

    private float thetaX = 0;
    private float thetaY = 0;//(nope) -> thetaPlanes[]

    private boolean isAscending = false;

    private float[] topDeadCentreCoords = new float[2];
    private float[] reportingBodyCoords = new float[2]; //idk what the original comment meant, so I'm putting this here so you know when you check version control for any ideas

    private float height = 0;
    private float width = 0;

    private static World localWorld;
    private static Level localLevel;
    private Shape localShape;
    private DynamicBody localDynamicPlatform;
    private static PlayerController localRefPlayerControllerDynamicPlatform;

    public DynamicPlatform(World givenWorld, float givenWidth, float givenHeight, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY, PlayerController givenPlayerController) {
        /*type refers to whether the body is static (0) or dynamic (1)
        vertical platforms are 'risers' */
        //first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class
        Shape localShape = new BoxShape(givenWidth, givenHeight);

        // object creation
        localDynamicPlatform = new DynamicBody(givenWorld, localShape);
        localDynamicPlatform.setPosition(new Vec2(givenX, givenY));
        // end of block

        localDynamicPlatform.setGravityScale(givenGravityScale);

        height = givenHeight;
        width = givenWidth;
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
        gravity = givenGravity;
        gravityScale = givenGravityScale;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;
        localRefPlayerControllerDynamicPlatform = givenPlayerController;
    }

    public DynamicPlatform(World givenWorld, float givenWidth, float givenHeight, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*type refers to whether the body is static (0) or dynamic (1)
        vertical platforms are 'risers' */
        localWorld = givenWorld;
        localShape = new BoxShape(givenWidth, givenHeight);

        // object creation
        localDynamicPlatform = new DynamicBody(givenWorld, localShape);
        localDynamicPlatform.setPosition(new Vec2(givenX, givenY));
        // end of block

        localDynamicPlatform.setGravityScale(givenGravityScale);

        height = givenHeight;
        width = givenWidth;
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
        gravity = givenGravity;
        gravityScale = givenGravityScale;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;

        if (width/height == 6) {
            sixbyone();
        }
        else if (width/height == 12) {
            twelvebyone();
        }
        else if (width/height == 18) {
            eighteenbyone();
        }
        else if (width/height == 60) {
            sixtybyone();
        }
    }

    /**
     * Active Constructor; transition from using other constructors and use this one
     * @param givenWidth float
     * @param givenHeight float
     * @param givenX float
     * @param givenY float
     * @param givenAmplitudeOfOscillationX float
     * @param givenAmplitudeOfOscillationY float
     * @param givenGravity float
     * @param givenGravityScale float
     * @param givenIncrementX float
     * @param givenIncrementY float
     * @param givenRadiusX float
     * @param givenRadiusY float
     */
    public DynamicPlatform(float givenWidth, float givenHeight, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*type refers to whether the body is static (0) or dynamic (1)
        vertical platforms are 'risers' */
        localShape = new BoxShape(givenWidth, givenHeight);

        // object creation
        localDynamicPlatform = new DynamicBody(localWorld, localShape);
        localDynamicPlatform.setPosition(new Vec2(givenX, givenY));
        // end of block

        localDynamicPlatform.setGravityScale(givenGravityScale);

        height = givenHeight;
        width = givenWidth;
        localX = givenX;
        localY = givenY;
        topDeadCentreCoords[0] = givenX;
        topDeadCentreCoords[1] = givenY;
        amplitudeOfOscillationX = givenAmplitudeOfOscillationX;
        amplitudeOfOscillationY = givenAmplitudeOfOscillationY;
        gravity = givenGravity;
        gravityScale = givenGravityScale;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;

        if (width/height == 6) {
            sixbyone();
        }
        else if (width/height == 12) {
            twelvebyone();
        }
        else if (width/height == 18) {
            eighteenbyone();
        }
        else if (width/height == 60) {
            sixtybyone();
        }
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

    public void setGravityScale(float givenGravityScale) {
        gravityScale = givenGravityScale;
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
        localRefPlayerControllerDynamicPlatform = givenPlayerController;
    }

    public static void setWorld(World givenWorld) {
        localWorld = givenWorld;
    }

    public static void setLevel(Level givenLevel) {
        localLevel = givenLevel;
    }
    //end of setters

    //slider code
    public void dynamicSlider() {
        localX += incrementX;
        currentDisplacementX += incrementX;

        if (abs(currentDisplacementX) >= amplitudeOfOscillationX) {
            incrementX =- incrementX;
        }
        localDynamicPlatform.setPosition(new Vec2(localX, localY));
    }
    //end of slider code. If it switches between sliding and orbit you may encounter a bug. So wait for no oscillation?? or don't do it or overload it and make a new method for this

    //riser code
    public void dynamicRiser() {
        localY += incrementY;
        currentDisplacementY += incrementY;

        if (abs(currentDisplacementY) >= amplitudeOfOscillationY) {
            incrementY =- incrementY;
        }
        localDynamicPlatform.setPosition(new Vec2(localX, localY));
    }
    //end of riser code read the comments for slider code, same issue here if switching to circular

    public void dynamicGravity() {
        localY -= gravity;
        localDynamicPlatform.setPosition(new Vec2(localX, localY));
    }

    public void dynamicGravity(float givenGravity) {
        localY -= givenGravity; //DO NOT USE, just use gravityScale
        localDynamicPlatform.setPosition(new Vec2(localX, localY));
    }

    public void dynamicGravityScale() {
        localDynamicPlatform.setGravityScale(gravityScale);
    }

    public void dynamicGravityScale(float givenGravityScale) {
        localDynamicPlatform.setGravityScale(givenGravityScale);
    }

    public void dynamicDiagonal() {
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
        localDynamicPlatform.setPosition(new Vec2(localX, localY));
    }

    public void dynamicOrbit() {
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

        localDynamicPlatform.setPosition(new Vec2(localX, localY));

        isAscending = thetaY >= 180;//simplified the last if statement to this
        //System.out.println(isAscending);
    }

    //getters
    public boolean getIsAscending() {
        return isAscending;
    }//never used (scrapped/axed)
    //end of getters

    public void isBreakableSensor() {
        localDynamicPlatform.destroy();//destroy and recreate object with smaller radius
        localShape = new BoxShape(width*0.1f, height*0.1f);
        localDynamicPlatform = new DynamicBody(localWorld, localShape);
        localDynamicPlatform.addImage(new BodyImage("data/coinCircle.png", height*2));
        localDynamicPlatform.setPosition(new Vec2(localX, localY));
        Sensor s = new Sensor(localDynamicPlatform, new BoxShape(width, height));
        s.addSensorListener(new SensorListener() {
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    localDynamicPlatform.destroy();
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {

            }
        });
    }//I just realised I do want the player to interact with the platform, as it should cause them to fall as if it had just disappeared from beneath their feet upon colliding with the platform physically

    public void isBreakable() {
        localDynamicPlatform.removeAllImages(); //no reason to render multiple images on top of one, remove all images
        localDynamicPlatform.addImage(new BodyImage("data/brokenplatform2.png")); //and add the new image
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener breakPlatform = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    reportingBody.destroy();
                } //destroy a coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localDynamicPlatform.addCollisionListener(breakPlatform);
    }//end of specific collision listener code

    public void isTransporter() {
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener grabPlayer = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    reportingBodyCoords[0] = reportingBody.getPosition().x;
                    reportingBodyCoords[1] = reportingBody.getPosition().y;
                    otherBody.setPosition(new Vec2(reportingBodyCoords[0] + incrementX, reportingBodyCoords[1] + incrementY));
                } //destroy a coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localDynamicPlatform.addCollisionListener(grabPlayer);
    }//end of specific collision listener code

    public void sixbyone() {
        localDynamicPlatform.removeAllImages();
        localDynamicPlatform.addImage(new BodyImage("data/6by1platform.png", height*2));
    }

    public void twelvebyone() {
        localDynamicPlatform.removeAllImages();
        localDynamicPlatform.addImage(new BodyImage("data/12by1platform.png", height*2));
    }

    public void eighteenbyone() {
        localDynamicPlatform.removeAllImages();
        localDynamicPlatform.addImage(new BodyImage("data/18by1platform.png", height*2));
    }

    public void sixtybyone() {
        localDynamicPlatform.removeAllImages();
        localDynamicPlatform.addImage(new BodyImage("data/60by1platform.png", height*2));
    }

    public void isWarningSign() {
        localDynamicPlatform.removeAllImages();
        localDynamicPlatform.addImage(new BodyImage("data/warning.png", height*2));
    }

    //image missing
    public void imageMissing() {
        try {
            localDynamicPlatform.removeAllImages();
            localDynamicPlatform.addImage(new BodyImage("data/missingimage.png", height*2));
        }
        catch (Exception e) {
            localDynamicPlatform.removeAllImages();
        }
    }

    //destroyer
    public void destroy() {
        localDynamicPlatform.destroy();
    }
}
