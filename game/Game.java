package game;

import city.cs.engine.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;


/** The Game class. Contains main method here and this file should be run to play the game.*/
public class Game {

    public static boolean restartGame = false;
    public static int tick = 0;//can change to private and non-static as it is passed always as a parameter

    private int maxTick = 29; //controls the frames in every in-game second (you can make seconds longer by increasing this value, but it will not speed up or slow down the physics, only logical time delays (so getting up takes longer, but the falling down part still takes the same amount of time, physical physics will be the same but logic for physics will change)
    private int fps = 60; //60fps is the max stable fps of the engine

    //all permanent bodies (should never be deleted)
    /**
     * The World that holds all interactable objects.
     */
    private World world_1_1;
    /**
     * Give a World and a width and height to the view in the constructor
     */
    private GameView view;
    /**
     * The window that shows the view
     */
    private JFrame game_window;
    /**
     * Player object in Game
     */
    private PlayerController player1;


    /**
     * The current level is stored in this object
     */
    public static Level currentLevelObject;//used to reduce fetching the same level repeatedly (refactored to private: check THIS, FUTURE ME!!! Future Me: Quick fix seeing as we got deadline (!!!) is to make it pub stat and then just unload the level in usernamePrep because it does not unload the level (no way to access the level code from there)
    /**
     * Holds all level subclasses
     */
    private ArrayList<Level> levelsCollection = new ArrayList<>(5);
    /**
     * integer that holds the value of the current level loaded. Checked against the Level class' own number. When they differ, level has changed and must be reloaded
     */
    private int gameLoadedLevel;

    /**
     * Random Number Generator
     */
    private Random rand = new Random();
    /**
     * Background Music File
     */
    private SoundFile bgMusic;
    {
        if (rand.nextInt(0, 2) == 0) {
            bgMusic = new SoundFile("data/beginning2.wav");
        } else {
            bgMusic = new SoundFile("data/moogcity2.wav");
        }
    }

    /**
     * Boolean flag to check if game is paused
     */
    public static boolean gamePaused = true;//it is static so we can sync vals with player. Only external link is with player controller class
    /**
     * Boolean flag to check if music is paused or not
     */
    public static boolean pausedMusic = false;


