package game;

import city.cs.engine.SoundClip;

public class LevelOne extends Level {

    private DynamicOrb dorb0;
    private DynamicOrb dorb1;
    private DynamicOrb dorb2;

    private StaticPlatform plat0;
    private StaticPlatform plat1;
    private StaticPlatform plat2;
    private StaticPlatform plat3;
    private StaticPlatform plat4;
    private DynamicOrb finalCoin;
    private DynamicOrb speedPotion;

    private StaticPlatform warningSign1;//only one safety measure, taking off the training wheels
    private StaticPlatform groundplat;

    private SeekingAirMine sMine1;

    private SoundClip bgMusic;{bgMusic = fetchSoundClip("data/sonic.wav");}

    public LevelOne() {
        //super();
    }

    @Override
    public void load() {
        DynamicOrb.endCoinsRemaining = 0;//good practice to reset this from level to level
        DynamicOrb.coinsRemaining = 0;
        setStaticLevelWithinBodies(this);

        //SP_PLAIN
        groundplat = new StaticPlatform(30, 0.5f, 0, -20f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(groundplat);
        warningSign1 = new StaticPlatform(3,3,-33f,-20,0,0,0,0,0,0,0);
        sAll.add(warningSign1);
        warningSign1.isWarningSign();
        plat0 = new StaticPlatform(3,0.5f,-26f,-14,0,0,0,0,0,0,0);
        sAll.add(plat0);
        plat1 = new StaticPlatform(3,0.5f,21f,-14,0,0,0,0,0,0,0);
        sAll.add(plat1);
        plat2 = new StaticPlatform(6,0.5f,12,24,0,0,0,0,0,0,0);
        sAll.add(plat2);
        plat3 = new StaticPlatform(3,0.5f,0,0,0,0,0,0,0,0,0);
        sAll.add(plat3);
        plat4 = new StaticPlatform(6,0.5f,-13,18,0,0,0,0,0,0,0);
        sAll.add(plat4);
        //DO_ORBIT here (make all coins DO so the tracking for coinsRemaining and endCoinsRemaining can track properly (or make the
        finalCoin = new DynamicOrb(1, 1f, 28, 0, 0, 0, 0, 0, 4, 0, 1f);
        finalCoin.isEndCoinSensor();
        doOrbit.add(finalCoin);
        doAll.add(finalCoin);
        dorb0 = new DynamicOrb(1, -5f, 3.75f, 0, 0, 0, 0, 0, 5, 0, 1f);
        dorb0.isCoinSensor();
        doOrbit.add(dorb0);
        doAll.add(dorb0);
        dorb1 = new DynamicOrb(1, 5, 11, 0, 0, 0, 0, 0, 6, 0, 1f);
        dorb1.isCoinSensor();
        doOrbit.add(dorb1);
        doAll.add(dorb1);
        dorb2 = new DynamicOrb(1, -20, 37, 0, 0, 0, 0, 4, 10, 2, 5f);
        dorb2.isCoinSensor();
        doOrbit.add(dorb2);
        doAll.add(dorb2);

        //SO_PLAIN here
        speedPotion = new DynamicOrb(1, 10, -18, -18, 0, 0, 0, 0, 4, 0, 0.5f);
        speedPotion.isSpeedPotionSensor();
        doOrbit.add(speedPotion);
        doAll.add(speedPotion);

        localPCLevel.newLevel(0,-19);
        resumeBGM();
    }

    @Override
    public void update() {
        if (localPCLevel.getZoom() > 10) {
            localPCLevel.zoomOut(0.1f);
        }
        doOrbitUpdate();
        //using collections to simplify the lines and make the code look cleaner and easier to understand/debug
        //I've only put the updates that need calling (some update collections are empty)
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
