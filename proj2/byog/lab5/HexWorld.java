package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 40;

    private static final long SEED = 323423242;

    private static final Random RANDOM = new Random(SEED);


    /** public static void addHexagon(TETile[][] world, Position p, int s, TETile t),
     * where Position is a very simple class with two variables p.x and p.y and no methods.
     * p specifies the lower left corner of the hexagon.*/
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        Position bp = botmStart(p, s);
        Position up = upStart(p, s);
        if (bp.x > 0 && bp.x + 3 * s - 2 < WIDTH && bp.y + s < HEIGHT) {
            drawHalf(world, bp, s, t, "down");
            drawHalf(world, up, s, t, "up");
        }
    }
    private static void drawHalf(TETile[][] world, Position p, int s, TETile t, String str) {
        int len = 3 * s - 2;
        while (len >= s) {
            addline(world, p, len, t);
            len -= 2;
            Pos(p, str);
        }
    }
    /** add a line of tiles of a hexagon. */
    private static void addline(TETile[][] world, Position p, int len, TETile t) {
        for (int i = p.x; i < p.x + len; i++) {
            //world[i][p.y] = TETile.colorVariant(t, 243, 443,653, new Random());
            world[i][p.y] = t;
        }
    }

    /** update start position, and check if hexagon will be out of the bound. */
    private static Position botmStart(Position p, int s) {
        return new Position(p.x - s + 1, p.y + s - 1);
    }
    private static Position upStart(Position p, int s) {
        return new Position(p.x - s + 1, p.y + s);
    }

    /** update position p */
    private static void Pos(Position p, String str) {
        p.x += 1;
        if (str.equals("up")) {
            p.y += 1;
        } else {
            p.y -= 1;
        }
    }

    private static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static void initializeTile(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.SAND;
        }
    }


    public static void tesselation(TETile[][] world, int s) {
        Position pl = new Position(WIDTH / 2, 0);
        int i = 1;
        Position pr = new Position(WIDTH / 2, 0);
        while (pl.x > 0 || pr.x < WIDTH) {
            tessHalf(world, s, pl);
            tessHalf(world, s, pr);
            pl.x -= 2 * s - 1;
            pr.x += 2 * s - 1;
            pl.y = i * s;
            pr.y = i * s;
            i ++;
        }
    }

    private static void tessHalf(TETile[][] world, int s, Position p) {
        while (p.y < HEIGHT) {
            TETile t = randomTile();
            addHexagon(world, p, s, t);
            p.y += 2 * s;
        }
    }
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexaworld = new TETile[WIDTH][HEIGHT];
        initializeTile(hexaworld);

        tesselation(hexaworld, 3);

        ter.renderFrame(hexaworld);
    }
}
