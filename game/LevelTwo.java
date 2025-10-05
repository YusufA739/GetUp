package game;

import city.cs.engine.SoundClip;

public class LevelTwo extends Level {
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

    public LevelTwo() {
        //super();
    }

    @Override
    public void load() {
        loadSetup();
//        S-MINE
        sMine1 = new SeekingAirMine(localPCLevel, localWorld, 30, 0);
        sMineAll.add(sMine1);

        //SP_PLAIN (plain)
        groundplat = new StaticPlatform(30, 0.5f, 0, -20f,0, 0, 0, 0, 0, 0, 0);
        sAll.add(groundplat);

        //SP_RISER here
        riser0 = new StaticPlatform(3, 0.5f, -3, 2f,0, 6, 0, 0, 0.1f, 0, 4);
        sRiser.add(riser0);
        sAll.add(riser0);

        //SP_SLIDER here
        slider0 = new StaticPlatform(6, 0.5f, -12, -12,2, 0, 0, 0.2f, 0, 5, 0);
        sSlider.add(slider0);
        sAll.add(slider0);
        largeslider0 = new StaticPlatform(9, 0.5f, 40, 26,8, 0, 0, 0.125f, 0, 4, 0);
        sSlider.add(largeslider0);
        sAll.add(largeslider0);

        //SP_DIAGONAL here
        orbit0 = new StaticPlatform(3, 0.5f, -24, -3,0.4f, 2, 0, 0, 0, 0, 0);
        sDiagonal.add(orbit0);
        sAll.add(orbit0);

        //DO_ORBIT here (make all coins DO so the tracking for coinsRemaining and endCoinsRemaining can track properly
        finalCoin0 = new DynamicOrb(1, 1f, 28, 0,0, 0, 0, 0, 6, 0, 1f);
        finalCoin0.isEndCoinSensor();
        doOrbit.add(finalCoin0);
        doAll.add(finalCoin0);
        finalCoin1 = new DynamicOrb(1,86,26,0,0,0,0,0,2,0,0.5f);
        finalCoin1.isEndCoinSensor();
        doOrbit.add(finalCoin1);
        doAll.add(finalCoin1);
        dorb0 = new DynamicOrb(1, 5f, -2f, 0,0, 0, 0, 0, 6, 0, 0.5f);
        dorb0.isCoinSensor();
        doOrbit.add(dorb0);
        doAll.add(dorb0);
        dorb1 = new DynamicOrb(1, -5, 1, 0,0, 0, 0, 0, 6, 0, 1f);
        dorb1.isCoinSensor();
        doOrbit.add(dorb1);
        doAll.add(dorb1);
        dorb2 = new DynamicOrb(1, 15, 25, 0, 0, 0, 0, 0, 6, 0, 1f);
        dorb2.isCoinSensor();
        doOrbit.add(dorb2);
        doAll.add(dorb2);
        dorb3 = new DynamicOrb(1, -12, 9, 0,0, 0, 0, 0, 3, 0, 0.5f);
        dorb3.isCoinSensor();
        doOrbit.add(dorb3);
        doAll.add(dorb3);
        heart0 = new DynamicOrb(1, -8, -16.5f, 0, 0, 0, 0, 0, 5, 0, 2f);
        heart0.isHeartSensor();
        doOrbit.add(heart0);
        doAll.add(heart0);


        //SO_PLAIN here
        speedPotion = new DynamicOrb(1,1, 10, -18, 0, 0, 0, 0, 0.1f, 0, 1);
        speedPotion.isSpeedPotionSensor();
        doOrbit.add(speedPotion);
        doAll.add(speedPotion);

        //SP_PLAIN_UNSORTED here
        platformTwo = new StaticPlatform(3, 0.5f, 5, -4f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformTwo);
        platformThree = new StaticPlatform(3, 0.5f, 6, 4f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformThree);
        platformFour = new StaticPlatform(3, 0.5f, 15, 8f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformFour);
        platformSeven = new StaticPlatform(3, 0.5f, 16, -12, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformSeven);
        platformEight = new StaticPlatform(3, 0.5f, 24, 19, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformEight);
        platformNine = new StaticPlatform(3, 0.5f, -13, 6, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformNine);
        platformTen = new StaticPlatform(3, 0.5f, 1, 24, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(platformTen);


        detourplatform1 = new StaticPlatform(3, 0.5f, 47, 28, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(detourplatform1);
        detourplatform2 = new StaticPlatform(3, 0.5f, 53, 28, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(detourplatform2);
        detourplatform3 = new StaticPlatform(3, 0.5f, 59, 29, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(detourplatform3);
        detourplatform4 = new StaticPlatform(3,0.5f,75,28,0,0,0,0,0,0,0);
        sAll.add(detourplatform4);
        detourplatform5 = new StaticPlatform(3,0.5f,68,29,0,0,0,0,0,0,0);
        sAll.add(detourplatform5);
        detourplatform6 = new StaticPlatform(3,0.5f,85,24,0,0,0,0,0,0,0);
        sAll.add(detourplatform6);

        //DP_PLAIN here (plain)
        fakeplat0 = new DynamicPlatform(localWorld, 3, 0.5f, -6, 15, 0, 0, 0, 0, 0, 0, 0, 0);
        fakeplat0.isBreakable();
        dAll.add(fakeplat0);

        localPCLevel.newLevel(0,-17);
        localPCLevel.setZoom(20f);//reset zoom from last level's change in zoom
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
