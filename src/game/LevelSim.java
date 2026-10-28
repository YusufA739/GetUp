package game;

import PlanetaryExpansionPack1.Planet;
import city.cs.engine.SoundClip;

public class LevelSim extends Level {
    Planet Sun;
    Planet cybertron;

    private SoundClip bgMusic;{bgMusic = fetchSoundClip("data/beginning2.wav");}

    public LevelSim() {
        //super();
    }

    @Override
    public void load() {
        loadSetup();
        Sun = new Planet(5,2099999999,0,0, 0, 0);
        pAll.add(Sun);
        cybertron = new Planet(1, 6363,0,14.9F,-6F,0);
        pAll.add(cybertron);
        Sun.setImmovable();
    }

    @Override
    public void update() {
        cybertron.addForce(Sun.mass,Sun.localX,Sun.localY);
        Sun.addForce(cybertron.mass,cybertron.localX,cybertron.localY);//not needed for immovable

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
