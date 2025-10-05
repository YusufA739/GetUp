package game;

//* Most ahead of features when compared with other Body classes that are interacted with via the player
//
// */



import city.cs.engine.*;
import org.jbox2d.common.Vec2;


import static org.jbox2d.common.MathUtils.*;

/**
 * DynamicBody with a CircleShape
 */
public class DynamicOrb {
    public static int coinsRemaining = 0;//all coins should be of type dynamic orb or otherwise I have to check all the body tyes to evaluate the final condition. Makes it more annoying!!
    public static int endCoinsRemaining = 0;

    private float incrementX = 0;
    private float incrementY = 0;
    private float radius = 0;
    private float radiusX = 0;
    private float radiusY = 0;

    private float amplitudeOfOscillationX = 0;
    private float amplitudeOfOscillationY = 0;
    private float currentDisplacementX = 0;
    private float currentDisplacementY = 0;

    private float gravity = 0;
    private float gravityScale = 0;

    private float localX;
    private float localY;//optional: make these into localCoords[], but nah no point in doing it, but if I wanted it to be nicer, as coord pairs look better and are way easier to deal with, but if I'm bored then yeah otherwise, nothing wrong with this implementation

    private float thetaX = 0;
    private float thetaY = 0;//(nope) -> thetaPlanes[]

    private boolean isAscending = false;

    private float[] topDeadCentreCoords = new float[2];

    //private static Game localGame;
    private static World localWorld;
    private static Level localLevel;//set at start of level in load() (current level we're on: similar to currentLevelObject in Game)
    private Shape localShape;
    private DynamicBody localDynamicOrb;
    private static PlayerController localRefPlayerControllerDynamicOrb;

    public DynamicOrb(World givenWorld, float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY, PlayerController givenPlayerController) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        Shape localShape = new CircleShape(givenRadius);

        // object coming into creation
        localDynamicOrb = new DynamicBody(givenWorld, localShape);
        localDynamicOrb.setPosition(new Vec2(givenX, givenY));
        // end of section

        localDynamicOrb.setGravityScale(givenGravityScale);

