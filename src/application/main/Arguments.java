package application.main;

import application.functions.Vector2D;

final public class Arguments {

    final public static String SERVER_HOST = "squidspirit.com";
    final public static int SERVER_PORT = 9217;
    final public static String DATA_PATH = "%appdata%/Tetris";
    
    final public static Vector2D SCENE_SIZE = new Vector2D(800, 840);
    final public static Vector2D GAME_PAD_SIZE = new Vector2D(300, 600);
    final public static int BLOCK_SIZE = 30;

    final public static long FRAME_TIME = (long)(1E9 / 60);
    
    final public static int FLASH_SPEED = 60;
    final public static int[] LEVELUP_REQUIRE = {
          10,   30,   60,  100,  150,
         210,  280,  360,  450,  550,
         650,  750,  850,  950, 1050,
        1150, 1260, 1380, 1510, 1650,
        1800, 1960, 2130, 2310, 2500,
        2700, 2900, 3100, 3300, Integer.MAX_VALUE
    };
    final public static int[] FALL_SPEED = {
        48, 43, 38, 33, 28,
        23, 18, 13,  8,  6,
         5,  5,  5,  4,  4,
         4,  3,  3,  3,  2,
         2,  2,  2,  2,  2,
         2,  2,  2,  2,  1
    };
}
