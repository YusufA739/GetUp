package game;



import city.cs.engine.*;
import org.jbox2d.common.Vec2;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

import static org.jbox2d.common.MathUtils.*;

/**
 * Class Wrapper for PlayerDynamicBody. Contains all player-oriented code, including but not limited to: moving, zoom levels and menu control.
 * The World needs to be provided, as well as the GameView that is being used in the current JFrame window.
 */

public class PlayerController {
    private float[] givenVelocityFloat = new float[2];
    private float[] givenCoordsFloat = new float[2];

    private int givenCoordsFloatBufferSize = 10;
    private float[][] givenCoordsFloatBuffer = new float[givenCoordsFloatBufferSize][2];

    private float speed = 10;
    private float jumpVelocity = 50f;
    private float jumpCharge = 0f;
    private float jumpChargeMax = 50f;
    private float jumpChargeIncrement = 1f;
    private float displacementX = 0;
    //private float displacementY = 0;
    //private float displacementZ = 0;
    /**
     * The distance the player has to move in x for a footstep to be considered and played
     */
    private float strideLength = 3f;
    private float superSpeedMultiplierFactor = 2f;
    private int superSpeedTimerMax = 450;
    private int superSpeedTimer = 450;
    private boolean superSpeedActive = false;

    //private boolean jumpReleased = false;

    private float angularVelocity;
    private int angle;

    private int tippingAngle = 45;
    private float gravity = 9.81f;
    private boolean canJump = false;
    //boolean canDive = false;
    private boolean onGround = false;
    private boolean inAir = false;
    //boolean inLadder = false;
    private boolean hasPeaked = false;
    private boolean isCrouched = false;

    private int lastDirectionFaced = 0; //zero means no direction 1 will be A and 3 will be D as the same mapping is used for the keys below (initial value of zero, not set to zero afterward)
    private int lastState = 0;//zero means no direction, 1 means left stand, 3 means right stand, 2 means left crouch, 4 means right crouch, 5 means left run and 6 means right run
    private int lastDirectionFacedBufferSize = 1;
    private int[] lastDirectionFacedBuffer = new int[lastDirectionFacedBufferSize];//x is direction value and y is time

    private boolean[] keysToggle = new boolean[11];//from 0 to 10: W A S D Space Shift I O Escape H L
    private int keysToggleBufferSize = 10;
    private boolean[][] keysToggleBuffer = new boolean[keysToggleBufferSize][10];//keysToggleBufferSize amount of values, and each index has 10 variable size
    private boolean getUp = false;
    private int getUpTick = 0;

    private int playerScore = 0;
    private int playerMinimumRespawnScore = 5;
    private int playerScoreDisplay = 0;
    private int playerScoreBuffer = 0;

    private int playerHealth = 1;
    private int playerMinimumRespawnHealth = 1;
    private int playerHealthDisplay = 0;
    private int playerHealthBuffer = 0;

    private float[] respawnCoordsFloat = new float[2]; //another good name for this variable is respawnPoint

    private float viewZoom = 20f;

    private boolean playSoundWhenLand = true;
    private SoundFile Jump = new SoundFile("data/Jump.wav");
    private SoundFile footstep = new SoundFile("data/footstep2.wav");
    private SoundFile fallingSound = new SoundFile("data/fallingsound.wav");
    private SoundFile deathfall = new SoundFile("data/deathfall.wav");
    private SoundFile deadSong = new SoundFile("data/ward.wav");//change to ward

    private boolean movementStored = false;
    private boolean restartPressed = false;
    public boolean gamePaused = Game.gamePaused;//centralised setting so more consistency when I change starting values in Game class
    /**
     * Tracks current menuState
     */
    public String menuState = "menu";
    /**
     * Tracks current helpscreen
     */
    public int helpscreen = 0;

    /**
     * Tracks number of calls that method has had and stops after x number of calls in allPreUpdate()
     */
    private int usernamePrepCalls = 0;
    public String name = "";
    private boolean[] alphanumericKeysToggle = new boolean[37];
    private int nameLengthMax = 10;
    private int keyCodeNumStart = 48;
    private int keyCodeNumEnd = 58;
    private int keyCodeAlphaStart = 65;
    private int keyCodeAlphaEnd = 91;
    private fileWriter scoreFile = new fileWriter("data/scoresandnames.txt");

    private Shape playerShape;
    private PlayerDynamicBody playerBody;//the physical body object that the player controls
    private World lastWorld;
    private GameView localGameView;
    private JFrame localGameWindow;