        radius = givenRadius;
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
        localRefPlayerControllerDynamicOrb = givenPlayerController;
    }

    public DynamicOrb(World givenWorld, float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        localWorld = givenWorld;
        localShape = new CircleShape(givenRadius);

        // object coming into creation
        localDynamicOrb = new DynamicBody(givenWorld, localShape);
        localDynamicOrb.setPosition(new Vec2(givenX, givenY));
        // end of section

        localDynamicOrb.setGravityScale(givenGravityScale);

        radius = givenRadius;
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
    }

    /**
     * Active Constructor; transition from using other constructors and use this one
     * @param givenRadius float
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
    public DynamicOrb(float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        localShape = new CircleShape(givenRadius);

        // object coming into creation
        localDynamicOrb = new DynamicBody(localWorld, localShape);
        localDynamicOrb.setPosition(new Vec2(givenX, givenY));
        // end of section

        localDynamicOrb.setGravityScale(givenGravityScale);

        radius = givenRadius;
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
        localRefPlayerControllerDynamicOrb = givenPlayerController;
    }

    //    public void setGame(Game givenGame) {
//        localGame = givenGame;
//    }Nope, don't use this implementation in the future

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
        localDynamicOrb.setPosition(new Vec2(localX, localY));
    }
    //end of slider code. If it switches between sliding and orbit you may encounter inconsistent behaviour

    //riser code
    public void dynamicRiser() {
        localY += incrementY;
        currentDisplacementY += incrementY;

        if (abs(currentDisplacementY) >= amplitudeOfOscillationY) {
            incrementY =- incrementY;
        }
        localDynamicOrb.setPosition(new Vec2(localX, localY));
    }
    //end of riser code read the comments for slider code, same issue here if switching to circular

    public void dynamicGravity() {
        localY -= gravity;
        localDynamicOrb.setPosition(new Vec2(localX, localY));
    }

    public void dynamicGravity(float givenGravity) {
        localY -= givenGravity; //DO NOT USE, just use gravityScale
        localDynamicOrb.setPosition(new Vec2(localX, localY));
    }

    public void dynamicGravityScale() {
        localDynamicOrb.setGravityScale(gravityScale);
    }

    public void dynamicGravityScale(float givenGravityScale) {
        localDynamicOrb.setGravityScale(givenGravityScale);
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
        localDynamicOrb.setPosition(new Vec2(localX, localY));
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

        localDynamicOrb.setPosition(new Vec2(localX, localY));

        isAscending = thetaY >= 180;
        //System.out.println(isAscending);
    }

    //getters
    public boolean getIsAscending() {
        return isAscending;
    }

    public int getCoinsRemaining() {
        return coinsRemaining;
    }

    public int getEndCoinsRemaining() {
        return endCoinsRemaining;
    }
    //end of getters

    public void isSpeedPotionSensor() {
        sensorPrep().addSensorListener(new SensorListener() {
            SoundClip sc = fetchSoundClip("data/drink.wav");
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    localDynamicOrb.destroy();
                    localRefPlayerControllerDynamicOrb.superSpeedPickup();
                    sc.play();
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {

            }
        });
        localDynamicOrb.addImage(new BodyImage("data/speedpotionOutline.png", radius*2));
    }

    public void isHeartSensor() {
        SoundClip sc = fetchSoundClip("data/grow.wav");//used to be 1upAmped.wav
        sensorPrep().addSensorListener(new SensorListener() {
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    localDynamicOrb.destroy();
                    localRefPlayerControllerDynamicOrb.healthIncrease();
                    sc.play();
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {

            }
        });
        localDynamicOrb.addImage(new BodyImage("data/heartsquare.png", radius*2));
    }

    public void isCoinSensor() {
        coinsRemaining ++;
        sensorPrep().addSensorListener(new SensorListener() {
            SoundClip coinPickupSound = fetchSoundClip("data/coin.wav");//sound is assigned within sensorListener
            SoundClip levelCompleteSound = fetchSoundClip("data/grow.wav");
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    coinsRemaining --;
                    localDynamicOrb.destroy();
                    coinPickupSound.play();
                    localRefPlayerControllerDynamicOrb.scoreIncrease();

                    if (coinsRemaining == 0) {
                        localLevel.levelComplete = true;
                        localRefPlayerControllerDynamicOrb.scoreIncrease(10);
                        levelCompleteSound.play();
                    }
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {

            }
        });
        localDynamicOrb.addImage(new BodyImage("data/coinCircle.png", radius*2));
    }

    public void isEndCoinSensor() {
        endCoinsRemaining ++;
        sensorPrep().addSensorListener(new SensorListener() {
            SoundClip endCoinPickupSound = fetchSoundClip("data/endCoin.wav");
            SoundClip levelFinishSound = fetchSoundClip("data/goal.wav");
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    endCoinsRemaining --;
                    localDynamicOrb.destroy();
                    endCoinPickupSound.play();
                    localRefPlayerControllerDynamicOrb.scoreIncrease(5);
                    if (endCoinsRemaining == 0) {
                        levelFinishSound.play();
                        localRefPlayerControllerDynamicOrb.gamePaused = false;//unpause game to avoid any issues from occurring...
                        Game.gamePaused = false;//...and keep changes consistent between these two classes
                        if (localLevel.levelComplete) {//would do coinsRemaining, but for readability and to see if the localLevel really works or not, it is done like this
                            localLevel.levelPerfect = true;
                        }
                        localLevel.levelFinish = true;//current level finished (does not actually make the level progress, that is done by the increment below
                        localLevel.unload();//works but you need to add the objects to these 4 collections as well (ik it's extra typed lines but deal with it!); remember to add them!
                        Level.currentLevelNumber ++;//move onto next level!
                    }
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {
                //this won't register/be executed/be called, unless the object is not destroyed upon first contact
            }
        });
        localDynamicOrb.addImage(new BodyImage("data/endCoinCircle.png", radius*2));
    }

    public Sensor sensorPrep() {
        localDynamicOrb.destroy();
        localShape = new CircleShape(radius*0.1f);
        localDynamicOrb = new DynamicBody(localWorld, localShape);
        localDynamicOrb.setGravityScale(gravity);
        localDynamicOrb.setPosition(new Vec2(localX, localY));
        return new Sensor(localDynamicOrb, new CircleShape(radius));
    }

    public void isCoin() {
        localDynamicOrb.removeAllImages(); //remove prev. images
        localDynamicOrb.addImage(new BodyImage("data/coinCircle.png", radius*2)); //add new image
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener coinCollect = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerDynamicOrb.scoreIncrease();
                    reportingBody.destroy();
                } //destroy the coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localDynamicOrb.addCollisionListener(coinCollect);
    }//end of code

    public void isHeart() {
        localDynamicOrb.removeAllImages(); //remove previous images
        localDynamicOrb.addImage(new BodyImage("data/heartsquare.png", radius*2));
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener coinCollect = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerDynamicOrb.healthIncrease();
                    reportingBody.destroy();
                } //destroy the coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localDynamicOrb.addCollisionListener(coinCollect);
    }//end of code

    public void endCoin() {
        localDynamicOrb.addImage(new BodyImage("data/coinCircleBig.png", radius*2));
        //will create a collision listener that is then added to the local dynamic orb
        CollisionListener coinCollect = new CollisionListener() {

            @Override
            public void collide(CollisionEvent e) {
                Body reportingBody = e.getReportingBody();
                Body otherBody = e.getOtherBody();
                if (otherBody instanceof PlayerDynamicBody) {
                    localRefPlayerControllerDynamicOrb.scoreIncrease(10);
                    reportingBody.destroy();
                } //destroy the coin when it hits a player
            }

        }; //end of CollisionListener code
        //add to object via local reference
        localDynamicOrb.addCollisionListener(coinCollect);
    }//end of code

    //image missing
    public void imageMissing() {
        try {
            localDynamicOrb.removeAllImages();
            localDynamicOrb.addImage(new BodyImage("data/missingimage.png", radius*2));
        }
        catch (Exception e) {
            localDynamicOrb.removeAllImages();
            System.err.println("Cannot Add New Image - Cannot Add missingimage.png Image: MAJOR ERROR" + e.getMessage());
        }
    }

    private SoundClip fetchSoundClip(String FilePath) {
        try {
            return new SoundClip(FilePath);
        } catch (Exception e) {
            System.err.println("Missing Sound Clip in Game -  missing:" + FilePath);
            throw new RuntimeException(e);
        }
    }
    public SoundClip missingSound(){
        return null;//add missing sound file but err handling good as is
    }

    //destroyer
    public void destroy() {
        localDynamicOrb.destroy();
    }
}
