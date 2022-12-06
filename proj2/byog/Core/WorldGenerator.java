package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

public class WorldGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    private long seed;
    public Map<String, Integer> pl;
    private Random random;

    /** Create a world generator with seed and an initialized world (filled with null). */
    public WorldGenerator(long sd) {
        seed = sd;
        random = new Random(seed);
        pl = new HashMap<>();
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
        int n = RandomUtils.uniform(random, 20, 30);
        w.setRooms(n);
        w.setHallways();
        w.drawRoom();
        w.setDoor();
        w.setPlayer();
        return w.world;
    }

    private class World {
        private TETile[][] world;

        private ArrayList<Room> rooms;
        private World() {
            world = new TETile[WIDTH][HEIGHT];
            initializeTile();
            rooms = new ArrayList<>();
        }

        /** add a door tile to the world.
         *  the door must be on the wall. */
        private void setDoor() {
            TETile d = Tileset.LOCKED_DOOR;
            while (true) {
                Position p = rdLocation();
                if (world[p.x][p.y].equals(Tileset.WALL)) {
                    if (world[p.x - 1][p.y].equals(Tileset.FLOOR) && world[p.x + 1][p.y].equals(Tileset.NOTHING)) {
                        world[p.x][p.y] = d;
                        break;
                    }
                }
            }
        }
        /** add a player tile to the world.
         *  the player must be on the floor. */

        private void setPlayer() {
            TETile player = TETile.colorVariant(Tileset.PLAYER, 23, 46, 90, random);
            while (true) {
                Position p = rdLocation();
                if (world[p.x][p.y].equals(Tileset.FLOOR)) {
                    world[p.x][p.y] = player;
                    pl.put("x", p.x);
                    pl.put("y", p.y);
                    break;
                }
            }
        }


        /** Set n rooms that not overlap with each others in a room list. */
        private void setRooms(int n) {
            while (rooms.size() < n) {
                RandomParametor rdp = new RandomParametor();
                Room r = new Room(rdp.p, rdp.width, rdp.height);
                if (r.wthinWorld() && r.notOverlap(rooms)) {
                    rooms.add(r);
                }
            }
        }

        private void setHallways() {
            // sort room in the order of room's lb.x from left to right.
            rooms.sort(Room.roomComparator());
            int n = rooms.size();
            for (int i = 0; i < n - 1; i++) {
                Room curRoom = rooms.get(i);
                Room nextRoom = rooms.get(i + 1);
                connect(curRoom, nextRoom);

            }
        }


        /** add room's floor and wall into the world. */
        public void drawRoom() {
            for (Room r : rooms) {
                addRoomFloor(r);
            }

            for (Room r: rooms) {
                addWall(r);
            }


        }
        /** Connect r1 r2 according their location.*/
        private void connect(Room r1, Room r2) {
            switch (compareLocation(r1, r2)) {
                case "T":
                    int w = 1;
                    int h = r2.lb.y - r1.rt.y;
                    Position p = new Position(RandomUtils.uniform(random, r2.lb.x, Math.min(r1.rt.x, r2.rt.x)), r1.rt.y);
                    rooms.add(new Room(p, w, h));
                    break;
                case "RT":
                    // add vertical hallway
                    w = 1;
                    h = r2.lb.y - r1.rt.y + 1;
                    p = new Position(r1.rt.x - 1, r1.rt.y);
                    rooms.add(new Room(p, w, h));

                    // add horizontal hallway.
                    w = r2.lb.x - r1.rt.x + 1;
                    h = 1;
                    p = new Position(r1.rt.x - 1, r2.lb.y + 1);
                    rooms.add(new Room(p, w, h));
                    break;
                case "R":
                    w = r2.lb.x - r1.rt.x;
                    h = 1;
                    int x = r1.rt.x;
                    int upper = Math.min(r1.rt.y, r2.rt.y);
                    int lower = Math.max(r1.lb.y, r2.lb.y);
                    int y = RandomUtils.uniform(random, lower, upper);
                    p = new Position(x, y);
                    rooms.add(new Room(p, w, h));
                    break;
                case "RB":
                    // add vertical hallway
                    w = 1;
                    h = r1.lb.y - r2.rt.y + 1;
                    p = new Position(r1.rt.x - 1, r2.rt.y - 1);
                    rooms.add(new Room(p, w, h));

                    // add horizontal hallway
                    w = r2.lb.x - r1.rt.x + 1;
                    h = 1;
                    rooms.add(new Room(p, w, h));
                    break;

                case "B":
                    h = r1.lb.y - r2.rt.y;
                    w = 1;
                    y = r2.rt.y;
                    x = RandomUtils.uniform(random, r2.lb.x, Math.min(r1.rt.x, r2.rt.x));
                    p = new Position(x, y);
                    rooms.add(new Room(p, w, h));
                    break;
            }
        }



        /** return a string represents the r2's relative location to r1.*/
        private String compareLocation(Room r1, Room r2) {
            if (r1.aTop(r2)) {
                return "T";
            }
            if (r1.righTop(r2)) {
                return "RT";
            }
            if (r1.aRight(r2)) {
                return "R";
            }
            if (r1.rightBottom(r2)) {
                return "RB";
            }
            return "B";
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
            TETile tile = TETile.colorVariant(Tileset.WALL, 33, 41, 20, random);
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

        private void initializeTile() {
            for (int x = 0; x < WIDTH; x += 1) {
                for (int y = 0; y < HEIGHT; y += 1) {
                    world[x][y] = Tileset.NOTHING;
                }
            }
        }


    }

    /** room random parameters*/
    private class RandomParametor {
        private int width;
        private int height;
        private Position p;

        /** create random horizontal or vertical hallway's parameter. */
        public RandomParametor() {
            p = rdLocation();
            width = RandomUtils.uniform(random, 4,  WIDTH / 7);
            height = RandomUtils.uniform(random, 2, HEIGHT / 3);


        }
    }




    /** Create a random position within WIDTH and HEIGHT */
    private Position rdLocation() {
        int x = RandomUtils.uniform(random, 1, WIDTH - 1);
        int y = RandomUtils.uniform(random, 1, HEIGHT - 1);
        return new Position(x, y);
    }

    public static class Room {
        private Position lb;
        private Position rt;


        private ArrayList<Room> connectR;
        private int width;
        private int height;

        public Room(Position p, int w, int h) {

            width = w;
            height = h;
            lb = new Position(p.x, p.y);
            rt = new Position(lb.x + w, lb.y + h);
            connectR = new ArrayList<>();
        }

        /**
         * return true if this room doesn't overlap with any room in Rooms List.
         */
        private boolean notOverlap(ArrayList<Room> rooms) {
            for (Room r: rooms) {
                if (!notOverlap(r)) {
                    return false;
                }
            }
            return true;
        }


        /** Return true if this room doesn't overlap r */
        private boolean notOverlap(Room r) {
            // r is at top of this room.
            if (r.lb.y > this.rt.y + 1) {
                return true;
            }
            // r is below this room.
            if (r.rt.y < this.lb.y - 1) {
                return true;
            }
            // r is at this room's right.
            if (r.lb.x > this.rt.x + 1) {
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
        private boolean aBottom(Room rm) {
            if (rm.rt.y < this.lb.y && !this.rightBottom(rm)) {
                return true;
            }
            return false;
        }
        /** return true if room is at the top of this room. */
        private boolean aTop(Room rm) {
            if (rm.lb.y > this.rt.y && !this.righTop(rm)) {
                return true;
            }
            return false;
        }

        /** return ture if room is at the right of this room. */
        private boolean aRight(Room rm) {
            if (!aTop(rm) && !righTop(rm) && !rightBottom(rm) && !aBottom(rm)) {
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
        WorldGenerator wG = new WorldGenerator(2);

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(wG.drawMap());

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