    /**
     *Constructor to create PlayerController with a PlayerDynamicBody as an attribute (class wrapper)
     * @param givenWorld World
     * @param givenView GameView
     */
    public PlayerController(World givenWorld, GameView givenView, JFrame givenGameWindow) {
        //set up the buffer with initial values (all zeroes/zeros/'0')
        setupGivenCoordsFloatBuffer();

        //make a character (with an overlaid image)
        //Shape playerShape = new PolygonShape(90,78);
        playerShape =  new BoxShape(1f, 2f);
        playerBody = new PlayerDynamicBody(givenWorld, playerShape);
        playerBody.setAlwaysOutline(false);
        playerBody.setPosition(new Vec2(0,-18));
        playerBody.addImage(new BodyImage("data/playerIdleRight.gif", 2));
        playerBody.setGravityScale(gravity);
        respawnCoordsFloat[0] = 0;
        respawnCoordsFloat[1] = -18;
        lastWorld = givenWorld;//used if player dies and game restart (new PDB for next game)(was never used, can be safely removed (AXE this))
        localGameView = givenView;
        localGameWindow = givenGameWindow;
        footstep.play();//initially play the sound so 1.it gets loaded before the first stride check 2.player makes a sound as if they just spawned into world and landed on the ground
    }

    /**
     * PreUpdate method for all PreStep tasks
     * @param givenTick
     */
    public void allPreUpdate(int givenTick) {
        movementUpdate(givenTick);//updates the velocities of the player
        imageUpdate();//updates the image (relies on velocities, so update is done afterwards for the current value, not the last (won't make much difference either way, just makes more sense this way)
        if (restartPressed) {//only way for Game.restartGame to be set to true now. Now, the restartPressed flag sets to true when the enter key on username submission is pressed, or when R is pressed.
            //and in doing so, this method executes and will set the restartGame flag to true in Game, resetting the whole game and player (and other classes that may need resetting)
            restartPressed = false;
            Game.restartGame = true;
        }
        else if (playerHealth == 0 && usernamePrepCalls == 0) {
            usernamePrep();//preps the game for receiving username from player assumption on call is that the player has no health
            restartPressed = false;
        }
    }

    /**
     * PostUpdate method for all PostStep tasks
     * @param givenTick
     */
    public void allPostUpdate(int givenTick) {
        viewUpdate();//updates the positions of the player
        if (canJump) {
            footstepSound();//position buffer 'givenCoordsFloatBuffer' will be updated in viewUpdate when the method positionBufferUpdate is called in it
            //so we can use the most correct final position of the current game frame to calculate whether the sound needs to play for the player movement
            //of the current frame (tbf it doesn't even matter much, only like a couple milliseconds different (some people are too slow to even see that!)
            //but close to no effort for a nice accuracy increase in sound playing
        }
        if (superSpeedActive) {//if there is a current speed buff...
            superSpeedTimerUpdate();//update the timer
        }
    }

    /**
     * AllUpdate method for all tasks that do not matter what part of the frame they take place
     * @param givenTick
     */
    public void allUpdate(int givenTick) {//could be renamed to update() but to keep consistency for naming within the class, I've kept it like this
    }

    /**
     * Method for controlling actions that happen while the game is paused
     */
    public void pauseGame() {
        if (!gamePaused) {
            if (keysToggle[8]) {
                keysToggle[8] = false;//prevents repeated keystroke input
                Game.gamePaused = !Game.gamePaused;
                gamePaused = !gamePaused;//used for the pause background screen in GameView class (foreground method)
                menuState = "menu";
            }
        }
        if (gamePaused) {
            if (!movementStored) {
                storeMovement();
            }
            if (menuState.equals("menu")) {
                if (keysToggle[9]) {
                    keysToggle[9] = false;//false after performing action (only set to true on key release)
                    menuState = "help";
                }
                else if (keysToggle[10]) {
                    keysToggle[10] = false;//prevent repeat input
                    menuState = "leaderboard";
                }
                else if (keysToggle[8]) {
                    keysToggle[8] = false;//prevents repeated keystroke input
                    Game.gamePaused = !Game.gamePaused;
                    gamePaused = !gamePaused;//used for the pause background screen in GameView class (foreground method)
                    menuState = "menu";
                    loadMovement();//returning back into the game so load the player's movement back from the saved frame's data
                }
            }
            else if (menuState.equals("help")) {
                if (keysToggle[8]) {
                    keysToggle[9] = false;//stops a press of H key from making us go back to this menu after we leave (pressing H while on this menu and then pressing esc would cause game to go to menu then back to help without this line
                    keysToggle[8] = false;
                    menuState = "menu";
                }
                else if (keysToggle[1] && helpscreen > 0) {
                    keysToggle[1] = false;//repeat input stopping (without it, game goes nuts and inputs like 10 times per keystroke)
                    helpscreen--;
                }
                else if (keysToggle[3] && helpscreen < 3) {
                    keysToggle[3] = false;//repeat input stopping
                    helpscreen++;
                }
            }
            else if (menuState.equals("leaderboard")) {
                if (keysToggle[8]) {
                    keysToggle[10] = false;//stops L from returning us here if it was pressed while we are on this screen
                    keysToggle[8] = false;
                    menuState = "menu";
                }
            }
        }
    }

