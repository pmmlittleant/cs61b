package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static Map<String, Integer> player = new HashMap<>();
    public static TETile[][] finalWorldFrame = null;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        input.toLowerCase();
        TETile[][] finalWorldFrame = null;
        char first = input.charAt(0);
        if (first == 'q') {
            System.exit(0);
        }
        if (first == 'l') {
            finalWorldFrame = null;
        }
        if (first == 'n') {
            int indexS = input.indexOf("s");
            long seed = Long.parseLong(input.substring(1, indexS));
            WorldGenerator wg = new WorldGenerator(seed);
            finalWorldFrame = wg.drawMap();
            //player = wg.pl;
            //String operation = input.substring(indexS + 1);
            //playGame(operation);
        }
    return finalWorldFrame;
    }

    /**save game*/
    private static void saveWorld() {
        File f = new File("./world.txt");
        return;
    }


    /** play the game with operation string. */
    public void playGame(String operation) {
        if (operation.length() == 0) {
            return;
        }
        for (int i = 0; i < operation.length(); i++) {
            char move = operation.charAt(i);
            int x = player.get("x");
            int y = player.get("y");
            TETile floor = Tileset.FLOOR;
            switch (move) {
                case 'w':
                    if (finalWorldFrame[x][y + 1].equals(floor)) {
                        movePlayer(0, 1);
                        break;
                    }
                case 's':
                    if (finalWorldFrame[x][y - 1].equals(floor)) {
                        movePlayer(0, -1);
                        break;
                    }
                case 'a':
                    if (finalWorldFrame[x - 1][y].equals(floor)) {
                        movePlayer(-1, 0);
                        break;
                    }
                case 'd':
                    if (finalWorldFrame[x + 1][y].equals(floor)) {
                        movePlayer(1, 0);
                        break;
                    }
                default:
            }
        }
    }

    /** change player's px, py by x, y and change finalWorld's tile type.*/
    private void movePlayer(int x, int y) {
        int px = player.get("x");
        int py = player.get("y");
        finalWorldFrame[px][py] = Tileset.FLOOR;
        finalWorldFrame[px + x][py + y] = Tileset.PLAYER;
        player.put("x", px + x);
        player.put("y", py + y);
    }
}
