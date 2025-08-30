package game;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Used to write to a file. Used in a KeyListener in the PlayerController class
 */
public class fileWriter {

    private String fileName;

    public fileWriter(String fileName) {
        this.fileName = fileName;
    }

    public void writeHighScore(String name, double score) throws IOException {
        boolean append = true;
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, append);
            writer.write(name + "," + score + "\n");
        } finally {
            if (writer != null) {//means if writer or the filewriter has a file open on it, close that file, otherwise you can't run close on an unassigned filewriter type variable
                writer.close();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        fileWriter hsWriter = new fileWriter("data/scoresandnames.txt");
        for (int i = 0; i < args.length; i += 2) {
            String name = args[i];
            int score = Integer.parseInt(args[i + 1]);
            hsWriter.writeHighScore(name, score);
        }
    }
}