    /**
     * Stores current movement information
     */
    public void storeMovement() {
        /**
         * takes a freeze-frame of the current motion attributes and position data of the player in the current frame and saves the values to variables already used in player.
         * Useful for storing values when the user pauses the game. Allows for motion to be suspended during pause screen for dynamic bodies. Will also freeze the player in place by disabling gravity (setting it to zero) and disabling velocity (setting it to zero)
         */
        movementStored = true;//stop this function from running again using an outside if statement and this line of code to deactivate the if statement/code block that calls this
        angularVelocity = playerBody.getAngularVelocity();
        givenVelocityFloat[0] = playerBody.getLinearVelocity().x;
        givenVelocityFloat[1] = playerBody.getLinearVelocity().y;
        givenCoordsFloat[0] = playerBody.getPosition().x;
        givenCoordsFloat[1] = playerBody.getPosition().y;
        playerBody.setGravityScale(0);//stop gravity
        playerBody.setAngularVelocity(0);//stop spinning around center of mass
        playerBody.setLinearVelocity(new Vec2(0,0));//stop movement
    }

    /**
     * Loads stored movement information
     */
    public void loadMovement() {
        /**
         * loads the freeze-frame of the stored motion attributes and position data of the player in the stored frame and loads the values to variables already used in player.
         * Useful for reloading the values after user unpauses the game. Allows for motion to be resumed to state before pause for dynamic bodies.
         */
        movementStored = false;//allows the movement to be re-stored upon pausing again
        playerBody.setGravityScale(gravity);
        playerBody.setPosition(new Vec2(givenCoordsFloat[0], givenCoordsFloat[1]));
        playerBody.setAngularVelocity(angularVelocity);
        playerBody.setLinearVelocity(new Vec2(givenVelocityFloat[0], givenVelocityFloat[1]));
//        System.out.println(givenVelocityFloat[0] + " " + givenVelocityFloat[1]);
        playerBody.setAngle(angle);
    }

    /**
     * Updates view to current buffered position of player
     */
    public void viewUpdate() {
        givenCoordsFloat[0] = givenCoordsFloatBuffer[givenCoordsFloatBufferSize-1][0];
        givenCoordsFloat[1] = givenCoordsFloatBuffer[givenCoordsFloatBufferSize-1][1];

        positionBufferUpdate(); //for clarity code is in another subroutine
        //to keep the code somewhat organised

        localGameView.setCentre(new Vec2(givenCoordsFloat[0], givenCoordsFloat[1]));

        if (keysToggle[6]) {
            zoomIn(0.1f);
        }
        if (keysToggle[7]) {
            zoomOut(0.1f);
        }
    }

    /**
     * Updates all buffers by 1 frame
     */
    public void allBuffersUpdate() {
        positionBufferUpdate();
        //lastDirectionFacedBufferUpdate();
        //keysToggleBufferUpdate();
    }

    /**
     * Updates the position buffer by 1 frame
     */
    public void positionBufferUpdate() {
        // Shift the buffer (from last to first)
        for (int i = givenCoordsFloatBufferSize-1; i > 0; i--) {
            givenCoordsFloatBuffer[i][0] = givenCoordsFloatBuffer[i-1][0];
            givenCoordsFloatBuffer[i][1] = givenCoordsFloatBuffer[i-1][1];
        }
        // Store the newest position at the front
        givenCoordsFloatBuffer[0][0] = (float) playerBody.getPosition().x;
        givenCoordsFloatBuffer[0][1] = (float) playerBody.getPosition().y;

    }

    /**
     * Not used... (Buffer for player facing direction)
     */
    public void lastDirectionFacedBufferUpdate() {//at the end of image update (imageUpdate())
        // Shift the buffer (from last to first)
        for (int i = lastDirectionFacedBufferSize-1; i > 0; i--) {
            lastDirectionFacedBuffer[i] = lastDirectionFacedBuffer[i-1];
        }
        // Store the newest position at the front
        lastDirectionFacedBuffer[0] = lastDirectionFaced;

    }

    /**
     * Updates the buffer for KeysToggle
     */
    public void keysToggleBufferUpdate() {//at the end of movement update (movementUpdate())
        // Shift the buffer (from last to first)
        for (int i = keysToggleBufferSize-1; i > 0; i--) {
            keysToggleBuffer[i][0] = keysToggleBuffer[i-1][0];
            keysToggleBuffer[i][1] = keysToggleBuffer[i-1][1];
        }
        // Store the newest position at the front
        System.arraycopy(keysToggle, 0, keysToggleBuffer[0], 0, 10);

    }

