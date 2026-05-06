package PlanetaryExpansionPack1;

import city.cs.engine.*;
import game.Level;
import game.PlayerController;
import game.PlayerDynamicBody;
import org.jbox2d.common.Vec2;


import static org.jbox2d.common.MathUtils.*;

/**
 * DynamicBody with a CircleShape
 */
public class Planet {
    private float radiusX = 0;
    private float radiusY = 0;

    public float localX;
    public float localY;//optional: make these into localCoords[], but nah no point in doing it, but if I wanted it to be nicer, as coord pairs look better and are way easier to deal with, but if I'm bored then yeah otherwise, nothing wrong with this implementation

    private float[] initialPosition = new float[2];

    //private static Game localGame;
    private static World localWorld;
    private static Level localLevel;//set at start of level in load() (current level we're on: similar to currentLevelObject in Game)
    private Shape localShape;
    private DynamicBody localDynamicOrb;
    private static PlayerController localRefPlayerControllerDynamicOrb;

    //physics values - units in SI standard units (Voltage in Volts, Current in Amperes, Distance in metres)
    public float mass = 0;
    private float accelerationX = 0;
    private float accelerationY = 0;
    private float totalFrameForceX = 0;
    private float totalFrameForceY = 0;
    private float forceFactor = 10000;
    private float simSpeedUp = 1;
    private float numerator = 0;
    private float newForce = 0;
    private float newForceX = 0;
    private float newForceY = 0;
    private float softening = 0.1F;
    private float displacementX = 0;
    private float displacementY = 0;
    private float angleX = 0;
//    private float angleY = 0;
    private float displacementHypot;
    private final float G = (float) ( 6.674 * Math.pow(10,-11));
    private float radius = 0;

    private float velocityX; //better than Velocity-9!!!
    private float velocityY;//these dont work well for full tracking as they ignore the body's current velocities and just overwrite it with their value
    //the fix: use this only for calc, then use getvelocity, and then add the prev velocity to new one, and then setveloctiy


    /**
     * Active Constructor; transition from using other constructors and use this one
     * @param givenRadius float
     * @param givenX float
     * @param givenY float
     */
    public Planet(float givenRadius, float givenMass, float givenX, float givenY, float initialVelocityX, float initialVelocityY) {
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
    public void addForce(float otherBodyMass, float otherBodyX, float otherBodyY) { //use the mass, Luke
        displacementX = otherBodyX - localX;//the object that is acting needs to pull us towards it, so its actually like they're the focus
        displacementY = otherBodyY - localY;//use an example as this obj as (0,0) and other acting/calling object as (-10,-10)

        displacementHypot = sqrt((float) (Math.pow(displacementX,2) + Math.pow(displacementY,2) + softening));//can be softening squared to match the others, but not needed
        //it will pull us towards the negative quadrant so forces should both be negative. But using localX- otherbodyx would make it positive forces
        //therefore, we swap as if the other object is the focus

        numerator = (forceFactor * G * mass * otherBodyMass);//if too small, it will be 0 due to computer floating point stuff, numerator ∈ R, numerator > 0

        if (numerator != 0) {
            newForce = numerator/(float)Math.pow(displacementHypot,2);//mistake was that if the y distance shrinks but the objs are physically far due to x
//            angleY = (float) Math.asin(displacementY/displacementHypot);
            newForceX = (newForce * displacementX/displacementHypot);
            newForceY = (float) (newForce * displacementY/displacementHypot);
            //there would still be a massive force applied. This is why moving 1 object between the relative quadrants of the other objects causes insane velocity
//            newForceX = (float) (numerator/Math.pow(displacementX,2)); // if x = 0/n happens, with n ∈ R, we get x=inf -> AVOID QUOTIENT/NUMERATOR FROM BECOMING 0, SKIP CALC IF IT IS
//            newForceY = (float) (numerator/Math.pow(displacementY,2)); // ok so note, because I square this, it becomes positive, disregarding the factor of where the displacement is, positve or negative. We need to add this back on at the end

        }
        else {//otherwise, apply no force from this body, either body may have no effective mass
            newForceX = 0;
            newForceY = 0;
        }

//        if (displacementX < 0) { //if neg, then
//            totalFrameForceX -= newForceX;
//        }
//        else {
            totalFrameForceX += newForceX;
//        }
//        if (displacementY < 0) {
//            totalFrameForceY -= newForceY;
//        }
//        else {
            totalFrameForceY += newForceY;
//        }
    }

    public void update(int updateRate) {
        if (totalFrameForceX != 0) {
            accelerationX = totalFrameForceX/mass;
            velocityX += simSpeedUp * (accelerationX / updateRate);
        }
        if (totalFrameForceY != 0) {
            accelerationY = totalFrameForceY/mass;
            velocityY += simSpeedUp * (accelerationY / updateRate);//counters the updateRate or step reduction of force to match realtime,
            //so it goes from 1s to 1/60s, if updateRate was 60, and then due to 60 ticks per frame it goes back to 1 sec simulated in 60 discrete steps
            //this should speed it up so the velocity is not fragmented over so many frames. This basically just speeds it up, compared to force amplification (they kinda work in the same way to be fair)
            //this should be different as instead of amplifying forces, it can be used in a way where if the updateRate is used as the value, it will act as a speed up instead of altering the underlying physics by amplifying forces by 100x
            //icl its the same thing just done at the end
        }
        totalFrameForceX = 0;//reset forces on body for recalc in next frame
        totalFrameForceY = 0;//reset here as if done in addforce, we wouldn't be adding a force

        Vec2 lastFramePos = localDynamicOrb.getLinearVelocity();//use last real velocities instead of last calculated or fake velocities
        lastFramePos.x += velocityX;
        lastFramePos.y += velocityY;
        velocityX = 0;//added here so first update can take initial velocity parameters into account first
        velocityY = 0;//Would not change much, if no init param for vel x and y were taken
        localDynamicOrb.setLinearVelocity(lastFramePos);
        localX = localDynamicOrb.getPosition().x;
        localY = localDynamicOrb.getPosition().y;
//        Vec2 lastFramePos = localDynamicOrb.getPosition();//use last real velocities instead of last calculated or fake velocities
//        lastFramePos.x += velocityX;
//        lastFramePos.y += velocityY;
//        localDynamicOrb.setPosition(lastFramePos);
//
//        velocityX = 0;//added here so first update can take initial velocity parameters into account first
//        velocityY = 0;//Would not change much, if no init param for vel x and y were taken
//        localX = localDynamicOrb.getPosition().x;//used for updating other bodies on the position of this body
//        localY = localDynamicOrb.getPosition().y;//should equal lastFramePos.x and y respectively
    }

    //getters
    public float getX(float givenX) {
        localX = givenX;
        initialPosition[0] = givenX;
        return localX;
    }

    public float getY(float givenY) {
        localY = givenY;
        initialPosition[1] = givenY;
        return localY;
    }

    public Vec2 getCoords(float givenX, float givenY) {
        localX = givenX;
        localY = givenY;
        initialPosition[0] = givenX;
        initialPosition[1] = givenY;
        return new Vec2(initialPosition[0], initialPosition[1]);
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
