package game;

import city.cs.engine.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import static org.jbox2d.common.MathUtils.floor;

/**
 * GameView is used as the viewport for the game to see into the world. You can view world through here, and it is not locked to the player, but it can be made to be locked by passing the player's coordinates to the setCentre method.
 * You should make sure the object you are following does not disappear as then the viewport will be static and not move anymore. You can use detectors or a boolean to switch over to tracking another object if this occurs.
 * You can change the viewport's position by using setCentre() which is part of the inherited UserView class. it contains all background images and sprites.
 * Make sure that image references are correct. Also, make sure you have assigned the correct images to the right arrays so when they loop, they go over the correct images.
 */


public class GameView extends UserView {
    private int frameduration = 7;
    private int blanklength = 20;
    private BufferedImage[] l0images = new BufferedImage[5];{
        try {
            l0images[0] = ImageIO.read(new File("data/waterfall0.png"));
            l0images[1] = ImageIO.read(new File("data/waterfall1.png"));
            l0images[2] = ImageIO.read(new File("data/waterfall2.png"));
            l0images[3] = ImageIO.read(new File("data/waterfall3.png"));
            l0images[4] = ImageIO.read(new File("data/waterfall4.png"));
        } catch (IOException e) {
            for (int i = 0; i < l0images.length; i++) {
                l0images[i] = missingImage();
            }
        }
    }
    private BufferedImage[] l1images = new BufferedImage[7+blanklength];{
        try {
            l1images[0] = ImageIO.read(new File("data/Charge0.png"));
            l1images[1] = ImageIO.read(new File("data/Charge1.png"));
            l1images[2] = ImageIO.read(new File("data/Charge2.png"));
            l1images[3] = ImageIO.read(new File("data/Charge3.png"));
            l1images[4] = ImageIO.read(new File("data/Charge4.png"));
            l1images[5] = ImageIO.read(new File("data/Charge5.png"));
            l1images[6] = ImageIO.read(new File("data/Charge6.png"));
            l1images[7] = ImageIO.read(new File("data/Charge6.png"));
            l1images[8] = ImageIO.read(new File("data/Charge6.png"));
            l1images[9] = ImageIO.read(new File("data/Charge6.png"));
            l1images[10] = ImageIO.read(new File("data/Charge6.png"));
            l1images[11] = ImageIO.read(new File("data/Charge6.png"));
            l1images[12] = ImageIO.read(new File("data/Charge6.png"));
            l1images[13] = ImageIO.read(new File("data/Charge6.png"));
            l1images[14] = ImageIO.read(new File("data/Charge6.png"));
            l1images[15] = ImageIO.read(new File("data/Charge6.png"));
            l1images[16] = ImageIO.read(new File("data/Charge6.png"));
            l1images[17] = ImageIO.read(new File("data/Charge6.png"));
            l1images[18] = ImageIO.read(new File("data/Charge6.png"));
            l1images[19] = ImageIO.read(new File("data/Charge6.png"));
            l1images[20] = ImageIO.read(new File("data/Charge6.png"));
            l1images[21] = ImageIO.read(new File("data/Charge6.png"));
            l1images[22] = ImageIO.read(new File("data/Charge6.png"));
            l1images[23] = ImageIO.read(new File("data/Charge6.png"));
            l1images[24] = ImageIO.read(new File("data/Charge6.png"));
            l1images[25] = ImageIO.read(new File("data/Charge6.png"));
            l1images[26] = ImageIO.read(new File("data/Charge6.png"));
        } catch (IOException e) {
            for (int i = 0; i < l0images.length; i++) {
                l1images[i] = missingImage();
            }
        }
    }
    private BufferedImage[] l2images = new BufferedImage[2];{
        try {
            l2images[0] = ImageIO.read(new File("data/Opening.png"));
            l2images[1] = ImageIO.read(new File("data/Opening2.png"));
        } catch (IOException e) {
            for (int i = 0; i < l0images.length; i++) {
                l2images[i] = missingImage();
            }
        }
    }
    private ImageFile[] l3images = new ImageFile[3];{
        l3images[0] = new ImageFile("data/Knight7Alt2-2.png");
        l3images[1] = new ImageFile("data/Knight7Alt2-3.png");
        l3images[2] = new ImageFile("data/Knight7Alt2-4.png");
    }