    /**
     * Updates the current image of the player to the correct one depending on the current state of the player (moving, crouching, facing left and standing etc.)
     */
    public void imageUpdate() {
        playerBody.removeAllImages();//an image WILL be added by the end of the evaluation below (as there are else statements present)
        if (round(abs(givenVelocityFloat[0])) == 0 && !keysToggle[2] && !keysToggle[5]) {
            if (lastDirectionFaced == 1) {
                //playerBody.removeAllImages(); done at top; saves not calling in each branch
                playerBody.addImage(new BodyImage("data/playerIdleLeft.gif", 4));
            }
            else { //as we know the other possible value must be 3, we need no checks (two state logic)
                //playerBody.removeAllImages();
                playerBody.addImage(new BodyImage("data/playerIdleRight.gif", 4));
            }
        }

        else if (round(abs(givenVelocityFloat[0])) == 0 && (keysToggle[2] || keysToggle[5])) {
            if (lastDirectionFaced == 1) {
                //playerBody.removeAllImages();
                playerBody.addImage(new BodyImage("data/playerCrouchLeft.png", 4));
            }
            else {
                //playerBody.removeAllImages();
                playerBody.addImage(new BodyImage("data/playerCrouchRight.png", 4));
            }
        }

        //walking gif assignment
        else if (givenVelocityFloat[0] > 0) {
            //playerBody.removeAllImages();
            playerBody.addImage(new BodyImage("data/playerRunRight.gif", 4));
            lastDirectionFaced = 3;//represents the key direction (D) right
        } //right

        else if (givenVelocityFloat[0] < 0) {
            //playerBody.removeAllImages();
            playerBody.addImage(new BodyImage("data/playerRunLeft.gif", 4));
            lastDirectionFaced = 1;//represents the key direction (A) left
        } //left



    }

    /**
     * Plays the footstep sound every time the player travels a certain distance in x. Also controls the counter for the absolute distance the player has walked (to determine whether to play a sound or not)
     */
    //can change this to rely on a coord that is updated every time the footstep plays. This way, the player has to walk away from that position for it to play instead of it playing even if the player moves about on the spot
    public void footstepSound() {
        displacementX += abs(givenCoordsFloatBuffer[0][0] - givenCoordsFloatBuffer[1][0]);
        if (displacementX>=strideLength) {
            footstep.play();
            displacementX = 0;
        }
    }

    /**
     * Checks for movement keys of the player and then updates the player using conditions relying on the player's current movement and current movement keys pressed
     * @param givenTick
     */
    public void movementUpdate (int givenTick) {
        givenVelocityFloat[0] = (float) playerBody.getLinearVelocity().x;
        givenVelocityFloat[1] = (float) playerBody.getLinearVelocity().y;
        angle = abs(round(playerBody.getAngleDegrees()));


        // Single Jump Enforcement Code (SJE Code)
        //added some tolerance into here (for risers not allowing movement) by using round() on the abs() of y velocity component
        if (round(abs(givenVelocityFloat[1])) == 0) { //the initial y=0 needs to be handled here and ignored
            hasPeaked = !hasPeaked;
            if (getUp && !hasPeaked) { //for large jumps/all jumps - once you can get up, all the stats should reset (given no y plane movement)
                onGround = true;
                inAir = false;
                canJump = true; // fully represents whether player can jump or not
                if (playSoundWhenLand) {
                    playSoundWhenLand = false;
                    if (angle > tippingAngle) {
                        fallingSound.play();
                    }
                    else {
                        footstep.play();
                    }
                }
            }
            //allKeysPaused = false;
            if (!getUp && !hasPeaked) {
                //small jumps, cannot get up yet but is still on the ground seems quite redundant
                //this is redundant because there is no reason to be classed as on the ground if you cannot do anything on the ground
                onGround = true;
                inAir = false;
                if (playSoundWhenLand) {
                    playSoundWhenLand = false;
                    if (angle > tippingAngle) {
                        fallingSound.play();
                    }
                    else {
                        footstep.play();
                    }
                }
            }

        }


        // Orientation Correction Code (OCC)
        if (angle >= +90 && (getUp)) { //swapped back to getUp so the player gets up at the right time
            playerBody.setAngleDegrees(0);
            playerBody.setPosition(new Vec2(playerBody.getPosition().x, playerBody.getPosition().y + 2f));
            jumpCharge = 0;
            localGameView.updateJumpCharge();//update the GUI to match intrinsic properties/internal logic
            //if keys are being reset, it makes sense that the key that was holding the spring/keeping the charge
            //being reset causes all the current energy to be lost
            keysToggleReset();
        }
        //end of OCC


        //Air Movement Check (AMC)
        if (round(abs(givenVelocityFloat[1])) > 0) {
            onGround = false;
            inAir = true;
            canJump = false;
            playSoundWhenLand = true;
        }
        //end of AMC

        //jumpCharge decay (JCD code)
        if (!keysToggle[5] && jumpCharge >= jumpChargeIncrement) { //removed check for shift key as it is bound to crouch only
            jumpCharge -= jumpChargeIncrement;
            localGameView.updateJumpCharge(jumpCharge);
        }
        //end of JCD code

        //Movement Control Code (MCC)
        if (canJump && (angle <= tippingAngle)) {
            /*reduce number of checks that occur (tipping angle and if player can jump)
            Do not merge with SJE code*/

            if (keysToggle[1]) {
                playerBody.setLinearVelocity(new Vec2(-speed, givenVelocityFloat[1]));
                givenVelocityFloat[0] = playerBody.getLinearVelocity().x;
                givenVelocityFloat[1] = playerBody.getLinearVelocity().y;
            }

            if (keysToggle[3]) {
                playerBody.setLinearVelocity(new Vec2(speed, givenVelocityFloat[1]));
                givenVelocityFloat[0] = playerBody.getLinearVelocity().x;
                givenVelocityFloat[1] = playerBody.getLinearVelocity().y;
            }

            if (keysToggle[4] || keysToggle[0]) {
                if (!keysToggle[2]) {
                    playerBody.setLinearVelocity(new Vec2(givenVelocityFloat[0], jumpVelocity + jumpCharge));
                }
                else {
                    playerBody.setLinearVelocity(new Vec2(givenVelocityFloat[0], (jumpVelocity + jumpCharge) * 0.8f));//may need tuning
                }
                givenVelocityFloat[0] = playerBody.getLinearVelocity().x;
                givenVelocityFloat[1] = playerBody.getLinearVelocity().y;

                jumpCharge = 0;
                localGameView.updateJumpCharge(jumpCharge); //reset jumpCharge and update HUD element for jumpCharge (do not rely on natural drain as it will only go to 1.0 for some reason (idk don't ask, as long as it is fixed now idc)

                //keysToggle[2] = false;//may need changing - minecraft crouch doesn't disengage when jumping so it makes NO sense to NOT do the same here (should cause no issues as controls only work when canJump)
                keysToggle[5] = false;

                canJump = false;
                getUp = false;
                hasPeaked = false; // when true it is the first y velocity is zero. We ignore true and accept false

                Jump.play();
                footstep.play();//manually play a footstep sound when the player jumps
            }

            if (keysToggle[5]) {
                playerBody.setLinearVelocity(new Vec2(givenVelocityFloat[0]/8, givenVelocityFloat[1])); //may need tuning
                givenVelocityFloat[0] = playerBody.getLinearVelocity().x;
                givenVelocityFloat[1] = playerBody.getLinearVelocity().y;
                if (jumpCharge < jumpChargeMax) {
                    jumpCharge += jumpChargeIncrement;
                    localGameView.updateJumpCharge(jumpCharge);
                }
            }

            if (keysToggle[2]) {
                playerBody.setLinearVelocity(new Vec2(givenVelocityFloat[0]/2, givenVelocityFloat[1])); //may need tuning
                givenVelocityFloat[0] = (float) playerBody.getLinearVelocity().x;
                givenVelocityFloat[1] = (float) playerBody.getLinearVelocity().y;
            }
        }
        //Orientation Correction Initiator (OCI)
        //once player jumps this executes instead
        else {
            angularVelocity = abs(round(playerBody.getAngularVelocity()));
            if (angularVelocity != 0) {
                getUpTick = -1;
                getUp = false;
            }
            if (getUpTick == givenTick && angularVelocity == 0) {
                getUp = true;
                getUpTick = -1;
            }
            if (angularVelocity == 0 && getUpTick == -1) {
                getUpTick = givenTick;
            }
        }

    }

