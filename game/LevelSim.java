package game;

import PlanetaryExpansionPack1.Planet;
import city.cs.engine.SoundClip;

public class LevelSim extends Level {
    Planet vulcan;
    Planet cybertron;

    private SoundClip bgMusic;{bgMusic = fetchSoundClip("data/beginning2.wav");}

    public LevelSim() {
        //super();
    }

    @Override
    public void load() {
        loadSetup();
        cybertron = new Planet(1,2000000000,0,0, 0, 0);
        pAll.add(cybertron);
        vulcan = new Planet(1,2000000000,5,5,0,0);
        pAll.add(vulcan);
    }

    @Override
    public void update() {
        cybertron.addForce(vulcan.mass,vulcan.localX,vulcan.localY);
        vulcan.addForce(cybertron.mass,cybertron.localX,cybertron.localY);

        pAllUpdate();
    }

    @Override
    public void updateInPauseOnce() {
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
