package byog.Core;


import static org.junit.Assert.*;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;
import org.junit.rules.Timeout;
import sun.security.util.ArrayUtil;

import java.util.*;
import java.util.concurrent.TimeoutException;

public class WorldGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    private long seed;

    private Random random;

    private TETile[][] world;

    /** Create a world generator with seed and an initialized world (filled with null). */
    public WorldGenerator(int sd) {
        seed = sd;
        random = new Random(seed);
        world = new TETile[WIDTH][HEIGHT];
        initializeTile(world);
    }

    private static void initializeTile(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** random Number of rooms, hallways.
     *  random Locations of rooms and hallways.
     *  random Width and Height of rooms.
     *  random Length of hallways.
     *
     *  Wall Floor unused Spaces should be distinct with each other.
     *  Rooms and Hallways should be connected.
     * */


    /**  return a world with random number of rooms with ramdom number of hallways connected to rooms. */


    public TETile[][] drawMap() {
        ArrayList<Room> rms = setNRooms(14);

        //setNHall(rms);

        drawRoom(rms);
        return world;
    }

    /** return a list of rooms with random location and random width and height.
     *  rooms don't overlap with each other.*/
    private ArrayList<Room> setNRooms(int n) {
        ArrayList<Room> rms = new ArrayList<>();
        while (rms.size() < n) {
            RandomParametor rdp = new RandomParametor("r");
            Room r = new Room(rdp.p, rdp.width, rdp.height, rdp.roomtype);
            if (NotOverlap(r, rms) && r.canConTo(rms) ) {
                rms.add(r);
            }
        }
        return rms;
    }

    /** set random number of random hallways to Rms.*/
    private ArrayList<Room> setNHall(ArrayList<Room> rms) {
        int n = rms.size();

        while (rms.size() <  n + 2 * RandomUtils.uniform(random, 10,15)) {
            addHall(rms);
        }
        deleteSingleRoom(rms);
        System.out.println(rms.size());
        return rms;
    }



    /** remove room which has no hallways from room list RMS.
     *  remove block of rooms which is isolated. */
    private void deleteSingleRoom(ArrayList<Room> rms) {
        ArrayList<Room> cpyrms = new ArrayList<>(rms);
        for (Room r: cpyrms) {
            if (r.rType.equals("r") && r.hallway == 0) {
                rms.remove(r);
            }
        }
    }
    /** set a hallway that connects two rooms in list of RMS*/
    private void addHall(ArrayList<Room> rms) {
        RandomParametor rdp = new RandomParametor();
        Room r = new Room(rdp.p, rdp.width, rdp.height, rdp.roomtype);
        if (isConnectHallway(r, rms)) {
            rms.add(r);
        }
    }

    /** Return true if room doesn't overlap with any room in rms.*/
    private boolean NotOverlap(Room r, ArrayList<Room> rms) {
        return r.leftBottom.x > 0 && r.rigthTop.x <= WIDTH && r.rigthTop.y <= HEIGHT && !r.isLapped(rms);
    }

    /**
     *  return true if hallway R connects two rooms without overlapping with other rooms in rooms.
     * */
    private boolean isConnectHallway(Room r, ArrayList<Room> rooms) {
        for (Room rm: rooms) {
            r.connect(rm);
        }
        if (r.connectR.size() == 2) {
            ArrayList<Room> rms = new ArrayList<>(rooms);
            rms.remove(r.connectR.get(0));
            rms.remove(r.connectR.get(1));
            if (NotOverlap(r, rms)) {
                r.connectR.get(0).hallway += 1;
                r.connectR.get(1).hallway += 1;
                return true;
            }
        }
        return false;
    }


    /** room random parameters*/
    private class RandomParametor {
        private int width;
        private int height;
        private String roomtype;
        private Position p;

        /** create random horizontal or vertical hallway's parameter. */
        public RandomParametor() {
            p = rdLocation();
            double p = RandomUtils.uniform(random) - 0.5;
            if (p < 0) {
                width = RandomUtils.uniform(random, 2, WIDTH);
                height = 3;
                roomtype = "hh";
            }
            if (p > 0) {
                width = 3;
                height = RandomUtils.uniform(random, 2, HEIGHT);
                roomtype = "hv";
            }
        }
        public RandomParametor(String str) {
            roomtype = str;
            p = rdLocation();
            if (str.equals("r")) {
                width = RandomUtils.uniform(random, 5, WIDTH / 6);
                height = RandomUtils.uniform(random, 5,HEIGHT / 2);
            }

        }
    }



    public void drawRoom(ArrayList<Room> rms) {
        for (Room r : rms) {
            addRoom(r, r.rType);
        }
    }

    /** Create a random position within WIDTH and HEIGHT */
    private Position rdLocation() {
        int x = RandomUtils.uniform(random, WIDTH);
        int y = RandomUtils.uniform(random, HEIGHT);
        return new Position(x, y);
    }

    /** create a single room in the world.
     *  according to room's type S.
     *  draw at position room's leftBottom position. */
    public void addRoom(Room r, String s) {
        // draw wall
        Position lefbot = r.leftBottom;
        Position rigtop = r.rigthTop;
        addRectangle(lefbot, rigtop, "wall");

        // draw floor inside walls
        Position lefbotfloor = new Position(lefbot.x + 1, lefbot.y + 1);
        Position rigtopfloor = new Position(rigtop.x - 1, rigtop.y - 1);
        addRectangle(lefbotfloor,rigtopfloor, "floor");

        if (s.equals("hv")) {
            lefbotfloor = new Position(lefbot.x + 1, lefbot.y);
            rigtopfloor = new Position(rigtop.x - 1, rigtop.y);
            addRectangle(lefbotfloor,rigtopfloor, "floor");
        }
        if (s.equals("hh")) {
            lefbotfloor = new Position(lefbot.x, lefbot.y + 1);
            rigtopfloor = new Position(rigtop.x, rigtop.y - 1);
            addRectangle(lefbotfloor,rigtopfloor, "floor");
        }
    }

    /** Add rectangular tiles. */
    private void addRectangle(Position lb, Position rt, String str) {
        TETile tile = TETile.colorVariant(Tileset.WALL, 23, 56, 31, random);
        if (str.equals("floor")) {
            tile = Tileset.FLOOR;
        }
        for (int x = lb.x; x < rt.x; x++) {
            for (int y = lb.y; y < rt.y; y++) {
                world[x][y] = tile;
            }
        }
    }

    public static class Room {
        private Position leftBottom;
        private Position rigthTop;

        private String rType;
        private ArrayList<Room> connectR;
        private int width;
        private int height;

        private int hallway;
        public Room(Position lb, int w, int h, String type) {
            hallway = 0;
            rType = type;
            width = w;
            height = h;
            leftBottom = new Position(lb.x, lb.y);
            rigthTop = new Position(leftBottom.x + w, leftBottom.y + h);
            connectR = new ArrayList<>();
        }

        /** Return room's inner floor.*/
        public Room innerFloor() {
            Position lfb = new Position(leftBottom.x + 1, leftBottom.y + 1);
            int wd = width - 2;
            int ht = height - 2;
            return new Room(lfb, wd, ht, "floor");
        }

        /**
         * Check if this room is lapped with any room in Room[].
         */
        public boolean isLapped(ArrayList<Room> rooms) {
            if (rooms.size() == 0) {
                return false;
            }
            for (Room r : rooms) {
                if (isLapped(r)) {
                    return true;
                }
            }
            return false;
        }

        /** Return true if r overlaps this room */
        private boolean isLapped(Room r) {
            if (r.rType.equals("hh") && rType.equals("hv")) {
                return false;
            } else if (r.rType.equals("hv") && rType.equals("hh")) {
                return false;
            }
            if (!(this.rigthTop.y <= r.leftBottom.y || this.leftBottom.x >= r.rigthTop.x)
                    && !(r.rigthTop.y <= this.leftBottom.y || r.leftBottom.x >= this.rigthTop.x) ){
                return true;
            }
            return false;
        }

        private void connect(Room r) {
            Room floor = r.innerFloor();
            if (this.rType.equals("hh")) {
                if (this.rigthTop.x == floor.leftBottom.x || this.leftBottom.x == floor.rigthTop.x) {
                    if (rigthTop.y <= floor.rigthTop.y && leftBottom.y >= floor.leftBottom.y) {
                        connectR.add(r);
                        return;
                    }
                }
            }
            if (this.rType.equals("hv")) {
                if (this.leftBottom.y == floor.rigthTop.y || this.rigthTop.y == floor.leftBottom.y) {
                    if (rigthTop.x <= floor.rigthTop.x && leftBottom.x >= floor.leftBottom.x) {
                        connectR.add(r);
                    }
                }
            }
        }

        /** return true if this room can connect to one of room in rms*/
        public boolean canConTo(ArrayList<Room> rms) {
            if (rms.size() == 0) {
                return true;
            }
            for (Room r: rms) {
                if (canConnectTo(r)) {
                    return true;
                }
            }
            return false;
        }

        /** Return true if this room can be connected to r. */
        public boolean canConnectTo(Room r) {
            int rx = r.rigthTop.x;
            int ry = r.rigthTop.y;
            int lx = r.leftBottom.x;
            int ly = r.leftBottom.y;

            if (lx >= rigthTop.x && ry <= leftBottom.y) {
                return false;
            }
            if (lx >= rigthTop.x && ly >= rigthTop.y) {
                return false;
            }
            if (rx <= leftBottom.x && ly >= rigthTop.y) {
                return false;
            }
            if (rx <= leftBottom.x && ry <= leftBottom.y) {
                return false;
            }
            return true;
        }
    }



    public static void main(String[] args) {
        WorldGenerator w = new WorldGenerator(75);

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(w.drawMap());

    }

    public static class Position {
        private int x;
        private int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
