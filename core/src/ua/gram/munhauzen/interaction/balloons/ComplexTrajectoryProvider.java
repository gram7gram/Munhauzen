package ua.gram.munhauzen.interaction.balloons;

import com.badlogic.gdx.math.Vector2;

public class ComplexTrajectoryProvider {

    private static final Vector2[] t1 = new Vector2[]{
            new Vector2(50, -20),//duplicate
            new Vector2(50, -20),

            new Vector2(30, 20),
            new Vector2(60, 30),
            new Vector2(55, 25),
            new Vector2(40, 33),
            new Vector2(60, 53),
            new Vector2(30, 70),
            new Vector2(26, 65),
            new Vector2(25, 52),
            new Vector2(15, 30),
            new Vector2(70, 40),
            new Vector2(70, 75),
            new Vector2(45, 85),
            new Vector2(20, 80),
            new Vector2(25, 68),
            new Vector2(70, 65),
            new Vector2(90, 43),
            new Vector2(35, 38),
            new Vector2(15, 55),
            new Vector2(3, 50),
            new Vector2(26, 25),
            new Vector2(68, 30),
            new Vector2(95, 35),
            new Vector2(55, 55),
            new Vector2(10, 70),
            new Vector2(40, 78),
            new Vector2(90, 75),
            new Vector2(65, 90),


            new Vector2(50, 100),
            new Vector2(50, 100),//duplicate
    };

    public static Vector2[] obtain() {
        Vector2[] match = t1;

        Vector2[] copy = new Vector2[match.length];
        for (int i = 0; i < match.length; i++) {
            copy[i] = match[i].cpy();
        }

        return copy;
    }
}
