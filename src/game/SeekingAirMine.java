package game;



import city.cs.engine.*;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Timer;
import org.jbox2d.common.Vec2;

import static org.jbox2d.common.MathUtils.*;

/**
 * Classic Seeking Air Mine. Will seek after players who enter its vicinity.
 * Will stop chasing players who escape its lock-on targeting vicinity and will then switch to idle mode where it will hover and bob - patrolling an area vertically to detect the player.
 * Has no fuel limit
 */
public class SeekingAirMine {
    private Timer time = new Timer();
    private float[] orbitCoords = new float[2];
    private float[] velocities = new float[2];
    private float angularVelocity = 0.0f;
    private float angle = 0.0f;
    private float[] coords = new float[2];
    private float[] playerCoords = new float[2];
    private float speed = 0f;
    private float travelAngle = 0.0f;
    private float speedIncrement = 0.01f;
    private float maxSpeed = 25;
    private float activationDistance = 30;
    private float gravity = 0;
    private float radius = 1;
    private boolean powerOff = false;
    private BodyImage[] images = new BodyImage[2];{images[0] = fetchImage("data/seekingsmall0.png");images[1] = fetchImage("data/seekingsmall1.png");}
    private SoundFile lockOn = new SoundFile("data/lock-on.wav", 10);
    private SoundFile persistentLock = new SoundFile("data/persistent-lock.wav");
    private int imagecounter = 0;
    private boolean playlockonce = true;
    private boolean playpersistentonce = true;

    private int frameWait = 20;//same function as frameduration in GameView
    private int thetaY = 0;
    private float localY;
    private float coefficientOfDrag = 0.95f;//or 'slowdown' factor
    private boolean dragging = false;//bool flag for drag (air resistance active or not)
    public boolean movementStored = false;//used to check in level updateOnceInPause if the movement is stored or not

//    private String mode = "slowing";//2 idle modes: slowing and orbit

    private EnemyDynamicBody EDB;
    private static PlayerController PC1;
    private static World localWorld;
    private static Level localLevel;

    /**
     * Constructor taking PlayerController object (current player), World, initial/starting X and Y values (float) (float)
     * @param pc PlayerController
     * @param givenWorld World
     * @param StartX (float)
     * @param StartY (float)
     */
    public SeekingAirMine(PlayerController pc, World givenWorld, float StartX, float StartY) {
        //make a character (with an overlaid image)
        Shape enemyShape =  new CircleShape(radius);
        EnemyDynamicBody enemyDynamicBody = new EnemyDynamicBody(givenWorld, enemyShape); //enemyDynamicBody should probably be named 'enemy' but idc I'm putting it as this for the current future
        enemyDynamicBody.setAlwaysOutline(false);
        enemyDynamicBody.setPosition(new Vec2(StartX,StartY));
        enemyDynamicBody.setGravityScale(gravity);
        EDB = enemyDynamicBody;
        PC1 = pc;
    }

    public SeekingAirMine(float StartX, float StartY) {
        //make a character (with an overlaid image)
        Shape enemyShape =  new CircleShape(radius);
        EnemyDynamicBody enemyDynamicBody = new EnemyDynamicBody(localWorld, enemyShape); //enemyDynamicBody should probably be named 'enemy' but idc I'm putting it as this for the current future
        enemyDynamicBody.setAlwaysOutline(false);
        enemyDynamicBody.setPosition(new Vec2(StartX,StartY));
        enemyDynamicBody.setGravityScale(gravity);
        EDB = enemyDynamicBody;
    }

    /**
     * Update the image of the mine so it looks like it's blinking at a fixed duration
     * @param givenTick int
     */
    public void imageUpdate(int givenTick) {
        if (imagecounter > images.length - 1) {
            imagecounter = 0;
        }
        if (givenTick % frameWait == 0) {
            EDB.removeAllImages();
            EDB.addImage(images[imagecounter]);
            imagecounter++;
        }

    }

    /**
     * Update method to process data for current frame and react to the data
     * @param tick
     */
    public void update(int tick) {
        movementUpdate();
        imageUpdate(tick);
    }

