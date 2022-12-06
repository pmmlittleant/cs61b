package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static WorldGenerator.Position player;

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
            finalWorldFrame = getSavedWorld();
            if (finalWorldFrame == null) {
                System.exit(0);
            }
            playGame(input.substring(1), finalWorldFrame);
        }
        if (first == 'n') {
            int indexS = input.indexOf("s");
            long seed = Long.parseLong(input.substring(1, indexS));
            WorldGenerator wg = new WorldGenerator(seed);
            finalWorldFrame = wg.drawMap();
            player = wg.pl;
            playGame(input.substring(indexS + 1), finalWorldFrame);
        }
        if (input.endsWith(":q")) {
            saveWorld(finalWorldFrame, player);
        }
    return finalWorldFrame;
    }

    /**save game*/
    private static void saveWorld(TETile[][] world, WorldGenerator.Position p) {
        File f = new File("./world.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(p);
            os.writeObject(world);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private TETile[][] getSavedWorld() {
        TETile[][] world;
        File f = new File("world.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                player = (WorldGenerator.Position) os.readObject();
                world = (TETile[][]) os.readObject();
                os.close();
                return world;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return null;
    }


    /** play the game with operation string. */
    public void playGame(String operation, TETile[][] world) {
        for (int i = 0; i < operation.length(); i++) {
            char move = operation.charAt(i);
            int x = player.x;
            int y = player.y;
            TETile floor = Tileset.FLOOR;
            switch (move) {
                case 'w':
                    if (world[x][y + 1].equals(floor)) {
                        movePlayer(0, 1, world);
                        break;
                    }
                case 's':
                    if (world[x][y - 1].equals(floor)) {
                        movePlayer(0, -1, world);
                        break;
                    }
                case 'a':
                    if (world[x - 1][y].equals(floor)) {
                        movePlayer(-1, 0, world);
                        break;
                    }
                case 'd':
                    if (world[x + 1][y].equals(floor)) {
                        movePlayer(1, 0, world);
                        break;
                    }

                default:
            }
        }
    }

    /** change player's px, py by x, y and change finalWorld's tile type.*/
    private void movePlayer(int x, int y, TETile[][] world) {
        int px = player.x;
        int py = player.y;
        world[px][py] = Tileset.FLOOR;
        player.x = px + x;
        player.y = py + y;
        world[player.x][player.y] = Tileset.PLAYER;

    }
}
