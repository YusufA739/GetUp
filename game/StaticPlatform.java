package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import static org.jbox2d.common.MathUtils.*;

/**
 * StaticBody with BoxShape
 */
public class StaticPlatform {
    private float incrementX = 0;
    private float incrementY = 0;
    private float radiusX = 0;
    private float radiusY = 0;

    private float amplitudeOfOscillationX = 0;
    private float amplitudeOfOscillationY = 0;
    private float currentDisplacementX = 0;
    private float currentDisplacementY = 0;

    private float gravity = 0;


    private float localX = 0;
    private float localY = 0;

    private float thetaX = 0;
    private float thetaY = 0;

    private boolean isAscending = false;

    private float[] topDeadCentreCoords = new float[2];
    private float[] reportingBodyCoords = new float[2];

    private float height = 0;
    private float width = 0;

    private static World localWorld;
    private static Level localLevel;
    private Shape localShape;
    private StaticBody localStaticPlatform;
    private static PlayerController localRefPlayerControllerStaticPlatform;

    //obsolete (due to static player controller variable) so will NOT be updated (starting with the auto assignment of an image in new constructor below)
    public StaticPlatform(World givenWorld, float givenWidth, float givenHeight, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY, PlayerController givenPlayerController) {
        /*type refers to whether the body is static (0) or dynamic (1)
        vertical platforms are 'risers' */
        //first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class
        Shape localShape = new BoxShape(givenWidth, givenHeight);

        // object creation
        localStaticPlatform = new StaticBody(givenWorld, localShape);
        localStaticPlatform.setPosition(new Vec2(givenX, givenY));

        height = givenHeight;
        width = givenWidth;
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
        localRefPlayerControllerStaticPlatform = givenPlayerController;
    }

    public StaticPlatform(World givenWorld, float givenWidth, float givenHeight, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*type refers to whether the body is static (0) or dynamic (1)
        vertical platforms are 'risers' */
        //first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class
        localWorld = givenWorld;
        localShape = new BoxShape(givenWidth, givenHeight);

        // object creation (this is the object that is stored within this object, to be edited by this object (StaticPlatform is to the object as PlayerController is PlayerDynamicBody)
        localStaticPlatform = new StaticBody(givenWorld, localShape);
        localStaticPlatform.setPosition(new Vec2(givenX, givenY));

        height = givenHeight;
        width = givenWidth;
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
        //else {}
    }

    //no need for world, passed statically

    /**
     * Active Constructor; transition from using other constructors and use this one
     * @param givenWidth float
     * @param givenHeight float
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
    public StaticPlatform(float givenWidth, float givenHeight, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*type refers to whether the body is static (0) or dynamic (1)
        vertical platforms are 'risers' */
        localShape = new BoxShape(givenWidth, givenHeight);

        // object creation (this is the object that is stored within this object, to be edited by this object (StaticPlatform is to the object as PlayerController is PlayerDynamicBody)
        localStaticPlatform = new StaticBody(localWorld, localShape);
        localStaticPlatform.setPosition(new Vec2(givenX, givenY));

        height = givenHeight;
        width = givenWidth;
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
        //else {}
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
        localRefPlayerControllerStaticPlatform = givenPlayerController;
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
        localStaticPlatform.setPosition(new Vec2(localX, localY));
    }
    //end of slider code. If it switches between sliding and orbit you may have some weird behaviour occur as the object has to be at top dead centre/initial position at the first orbit call

    //riser code
    public void staticRiser() {
        localY += incrementY;
        currentDisplacementY += incrementY;

        if (abs(currentDisplacementY) >= amplitudeOfOscillationY) {
            incrementY =- incrementY;
        }
        localStaticPlatform.setPosition(new Vec2(localX, localY));
    }
    //end of riser code read the comments for slider code, same issue here if switching to circular

    public void staticGravity() {
        localY -= gravity;
        localStaticPlatform.setPosition(new Vec2(localX, localY));
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
        localStaticPlatform.setPosition(new Vec2(localX, localY));
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

        localStaticPlatform.setPosition(new Vec2(localX, localY));

        isAscending = thetaY >= 180;
        //System.out.println(isAscending);
    }

    //getters
    public boolean getIsAscending() {
        return isAscending;
    }//never used (scrapped/axed)
    //end of getters

    //CollisionListeners
    public void isBreakable() {
        localStaticPlatform.removeAllImages(); //prevent any foreseeable issues due to rendering multiple images upon a single object
        localStaticPlatform.addImage(new BodyImage("data/brokenplatform.png", height*2));
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener breakPlatform = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    reportingBody.destroy();
                } //destroy when colliding with player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticPlatform.addCollisionListener(breakPlatform);
    }

    public void isCheckpoint() {
        localStaticPlatform.removeAllImages(); //prevent any foreseeable issues due to rendering multiple images upon a single object
        localStaticPlatform.addImage(new BodyImage("data/6by1respawn.png", height*2));//this code is better off outside as it makes no sense to be in collision listener code
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener breakPlatform = new CollisionListener() {
            int uses = 1;
            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    if (uses>0) {
                        localRefPlayerControllerStaticPlatform.setCheckpoint();
                        uses--;//stops it from repeatedly calling the setCheckpoint() method (reduce lag from/and irrelevant assignments)
                    }
                } //destroy a coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticPlatform.addCollisionListener(breakPlatform);
    }

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
                    otherBody.setPosition(new Vec2(reportingBodyCoords[0] + incrementX, reportingBodyCoords[1] + incrementY + 0.1f));
                } //destroy a coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localStaticPlatform.addCollisionListener(grabPlayer);
    }//this is just an idea for now
    //end of CollisionListeners

    public void sixbyone() {
        localStaticPlatform.removeAllImages(); //prevent any foreseeable issues due to multiple layers of images (when we only need the top layer)
        localStaticPlatform.addImage(new BodyImage("data/6by1platform.png", height*2));
    }

    public void sixbyone_alt0() {
        localStaticPlatform.removeAllImages(); //prevent any foreseeable issues due to rendering multiple images upon a single object
        localStaticPlatform.addImage(new BodyImage("data/6by1platform.png", height*2));
    }

    public void twelvebyone() {
        localStaticPlatform.removeAllImages(); //remove irrelevant image and put the new relevant image on
        localStaticPlatform.addImage(new BodyImage("data/12by1platform.png", height*2)); //relevant image
    }

    public void eighteenbyone() {
        localStaticPlatform.removeAllImages(); //irrelevant removed
        localStaticPlatform.addImage(new BodyImage("data/18by1platform.png", height*2)); //relevant added
    }

    public void sixtybyone() {
        localStaticPlatform.removeAllImages(); //remove all images
        localStaticPlatform.addImage(new BodyImage("data/60by1platform.png", height*2)); //add new image
    }

    public void isWarningSign() {
        localStaticPlatform.removeAllImages(); //remove all images
        localStaticPlatform.addImage(new BodyImage("data/warning.png", height*2)); //add new image
    }

    public void isWellDoneSign() {
        localStaticPlatform.removeAllImages();
        localStaticPlatform.addImage(new BodyImage("data/welldone.png", height*2));
    }

    //image missing
    public void imageMissing() {
        try {
            localStaticPlatform.removeAllImages();
            localStaticPlatform.addImage(new BodyImage("data/missingimage.png", height*2));
        }
        catch (Exception e) {
            localStaticPlatform.removeAllImages();
        }
    }

    //destroyer
    public void destroy() {
        localStaticPlatform.destroy(); //destroy the physical body object stored in the localStaticPlatform attribute
    }
}
