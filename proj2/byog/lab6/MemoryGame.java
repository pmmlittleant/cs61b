package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String s = "";
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(CHARACTERS.length);
            s += CHARACTERS[index];
        }

        return s;
    }


    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        StdDraw.clear(Color.BLACK);
        Font font = new Font("RandomString" ,Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(StdDraw.WHITE);
        if (gameOver == false) {
            StdDraw.text(3, height - 2, "Round: " + round);
            StdDraw.text(this.width / 2, height - 2, playerTurn ? "Type!" : "Watch!");

            StdDraw.text(this.width - 5, height - 2, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
            StdDraw.line(0, height - 3, width, height - 3);
        }
        Font bigFont = new Font("M", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.text(this.width / 2, this.height / 2 , s);
        StdDraw.show();
        StdDraw.pause(1000);

        //TODO: If game is not over, display relevant game information at the top of the screen

    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        char[]  letterChar = letters.toCharArray();
        for (Character c: letterChar) {
            drawFrame(c.toString());
            drawFrame("");
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String inputString = "";
        drawFrame(inputString);
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                inputString += StdDraw.nextKeyTyped();
                drawFrame(inputString);
                n -= 1;
            }
        }
        return inputString;
    }

    public void startGame() {
        round = 1;
        gameOver = false;

        while (gameOver == false) {
            playerTurn = false;
            String message = "Round: " + round;
            drawFrame(message);

            String s = generateRandomString(round);
            flashSequence(s);

            playerTurn = true;
            String userInput = solicitNCharsInput(round);

            if (userInput.equals(s)) {
                round += 1;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }

    }

}