    private ImageFile[] l4images = new ImageFile[2];{
        l4images[0] = new ImageFile("data/Metal-1.png");
        l4images[1] = new ImageFile("data/Metal-2.png");
    }

    private int l0imagecounter = 0;
    private int l1imagecounter = 0;
    private int l2imagecounter = 0;
    private int l3imagecounter = 0;
    private int l4imagecounter = 0;

    private PlayerController localPCGV;
    private boolean nameInputterCreated = false;
    private int score = 0;
    private int health = 1;
    private float jumpCharge = 0;
    private float superSpeedTimer = 0;
    //private boolean superSpeedActive = false; not useful (Text-based and I want GUI)
    private float[] hsbvals;{hsbvals = Color.RGBtoHSB(65,114,201,null);}
    private Color light_blue = Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]);
    private ImageFile imageBG = new ImageFile("data/waterfall.gif");
    private ImageFile imageLevelCleared = new ImageFile("data/levelcleared.png");
    private ImageFile gamefinish = new ImageFile("data/gamefinish1.png");//changed from final.png to gamefinish.png to gamefinish1.png
    private ImageFile imagemenu = new ImageFile("data/menu.jpg");
    private ImageFile help0 = new ImageFile("data/helpscreen0.jpeg");
    private ImageFile help1 = new ImageFile("data/helpscreen1.jpeg");
    private ImageFile help2 = new ImageFile("data/helpscreen2.jpeg");
    private ImageFile help3 = new ImageFile("data/helpscreen3.jpeg");
    private ImageFile lb = new ImageFile("data/leaderboard.png");
    public String scorefilePath = "data/scoresandnames.txt";
    private ArrayList<String> names = new ArrayList<>(10);
    private ArrayList<Float> scores = new ArrayList<>(10);
    {reloadFile(scorefilePath);}//has to run after names and scores ArrayLists are defined

    private JFrame window;

    //unused constructor (replaced by below constructor: int int PlayerController)
    public GameView(World w, int width, int height, PlayerController givenPlayerController) { //big issue: the PlayerController is defined either before or after (cyclic dependency on each other)
        super(w, width, height);
        localPCGV = givenPlayerController;
//        setBackgroundImage("data/waterfall.gif");//placeholder image given. Will stop updating this constructor as it is never used, and is only needed for one call (instantiation)
    } //alternate constructor given

    //active/in-use constructor
    public GameView(World w, int width, int height) { //fix for big issue: just define PC object after both are created, using a setter
        super(w, width, height);
    }


    //setters
    public void setPlayerController(PlayerController givenPlayerController) {
        //fix for big issue: you're looking at it; we get the PC later using this setter instead of setting PC in constructor...
        //...(avoids the cyclic dependency issue, both needed the other in the constructor param, but now I've made it so that only PC needs GameView in constructor, GameView fetches PC object at a later time)
        localPCGV = givenPlayerController;
        updateScore();
        updateHealth();
        updateJumpCharge();
    }
    //end of setters

    //getters
    public void getWindow(JFrame givenWindow) {
        window = givenWindow;
    }
    //end of getters

    public void updateScore() { // best used within class
        score = localPCGV.getScore();
    }

    public void updateScore(int givenScore) { //best used in PlayerController (<- an example)
        score = givenScore;
    }

    public void updateHealth() { //best used within class
        health = localPCGV.getHealth();
    }

    public void updateHealth(int givenHealth) { //best used in PlayerController (<- an example)
        health = givenHealth;
    }

    public void updateJumpCharge() {
        jumpCharge = localPCGV.getJumpCharge();
    }

    public void updateJumpCharge(float givenJumpCharge) {
        jumpCharge = givenJumpCharge;
    }
    
    public void updateSuperSpeedTimer(float givenSuperSpeedTimer) {
        superSpeedTimer = givenSuperSpeedTimer;
    }


    //error catcher for images
    public BufferedImage missingImage() {
        try {
            System.out.println("Initial Image Missing - PlaceHolder Image Found: MINOR ERROR (GAMEVIEW)");
            return ImageIO.read(new File("data/missingimage.png"));
        }
        catch (IOException e) {
            System.err.println("Initial Image Missing - PlaceHolder Image Missing: MAJOR ERROR (GAMEVIEW)");
            //throw new RuntimeException(e);//code can still run, as object will just appear without an image...
            return missingImage();
        }
    }

    public void setGameWindow(JFrame givenWindow) {
        window = givenWindow;
    }

    @Override
    public void paintForeground(Graphics2D g) {
        if (Level.currentLevelNumber%2 == 1 && !localPCGV.gamePaused) {
            g.drawImage(imageLevelCleared.image, 0, 0, getWidth(), getHeight(), this);
        }
        else if (localPCGV.gamePaused) {
            if (localPCGV.menuState.equals("help")) {
                if (localPCGV.helpscreen == 0) {
                    g.drawImage(help0.image, 0, 0, getWidth(), getHeight(), this);
                }
                else if (localPCGV.helpscreen == 1) {
                    g.drawImage(help1.image, 0, 0, getWidth(), getHeight(), this);
                }
                else if (localPCGV.helpscreen == 2) {
                    g.drawImage(help2.image, 0, 0, getWidth(), getHeight(), this);
                }
                else if (localPCGV.helpscreen == 3) {
                    g.drawImage(help3.image, 0, 0, getWidth(), getHeight(), this);
                }
            }
            else if (localPCGV.menuState.equals("menu")) {
                g.drawImage(imagemenu.image, 0, 0, getWidth(), getHeight(), this);
            }
            else if (localPCGV.menuState.equals("leaderboard")) {
                g.setFont(new Font("ComicSans", Font.BOLD, 32));
                g.setColor(Color.green);
                localPCGV.setViewPosition(-1000,0);
                if (!names.isEmpty()) {
                    g.drawString("NAME" + "            :            SCORE", (getWidth()/2)-200, (80));
                    for (int i = 0; i < names.size() && i < 10; i++) {
                        g.drawString(names.get(i), (getWidth()/2)-200, (200 + i*50));
                        g.drawString(scores.get(i).toString(), (getWidth()/2) + 50, (200 + i*50));
                    }
                }
            }
        }

        else if (Level.currentLevelNumber == 10) {
            g.setColor(Color.green);
            g.setFont(new Font("ComicSans", Font.BOLD, 32));
            if (localPCGV.name.isBlank()) {
                g.drawString("TYPE YOUR NAME:", (getWidth()/2) - 200, (getHeight()/2) - 60);
            }
            else {
                g.drawString("YOU WILL NOW BE KNOWN AS:" + localPCGV.name, (getWidth()/2) - 394, (getHeight()/2) - 60);
            }
        }

        else {
            g.setColor(Color.gray);
            g.fillRect(0, 0, 136, 90);
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 16));
//            g.drawString("SCORE:" + score, 6, 16);
//            g.drawString("HEALTH:" + health, 6, 32);
//            g.drawString("JUMP CHARGE:" + jumpCharge, 6, 48);
            g.drawString("SCORE:", 6, 17);//spacing is 22 pixels (y)
            g.drawString("HEALTH: ", 6, 39);
            g.drawString("JUMP CHARGE: ", 6, 61);
            g.drawString("SUPER SPEED: ", 6, 83);

            g.setColor(Color.black);
            g.fillRect(138, 2, (5 * score) + 4, 20);
            g.fillRect(138, 24, (15 * health) + 4, 20);//health is more valuable (5x more valuable from the perspective of respawning, so it should have a bigger bar, not 5x bigger but still bigger than score bar for the same actual value)
            g.fillRect(138, 46, (5 * (int) jumpCharge) + 4, 20);
            g.fillRect(138, 68, ((int) superSpeedTimer) + 4, 20);
            g.setColor(Color.yellow);
            g.fillRect(140, 4, 5 * score, 16);
            g.setColor(Color.red);
            g.fillRect(140, 26, 15 * health, 16);//have to change inside of bar as well
            g.setColor(Color.blue);
            g.fillRect(140, 48, 5 * (int) jumpCharge, 16);
            g.setColor(light_blue);
            g.fillRect(140, 70, (int) superSpeedTimer, 16);
        }
    }

    @Override
    protected void paintBackground(Graphics2D g) {
        if (!localPCGV.gamePaused) {
            if (Level.currentLevelNumber == 0) {
                if (l0imagecounter >= l0images.length * (frameduration + 14 )) {
                    l0imagecounter = 0;
                }
                g.drawImage(l0images[floor(l0imagecounter / (frameduration + 14))], 0, 0, getWidth(), getHeight(), this);
                l0imagecounter++;
            }

            //setup for second playable level
            else if (Level.currentLevelNumber == 1) {
                frameduration = 10;
            }
            else if (Level.currentLevelNumber == 2) {
                if (l2imagecounter >= l2images.length * frameduration) {
                    l2imagecounter = 0;
                }
                g.drawImage(l2images[floor(l2imagecounter / frameduration)], 0, 0, getWidth(), getHeight(), this);
                l2imagecounter++;
            }
            else if (Level.currentLevelNumber == 3) {
                frameduration = 12;
            }

            //leveltransition takes up a level in between so all odd levels are a transition level
            //Note: I swapped level 1 and level 2 images
            else if (Level.currentLevelNumber == 4) {
                //swapped l1 images with l2 images (they match better imo)
                if (l1imagecounter >= l1images.length * frameduration) {
                    l1imagecounter = 0;
                }
                g.drawImage(l1images[floor(l1imagecounter / frameduration)], 0, 0, getWidth(), getHeight(), this);
                l1imagecounter++;
            }

            else if (Level.currentLevelNumber == 5) {
                frameduration = 8;
            }

            else if (Level.currentLevelNumber == 6) {
                if (l3imagecounter >= l3images.length * frameduration) {
                    l3imagecounter = 0;
                }
                g.drawImage(l3images[floor(l3imagecounter / frameduration)].image, 0, 0, getWidth(), getHeight(), this);
                l3imagecounter++;
            }

            else if (Level.currentLevelNumber == 7) {
                frameduration = 10;
            }

            else if (Level.currentLevelNumber == 8) {
                if (l4imagecounter >= l4images.length * frameduration) {
                    l4imagecounter = 0;
                }
                g.drawImage(l4images[floor(l4imagecounter / frameduration)].image, 0, 0, getWidth(), getHeight(), this);
                l4imagecounter++;
            }

            else if (Level.currentLevelNumber == 9) {
                frameduration = 10;
            }

            else if (Level.currentLevelNumber == 10) {
                localPCGV.setViewPosition(-100,100);
                if (!nameInputterCreated) {
                    window.removeKeyListener(window.getKeyListeners()[0]);//re-add this back in Game reset method
                    window.addKeyListener(localPCGV.getNameInputter());//username inputter created here, so we only need to set to level 10 and we then get username entering capabilities automated
                }
                g.drawImage(gamefinish.image, 0, 0, getWidth(), getHeight(), this);
            }
            else {
                g.drawImage(imageBG.image, 0, 0, getWidth(), getHeight(), this);
            }
        }
        else {
            if (localPCGV.menuState.equals("leaderboard")) {
                g.drawImage(lb.image, 0, 0, getWidth(), getHeight(), this);
            }

        }


    }

    public void reloadFile(String FilePath) {

        FileReader fr = null;
        BufferedReader reader = null;
        names = new ArrayList<>(10);
        scores = new ArrayList<>(10);
        try {
            System.out.println("Reading " + FilePath + " ...");
            fr = new FileReader(FilePath);
            reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                // file is assumed to contain one name, score pair per line
                String[] tokens = line.split(",");
                names.add(tokens[0]);
                scores.add(Float.parseFloat(tokens[1]));
                line = reader.readLine();
            }
            System.out.println("...done.");
        }
        catch (Exception e) {
            System.err.println("cannot read file - PlayerController");
            e.printStackTrace();
        }
        try {
            if (reader != null) {
                reader.close();
            }
            if (fr != null) {
                fr.close();
            }
        } catch (Exception e) {
            System.err.println("cannot close file - PlayerController");
            e.printStackTrace();
        }
        
        bubbleSort();
    }
    
    public void bubbleSort() {
        float tempScore;
        String tempName;
        if (!names.isEmpty()) {
            for (int i = 0; i < names.size() - 1; i++) {
                for (int j = 1; j < (names.size() - i); j++) {
                    if (scores.get(j - 1) < scores.get(j)) {
                        tempScore = scores.get(j);
                        tempName = names.get(j);
                        scores.set(j, scores.get(j - 1));
                        names.set(j, names.get(j - 1));
                        scores.set((j - 1), tempScore);
                        names.set((j - 1), tempName);
                    }
                }
            }
        }
    }
}
