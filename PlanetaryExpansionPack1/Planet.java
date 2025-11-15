package PlanetaryExpansionPack1;

//* Most ahead of features when compared with other Body classes that are interacted with via the player
//
// */



import city.cs.engine.*;
import game.Game;
import game.Level;
import game.PlayerController;
import game.PlayerDynamicBody;
import org.jbox2d.common.Vec2;


import static game.Game.tick;
import static org.jbox2d.common.MathUtils.*;

/**
 * DynamicBody with a CircleShape
 */
public class Planet {
    private float incrementX = 0;
    private float incrementY = 0;
    private float radiusX = 0;
    private float radiusY = 0;

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

    //physics - units in SI standard units (Voltage in Volts, Current in Amperes, Distance in metres)
    private double mass;
    private double accelerationX;
    private double accelerationY;
    private double ForceX;
    private double ForceY;
    private double newForceX;
    private double newForceY;
    private double displacementX;
    private double displacementY;
    private final double G = 6.674 * Math.pow(10,-11);
    private double radius = 0;

    private double velocityX; //better than Velocity-9!!!
    private double velocityY;


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
    public Planet(float givenRadius, float givenX, float givenY, float givenAmplitudeOfOscillationX, float givenAmplitudeOfOscillationY, float givenGravity, float givenGravityScale, float givenIncrementX, float givenIncrementY, float givenRadiusX, float givenRadiusY) {
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
        gravity = givenGravity;
        gravityScale = givenGravityScale;
        incrementX = givenIncrementX;
        incrementY = givenIncrementY;
        radiusX = givenRadiusX;
        radiusY = givenRadiusY;
    }

    //Physics
    public void addForce(float otherBodyMass, float otherBodyX, float otherBodyY) { //use the mass, Luke
        displacementX = localX - otherBodyX;
        displacementY = localY - otherBodyY;
        newForceX = (G * mass * otherBodyMass)/displacementX;
        newForceY = (G * mass * otherBodyMass)/displacementY;
        ForceX += newForceX;
        ForceY += newForceY;
        accelerationX = ForceX/mass;
        accelerationY = ForceY/mass;
    }

    public void update(int updateRate) {
        velocityX += accelerationX / updateRate;
        velocityY += accelerationY / updateRate;
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

    public void setGravity(float givenGravity) {
        gravity = givenGravity;
    }

    public void setGravityScale(float givenGravityScale) {
        gravityScale = givenGravityScale;
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

    public static void setWorld(World givenWorld) {
        localWorld = givenWorld;
    }

    public static void setLevel(Level givenLevel) {
        localLevel = givenLevel;
    }
    //end of setters

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
    //end of getters

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
        localDynamicOrb.addImage(new BodyImage("data/heartsquare.png", (float) (radius*2)));
    }

    public void isCoinSensor() {
        sensorPrep().addSensorListener(new SensorListener() {
            SoundClip coinPickupSound = fetchSoundClip("data/coin.wav");//sound is assigned within sensorListener
            SoundClip levelCompleteSound = fetchSoundClip("data/grow.wav");
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    localDynamicOrb.destroy();
                    coinPickupSound.play();
                    localRefPlayerControllerDynamicOrb.scoreIncrease();
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {

            }
        });
        localDynamicOrb.addImage(new BodyImage("data/coinCircle.png", (float) (radius*2)));
    }

    public void isEndCoinSensor() {
        sensorPrep().addSensorListener(new SensorListener() {
            SoundClip endCoinPickupSound = fetchSoundClip("data/endCoin.wav");
            SoundClip levelFinishSound = fetchSoundClip("data/goal.wav");
            @Override
            public void beginContact(SensorEvent sensorEvent) {
                Body reportingBody = sensorEvent.getContactBody();
                SolidFixture otherBody = sensorEvent.getContactFixture();
                if (reportingBody instanceof PlayerDynamicBody) {
                    localDynamicOrb.destroy();
                    endCoinPickupSound.play();
                    localRefPlayerControllerDynamicOrb.scoreIncrease(5);
                }
            }

            @Override
            public void endContact(SensorEvent sensorEvent) {
                //this won't register/be executed/be called, unless the object is not destroyed upon first contact
            }
        });
        localDynamicOrb.addImage(new BodyImage("data/endCoinCircle.png", (float) (radius*2)));
    }

    public Sensor sensorPrep() {
        localDynamicOrb.destroy();
        localShape = new CircleShape(radius*0.1f);
        localDynamicOrb = new DynamicBody(localWorld, localShape);
        localDynamicOrb.setGravityScale(gravity);
        localDynamicOrb.setPosition(new Vec2(localX, localY));
        return new Sensor(localDynamicOrb, new CircleShape(radius));
    }

    //image missing
    public void imageMissing() {
        try {
            localDynamicOrb.removeAllImages();
            localDynamicOrb.addImage(new BodyImage("data/missingimage.png",(float) (radius*2)));
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