    /**
     * Returns a KeyListener that will listen for specific keys. Can be customised for actions to do for all types of key signals. Can also be customised to listen to specific keys only
     * @return KeyListener
     */
    public KeyListener getKeyListener() {
        //method is used to create a KeyAdapter for player inputs
        KeyListener playerKeyListener = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                    keysToggle[0] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    keysToggle[1] = true;
                    keysToggle[3] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    keysToggle[2] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    keysToggle[3] = true;
                    keysToggle[1] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    keysToggle[4] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    keysToggle[5] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_I) {
                    keysToggle[6] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_O) {
                    keysToggle[7] = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
                    keysToggle[0] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    keysToggle[1] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    keysToggle[2] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    keysToggle[3] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    keysToggle[4] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    keysToggle[5] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_I) {
                    keysToggle[6] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_O) {
                    keysToggle[7] = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    keysToggle[8] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_H) {
                    keysToggle[9] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_L) {
                    keysToggle[10] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_Q && gamePaused) {//only allow Q quit on pause screen, don't want a user to misclick a key and then end up losing their progress
                    localGameWindow.dispatchEvent(new WindowEvent(localGameWindow, WindowEvent.WINDOW_CLOSING));
                }
                if (e.getKeyCode() == KeyEvent.VK_R && !gamePaused) {//make sure game isn't paused before allowing the key to alter flag, don't want a user to spam restart when
                    //on paused screen and then when they resume, the game jumps back to pause screen instantly. Looks weird and funky. (Paused->presses R->Game does not reset if paused->unpauses->game resets->looks weird bc you just left the screen)
                    restartPressed = true;
                }
                
            }
        };
        return playerKeyListener;
        //end of keyAdapter
        //If I do keyDown, it would have to have velocities just put in here (if this was keydown keyListener)
    }

    /**
     * Returns a KeyListener specifically designed to listen to only alphanumeric keys and responds differently compared to KeyListener for movement (getKeyListener())
     * @return KeyListener
     */
    public KeyListener getNameInputter() {
        //method is used to create a KeyAdapter for player inputs
        KeyListener nameInputter = new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && name.length() >= 1) {
                    alphanumericKeysToggle[36] = true;
                }
                else if (name.length() < nameLengthMax){
                    if ((e.getKeyCode() >= keyCodeNumStart && e.getKeyCode() <= keyCodeNumEnd)) {
                        alphanumericKeysToggle[e.getKeyCode() - keyCodeNumStart] = true;
                    } else if ((e.getKeyCode() >= keyCodeAlphaStart && e.getKeyCode() <= keyCodeAlphaEnd)) {
                        alphanumericKeysToggle[e.getKeyCode() - keyCodeAlphaStart] = true;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if ((e.getKeyCode() >= keyCodeNumStart && e.getKeyCode() <= keyCodeNumEnd)) {
                    if (alphanumericKeysToggle[e.getKeyCode()-keyCodeNumStart]) {
                        name += e.getKeyChar();
                        alphanumericKeysToggle[e.getKeyCode()-keyCodeNumStart] = false;
                    }
                }
                else if ((e.getKeyCode() >= keyCodeAlphaStart && e.getKeyCode() <= keyCodeAlphaEnd)) {
                    if (alphanumericKeysToggle[e.getKeyCode()-keyCodeAlphaStart]) {
                        name += e.getKeyChar();
                        alphanumericKeysToggle[e.getKeyCode() - keyCodeAlphaStart] = false;
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    if (alphanumericKeysToggle[36]) {
                        name = name.substring(0, name.length() - 1);
                        alphanumericKeysToggle[36] = false;
                    }
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    //record to file
                    if (!name.isEmpty()) {//can replace with name.isEmpty() but I won't for now because no reason to so far
                        try {
                            scoreFile.writeHighScore(name, (playerScore * 0.95 + (playerHealth * 5)));//writes and closes file in one method (final score calculation done in argument
                            localGameView.reloadFile(localGameView.scorefilePath);//force reload the file from disk
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    restartPressed = true;//now we set bool flag to true instead of in the code block where it was before in PlayerController code (playerhealth == 0 check)
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    restartPressed = true;
                }
                else if (e.getKeyCode() == KeyEvent.VK_R) {
                    restartPressed = true;
                }
            }
        };
        return nameInputter;
    }

    /**
     * Returns a MouseListener for interpreting button inputs for mouse
     * @return MouseListener
     */
    public MouseListener getMouseListener() {
        MouseListener playerMouseListener = new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        return playerMouseListener;
    }

    /**
     * Returns a MouseWheelListener that zooms out the screen
     * @return MouseWheelListener
     */
    public MouseWheelListener getMouseWheelListener() {
        MouseWheelListener playerMouseWheelListener = new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                viewZoom -= e.getWheelRotation();
                localGameView.setZoom(viewZoom);
            }
        };
        return playerMouseWheelListener;
    }

    /**
     * Sets up the Coordinate float buffer with (0,0) coordinates initially
     */
    public void setupGivenCoordsFloatBuffer() {
        //is not needed and I have checked, but it runs only at startup anyway
        for (int i = 0; i < givenCoordsFloatBufferSize; i++) {
            Arrays.fill(givenCoordsFloatBuffer[i], 0);
        }
    }

    /**
     * Stops the playerBody from falling asleep as dynamic bodies go to sleep if not interacting with anything for more than 1 second
     */
    public void StopSleep() {
        if (canJump) {//can also be replaced with check for zero velocity in y plane (as this is when it stays standing in mid-air
            playerBody.setLinearVelocity(new Vec2(givenVelocityFloat[0], givenVelocityFloat[1] + 0.00001f));
        }
    }

    /**
     * Fills keysToggle array with all false values
     */
    public void keysToggleReset() {
        Arrays.fill(keysToggle,false);
    }


    public void scoreIncrease() {
        playerScore += 1;
        localGameView.updateScore(playerScore);
    } // for fixed increase of 1


    public void scoreIncrease(int givenScoreDelta) {
        playerScore += givenScoreDelta;
        localGameView.updateScore(playerScore);
    } // for custom score increase amount


    public void scoreDecrease() {
        playerScore -= 1;
        localGameView.updateScore(playerScore);
    } // done in respawn()


    public void scoreDecrease(int givenScoreDelta) {
        playerScore -= givenScoreDelta;
        localGameView.updateScore(playerScore);
    } // done in respawn()


    public void healthIncrease() {
        playerHealth +=1;
        localGameView.updateHealth(playerHealth);
    } // for fixed increase of 1


    public void healthIncrease(int givenHealthDelta) {
        playerHealth += givenHealthDelta;
        localGameView.updateHealth(playerHealth);
    } // for custom health increase amount


    public void healthDecrease() {
        playerHealth -= 1;
        localGameView.updateHealth(playerHealth);
    } // done in respawn()


    public void healthDecrease(int givenHealthDelta) {
        playerHealth += givenHealthDelta;
        localGameView.updateScore(playerHealth);
    } // done in respawn()

    /**
     * Respawns player, controlling whether to take health or score, not both or mixed. Either OR action.
     */
    public void respawn() {
        if (playerScore >= playerMinimumRespawnScore) { //check if the score is greater than the minimum for score respawn
            playerScore -= playerMinimumRespawnScore;
            localGameView.updateScore(playerScore);  // could be scoreDecrease(playerMinimumRespawnSore) as well (for simplicity?) both solutions are valid
            //code duplicate
            respawnActivate();
            //end of duplicate
        }
        else if (playerHealth > playerMinimumRespawnHealth) { //check if there is enough health for a health respawn
            //(as player does not have enough score for a score respawn, which is checked beforehand)
            playerHealth -= playerMinimumRespawnHealth;
            localGameView.updateHealth(playerHealth);
            //code duplicate 2
            respawnActivate();
            //end of duplicate 2
        }//changed health check it acts more like 'health' instead of 'lives' where you can have zero lives and still be alive
        else {//player has no more health
            playerHealth = 0;
            localGameView.updateHealth(playerHealth);
            playerBody.setPosition(new Vec2(respawnCoordsFloat[0], respawnCoordsFloat[1]));
            deathfall.play();
            //playerBody.destroy(); do not destroy
            localGameView.setCentre(new Vec2(respawnCoordsFloat[0], respawnCoordsFloat[1]));
            keysToggleReset();
        }
    }

    /**
     * Duplicate code was extracted into a new method to reduce code redundancy and duplication of code
     */
    public void respawnActivate() {
        jumpCharge = 0;
        localGameView.updateJumpCharge(jumpCharge);
        playerBody.setPosition(new Vec2(respawnCoordsFloat[0], respawnCoordsFloat[1]));
        deathfall.play();
        playerBody.setAngularVelocity(0);
        playerBody.setAngle(0);
        playerBody.setLinearVelocity(new Vec2(0, 0));
        keysToggleReset();
    }

    /**
     * Sets new spawn point for level and puts player upright (tilt angle = 0) and with 0 value for velocities (angular,linear) that could've been carried over from last level
     * @param givenX (float)
     * @param givenY (float)
     */
    public void newLevel(float givenX, float givenY) {
        playerBody.setAngularVelocity(0);//we don't want to set angle to 0 deg only for the player to keep spinning and fall over
        playerBody.setAngle(0);//doesn't matter whether radians or degrees, sin(0 deg) === sin(0 rad)
        playerBody.setLinearVelocity(new Vec2(0, 0));//I do love seeing the player get knocked into the next level,
        superSpeedTimer = 1;
        //but for the sake of user experience, I'll make these reset the player before every level.
        //Feedback from users can have this changed back
        playerBody.setPosition(new Vec2(givenX, givenY));
        setCheckpoint();//set checkpoint as the start of level
    }

    /**
     * Sets superSpeedTimer to max value and increases speed value using a multiplier
     */
    public void superSpeedPickup() {
        //call once upon pickup (collision) with anything that causes a speed boost
        superSpeedTimer = superSpeedTimerMax;
        if (!superSpeedActive) {/*when picked up, at the first instance, the speed buff is not active, so this runs once. Setting flag to true in this block stops it from running this block more than once.
            Prevents multiple speed buffs from stacking, only resetting the timer to max time. This is desired as the speed potion should increase speed to a certain amount, not keep amplifying the speed repeatedly.*/
            speed = speed * superSpeedMultiplierFactor;
            superSpeedActive = true;//stops block from running more than once
        }
    }

    /**
     * Deactivates super speed by keeping track of a timer and then setting speed back to normal by multiplying speed by reciprocal of multiplier.
     * Updates the HUD's time tracker for the player to know how much time is left on the clock.
     */
    public void superSpeedTimerUpdate() {
        //was going to call it speedBuff but
        //superSpeed sounds cooler and looks cooler when written than the generic, bland 'speedBuff'. SUPERSPEEED!!!!!
        //call this method at the end of a frame (similar to while loops, maintain loop invariant condition and also to make sure timer is zeroed on last
        //frame)
        superSpeedTimer--;//shows time remaining (when zero this means no time is left, so we deactivate the boost)
        if (superSpeedTimer <= 0) {
            speed = speed * (1/superSpeedMultiplierFactor);
            superSpeedActive = false;//allows the superSpeedPickup if statement block to run again, only after the timer reaches zero and the speed is reset
        }
        localGameView.updateSuperSpeedTimer(superSpeedTimer);
        //if you put a sout statement here for time, it will show you the time remaining on next frame
    }


    //setters

    /**
     * Sets the checkpoint to current Coordinates of the playerBody
     */
    public void setCheckpoint() {
        respawnCoordsFloat[0] = playerBody.getPosition().x;//could change to coords buffer first value
        respawnCoordsFloat[1] = playerBody.getPosition().y;
    }

    /**
     * Teleports the player
     * @param x (float)
     * @param y (float)
     */
    public void teleport(float x, float y) {
        playerBody.setPosition(new Vec2(x, y));
        keysToggleReset();
    }

    /**
     * Prepares Player for Username inputting
     */
    public void usernamePrep() {
        usernamePrepCalls += 1;
        Game.currentLevelObject.unload();//stops bgm as well as removing all lvl bodies
        Level.currentLevelNumber = 10;
        gravity = playerBody.getGravityScale();//start of kind of a quick fix
        playerBody.setGravityScale(0);
        playerBody.setAngularVelocity(0);
        playerBody.setAngle(0);
        playerBody.setLinearVelocity(new Vec2(0, 0));//end of kind of a quick fix
        deadSong.loop();
    }

    /**
     * Resets the PlayerController management variables (extrinsic) for the playerBody and resets the (intrinsic) attributes of the playerBody itself
     */
    public void resetPlayer() {
//        if (playerHealth < playerMinimumRespawnHealth) {
//            playerBody = new PlayerDynamicBody(lastWorld, playerShape);//new body as old one was destroyed
//            playerBody.setPosition(new Vec2(1050,1010));
//        } respawn now does not delete the body, so underlying issue/bug resolved instead of a patch added to a bug
        usernamePrepCalls = 0;//reset the number of calls in usernamePrepCalls
        deadSong.stop();//stop the song
        playerBody.setLinearVelocity(new Vec2(0, 0));
        givenVelocityFloat[0] = 0;
        givenVelocityFloat[1] = 0;
        playerBody.setPosition(new Vec2(0, 1010));//done again, in case player didn't get destroyed the first time
        givenCoordsFloat[0] = 0;
        givenCoordsFloat[1] = 1010;
        setupGivenCoordsFloatBuffer();
        jumpCharge = 0f;
        displacementX = 0f;
        superSpeedTimer = 0;//can set to anything 1 or lower, and it will now reset properly, I added more robustness to the timer reset (<=0 instead of == 0)
        if (superSpeedActive) {
            superSpeedTimerUpdate();
        }
        playerBody.setAngularVelocity(0);
        playerBody.setAngleDegrees(0);
        angularVelocity = 0;
        angle = 0;
        canJump = false;
        onGround = false;
        inAir = false;
        hasPeaked = false;
        isCrouched = false;
        lastDirectionFaced = 0;
        lastState = 0;
        lastDirectionFacedBuffer = new int[lastDirectionFacedBufferSize];
        keysToggleReset();
        getUp = false;
        getUpTick = 0;
        playerScore = 0;
        playerHealth = 1;
        respawnCoordsFloat[0] = 0;
        respawnCoordsFloat[1] = 1010;
        viewZoom = 20f;
        setZoom(viewZoom);
        playSoundWhenLand = true;
        restartPressed = false;
        Game.gamePaused = false;
        gamePaused = Game.gamePaused;
        menuState = "menu";
        playerBody.setGravityScale(gravity);
        localGameView.updateHealth();
        localGameView.updateScore();
        localGameView.updateJumpCharge();

    }

    /**
     * Sets the GameView to the given GameView
     * @param givenGameView GameView
     */
    //get the View (allows for in-game player statistics to be updated (HUD updates))
    public void setGameView(GameView givenGameView) {
        localGameView = givenGameView;
    }

    /**
     * Sets the zoom to a given float value
     * @param givenZoom (float)
     */
    public void setZoom(float givenZoom) {//internal use
        viewZoom = givenZoom;
        localGameView.setZoom(viewZoom);
    }

    /**
     * Zoom Out the GameView
     * @param ZoomChange
     */
    public void zoomOut(float ZoomChange) {//external use
        viewZoom -= ZoomChange;
        localGameView.setZoom(viewZoom);
    }

    /**
     * Zoom In the GameView
     * @param ZoomChange
     */
    public void zoomIn(float ZoomChange) {//external use
        viewZoom += ZoomChange;
        localGameView.setZoom(viewZoom);
    }

    /**
     * Sets the view position to the given Coordinates parsed. (X) and (Y) respectively
     * @param x (float)
     * @param y (float)
     */
    public void setViewPosition(float x, float y) {//should be used during paused screen so that the player cannot see the level
        localGameView.setCentre(new Vec2(x, y));
    }
    //end of setters


    //getters (purely getter - no logic/non-Listener)
    /**
     * Returns the current Coordinate pair of the player (X,Y)
     * @return (float)
     */
    public float[] getPosition() {
        return givenCoordsFloat;
    }

    /**
     * Returns the current score of the player
     * @return int
     */
    public int getScore() {
        return playerScore;
    }

    /**
     * Returns the current health of the player
     * @return int
     */
    public int getHealth() {
        return playerHealth;
    }

    /**
     * Returns the current jumpCharge of the player
     * @return (float)
     */
    public float getJumpCharge() {
        return jumpCharge;
    }

    /**
     * Returns the current time left on the current speed boost
     * @return (float)
     */
    public float getSuperSpeedTimer() {
        return superSpeedTimer;
    }

    /**
     * Returns the current status of the boolean flag 'superSpeedActive'
     * @return boolean
     */
    public boolean getSuperSpeedActive() {
        return superSpeedActive;
    }

    /**
     * Returns the current Zoom Level set
     * @return (float)
     */
    public float getZoom() {
        return viewZoom;
    }

    //end of getters (purely getter - no logic/non-Listener)


    /*deprecated
    public void movementPlatformUpdate() {
    unused idea
    }
    end of deprecated*/
}
