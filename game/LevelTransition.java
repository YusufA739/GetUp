package game;

/**
 * A subclass of Level superclass. It's used as a way to detect when to play the 'Level Cleared!' sound and image. Useful filler level for waiting out the time between levels
 */

public class LevelTransition extends Level {
    private int initTick;
    private int tickLoops;
    /**
     * Controls how many in-game seconds to wait before win condition is met to move onto next 'real' level (playable level)
     */
    private int tickLoopsTarget = 4;

    public LevelTransition() {
        super();
    }

    @Override
    public void load() {
        initTick = Game.tick;
        StaticPlatform wall0 = new StaticPlatform(localWorld,10,100,-20,-20,0,0,0,0,0,0,0,localPCLevel);
        sAll.add(wall0);
        StaticPlatform wall1 = new StaticPlatform(localWorld,10,100,20,-20,0,0,0,0,0,0,0,localPCLevel);
        sAll.add(wall1);
        StaticPlatform floor = new StaticPlatform(localWorld,100,10,-10,20,0,0,0,0,0,0,0,localPCLevel);
        sAll.add(floor);
        StaticPlatform ceiling = new StaticPlatform(localWorld,100,10,-10,-20,0,0,0,0,0,0,0,localPCLevel);
        sAll.add(ceiling);
        localPCLevel.teleport(0,1000);
    }

    @Override
    public void update() {
        if (Game.tick == initTick) {
            tickLoops++;
            if (tickLoops >= tickLoopsTarget) {
                currentLevelNumber++;
            }
            unload();//unload the current level's objects in prep for next level
        }
    }

    /**
     * Customised as I know there are only StaticPlatform objects
     */
    @Override
    public void unload() {
        for (StaticPlatform s : sAll) {
            s.destroy();
        }
        sAll.clear();
    }
}
