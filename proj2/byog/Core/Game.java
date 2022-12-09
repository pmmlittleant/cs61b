package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Optional;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static WorldGenerator.Position player;
    private boolean GameStart;
    private TETile[][] world;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH + 10 , HEIGHT + 10 , 10,10);

        // show game Manu UI
        showManu();

        // process User input key Option.
        String key = getInputKey();
        if (key.equals("n")) {
            String seed = "";
            while (!seed.endsWith("s")) {
                GameInfo(seed);
                seed += getInputKey();
            }
            long sd = Long.parseLong(seed.substring(0, seed.indexOf("s")));
            WorldGenerator wg = new WorldGenerator(sd);
            world = wg.drawMap();
            player = wg.pl;
            GameStart = true;
            playGame();
        }

        if (key.equals("l")) {
            world = getSavedWorld();
            if (world == null) {
                System.exit(0);
            }
            GameStart = true;
            playGame();
        }

        if (key.equals("q")) {
            System.exit(0);
        }
    }

    /** play game using keyboard */
    private void playGame() {
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);


        while (GameStart == true) {
            ter.renderFrame(world);
            String key = getInputKey();
            if (key.equals("q")) {
                saveWorld(world, player);
                System.exit(0);
            }
            playGame(key);
        }

        GameInfo("GOOD JOB ! YOU WIN! ");
    }

    /** prompt "random seed" after user pressed n.
     *  draw user input seed before user press s.*/
    private void GameInfo(String s) {

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);

        if (s.equals("")) {

            StdDraw.text(WIDTH / 2, HEIGHT / 2, "random seed");
        } else {
            StdDraw.setFont(new Font("info", Font.TRUETYPE_FONT, 50));
            StdDraw.text(WIDTH / 2, HEIGHT / 2, s);
        }

        StdDraw.show();
        StdDraw.pause(2000);
    }

    /** get a single key press string. */
    private String getInputKey() {
        String key = "";
        while (key.length() < 1) {
            if (StdDraw.hasNextKeyTyped()) {
                key += StdDraw.nextKeyTyped();

            }
        }
        return key.toLowerCase();
    }

    /** draw GAME MANU UI*/
    private void showManu() {

        Font bigfont = new Font("tile", Font.BOLD, 40);
        StdDraw.setFont(bigfont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "CS61B: THE GAME");
        Font smallfont = new Font("option", Font.LAYOUT_LEFT_TO_RIGHT, 30);
        StdDraw.setFont(smallfont);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT - 23, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 26, "Quit (Q)");
        StdDraw.show();
        StdDraw.pause(1000);
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
        world = null;
        char first = input.charAt(0);
        if (first == 'q') {
            System.exit(0);
        }
        if (first == 'l') {
            world = getSavedWorld();
            if (world == null) {
                System.exit(0);
            }
            playGame(input.substring(1));
        }
        if (first == 'n') {
            int indexS = input.indexOf("s");
            long seed = Long.parseLong(input.substring(1, indexS));
            WorldGenerator wg = new WorldGenerator(seed);
            world = wg.drawMap();
            player = wg.pl;
            playGame(input.substring(indexS + 1));
        }
        if (input.endsWith(":q")) {
            saveWorld(world, player);
        }
    return world;
    }

    /**save game*/
    private void saveWorld(TETile[][] world, WorldGenerator.Position p) {
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
    /** load player's position from saved game.
     *  return TETile[][] world which is finalWorldFrame from savedGame.  */
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


    /** play the game with operation string.
     *  if next Tile is floor move player's position to next tile
     *  if next Tile is Door , change GameStart to false. */

    public void playGame(String operation) {
        for (int i = 0; i < operation.length(); i++) {
            char move = operation.charAt(i);
            int x = player.x;
            int y = player.y;
            TETile floor = Tileset.FLOOR;
            switch (move) {
                case 'w':
                    if (world[x][y + 1].equals(floor)) {
                        movePlayer(0, 1);
                        break;
                    }
                case 's':
                    if (world[x][y - 1].equals(floor)) {
                        movePlayer(0, -1);
                        break;
                    }
                case 'a':
                    if (world[x - 1][y].equals(floor)) {
                        movePlayer(-1, 0);
                        break;
                    }
                case 'd':
                    if (world[x + 1][y].equals(Tileset.LOCKED_DOOR)) {
                        GameStart = false;
                        break;
                    }
                    if (world[x + 1][y].equals(floor)) {
                        movePlayer(1, 0);
                        break;
                    }

                default:
            }
        }
    }

    /** change player's px, py by x, y and change finalWorld's tile type.*/
    private void movePlayer(int x, int y) {
        int px = player.x;
        int py = player.y;
        world[px][py] = Tileset.FLOOR;
        player.x = px + x;
        player.y = py + y;
        world[player.x][player.y] = Tileset.PLAYER;

    }
}
