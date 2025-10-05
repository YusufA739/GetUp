package game;


public class LevelThree extends Level {
    private StaticPlatform fakeplat0;

    private StaticOrb spikeball0;

    private DynamicOrb heart0;
    private DynamicOrb dorb0;
    private DynamicOrb dorb1;

    private StaticOrb inertspike0;
    private StaticOrb inertspike1;
    private StaticPlatform platformTwo;
    private StaticPlatform slider0;
    private StaticPlatform orbit0;
    private StaticPlatform largeslider0;

    private DynamicOrb finalCoin;
    private DynamicOrb heart1;
    private DynamicOrb heart2;

    private StaticPlatform riser;

    private DynamicPlatform breakaway0;
    private DynamicPlatform breakaway1;
    private DynamicPlatform breakaway2;
    private DynamicPlatform breakaway3;
    private DynamicPlatform breakaway4;

    private StaticPlatform groundplat;


    private SoundFile bgMusic = new SoundFile("data/undergroundNew.wav");


    public LevelThree() {
        super();
    }

    @Override
    public void load() {
        super.load();

        groundplat = new StaticPlatform(30, 0.5f, 0, -20f, 0, 0, 0, 0, 0, 0, 0);
        sAll.add(groundplat);

        inertspike1 = new StaticOrb(1, -8, -16.5f, 0, 0, 0, 1f, 0, 50, 0);
        inertspike1.isInertBall();
        soOrbit.add(inertspike1);
        soAll.add(inertspike1);
        inertspike0 = new StaticOrb(1, 4, -16.5f, 0, 0, 0, -1.2f, 0, 50, 0);
        inertspike0.isInertBall();
        soOrbit.add(inertspike0);
        soAll.add(inertspike0);

        slider0 = new StaticPlatform(9, 0.5f, -16, -13, 2, 0, 0, 0.2f, 0, 5, 0);
        sAll.add(slider0);

        fakeplat0 = new StaticPlatform(3, 0.5f, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        fakeplat0.isBreakable();
        sAll.add(fakeplat0);

        largeslider0 = new StaticPlatform(3, 0.5f, 3, 3, 8, 0, 0, 0.125f, 0, 4, 0);
        sSlider.add(largeslider0);
        sAll.add(largeslider0);

        orbit0 = new StaticPlatform(3, 0.5f, -24, -3, 0.4f, 2, 0, 0, 0, 0, 0);
        sDiagonal.add(orbit0);
        sAll.add(orbit0);

        //SO_ORBIT here
        spikeball0 = new StaticOrb(1, -5, 4, 0, 0, 0, 0.2f, 5f, 0, 6);
        spikeball0.isSpike();
        soOrbit.add(spikeball0);
        soAll.add(spikeball0);

        finalCoin = new DynamicOrb(1, 10, 28, 0, 0, 0, 0, 0, 6, 0, 1f);
        finalCoin.isEndCoinSensor();
        doOrbit.add(finalCoin);
        doAll.add(finalCoin);
        platformTwo =  new StaticPlatform(3,0.5f,9,25.5f,0,0,0,0,0,0,0);
        sAll.add(platformTwo);
        riser = new StaticPlatform(3,0.5f,-6,25,0,0,0,0,1f,0,24);
        sOrbit.add(riser);
        sAll.add(riser);
        dorb0 = new DynamicOrb(1, -29, -15, 0, 0, 0, 0, 0, 6, 0, 1f);
        dorb0.isCoinSensor();
        doOrbit.add(dorb0);
        doAll.add(dorb0);
        dorb1 = new DynamicOrb(1, 26, -17, 0, 0, 0, 0, 0, 6, 0, 1f);
        dorb1.isCoinSensor();
        doOrbit.add(dorb1);
        doAll.add(dorb1);
        heart0 = new DynamicOrb(1, -8, -16.5f, 0, 0, 0, 0, 0, 5, 0, 2f);
        heart0.isHeartSensor();
        doOrbit.add(heart0);
        doAll.add(heart0);


        heart2 = new DynamicOrb(1,1,12, 10, 0, 0, 0, 0, 4f, 0, 2);
        heart2.isHeartSensor();
        doOrbit.add(heart2);
        doAll.add(heart2);

        heart1 = new DynamicOrb(1,1, 6, -18, 0, 0, 0, 0, 4f, 0, 2);
        heart1.isHeartSensor();
        doOrbit.add(heart1);
        doAll.add(heart1);


        localPCLevel.newLevel(0,-18);
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
