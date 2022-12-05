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

    /** Create a world generator with seed and an initialized world (filled with null). */
    public WorldGenerator(int sd) {
        seed = sd;
        random = new Random(seed);
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
        World w = new World();
        int n = RandomUtils.uniform(random, 10, 15);
        w.setRooms(10);
        w.setHallways();
        w.drawRoom();
        return w.world;
    }

    private class World {
        private TETile[][] world;

        private ArrayList<Room> rooms;
        private World() {
            world = new TETile[WIDTH][HEIGHT];
            initializeTile(world);
            rooms = new ArrayList<>();
        }


        /** Set n rooms that not overlap with each others in a room list. */
        private void setRooms(int n) {
            while (rooms.size() < n) {
                RandomParametor rdp = new RandomParametor("r");
                Room r = new Room(rdp.p, rdp.width, rdp.height, rdp.roomtype);
                if (r.wthinWorld() && r.Notoverlap(rooms)) {
                    rooms.add(r);
                }
            }
        }

        private void setHallways() {
            // sort room in the order of room's lb.x from left to right.
            rooms.sort(Room.roomComparator());
            for (int i = 0; i < rooms.size() - 1; i++) {
                Room currenRoom = rooms.get(i);
                Room nextRoom = rooms.get(i + 1);
                connect(currenRoom, nextRoom);
            }
        }


        private void connect(Room r1, Room r2) {

        }



        /** add room's floor and wall into the world. */
        public void drawRoom() {
            for (Room r : rooms) {
                addRoomFloor(r);
            }
            /**
            for (Room r: rooms) {
                addWall(r);
            }
             */

        }

        public void addRoomFloor(Room r) {
            // draw floor
            TETile tile = Tileset.FLOOR;
            int lx = r.lb.x;
            int ly  = r.lb.y;
            for (int x = lx; x < lx + r.width; x++) {
                for (int y = ly; y < ly + r.height; y++) {
                    world[x][y] = tile;
                }
            }

        }
        private void addWall(Room r) {
            TETile tile = TETile.colorVariant(Tileset.WALL, 33,41, 20, random);
            int lx = r.lb.x - 1;
            int ly = r.lb.y - 1;
            int ry = r.rt.y + 1;
            int rx = r.rt.x + 1;
            for (int x = lx; x < rx; x++) {
                for (int y = ly; y < ry; y++) {
                    if (world[x][y].equals(Tileset.FLOOR)) {
                        continue;
                    }
                    world[x][y] = tile;
                }
            }
        }

        private void initializeTile(TETile[][] world) {
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    world[x][y] = Tileset.NOTHING;
                }
            }
        }


    }




    /** set random number of random hallways to Rms.*/


    /** remove room which has no hallways from room list RMS.
     *  remove block of rooms which is isolated. */
    /** set a hallway that connects two rooms in list of RMS*/

    /** Return true if room doesn't overlap with any room in rms.*/

    /**
     *  return true if hallway R connects two rooms without overlapping with other rooms in rooms.
     * */

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




    /** Create a random position within WIDTH and HEIGHT */
    private Position rdLocation() {
        int x = RandomUtils.uniform(random, 1, WIDTH - 1);
        int y = RandomUtils.uniform(random, 1, HEIGHT - 1);
        return new Position(x, y);
    }

    /** create a single room in the world.
     *  according to room's type S.
     *  draw at position room's leftBottom position. */

    /** Add rectangular tiles. */

    public static class Room{
        private Position lb;
        private Position rt;

        private String rType;
        private ArrayList<Room> connectR;
        private int width;
        private int height;

        public Room(Position p, int w, int h, String type) {
            rType = type;
            width = w;
            height = h;
            lb = new Position(p.x, p.y);
            rt = new Position(lb.x + w, lb.y + h);
            connectR = new ArrayList<>();
        }

        /**
         * return true if this room doesn't overlap with any room in Rooms List.
         */
        private boolean Notoverlap(ArrayList<Room> rooms) {
            for (Room r: rooms) {
                if (!Notoverlap(r)) {
                    return false;
                }
            }
            return true;
        }


        /** Return true if this room doesn't overlap r */
        private boolean Notoverlap(Room r) {
            // r is at top of this room.
            if (r.lb.y > this.rt.y + 1) {
                return true;
            }
            // r is below this room.
            if (r.rt.y < this.lb.y - 1) {
                return true;
            }
            // r is at this room's right.
            if (r.lb.x > this.rt.x + + 1) {
                return true;
            }
            // r is at this room's left.
            if (r.rt.x < this.lb.x - 1) {
                return true;
            }
            return false;
        }
        /** return true if this room is within the world bound. */
        private boolean wthinWorld() {
            if (rt.x < WIDTH - 1 && rt.y < HEIGHT - 1) {
                return true;
            }
            return false;
        }

        /** return true if room is at the rightTop of this room. */
        private boolean righTop(Room rm) {
            if (rm.lb.x >= this.rt.x && rm.lb.y >= this.rt.y) {
                return true;
            }
            return false;
        }
        /** return true if room is at the leftBottom of this room. */
        private boolean rightBottom(Room rm) {
            if (rm.lb.x >= this.rt.x && rm.rt.y <= this.lb.y) {
                return true;
            }
            return false;
        }

        /** return true if room is at bottom of this room. */
        private boolean Bottom(Room rm) {
            if (rm.rt.y < this.lb.y && !this.rightBottom(rm)) {
                return true;
            }
            return false;
        }
        /** return true if room is at the top of this room. */
        private boolean Top(Room rm) {
            if (rm.lb.y > this.rt.y && !this.righTop(rm)) {
                return true;
            }
            return false;
        }

        /** return ture if room is at the right of this room. */
        private boolean Right(Room rm) {
            if (!Top(rm) && ! righTop(rm) && !rightBottom(rm) && !Bottom(rm)) {
                return true;
            }
            return false;
        }


        private static class RoomComparator implements Comparator<Room> {

            /** return a negative value if r1 is at left of r2.
             *  return zero if r1
             *  return a positive value if r1 is at the right of r2.
             * */
            @Override
            public int compare(Room r1, Room r2) {
                return r1.lb.x - r2.lb.x;
            }
        }
        public static Comparator<Room> roomComparator() {
            return new RoomComparator();
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
