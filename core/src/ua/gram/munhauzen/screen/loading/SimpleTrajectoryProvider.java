package ua.gram.munhauzen.screen.loading;

import com.badlogic.gdx.math.Vector2;

public class SimpleTrajectoryProvider {

    private static final Vector2[] t1 = new Vector2[]{
            new Vector2(-5, -5),
            new Vector2(50, 50),
            new Vector2(100, -5),
    };

    private static final Vector2[] t2 = new Vector2[]{
            new Vector2(-5, -5),
            new Vector2(40, 70),
            new Vector2(100, -5),
    };

    private static final Vector2[] t3 = new Vector2[]{
            new Vector2(-5, -5),
            new Vector2(60, 50),
            new Vector2(100, -5),
    };

    private static Vector2[][] trajectories = new Vector2[][]{
            t1,
            t2,
            t3,
    };

    private static int count = -1;

    public static Vector2[] obtain() {

        ++count;
        if (count >= trajectories.length) count = 0;

        Vector2[] match = trajectories[count];

        Vector2[] copy = new Vector2[match.length];
        for (int i = 0; i < match.length; i++) {
            copy[i] = match[i].cpy();
        }

        return copy;
    }
}
