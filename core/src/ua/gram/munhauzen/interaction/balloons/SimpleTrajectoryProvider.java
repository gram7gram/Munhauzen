package ua.gram.munhauzen.interaction.balloons;

import com.badlogic.gdx.math.Vector2;

public class SimpleTrajectoryProvider {

    private static final Vector2[] t1 = new Vector2[]{
            new Vector2(50, 0),//duplicate
            new Vector2(50, 0),

            new Vector2(40, 20),
            new Vector2(70, 40),
            new Vector2(30, 60),
            new Vector2(50, 80),

            new Vector2(50, 100),
            new Vector2(50, 100),//duplicate
    };

    private static final Vector2[] t2 = new Vector2[]{
            new Vector2(40, 0),//duplicate
            new Vector2(40, 0),

            new Vector2(30, 30),
            new Vector2(50, 60),
            new Vector2(40, 80),
            new Vector2(60, 90),

            new Vector2(50, 100),
            new Vector2(50, 100),//duplicate
    };

    private static final Vector2[] t3 = new Vector2[]{
            new Vector2(10, 0),//duplicate
            new Vector2(10, 0),

            new Vector2(20, 0),
            new Vector2(60, 20),
            new Vector2(50, 40),
            new Vector2(40, 60),
            new Vector2(45, 80),

            new Vector2(50, 100),
            new Vector2(50, 100),//duplicate
    };

    private static final Vector2[] t4 = new Vector2[]{
            new Vector2(60, 0),//duplicate
            new Vector2(60, 0),

            new Vector2(70, 10),
            new Vector2(50, 15),
            new Vector2(60, 25),
            new Vector2(40, 30),
            new Vector2(50, 40),
            new Vector2(30, 50),
            new Vector2(40, 55),
            new Vector2(20, 75),
            new Vector2(30, 80),
            new Vector2(10, 85),
            new Vector2(20, 90),

            new Vector2(10, 100),
            new Vector2(10, 100),//duplicate
    };

    private static final Vector2[] t5 = new Vector2[]{
            new Vector2(100, 0),//duplicate
            new Vector2(100, 0),

            new Vector2(70, 10),
            new Vector2(95, 20),
            new Vector2(60, 25),
            new Vector2(70, 35),
            new Vector2(50, 40),
            new Vector2(40, 50),
            new Vector2(50, 60),
            new Vector2(60, 70),
            new Vector2(70, 80),
            new Vector2(30, 90),

            new Vector2(0, 100),
            new Vector2(0, 100),//duplicate
    };

    private static Vector2[][] trajectories = new Vector2[][]{
            t1,
            t2,
            t3,
            t4,
            t5,
    };

    private static int count = -1;

    public static Vector2[] obtain() {

        ++count;
        if (count > trajectories.length) count = 0;

        Vector2[] match = trajectories[count];

        Vector2[] copy = new Vector2[match.length];
        for (int i = 0; i < match.length; i++) {
            copy[i] = match[i].cpy();
        }

        return copy;
    }
}
