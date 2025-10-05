package game;

import city.cs.engine.SoundClip;
import city.cs.engine.World;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Level Superclass. Abstract class used for all levels to reduce code redundancy and duplication.#
 */
public abstract class Level {
    StaticPlatform platformStaticNull;
    DynamicPlatform platformDynamicNull;

    ArrayList<StaticPlatform> sAll = new ArrayList<>(10);//used for deletion of all level objects of this type
    ArrayList<DynamicPlatform> dAll = new ArrayList<>(10);//used for deletion of all level objects of this type
    ArrayList<StaticOrb> soAll = new ArrayList<>(10);//used for deletion of all level objects of this type
    ArrayList<DynamicOrb> doAll = new ArrayList<>(10);//used for deletion of all level objects of this type
    ArrayList<SeekingAirMine> sMineAll = new ArrayList<>(2);//used for deletion of all level objects of this type

//    ArrayList<StaticPlatform> sStationary = new ArrayList<>(10);//current use: none
//    ArrayList<DynamicPlatform> dStationary = new ArrayList<>(10);//current use: none
//    ArrayList<StaticOrb> soStationary = new ArrayList<>(10);//current use: none
//    ArrayList<DynamicOrb> doStationary = new ArrayList<>(10);//current use: none

    ArrayList<StaticPlatform> sRiser = new ArrayList<>(10);
    ArrayList<StaticPlatform> sSlider = new ArrayList<>(10);
    ArrayList<StaticPlatform> sGravity = new ArrayList<>(10);
    ArrayList<StaticPlatform> sDiagonal = new ArrayList<>(10);
    ArrayList<StaticPlatform> sOrbit = new ArrayList<>(10);

    ArrayList<DynamicPlatform> dRiser = new ArrayList<>(10);
    ArrayList<DynamicPlatform> dSlider = new ArrayList<>(10);
    ArrayList<DynamicPlatform> dGravity = new ArrayList<>(10);
    ArrayList<DynamicPlatform> dDiagonal = new ArrayList<>(10);
    ArrayList<DynamicPlatform> dOrbit = new ArrayList<>(10);

    ArrayList<StaticOrb> soRiser = new ArrayList<>(10);
    ArrayList<StaticOrb> soSlider = new ArrayList<>(10);
    ArrayList<StaticOrb> soGravity = new ArrayList<>(10);
    ArrayList<StaticOrb> soDiagonal = new ArrayList<>(10);
    ArrayList<StaticOrb> soOrbit = new ArrayList<>(10);

    ArrayList<DynamicOrb> doRiser = new ArrayList<>(10);
    ArrayList<DynamicOrb> doSlider = new ArrayList<>(10);
    ArrayList<DynamicOrb> doGravity = new ArrayList<>(10);
    ArrayList<DynamicOrb> doDiagonal = new ArrayList<>(10);
    ArrayList<DynamicOrb> doOrbit = new ArrayList<>(10);

    ArrayList<SeekingAirMine> sMineUpdate = new ArrayList<>(10);
    //ArrayList<ArrayList> CollectionsCollection = new ArrayList<>(10);

    boolean levelComplete = false;
    boolean levelFinish = false;
    boolean levelLoaded = false;
    boolean levelPerfect = false;

    static int currentLevelNumber = 0;//increment every unload() of a level
    static Game localGame;
    static World localWorld;
    static PlayerController localPCLevel;


    public Level(World givenWorld, PlayerController givenPlayerController) {
//        setStaticVariables(givenWorld,givenPlayerController); called at start of game, not needed here
    }

    /**
     * unused alternate
     * @param givenPlayerController PlayerController
     */
    public Level(PlayerController givenPlayerController) {
        localPCLevel = givenPlayerController;
//        setStaticVariables(); called at start of game, not needed here
    }

    /**
     * (IN USE) takes no params
     */
    public Level() {
//        setStaticVariables(); called at start of game, not needed here
    }

    /**
     * (IN USE) Calls static variables and takes new World and PlayerController as params.
     * Called before Game main game loop
     * @param givenWorld World
     * @param givenPlayerController PlayerController
     */
    public static void setStaticVariables(World givenWorld, PlayerController givenPlayerController) {
        localWorld = givenWorld;
        localPCLevel = givenPlayerController;
        setStaticVariables();
    }//static assignment of variables that will never (as currently planned) change

    //sets player and world static vars within bodies. Not meant to be called by a level body, meant to be used statically in Game once