    /**
     * called every game frame; updates the movement position and all movement-related data
     */
    public void movementUpdate () {
        if (!Game.pausedMusic) {
            if (movementStored) {
//                System.out.println("RUNNING");
                loadMovement();
            }
            if (coords[1] < -50) {
                EDB.destroy();
                powerOff = true;
                persistentLock.stop();
                lockOn.stop();
            }
            else if (!powerOff) {
                coords[0] = EDB.getPosition().x;
                coords[1] = EDB.getPosition().y;
                velocities[0] = EDB.getLinearVelocity().x;
                velocities[1] = EDB.getLinearVelocity().y;
                playerCoords = PC1.getPosition();
                if (calculateDistance(playerCoords[0], playerCoords[1]) < activationDistance) {
                    if (playlockonce) {
                        playlockonce = false;
//                    mode = "slowing";
                        lockOn.play();
                        time.reset();
                    }
                    if (speed < maxSpeed) {
                        speed += speedIncrement;
                    }
                    if (time.getMilliseconds() / 1000 >= lockOn.length && playpersistentonce) {
                        playpersistentonce = false;
                        persistentLock.loop();

                    }
                    travelAngle = MathUtils.atan2(playerCoords[0] - coords[0], playerCoords[1] - coords[1]);
                    EDB.setLinearVelocity(new Vec2(speed * sin(travelAngle) + velocities[0], speed * cos(travelAngle) + velocities[1]));
                }
                else {
                    //this must run once, and it will, because we turn playlockonce to true after this block executes (when powered on)
                    if (!playlockonce || powerOff) {//if true, changes must have occurred to these vars. so reset these 3 affected vars.
                        persistentLock.stop();
                        lockOn.stop();
                        speed = 0;
                        playlockonce = true;
                        playpersistentonce = true;
                        //calcs we only want to do once for the first moment of the seeking air mine losing sight of the player
                        coords[0] = EDB.getPosition().x;//update to this current stopped position x
                        coords[1] = EDB.getPosition().y;//update to this current stopped position y
                        velocities[0] = EDB.getLinearVelocity().x;
                        velocities[1] = EDB.getLinearVelocity().y;
                    }
                    dynamicOrbit();
                }
            }
        }
//        else {
//            if (!movementStored) {
//                storeMovement();
//            }
//        }
    }

    /**
     * Controls IDLE function - PATROL MODE
     */
    public void dynamicOrbit() {
        if (!Game.pausedMusic) {
            //angular velocity reduction
            angularVelocity = EDB.getAngularVelocity();
            if (abs(angularVelocity) > 3.1415926535 / 180) {
                EDB.setAngularVelocity(EDB.getAngularVelocity() * coefficientOfDrag);
            } else if (abs(angularVelocity) <= 3.1415926535 / 180 && angularVelocity != 0) {//that value is the radians/sec value equivalent of 1 degree/sec (2pi rad/360 deg) = 1 deg
                EDB.setAngularVelocity(0);
            }

            //velocity reduction
            velocities[0] = EDB.getLinearVelocity().x;
            velocities[1] = EDB.getLinearVelocity().y;
            coords[0] = EDB.getPosition().x;
            coords[1] = EDB.getPosition().y;
            if (abs(velocities[0]) > 2) {
                velocities[0] = velocities[0] * coefficientOfDrag;
                EDB.setLinearVelocity(new Vec2(velocities[0], velocities[1]));
                dragging = true;
            } else if (abs(velocities[0]) <= 2 && velocities[0] != 0) {
                velocities[0] = 0;
                EDB.setLinearVelocity(new Vec2(velocities[0], velocities[1]));
                dragging = true;
            }
            if (abs(round(velocities[1])) > 2) {
                velocities[1] = velocities[1] * coefficientOfDrag;
                EDB.setLinearVelocity(new Vec2(velocities[0], velocities[1]));
                dragging = true;
            } else if (abs(round(velocities[1])) <= 2 && velocities[1] != 0) {
                velocities[1] = 0;
                EDB.setLinearVelocity(new Vec2(velocities[0], velocities[1]));
                dragging = true;
            }
            //all velocity reductions are done for this iteration

            //now check whether we should reset the orbital coordinates (a change, and zero velocity must occur otherwise we cannot re-set the orbital coordinates)
            if (velocities[0] == 0 && velocities[1] == 0 && dragging) {//done to reduce block execution (by checking for change (dragging))
                dragging = false;
                orbitCoords[0] = coords[0];
                orbitCoords[1] = coords[1];
                //set new orbit position, as we may have moved far away from initial orbit position
                //set to new values every time we have checked our velocity to be zero
            }
            localY = coords[1] + 0.1f * sin(thetaY * ((2 * PI) / 360));
            thetaY += 1;
            if (thetaY >= 360) {
                thetaY -= 360;//keeps motion smooth if there are decimal values still present, as sine wave won't be cut
            }
            EDB.setPosition(new Vec2(coords[0], localY));
        }
    }

