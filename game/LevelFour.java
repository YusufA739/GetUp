package game;

public class LevelFour extends Level {
    private StaticPlatform fakeplat0;

    private StaticOrb spikeball0;
    private SeekingAirMine smine0;
    private SeekingAirMine smine1;

    private DynamicOrb heart0;
    private DynamicOrb coin0;
    private DynamicOrb coin1;
    private DynamicOrb coin2;

    private StaticPlatform plat12;
    private StaticPlatform slider0;

    private StaticPlatform plat1;
    private StaticPlatform plat2;
    private StaticPlatform plat3;
    private StaticPlatform plat4;
    private StaticPlatform plat5;
    private StaticPlatform plat6;
    private StaticPlatform plat7;
    private StaticPlatform plat8;
    private StaticPlatform plat9;
    private StaticPlatform plat10;
    private StaticPlatform plat11;
    private DynamicOrb finalCoin;
    private DynamicOrb finalCoin2;
    private DynamicOrb finalCoin3;
    private DynamicOrb speedPotion;

    private StaticPlatform groundplat;
    private SoundFile bgMusic = new SoundFile("data/thirteen.wav");

    public LevelFour() {
        super();
    }

    @Override
    public void load() {
        loadSetup();

        groundplat = new StaticPlatform(30, 0.5f, 0, -20f, 0, 0, 0, 0,0, 0, 0);
        sAll.add(groundplat);

        heart0 = new DynamicOrb(1, -8, -16.5f, 0, 0, 0, 0, 0, 10, 0, 0.5f);
        heart0.isHeartSensor();
        doOrbit.add(heart0);
        doAll.add(heart0);

        speedPotion = new DynamicOrb(1, 10, -18, 0, 0,0, 0, 0, 5, 0, 0.5f);
        speedPotion.isSpeedPotionSensor();
        doOrbit.add(speedPotion);
        doAll.add(speedPotion);

        plat1 = new StaticPlatform(3, 0.5f, -18, -12, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(plat1);

        plat2 = new StaticPlatform(3, 0.5f, -6, -7, 0, 10, 0, 0, 0.04f, 0, 0);
        sRiser.add(plat2);
        sAll.add(plat2);

        slider0 = new StaticPlatform(3, 0.5f, 10, 6, 10, 0, 0, 0.05f, 0, 0, 0);
        sSlider.add(slider0);
        sAll.add(slider0);

        fakeplat0 = new StaticPlatform(3, 0.5f, 3, 4, 0, 0, 0, 0, 0, 0, 0);
        fakeplat0.isBreakable();
        sAll.add(fakeplat0);

        plat3 = new StaticPlatform(3,0.5f,-25,-1,0,0,0,0,0,0,0);
        sAll.add(plat3);

        coin0 = new DynamicOrb(1, -25f, 1.5f, 0, 0, 0, 0, 0, 6, 0, 0.5f);
        coin0.isCoinSensor();
        doOrbit.add(coin0);
        doAll.add(coin0);

        plat4 = new StaticPlatform(3, 0.5f, -45, 15, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(plat4);

        finalCoin = new DynamicOrb(1,-44.5f,17,0,0,0,0,0,8,0,0.5f);
        finalCoin.isEndCoinSensor();
        doOrbit.add(finalCoin);
        doAll.add(finalCoin);

        plat5 = new StaticPlatform(3, 0.5f, 12, 0, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(plat5);

        plat6 = new StaticPlatform(9,0.5f,28,-4,0,0,0,0,0,0,0);
        sAll.add(plat6);

        coin1 = new DynamicOrb(1, 29, -2, 0, 0, 0, 0, 0, 6, 0, 0.5f);
        coin1.isCoinSensor();
        doOrbit.add(coin1);
        doAll.add(coin1);

        plat7 = new StaticPlatform(3,0.5f,30,3,0,0,0,0,0,0,0);
        plat7.isBreakable();
        sAll.add(plat7);

        plat8 = new StaticPlatform(3,0.5f,42,3,0,0,0,0,0,0,0);
        sAll.add(plat8);

        plat9 = new StaticPlatform(3,0.5f,40,26,0,0,0,0,0,0,0);
        sAll.add(plat9);

        plat10 = new StaticPlatform(3,0.5f,50,13,0,0,0,0,0,0,0);
        sAll.add(plat10);

        finalCoin2 = new DynamicOrb(1, 50, 15, 0, 0,0,0,0,10,0,0.5f);
        finalCoin2.isEndCoinSensor();
        doOrbit.add(finalCoin2);
        doAll.add(finalCoin2);

        spikeball0 = new StaticOrb(1, -34, -15, 0, 0, 0, 0, 2, 0, 40);
        spikeball0.isSpike();
        soOrbit.add(spikeball0);
        soAll.add(spikeball0);

        smine0 = new SeekingAirMine(-15, 15);
        sMineUpdate.add(smine0);
        sMineAll.add(smine0);

        smine1 = new SeekingAirMine(35, 25);
        sMineUpdate.add(smine1);
        sMineAll.add(smine1);

        plat11 = new StaticPlatform(3,0.5f,15,28,0,0,0,.1f,.1f,15,15);
        sOrbit.add(plat11);
        sAll.add(plat11);

        coin2 = new DynamicOrb(1, 22, 30, 0, 0, 0, 0, 0, 6, 0, 0.5f);
        coin2.isCoinSensor();
        doOrbit.add(coin2);
        doAll.add(coin2);

        plat12 = new StaticPlatform(3,0.5f,0,46,0,0,0,0,0,0,0);
        sAll.add(plat12);

        finalCoin2 = new DynamicOrb(1,0f,48,0,0,0,0,0,8,0,0.5f);
        finalCoin2.isEndCoinSensor();
        doOrbit.add(finalCoin2);
        doAll.add(finalCoin2);



        localPCLevel.newLevel(0,-18);
        resumeBGM();
    }

    @Override
    public void resumeBGM() {
        bgMusic.loop();
    }

    @Override
    public void pauseBGM() {
        bgMusic.pause();
    }

    @Override
    public void stopBGM() {
        bgMusic.stop();
    }

    @Override
    public void update() {
        sRiserUpdate();
        sSliderUpdate();
        sOrbitUpdate();
        soOrbitUpdate();
        doOrbitUpdate();
        sMineUpdate();
        //using collections to simplify the lines and make the code look cleaner and easier to understand/debug
        //I've only put the updates that need calling (some update collections are empty)
    }
}
