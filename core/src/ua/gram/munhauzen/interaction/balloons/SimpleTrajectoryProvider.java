package ua.gram.munhauzen.interaction.balloons;

import com.badlogic.gdx.math.Vector2;

import ua.gram.munhauzen.utils.MathUtils;

public class SimpleTrajectoryProvider {

    private static final Vector2[] t1 = new Vector2[]{
            new Vector2(50, 0),//duplicate
            new Vector2(50, 0),

            new Vector2(25, 33),
            new Vector2(75, 66),

            new Vector2(50, 100),
            new Vector2(50, 100),//duplicate
    };

    private static final Vector2[] t2 = new Vector2[]{
            new Vector2(40, 0),//duplicate
            new Vector2(40, 0),

            new Vector2(30, 30),
            new Vector2(50, 70),
            new Vector2(40, 80),
            new Vector2(60, 60),

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

    public static Vector2[] obtain() {
        Vector2[] match = MathUtils.random(new Vector2[][]{
                t1,
                t2,
                t3,
        });

        Vector2[] copy = new Vector2[match.length];
        for (int i = 0; i < match.length; i++) {
            copy[i] = match[i].cpy();
        }

        return copy;
    }
}
