package game;

import java.util.Random;

public class InputUsername extends Level {
    Random rand = new Random();
    SoundFile bgMusic;{
        if (rand.nextInt(0,2) == 0) {
            bgMusic = new SoundFile("data/underwater.wav");
        }
        else {
            bgMusic = new SoundFile("data/radio.wav");
        }
    }

    @Override
    public void load() {
        StaticPlatform wall0 = new StaticPlatform(10,100,-20,-20,0,0,0,0,0,0,0);
        sAll.add(wall0);
        StaticPlatform wall1 = new StaticPlatform(10,100,20,-20,0,0,0,0,0,0,0);
        sAll.add(wall1);
        StaticPlatform floor = new StaticPlatform(100,10,-10,20,0,0,0,0,0,0,0);
        sAll.add(floor);
        StaticPlatform ceiling = new StaticPlatform(100,10,-10,-20,0,0,0,0,0,0,0);
        sAll.add(ceiling);
        localPCLevel.teleport(0,18);
        localPCLevel.setZoom(20f);
        if (localPCLevel.getHealth() > 0) {
            StaticPlatform image = new StaticPlatform(100,10,-100,110,0,0,0,0,0,0,0);
            image.isWellDoneSign();
            sAll.add(image);
            resumeBGM();
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

    @Override
    public void update() {
        //used to block the mouse wheel from affecting the zoom
        if (localPCLevel.getZoom() != 20) {//higher the number, the more zoomed it is
            localPCLevel.setZoom(20f);
        }
    }
}