    /** Initialise a new Game. */
    public Game() {

        //make an empty game world
        world_1_1 = new World(fps);

        /*make a (game)view to look into the game world and create a Java window (frame) and add the view to it*/
        view = new GameView(world_1_1, 1200, 800); //we need a getter after this as either window or player needs to me made first, and I will keep the order I currently have
        //may change variable name 'view' to 'gv'
        game_window = new JFrame("Get Up - The Game");
        view.setGameWindow(game_window);//tells the view what window it is attached to

        // set the zoom on the view (default is 20)
        view.setZoom(20f);

        //add the view to the game_window so we can see it on our JFrame window
        game_window.add(view);

        //get the (JFrame) 'game_window' window
        view.getWindow(game_window);


        //optional: draw a 1-metre grid over the view
        //view.setGridResolution(1);


        //create a PlayerController for student, add a GameView reference in the PlayerController obj //KeyListener for game_window
        player1 = new PlayerController(world_1_1, view, game_window); //embedded in the constructor code is the buffer values initialiser, because it is essential I do not read a null value for the viewport
//        player1.setGameView(view);//whats the point of this again? idk redundant as constructor does it already (this needs to be uncommented depending on order of creation. seeing as this constructor is after, its built in to take the view knowing that it will be after)
        game_window.addKeyListener(player1.getKeyListener());
        game_window.addMouseListener(player1.getMouseListener());
        game_window.addMouseWheelListener(player1.getMouseWheelListener());

        //waiting platform (loading/recreating game)
        StaticPlatform sp = new StaticPlatform(world_1_1, 100, 10, 1000, 1000, 0,0,0,0,0,0,0,player1);


        //level instantiation and setup
        //the Level.setStaticLevelVariables(...) line should be moved outside where the attributes for World GameView JFrame and PlayerController are created
        Level.setStaticVariables(world_1_1,player1);//set all static variables up in Level before instantiation of subclass levels below, so all bodies do not these parsed at instantiation
        LevelZero l0 = new LevelZero();
        levelsCollection.add(l0);
        LevelTransition lt0 = new LevelTransition();
        levelsCollection.add(lt0);
        LevelOne l1 = new LevelOne();
        levelsCollection.add(l1);
        LevelTransition lt1 = new LevelTransition();
        levelsCollection.add(lt1);
        LevelTwo l2 = new LevelTwo();
        levelsCollection.add(l2);
        LevelTransition lt2 = new LevelTransition();
        levelsCollection.add(lt2);
        LevelThree l3 = new LevelThree();
        levelsCollection.add(l3);
        LevelTransition lt3 = new LevelTransition();
        levelsCollection.add(lt3);
        LevelFour l4 = new LevelFour();
        levelsCollection.add(l4);
        LevelTransition lt4 = new LevelTransition();
        levelsCollection.add(lt4);
        //Do not end on a transition (it increments the level counter, and it causes Array Index Out of Bounds error)
        InputUsername iu = new InputUsername();
        levelsCollection.add(iu);

        currentLevelObject = l0;
        currentLevelObject.load();//preload level so when user presses start, first level loads quickly
        currentLevelObject.levelLoaded = true;
        currentLevelObject.pauseBGM();//as we start in paused state of game, we pause music so it doesn't play even for a split second while changing over to the paused logic
        Level.currentLevelNumber = 0;//set the current level number, in Level. This value is used all around the code for logic
        gameLoadedLevel = 0;//current loaded level
        //whereas the gameLoadedLevel is just used within here. They are checked against each other for a change, so we know when logic
        //has moved the level number along

        //we do level creation after player creation (should also do a for loop and then have the levels be stored in a collection)

        //player object is now set in view (ok idk why the player one still has it set twice, if the view is given once already in the constructor)
        view.setPlayerController(player1);//to retrieve and display player score on the screen





//        forceLoad(10);


        // Create a new stepListener called gameStep.
        StepListener gameStep = new StepListener() {
            @Override
            public void preStep(StepEvent e) {
                player1.pauseGame();//alters the gamePaused attribute of current class (separately called here so guaranteed to run first before any other player processes)
                if (!gamePaused) {
                    // pass the global keysPaused??? or maybe not?
                    player1.allPreUpdate(tick); // Run movement update before each physics step, maybe also add view change here or in a new method
                    if (restartGame) {
                        restartGame = false;
                        resetGame();
                    }
                }
            }

            @Override
            public void postStep(StepEvent e) {
                //playing code
                if (!gamePaused) {
                    // all postStep logic (like setting new tick value for next frame)
                    //stuff to do while getting ready for the next frame, prepping the logic for the next frame
                    Game.tick += 1;
                    if (Game.tick > maxTick) {
                        //reset tick once it increments to maxTick  (example: if maxTick is 59, 61st frame/frame 60 = 1st frame/frame 0)
                        Game.tick = 0;
                        if (pausedMusic) {//level music was paused, set to false as we are switching to the level music and no longer playing pause music
                            bgMusic.pause();
                            currentLevelObject.resumeBGM();
                            pausedMusic = false;
                        }

                        player1.StopSleep();
                        if (player1.getPosition()[1] <= -30) {
                            player1.respawn();
                            //also will move player back in bounds even if dead, as logic will still report the local X position as it
                            //is not bound to the PDB and is bound to the Player Controller. It puts the player at last checkpoint
                            //as this is the last known safe place for the player recorded by the game
                        }
                        //code done in this scope level is done once per in-game second
                    }


                    //final frame updates/preparation for next frame
                    if (currentLevelObject.levelLoaded && gameLoadedLevel == Level.currentLevelNumber) {//once the numbers differ, we then update level instead of just passively updating levels
                        currentLevelObject.update();
                    }
                    else {
                        currentLevelObject = levelsCollection.get(Level.currentLevelNumber);//update the level object to the current level indicated by the public static variable of Level called currentLevelNumber
                        gameLoadedLevel = Level.currentLevelNumber;//match up the level numbers to move on the level tracker with the level number
                        currentLevelObject.load();//if you're wondering where the level unload() call is, it is in the DynamicOrb isEndCoinSensor() code.
                        //This way checks only run during collision events, drastically reducing the number of times the if statement runs.
                        //This is beneficial as it will only be true once per level so cutting it out from main checking loop in StepListener is good
                        currentLevelObject.levelLoaded = true;//this means level has loaded
                    }

                    player1.allPostUpdate(Game.tick);//done after; let's update the levelBeforePause first
                }

                //menu code
                else if (gamePaused) {
                    if (!pausedMusic) {
                        currentLevelObject.pauseBGM();
                        bgMusic.loop();
                        pausedMusic = true;
                        currentLevelObject.updateInPauseOnce();
                        //levelsCollection commit first!! (Pausing works afaik)
                    }
                    currentLevelObject.updateInPause();//updates that need to be run during pausing/put code that should run in pausing in this method
                    //stop level music here
                }
                Toolkit.getDefaultToolkit().sync();//will sync up all graphics before next frame starts calculating the new frame's graphics
                //advice given: If you notice some lag spikes, add this code, otherwise avoid modifying threads/step listener
            }
        };



        world_1_1.addStepListener(gameStep);


        /* enable the frame to quit the application
        when the x button is pressed */
        game_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game_window.setLocationByPlatform(true);
        // don't let the frame be resized
        game_window.setResizable(true);
        // size the frame to fit the world view
        game_window.pack();
        // finally, make the frame visible
        game_window.setVisible(true);

        //optional: uncomment this to make a debugging view
        //JFrame debugView = new DebugViewer(world_1_1, 640, 480);

        // start our game world simulation!
        world_1_1.start();

    }

