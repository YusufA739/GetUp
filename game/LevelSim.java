package game;

import city.cs.engine.SoundClip;

public class LevelSim extends Level {
    private DynamicPlatform fakeplat0;

    private DynamicOrb heart0;
    private DynamicOrb finalCoin0;
    private DynamicOrb finalCoin1;
    private DynamicOrb dorb0;
    private DynamicOrb dorb1;
    private DynamicOrb dorb2;
    private DynamicOrb dorb3;

    private DynamicPlatform breakable0;

    private StaticPlatform riser0;
    private StaticPlatform platformTwo;
    private StaticPlatform platformThree;
    private StaticPlatform platformFour;
    private StaticPlatform slider0;
    private StaticPlatform platformSeven;
    private StaticPlatform platformEight;
    private StaticPlatform platformNine;
    private StaticPlatform platformTen;
    private StaticPlatform orbit0;
    private StaticPlatform largeslider0;

    private StaticPlatform detourplatform1;
    private StaticPlatform detourplatform2;
    private StaticPlatform detourplatform3;
    private StaticPlatform detourplatform4;
    private StaticPlatform detourplatform5;
    private StaticPlatform detourplatform6;
    private DynamicOrb speedPotion;

    private StaticPlatform groundplat;

    private SeekingAirMine sMine1;

    private SoundClip bgMusic;{bgMusic = fetchSoundClip("data/wilystage4vol_down.wav");}

    public LevelSim() {
        //super();
    }

    @Override
    public void load() {
        loadSetup();

    }

    @Override
    public void update() {
        sRiserUpdate();
        sSliderUpdate();
        sOrbitUpdate();
        soDiagonalUpdate();
        soOrbitUpdate();
        doOrbitUpdate();
        sMine1.update(Game.tick);
        //using collections to simplify the lines and make the code look cleaner and easier to understand/debug
        //I've only put the updates that need calling (some update collections would be empty if not all body types are used (21 different types(5+5+5+5+1))
    }

    @Override
    public void updateInPauseOnce() {
        if (!sMine1.movementStored) {
            sMine1.storeMovement();
        }
    }

    @Override
    public void pauseBGM() {
        bgMusic.pause();
    }

    @Override
    public void resumeBGM() {
        bgMusic.loop();
    }

    @Override
    public void stopBGM() {
        bgMusic.stop();
    }
}
