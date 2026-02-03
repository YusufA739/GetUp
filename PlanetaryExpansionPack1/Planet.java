package PlanetaryExpansionPack1;

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
    private double radiusX = 0;
    private double radiusY = 0;

    public double localX;
    public double localY;//optional: make these into localCoords[], but nah no point in doing it, but if I wanted it to be nicer, as coord pairs look better and are way easier to deal with, but if I'm bored then yeah otherwise, nothing wrong with this implementation

    private double[] initialPosition = new double[2];

    //private static Game localGame;
    private static World localWorld;
    private static Level localLevel;//set at start of level in load() (current level we're on: similar to currentLevelObject in Game)
    private Shape localShape;
    private DynamicBody localDynamicOrb;
    private static PlayerController localRefPlayerControllerDynamicOrb;

    //physics - units in SI standard units (Voltage in Volts, Current in Amperes, Distance in metres)
    public double mass = 0;
    private double accelerationX = 0;
    private double accelerationY = 0;
    private double ForceX = 0;
    private double ForceY = 0;
    private double numerator = 0;
    private double newForceX = 0;
    private double newForceY = 0;
    private double displacementX = 0;
    private double displacementY = 0;
    private final double G = 6.674 * Math.pow(10,-11);
    private double radius = 0;

    private double velocityX; //better than Velocity-9!!!
    private double velocityY;


    /**
     * Active Constructor; transition from using other constructors and use this one
     * @param givenRadius float
     * @param givenX float
     * @param givenY float
     */
    public Planet(double givenRadius, double givenMass, double givenX, double givenY, double initialVelocityX, double initialVelocityY) {
        /*vertical platforms are 'risers'
        first 3 passed variables should NOT be changed after instantiation, which is why no setters are provided for them in the class*/
        localShape = new CircleShape((float)givenRadius);

        // object coming into creation
        localDynamicOrb = new DynamicBody(localWorld, localShape);
        localDynamicOrb.setPosition(new Vec2((float)givenX, (float)givenY));
        // end of section

        localDynamicOrb.setGravityScale(0);

        radius = givenRadius;
        mass = givenMass;
        localX = givenX;
        localY = givenY;
        initialPosition[0] = givenX;
        initialPosition[1] = givenY;
        velocityX = initialVelocityX;
        velocityY = initialVelocityY;
    }

    //Physics
    public void addForce(double otherBodyMass, double otherBodyX, double otherBodyY) { //use the mass, Luke
        displacementX = localX - otherBodyX;
        displacementY = localY - otherBodyY;

        numerator = (G * mass * otherBodyMass);//if too small, it will be 0 due to computer floating point stuff
        if (numerator != 0) {
            newForceX = numerator/Math.pow(displacementX,2); // x = 0/n, n ∈ R ∴ x=inf -> AVOID QUOTIENT/NUMERATOR FROM BECOMING 0, SKIP CALC IF IT IS
            newForceY = numerator/Math.pow(displacementY,2);
        }

        ForceX += newForceX;
        ForceY += newForceY;
    }

    public void update(int updateRate) {
        if (abs((float)ForceX) > 0) {
            accelerationX = ForceX/mass;
            velocityX += accelerationX / updateRate;
        }
        if (abs((float)ForceY) > 0) {
            accelerationY = ForceY/mass;
            velocityY += accelerationY / updateRate;
        }
        ForceX = 0;
        ForceY = 0;
        if (randomFloat(0,100) <= 1) {
            System.out.println("VelX:" +  velocityX + " VelY:" + velocityY);
        }
        localDynamicOrb.setLinearVelocity(new Vec2((float)velocityX*100, (float)velocityY*100));
    }



    //setters
    public void setX(float givenX) {
        localX = givenX;
        initialPosition[0] = givenX;
    }

    public void setY(float givenY) {
        localY = givenY;
        initialPosition[1] = givenY;
    }

    public void setCoords(float givenX, float givenY) {
        localX = givenX;
        localY = givenY;
        initialPosition[0] = givenX;
        initialPosition[1] = givenY;
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
        localShape = new CircleShape((float)radius*0.1f);
        localDynamicOrb = new DynamicBody(localWorld, localShape);
        localDynamicOrb.setGravityScale(0);
        localDynamicOrb.setPosition(new Vec2((float)localX,(float) localY));
        return new Sensor(localDynamicOrb, new CircleShape((float)radius));
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