    /**
     * Sets all initial, unchanging static variables up in all non-player bodies.
     * Called by method with same name, and that method is called before main game loop in Game
     */
    public static void setStaticVariables() {
        //sets all statics within bodies
        //the method with the same name and different params
        StaticPlatform.setPlayerController(localPCLevel);
        StaticPlatform.setWorld(localWorld);
        StaticOrb.setPlayerController(localPCLevel);
        StaticOrb.setWorld(localWorld);
        DynamicPlatform.setPlayerController(localPCLevel);
        DynamicPlatform.setWorld(localWorld);
        DynamicOrb.setPlayerController(localPCLevel);
        DynamicOrb.setWorld(localWorld);
        SeekingAirMine.setPlayerController(localPCLevel);
        SeekingAirMine.setWorld(localWorld);
    }//static assignment of variables that will never (as currently planned) change
    //called before game starts up, once

    /**
     * updates all static variables relying on level number within bodies (changes between levels).
     * Called in load() for level at beginning of level load() before any bodies are constructed.
     * @param givenLevel Level
     */
    public void setStaticLevelWithinBodies(Level givenLevel) {
        StaticPlatform.setLevel(givenLevel);
        StaticOrb.setLevel(givenLevel);
        DynamicPlatform.setLevel(givenLevel);
        DynamicOrb.setLevel(givenLevel);
        SeekingAirMine.setLevel(givenLevel);//not used currently, but added for future usages
    }

    //updaters (s or d means static or dynamic platform. 'so' or 'do' mean static orb and dynamic orb respectively)
    public void sRiserUpdate() {
        for (StaticPlatform body : sRiser) {
            body.staticRiser();
        }
    }

    public void sSliderUpdate() {
        for (StaticPlatform body : sSlider) {
            body.staticSlider();
        }
    }

    public void sDiagonalUpdate() {
        for (StaticPlatform body : sDiagonal) {
            body.staticDiagonal();
        }
    }

    public void sGravityUpdate() {
        for (StaticPlatform body : sGravity) {
            body.staticGravity();
        }
    }

    public void sOrbitUpdate() {
        for (StaticPlatform body : sOrbit) {
            body.staticOrbit();
        }
    }

    public void dRiserUpdate() {
        for (DynamicPlatform body : dRiser) {
            body.dynamicRiser();
        }
    }

    public void dSliderUpdate() {
        for (DynamicPlatform body : dSlider) {
            body.dynamicSlider();
        }
    }

    public void dDiagonalUpdate() {
        for (DynamicPlatform body : dDiagonal) {
            body.dynamicDiagonal();
        }
    }

    public void dGravityUpdate() {
        for (DynamicPlatform body : dGravity) {
            body.dynamicGravity();
        }
    }

    public void dOrbitUpdate() {
        for (DynamicPlatform body : dOrbit) {
            body.dynamicOrbit();
        }
    }

    public void soRiserUpdate() {
        for (StaticOrb body : soRiser) {
            body.staticRiser();
        }
    }

    public void soSliderUpdate() {
        for (StaticOrb body : soSlider) {
            body.staticSlider();
        }
    }

    public void soDiagonalUpdate() {
        for (StaticOrb body : soDiagonal) {
            body.staticDiagonal();
        }
    }

    public void soGravityUpdate() {
        for (StaticOrb body : soGravity) {
            body.staticGravity();
        }
    }

    public void soOrbitUpdate() {
        for (StaticOrb body : soOrbit) {
            body.staticOrbit();
        }
    }

    public void doRiserUpdate() {
        for (DynamicOrb body : doRiser) {
            body.dynamicRiser();
        }
    }

    public void doSliderUpdate() {
        for (DynamicOrb body : doSlider) {
            body.dynamicSlider();
        }
    }

    public void doDiagonalUpdate() {
        for (DynamicOrb body : doDiagonal) {
            body.dynamicDiagonal();
        }
    }

    public void doGravityUpdate() {
        for (DynamicOrb body : doGravity) {
            body.dynamicGravity();
        }
    }

    public void doOrbitUpdate() {
        for (DynamicOrb body : doOrbit) {
            body.dynamicOrbit();
        }
    }

    /**
     * Uses given tick value
     * @param givenTick
     */
    public void sMineUpdate(int givenTick) {
        for (SeekingAirMine body : sMineUpdate) {
            body.update(givenTick);
        }
    }

    /**
     * Uses public static tick from Game.java
     */
    public void sMineUpdate() {
        for (SeekingAirMine body : sMineUpdate) {
            body.update(Game.tick);
        }
    }
    //end of updaters

    /*quick tip: do not pass values for amplitude or orbit radii if you are not going to use it! In the future, I may add all the StaticPlatforms (and others) into
    one collection and just make an update method on it. If it turns out you put values for things that shouldn't move, that's on you for not reading my comments ~ YusufA442*/

    /**
     * Loads the level
     */
    public void load() {
        loadSetup();//this way, I can show more of my knowledge of inheritance, but it's the same as just calling this method on the first line of load, as I still have to do super call, first
        //setStaticLevelWithinBodies(); wont work as it needs the 'this' param
    }