    /**
     * stores the freeze-frame of the current motion attributes and position data of the player in the current frame and loads the values to variables already used in player.
     * Useful for reloading the values after user unpauses the game. Allows for motion to be resumed to state before pause for dynamic bodies.
     */
    public void storeMovement() {
        coords[0] = EDB.getPosition().x;
        coords[1] = EDB.getPosition().y;
//        EDB.setPosition(new Vec2(-1000,-1000));//move outta the way! (literally moves it out of the way, nothing is in the empty space at (-1000, -1000)
        lockOn.pause();
        persistentLock.stop();
        playpersistentonce = true;
        movementStored = true;//stop this function from running again using an outside if statement and this line of code to deactivate the if statement/code block that calls this
        angularVelocity = EDB.getAngularVelocity();
        angle = EDB.getAngle();//doesn't matter whether deg or rad as we do no extra processing on the data
        velocities[0] = EDB.getLinearVelocity().x;
        velocities[1] = EDB.getLinearVelocity().y;

        EDB.setGravityScale(0);//stop gravity
        EDB.setAngularVelocity(0);//stop spinning around center of mass
        EDB.setLinearVelocity(new Vec2(0,0));//stop movement
//        System.out.println("stored movement");
    }

    /**
     * loads the freeze-frame of the stored motion attributes and position data of the player in the stored frame and loads the values to variables already used in player.
     * Useful for reloading the values after user unpauses the game. Allows for motion to be resumed to state before pause for dynamic bodies.
     */
    public void loadMovement() {
        movementStored = false;//allows the movement to be re-stored upon pausing again
        if (calculateDistance(playerCoords[0], playerCoords[1]) < activationDistance) {
            if (time.getMilliseconds() / 1000 < lockOn.length) {
                lockOn.resume();
            } else {
                persistentLock.resume();
            }
        }
        EDB.setGravityScale(gravity);
        EDB.setPosition(new Vec2(coords[0], coords[1]));//this alone should be helpful but for some reasoning I need to also have this line in the storeMovement() code
        EDB.setAngularVelocity(angularVelocity);
        EDB.setLinearVelocity(new Vec2(velocities[0], velocities[1]));
    }

    /**
     * Calculate the distance using Pythagoras' theorem
     * @param enemyX
     * @param enemyY
     * @return
     */
    public double calculateDistance (float enemyX, float enemyY) {
        return Math.sqrt(Math.pow(abs(coords[0] - enemyX), 2) + Math.pow(abs(coords[1] - enemyY), 2));
    }

    /**
     * BodyImage fetcher
     * @param filepath
     * @return
     */
    public BodyImage fetchImage(String filepath) {
        try {
            return new BodyImage(filepath, radius*2);
        } catch (Exception e) {
            return missingImage();
        }
    }

    /**error catcher for images
     *
     * @return BodyImage (placeholder image)
     */
    public BodyImage missingImage() {
        System.out.println("Initial Image Missing - PlaceHolder Image Found: MINOR ERROR (FOR SEEKING AIR MINE)");
        return new BodyImage("data/missingimage.png");
    }

    
    public void destroy() {
        time.reset();//can remove if wanted
        lockOn.stop();
        persistentLock.stop();
        EDB.destroy();
    }
    
    public static void setLevel(Level givenLevel) {
        localLevel = givenLevel;
    }

    public static void setWorld(World givenWorld) {
        localWorld = givenWorld;
    }

    public static void setPlayerController(PlayerController givenPlayerController) {
        PC1 = givenPlayerController;
    }
    
}
