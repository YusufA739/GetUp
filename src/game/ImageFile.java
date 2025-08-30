package game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * BufferedImage wrapper to reduce method duplicates spread across multiple classes.
 */
public class ImageFile {
    BufferedImage image;

    public ImageFile(String FilePath) {
        try {
            image = ImageIO.read(new File(FilePath));
        } catch (Exception e) {
            image = missingImage();
        }
    }
    //old method (new name for it below)
    public void fetchImage(String givenPath) {
        try {
            image = ImageIO.read(new File(givenPath));
        } catch (Exception e) {
            image = missingImage();
        }
    }
    //alt name
    public void setImage(String givenPath) {
        try {
            image = ImageIO.read(new File(givenPath));
        } catch (Exception e) {
            image = missingImage();
        }
    }
    //error catcher for images
    private BufferedImage missingImage() {
        try {
            System.out.println("Initial Image Missing - PlaceHolder Image Found: MINOR ERROR (GAMEVIEW)");
            return ImageIO.read(new File("data/missingimage.png"));
        }
        catch (IOException e) {
            System.err.println("Initial Image Missing - PlaceHolder Image Missing: MAJOR ERROR (GAMEVIEW)");
            //throw new RuntimeException(e);//
            return null;
        }
    }
    public void setBackgroundImage(String givenPath) {
        try {
            image = ImageIO.read(new File(givenPath));
        } catch (IOException e) {
            image = missingImage();
        }
    }

    public void setBackgroundImage(BufferedImage givenImage) {
        try {
            image = givenImage;
        } catch (Exception e) {
            image = missingImage();
        }
    }
}
