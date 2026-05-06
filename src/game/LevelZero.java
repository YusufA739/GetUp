package game;

import city.cs.engine.SoundClip;

public class LevelZero extends Level {
    private StaticOrb speedPotion;

    private DynamicOrb dorb0;
    private DynamicOrb dorb1;
    private DynamicOrb dorb2;
    private DynamicOrb dorb3;
    private DynamicOrb dorb4;
    private DynamicOrb dorb5;

    private StaticPlatform riser0;
    private StaticPlatform platformZero;
    private StaticPlatform platformOne;
    private StaticPlatform platformTwo;
    private StaticPlatform platformThree;
    private StaticPlatform platformFour;
    private StaticPlatform platformFive;
    private StaticPlatform platformSix;
    private StaticPlatform platformSeven;
    private StaticPlatform warningSign2;
    private DynamicOrb finalCoin;

    private StaticPlatform warningSign1;
    private StaticPlatform groundplat;
    private SoundClip bgMusic;{bgMusic = fetchSoundClip("data/overworld.wav");}


    public LevelZero() {
        //super(); no need to call super if I do all static setting beforehand in Game
    }

    @Override
    public void load() {
        DynamicOrb.endCoinsRemaining = 0;//reset this as if the player restarted without picking up the endCoin(s)
        DynamicOrb.coinsRemaining = 0;
        setStaticLevelWithinBodies(this);
        //SP_PLAIN
        groundplat = new StaticPlatform(30, 0.5f, 0, -20f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(groundplat);
        warningSign1 = new StaticPlatform(3,3,-33f,-20,0,0,0,0,0,0,0);
        sAll.add(warningSign1);
        warningSign2 = new StaticPlatform(3,3,33f,-20,0,0,0,0,0,0,0);
        sAll.add(warningSign2);
        warningSign1.isWarningSign();
        warningSign2.isWarningSign();

        //DO_ORBIT (endCoins first)
        finalCoin = new DynamicOrb(1, -5f, 10f, 0, 0, 0, 0, 0, 6, 0, 1f);
        doAll.add(finalCoin);//for deleting
        doOrbit.add(finalCoin);//for updating
        finalCoin.isEndCoinSensor();
        dorb0 = new DynamicOrb(1, 3, -10f, 0, 0, 0, 0, 0, 8, 0, 1f);
        doAll.add(dorb0);
        doOrbit.add(dorb0);
        dorb0.isCoinSensor();
        dorb1 = new DynamicOrb(1, 0, 0, 0, 0, 0, 0, 0, 6, 0, 1f);
        doAll.add(dorb1);
        doOrbit.add(dorb1);
        dorb1.isCoinSensor();
        dorb2 = new DynamicOrb(1, 26, -5, 0, 0, 0, 0, 0, 3, 0, 0.75f);
        doAll.add(dorb2);
        doOrbit.add(dorb2);
        dorb2.isCoinSensor();
        dorb3 = new DynamicOrb(1, 5, 40, 0, 0, 0, 0, 0, 5, 0, 0.75f);
        doAll.add(dorb3);
        doOrbit.add(dorb3);
        dorb3.isCoinSensor();
        dorb4 = new DynamicOrb(1, 5, 33, 0, 0, 0, 0, 0, 4.5f, 0, 0.75f);
        doAll.add(dorb4);
        doOrbit.add(dorb4);
        dorb4.isCoinSensor();
        dorb5 = new DynamicOrb(1, 5, 28, 0, 0, 0, 0, 0, 5.5f, 0, 0.75f);
        doAll.add(dorb5);
        doOrbit.add(dorb5);
        dorb5.isCoinSensor();

        //SP_PLAIN_UNSORTED here
        platformZero = new StaticPlatform(3,0.5f,5f,23,0,0,0,0,0,0,0);
        sAll.add(platformZero);
        platformOne = new StaticPlatform(3,0.5f,24,-7,0,0,0,0,0,0,0);
        sAll.add(platformOne);
        platformTwo = new StaticPlatform(3, 0.5f, 16, -13, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformTwo);
        platformThree = new StaticPlatform(3, 0.5f, 2, -13f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformThree);
        platformFour = new StaticPlatform(3, 0.5f, -4, -9f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformFour);
        platformSeven = new StaticPlatform(3, 0.5f, -12, -5f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformSeven);
        platformSix = new StaticPlatform(3, 0.5f, -13, 3, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformSix);


        //finally, put the player at the world's initial/first respawn coords
        localPCLevel.newLevel(17,-18);//manually input your desired starting position (check against platform you want)
        resumeBGM();
    }

    @Override
    public void update() {
        sRiserUpdate();
        sSliderUpdate();
        sOrbitUpdate();
        soDiagonalUpdate();
        soOrbitUpdate();
        doOrbitUpdate();
        //using collections to simplify the lines and make the code look cleaner and easier to understand/debug
        //I've only put the updates that need calling (some update collections are empty, so no reason to write them down)

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