    /**
     * initial setup for load
     */
    public void loadSetup() {
        DynamicOrb.endCoinsRemaining = 0;
        DynamicOrb.coinsRemaining = 0;
        setStaticLevelWithinBodies(this);
    }

    /**
     * removes all bodies from collections
     */
    public void unload() {
        sRiser = new ArrayList<>(10);
        sSlider = new ArrayList<>(10);
        sGravity = new ArrayList<>(10);
        sDiagonal = new ArrayList<>(10);
        sOrbit = new ArrayList<>(10);

        dRiser = new ArrayList<>(10);
        dSlider = new ArrayList<>(10);
        dGravity = new ArrayList<>(10);
        dDiagonal = new ArrayList<>(10);
        dOrbit = new ArrayList<>(10);

        soRiser = new ArrayList<>(10);
        soSlider = new ArrayList<>(10);
        soGravity = new ArrayList<>(10);
        soDiagonal = new ArrayList<>(10);
        soOrbit = new ArrayList<>(10);

        doRiser = new ArrayList<>(10);
        doSlider = new ArrayList<>(10);
        doGravity = new ArrayList<>(10);
        doDiagonal = new ArrayList<>(10);
        doOrbit = new ArrayList<>(10);
        //ok so note for research (ArrayList.clear() removes all values stored in each index
        //(useful for reusing the array/avoiding null pointer exception) but I won't reuse within the current class, as levels go forwards only, so null allows for garbage collection to clean up)
        //remove the updating bodies (duplicate object references) from their respective Collections
        //(any body's ref can be found in one of the 'All' collections, where it is used to delete the bodies) (if you remembered to add it!!!)
        if (!sAll.isEmpty()) {
            for (StaticPlatform body : sAll) {
                body.destroy();
            }
            sAll.clear();//clear sets all elems to null but keeps the array itself
        }
        if (!dAll.isEmpty()) {
            for (DynamicPlatform body : dAll) {
                body.destroy();
            }
            dAll.clear();
        }
        if (!soAll.isEmpty()) {
            for (StaticOrb body : soAll) {
                body.destroy();
            }
            soAll.clear();
        }
        if (!doAll.isEmpty()) {
            for (DynamicOrb body : doAll) {
                body.destroy();
            }
            doAll.clear();
        }
        if (!sMineAll.isEmpty()) {
            for (SeekingAirMine body : sMineAll) {
                body.destroy();
            }
            sMineAll.clear();
        }

        //stop background music by adding in code in Overriden method in subclass
        stopBGM();
    } // may not use but will work on in future if time is good

    /**
     * update called every game step
     */
    public void update() {}

    /**
     * Updates once when game is paused
     */
    public void updateInPauseOnce() {//same as updateInPause() but will only run once per pause (uses the pausedMusic block of code in menu code in Game)
    }


    /**
     * Used to update levels while game is paused
     */
    public void updateInPause() {//will force an update through a pause, useful for dynamic objects within levels that cannot have their states saved updated during, or after, a pause
        // This is because trying to save during an update won't work as update() stops running in the paused state.
        // This method will take over and force an update during the pause state to allow certain bodies to continue to update themselves,
        // mainly for executing a block of code once, for saving state.
    }

    /**
     * Stops the background music
     */
    public void stopBGM() {}

    /**
     * Pauses the background music
     */
    public void pauseBGM() {
    }

    /**
     * Resumes the background music
     */
    public void resumeBGM() {
    }

    /**
     * Fetch a SoundClip and return it (USE SoundFile class wrapper instead)
     * @param FilePath
     * @return SoundClip
     */
    public SoundClip fetchSoundClip(String FilePath) {
        try {
            return new SoundClip(FilePath);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
            //try to add a file (I don't think you can/won't because time constraints)
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }//built into all levels for people who choose not to use the SoundFile wrapper

    /**
     * Sets the Game that the Level is running in
     * @param givenGame
     */
    public void setGame(Game givenGame) {
        localGame = givenGame;
    }

    /**
     * Sets the World the game is running in
     * @param givenWorld
     */
    public void setWorld(World givenWorld) {
        localWorld = givenWorld;
    }//localWorld is static variable so you only need to set it once

    /**
     * Sets the player in the current Game instance
     * @param givenPlayerController
     */
    public void setPlayerController(PlayerController givenPlayerController) {
        localPCLevel = givenPlayerController;
    }

    /**sets the value to true, when the level is complete (deprecated, public variable so no setter needed)
     */
    //deprecated
    public void updateLevelComplete() {
        levelComplete = true;
    }//not needed because variable is not private

    /**sets the value to true, when the level is finished (last endCoin picked up)*/
    public void updateLevelFinished() {
        levelFinish = true;
    }//not needed because variable is not private
    //end of deprecated
}