    /**
     * Unloads current level and loads first level. Used in resetGame()
     */
    public void resetLevels() {
        currentLevelObject.unload();
        Level.currentLevelNumber = 0;//set the current level number, in Level. This value is used all around the code for logic
        gameLoadedLevel = 0;//current loaded level

        currentLevelObject = levelsCollection.get(Level.currentLevelNumber);
        currentLevelObject.load();//preload level so when user presses start, first level loads quickly
        currentLevelObject.levelLoaded = true;
        currentLevelObject.pauseBGM();//as we start in paused state of game, lvl music should start paused as well

    }

    /**
     * Resets attributes of Game class. Used in resetGame()
     */
    public void resetAttributes() {//all other non-level attrs reset here
        tick = 0;
        restartGame = false;
        gamePaused = true;
        player1.gamePaused = true;
        pausedMusic = false;
    }

    /**
     * Resets KeyListener attached to window back to the KeyListener that detects movement. Useful as the KeyListener will change on the last level, so that the player can input a username and stop moving. Used in resetGame()
     */
    public void resetWindowKeyListeners() {
        game_window.removeKeyListener(game_window.getKeyListeners()[0]);
        game_window.addKeyListener(player1.getKeyListener());
    }

    /**
     * Resets the game. Calls other methods within itself that assist with resetting it
     */
    public void resetGame() {
        player1.resetPlayer();
        resetLevels();
        resetAttributes();
        resetWindowKeyListeners();
    }

    /**
     * Forces the loading of a desired level. Runs unload() on current level. Then runs load() on new level number
     * Skips and resets all level complete conditions. Used for debugging
     * @param LevelNumber int
     */
    public void forceLoad(int LevelNumber) {
        currentLevelObject.unload();
        Level.currentLevelNumber = LevelNumber;
        gameLoadedLevel = LevelNumber;
        currentLevelObject = levelsCollection.get(Level.currentLevelNumber);
        currentLevelObject.load();
        player1.newLevel(0, -17);
        currentLevelObject.levelLoaded = true;
        gamePaused = false;
        player1.gamePaused = false;
        pausedMusic = true;
    }


    /** Run the game.*/
    public static void main(String[] args) {
        Game game = new Game();

    }




}